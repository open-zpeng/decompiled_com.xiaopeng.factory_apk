package io.sentry.cache;

import io.sentry.DateUtils;
import io.sentry.ISerializer;
import io.sentry.SentryCrashLastRunState;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryItemType;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.Session;
import io.sentry.hints.DiskFlushNotification;
import io.sentry.hints.SessionEnd;
import io.sentry.hints.SessionStart;
import io.sentry.transport.NoOpEnvelopeCache;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class EnvelopeCache extends CacheStrategy implements IEnvelopeCache {
    public static final String CRASH_MARKER_FILE = "last_crash";
    public static final String NATIVE_CRASH_MARKER_FILE = ".sentry-native/last_crash";
    public static final String PREFIX_CURRENT_SESSION_FILE = "session";
    static final String SUFFIX_CURRENT_SESSION_FILE = ".json";
    public static final String SUFFIX_ENVELOPE_FILE = ".envelope";
    @NotNull
    private final Map<SentryEnvelope, String> fileNameMap;

    @NotNull
    public static IEnvelopeCache create(@NotNull SentryOptions options) {
        String cacheDirPath = options.getCacheDirPath();
        int maxCacheItems = options.getMaxCacheItems();
        if (cacheDirPath == null) {
            options.getLogger().log(SentryLevel.WARNING, "maxCacheItems is null, returning NoOpEnvelopeCache", new Object[0]);
            return NoOpEnvelopeCache.getInstance();
        }
        return new EnvelopeCache(options, cacheDirPath, maxCacheItems);
    }

    private EnvelopeCache(@NotNull SentryOptions options, @NotNull String cacheDirPath, int maxCacheItems) {
        super(options, cacheDirPath, maxCacheItems);
        this.fileNameMap = new WeakHashMap();
    }

    @Override // io.sentry.cache.IEnvelopeCache
    public void store(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        Objects.requireNonNull(envelope, "Envelope is required.");
        rotateCacheIfNeeded(allEnvelopeFiles());
        File currentSessionFile = getCurrentSessionFile();
        if ((hint instanceof SessionEnd) && !currentSessionFile.delete()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Current envelope doesn't exist.", new Object[0]);
        }
        if (hint instanceof SessionStart) {
            boolean crashedLastRun = false;
            if (currentSessionFile.exists()) {
                this.options.getLogger().log(SentryLevel.WARNING, "Current session is not ended, we'd need to end it.", new Object[0]);
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(currentSessionFile), UTF_8));
                    Session session = (Session) this.serializer.deserialize(reader, Session.class);
                    if (session == null) {
                        this.options.getLogger().log(SentryLevel.ERROR, "Stream from path %s resulted in a null envelope.", currentSessionFile.getAbsolutePath());
                    } else {
                        File crashMarkerFile = new File(this.options.getCacheDirPath(), NATIVE_CRASH_MARKER_FILE);
                        Date timestamp = null;
                        if (crashMarkerFile.exists()) {
                            this.options.getLogger().log(SentryLevel.INFO, "Crash marker file exists, last Session is gonna be Crashed.", new Object[0]);
                            timestamp = getTimestampFromCrashMarkerFile(crashMarkerFile);
                            crashedLastRun = true;
                            if (!crashMarkerFile.delete()) {
                                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete the crash marker file. %s.", crashMarkerFile.getAbsolutePath());
                            }
                            session.update(Session.State.Crashed, null, true);
                        }
                        session.end(timestamp);
                        SentryEnvelope fromSession = SentryEnvelope.from(this.serializer, session, this.options.getSdkVersion());
                        File fileFromSession = getEnvelopeFile(fromSession);
                        writeEnvelopeToDisk(fileFromSession, fromSession);
                    }
                    reader.close();
                } catch (Throwable e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Error processing session.", e);
                }
                if (!currentSessionFile.delete()) {
                    this.options.getLogger().log(SentryLevel.WARNING, "Failed to delete the current session file.", new Object[0]);
                }
            }
            updateCurrentSession(currentSessionFile, envelope);
            if (!crashedLastRun) {
                File javaCrashMarkerFile = new File(this.options.getCacheDirPath(), CRASH_MARKER_FILE);
                if (javaCrashMarkerFile.exists()) {
                    this.options.getLogger().log(SentryLevel.INFO, "Crash marker file exists, crashedLastRun will return true.", new Object[0]);
                    crashedLastRun = true;
                    if (!javaCrashMarkerFile.delete()) {
                        this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete the crash marker file. %s.", javaCrashMarkerFile.getAbsolutePath());
                    }
                }
            }
            SentryCrashLastRunState.getInstance().setCrashedLastRun(crashedLastRun);
        }
        File envelopeFile = getEnvelopeFile(envelope);
        if (envelopeFile.exists()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Not adding Envelope to offline storage because it already exists: %s", envelopeFile.getAbsolutePath());
            return;
        }
        this.options.getLogger().log(SentryLevel.DEBUG, "Adding Envelope to offline storage: %s", envelopeFile.getAbsolutePath());
        writeEnvelopeToDisk(envelopeFile, envelope);
        if (hint instanceof DiskFlushNotification) {
            writeCrashMarkerFile();
        }
    }

    private void writeCrashMarkerFile() {
        File crashMarkerFile = new File(this.options.getCacheDirPath(), CRASH_MARKER_FILE);
        try {
            OutputStream outputStream = new FileOutputStream(crashMarkerFile);
            String timestamp = DateUtils.getTimestamp(DateUtils.getCurrentDateTime());
            outputStream.write(timestamp.getBytes(UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error writing the crash marker file to the disk", e);
        }
    }

    @Nullable
    private Date getTimestampFromCrashMarkerFile(@NotNull File markerFile) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(markerFile), UTF_8));
            String timestamp = reader.readLine();
            this.options.getLogger().log(SentryLevel.DEBUG, "Crash marker file has %s timestamp.", timestamp);
            Date dateTime = DateUtils.getDateTime(timestamp);
            reader.close();
            return dateTime;
        } catch (IOException e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error reading the crash marker file.", e);
            return null;
        } catch (IllegalArgumentException e2) {
            this.options.getLogger().log(SentryLevel.ERROR, e2, "Error converting the crash timestamp.", new Object[0]);
            return null;
        }
    }

    private void updateCurrentSession(@NotNull File currentSessionFile, @NotNull SentryEnvelope envelope) {
        Iterable<SentryEnvelopeItem> items = envelope.getItems();
        if (items.iterator().hasNext()) {
            SentryEnvelopeItem item = items.iterator().next();
            if (SentryItemType.Session.equals(item.getHeader().getType())) {
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(item.getData()), UTF_8));
                    Session session = (Session) this.serializer.deserialize(reader, Session.class);
                    if (session == null) {
                        this.options.getLogger().log(SentryLevel.ERROR, "Item of type %s returned null by the parser.", item.getHeader().getType());
                    } else {
                        writeSessionToDisk(currentSessionFile, session);
                    }
                    reader.close();
                    return;
                } catch (Throwable e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Item failed to process.", e);
                    return;
                }
            }
            this.options.getLogger().log(SentryLevel.INFO, "Current envelope has a different envelope type %s", item.getHeader().getType());
            return;
        }
        this.options.getLogger().log(SentryLevel.INFO, "Current envelope %s is empty", currentSessionFile.getAbsolutePath());
    }

    private void writeEnvelopeToDisk(@NotNull File file, @NotNull SentryEnvelope envelope) {
        if (file.exists()) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Overwriting envelope to offline storage: %s", file.getAbsolutePath());
            if (!file.delete()) {
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            this.serializer.serialize(envelope, outputStream);
            outputStream.close();
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Error writing Envelope %s to offline storage", file.getAbsolutePath());
        }
    }

    private void writeSessionToDisk(@NotNull File file, @NotNull Session session) {
        if (file.exists()) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Overwriting session to offline storage: %s", session.getSessionId());
            if (!file.delete()) {
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
            }
        }
        try {
            OutputStream outputStream = new FileOutputStream(file);
            Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8));
            try {
                this.serializer.serialize((ISerializer) session, writer);
                writer.close();
                outputStream.close();
            } catch (Throwable th) {
                try {
                    writer.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Error writing Session to offline storage: %s", session.getSessionId());
        }
    }

    @Override // io.sentry.cache.IEnvelopeCache
    public void discard(@NotNull SentryEnvelope envelope) {
        Objects.requireNonNull(envelope, "Envelope is required.");
        File envelopeFile = getEnvelopeFile(envelope);
        if (envelopeFile.exists()) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Discarding envelope from cache: %s", envelopeFile.getAbsolutePath());
            if (!envelopeFile.delete()) {
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to delete envelope: %s", envelopeFile.getAbsolutePath());
                return;
            }
            return;
        }
        this.options.getLogger().log(SentryLevel.DEBUG, "Envelope was not cached: %s", envelopeFile.getAbsolutePath());
    }

    @NotNull
    private synchronized File getEnvelopeFile(@NotNull SentryEnvelope envelope) {
        String fileName;
        String fileName2;
        if (this.fileNameMap.containsKey(envelope)) {
            fileName2 = this.fileNameMap.get(envelope);
        } else {
            if (envelope.getHeader().getEventId() != null) {
                fileName = envelope.getHeader().getEventId().toString();
            } else {
                fileName = UUID.randomUUID().toString();
            }
            fileName2 = fileName + SUFFIX_ENVELOPE_FILE;
            this.fileNameMap.put(envelope, fileName2);
        }
        return new File(this.directory.getAbsolutePath(), fileName2);
    }

    @NotNull
    private File getCurrentSessionFile() {
        return new File(this.directory.getAbsolutePath(), "session.json");
    }

    @Override // java.lang.Iterable
    @NotNull
    public Iterator<SentryEnvelope> iterator() {
        File[] allCachedEnvelopes = allEnvelopeFiles();
        List<SentryEnvelope> ret = new ArrayList<>(allCachedEnvelopes.length);
        for (File file : allCachedEnvelopes) {
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(file));
                try {
                    ret.add(this.serializer.deserializeEnvelope(is));
                    is.close();
                } catch (Throwable th) {
                    try {
                        is.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                    break;
                }
            } catch (FileNotFoundException e) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Envelope file '%s' disappeared while converting all cached files to envelopes.", file.getAbsolutePath());
            } catch (IOException e2) {
                this.options.getLogger().log(SentryLevel.ERROR, String.format("Error while reading cached envelope from file %s", file.getAbsolutePath()), e2);
            }
        }
        return ret.iterator();
    }

    @NotNull
    private File[] allEnvelopeFiles() {
        File[] files;
        if (isDirectoryValid() && (files = this.directory.listFiles(new FilenameFilter() { // from class: io.sentry.cache.-$$Lambda$EnvelopeCache$7-eIJ0Vkpi1WSS08a1w-qss3p1M
            @Override // java.io.FilenameFilter
            public final boolean accept(File file, String str) {
                boolean endsWith;
                endsWith = str.endsWith(EnvelopeCache.SUFFIX_ENVELOPE_FILE);
                return endsWith;
            }
        })) != null) {
            return files;
        }
        return new File[0];
    }
}
