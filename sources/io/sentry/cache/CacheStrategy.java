package io.sentry.cache;

import io.sentry.ILogger;
import io.sentry.ISerializer;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryItemType;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.Session;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class CacheStrategy {
    protected static final Charset UTF_8 = Charset.forName("UTF-8");
    @NotNull
    protected final File directory;
    private final int maxSize;
    @NotNull
    protected final SentryOptions options;
    @NotNull
    protected final ISerializer serializer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CacheStrategy(@NotNull SentryOptions options, @NotNull String directoryPath, int maxSize) {
        Objects.requireNonNull(directoryPath, "Directory is required.");
        this.options = (SentryOptions) Objects.requireNonNull(options, "SentryOptions is required.");
        this.serializer = options.getSerializer();
        this.directory = new File(directoryPath);
        this.maxSize = maxSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isDirectoryValid() {
        if (this.directory.isDirectory() && this.directory.canWrite() && this.directory.canRead()) {
            return true;
        }
        this.options.getLogger().log(SentryLevel.ERROR, "The directory for caching files is inaccessible.: %s", this.directory.getAbsolutePath());
        return false;
    }

    private void sortFilesOldestToNewest(@NotNull File[] files) {
        if (files.length > 1) {
            Arrays.sort(files, new Comparator() { // from class: io.sentry.cache.-$$Lambda$CacheStrategy$vtbFyy0YlpKkCL4ESa4Q9QK9zEw
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Long.compare(((File) obj).lastModified(), ((File) obj2).lastModified());
                    return compare;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void rotateCacheIfNeeded(@NotNull File[] files) {
        int length = files.length;
        if (length >= this.maxSize) {
            this.options.getLogger().log(SentryLevel.WARNING, "Cache folder if full (respecting maxSize). Rotating files", new Object[0]);
            int totalToBeDeleted = (length - this.maxSize) + 1;
            sortFilesOldestToNewest(files);
            File[] notDeletedFiles = (File[]) Arrays.copyOfRange(files, totalToBeDeleted, length);
            for (int i = 0; i < totalToBeDeleted; i++) {
                File file = files[i];
                moveInitFlagIfNecessary(file, notDeletedFiles);
                if (!file.delete()) {
                    this.options.getLogger().log(SentryLevel.WARNING, "File can't be deleted: %s", file.getAbsolutePath());
                }
            }
        }
    }

    private void moveInitFlagIfNecessary(@NotNull File currentFile, @NotNull File[] notDeletedFiles) {
        Session currentSession;
        Boolean currentSessionInit;
        SentryEnvelope currentEnvelope;
        char c;
        File[] fileArr = notDeletedFiles;
        SentryEnvelope currentEnvelope2 = readEnvelope(currentFile);
        if (currentEnvelope2 != null && isValidEnvelope(currentEnvelope2) && (currentSession = getFirstSession(currentEnvelope2)) != null && isValidSession(currentSession) && (currentSessionInit = currentSession.getInit()) != null && currentSessionInit.booleanValue()) {
            int length = fileArr.length;
            char c2 = 0;
            int i = 0;
            while (i < length) {
                File notDeletedFile = fileArr[i];
                SentryEnvelope envelope = readEnvelope(notDeletedFile);
                if (envelope != null) {
                    if (!isValidEnvelope(envelope)) {
                        currentEnvelope = currentEnvelope2;
                        c = c2;
                    } else {
                        SentryEnvelopeItem newSessionItem = null;
                        Iterator<SentryEnvelopeItem> itemsIterator = envelope.getItems().iterator();
                        while (true) {
                            if (!itemsIterator.hasNext()) {
                                currentEnvelope = currentEnvelope2;
                                break;
                            }
                            SentryEnvelopeItem envelopeItem = itemsIterator.next();
                            if (isSessionType(envelopeItem)) {
                                Session session = readSession(envelopeItem);
                                if (session == null) {
                                    c2 = 0;
                                } else if (isValidSession(session)) {
                                    Boolean init = session.getInit();
                                    if (init != null && init.booleanValue()) {
                                        ILogger logger = this.options.getLogger();
                                        SentryLevel sentryLevel = SentryLevel.ERROR;
                                        Object[] objArr = new Object[1];
                                        objArr[c2] = currentSession.getSessionId();
                                        logger.log(sentryLevel, "Session %s has 2 times the init flag.", objArr);
                                        return;
                                    } else if (currentSession.getSessionId() == null || !currentSession.getSessionId().equals(session.getSessionId())) {
                                        currentEnvelope2 = currentEnvelope2;
                                        c2 = 0;
                                    } else {
                                        session.setInitAsTrue();
                                        try {
                                            newSessionItem = SentryEnvelopeItem.fromSession(this.serializer, session);
                                            itemsIterator.remove();
                                            currentEnvelope = currentEnvelope2;
                                            break;
                                        } catch (IOException e) {
                                            currentEnvelope = currentEnvelope2;
                                            this.options.getLogger().log(SentryLevel.ERROR, e, "Failed to create new envelope item for the session %s", currentSession.getSessionId());
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        if (newSessionItem == null) {
                            c = 0;
                        } else {
                            SentryEnvelope newEnvelope = buildNewEnvelope(envelope, newSessionItem);
                            long notDeletedFileTimestamp = notDeletedFile.lastModified();
                            if (!notDeletedFile.delete()) {
                                this.options.getLogger().log(SentryLevel.WARNING, "File can't be deleted: %s", notDeletedFile.getAbsolutePath());
                            }
                            saveNewEnvelope(newEnvelope, notDeletedFile, notDeletedFileTimestamp);
                            return;
                        }
                    }
                } else {
                    currentEnvelope = currentEnvelope2;
                    c = c2;
                }
                i++;
                fileArr = notDeletedFiles;
                c2 = c;
                currentEnvelope2 = currentEnvelope;
            }
        }
    }

    @Nullable
    private SentryEnvelope readEnvelope(@NotNull File file) {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            SentryEnvelope deserializeEnvelope = this.serializer.deserializeEnvelope(inputStream);
            inputStream.close();
            return deserializeEnvelope;
        } catch (IOException e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Failed to deserialize the envelope.", e);
            return null;
        }
    }

    @Nullable
    private Session getFirstSession(@NotNull SentryEnvelope envelope) {
        for (SentryEnvelopeItem item : envelope.getItems()) {
            if (isSessionType(item)) {
                return readSession(item);
            }
        }
        return null;
    }

    private boolean isValidSession(@NotNull Session session) {
        if (session.getStatus().equals(Session.State.Ok)) {
            UUID sessionId = session.getSessionId();
            return sessionId != null;
        }
        return false;
    }

    private boolean isSessionType(@Nullable SentryEnvelopeItem item) {
        if (item == null) {
            return false;
        }
        return item.getHeader().getType().equals(SentryItemType.Session);
    }

    @Nullable
    private Session readSession(@NotNull SentryEnvelopeItem item) {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(item.getData()), UTF_8));
            Session session = (Session) this.serializer.deserialize(reader, Session.class);
            reader.close();
            return session;
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Failed to deserialize the session.", e);
            return null;
        }
    }

    private void saveNewEnvelope(@NotNull SentryEnvelope envelope, @NotNull File file, long timestamp) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            this.serializer.serialize(envelope, outputStream);
            file.setLastModified(timestamp);
            outputStream.close();
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Failed to serialize the new envelope to the disk.", e);
        }
    }

    @NotNull
    private SentryEnvelope buildNewEnvelope(@NotNull SentryEnvelope envelope, @NotNull SentryEnvelopeItem sessionItem) {
        List<SentryEnvelopeItem> newEnvelopeItems = new ArrayList<>();
        for (SentryEnvelopeItem newEnvelopeItem : envelope.getItems()) {
            newEnvelopeItems.add(newEnvelopeItem);
        }
        newEnvelopeItems.add(sessionItem);
        return new SentryEnvelope(envelope.getHeader(), newEnvelopeItems);
    }

    private boolean isValidEnvelope(@NotNull SentryEnvelope envelope) {
        if (!envelope.getItems().iterator().hasNext()) {
            return false;
        }
        return true;
    }
}
