package io.sentry;

import io.sentry.SentryEnvelopeItem;
import io.sentry.exception.SentryEnvelopeException;
import io.sentry.protocol.SentryTransaction;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryEnvelopeItem {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @Nullable
    private byte[] data;
    @Nullable
    private final Callable<byte[]> dataFactory;
    private final SentryEnvelopeItemHeader header;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryEnvelopeItem(@NotNull SentryEnvelopeItemHeader header, byte[] data) {
        this.header = (SentryEnvelopeItemHeader) Objects.requireNonNull(header, "SentryEnvelopeItemHeader is required.");
        this.data = data;
        this.dataFactory = null;
    }

    SentryEnvelopeItem(@NotNull SentryEnvelopeItemHeader header, @Nullable Callable<byte[]> dataFactory) {
        this.header = (SentryEnvelopeItemHeader) Objects.requireNonNull(header, "SentryEnvelopeItemHeader is required.");
        this.dataFactory = (Callable) Objects.requireNonNull(dataFactory, "DataFactory is required.");
        this.data = null;
    }

    @NotNull
    public byte[] getData() throws Exception {
        Callable<byte[]> callable;
        if (this.data == null && (callable = this.dataFactory) != null) {
            this.data = callable.call();
        }
        return this.data;
    }

    @NotNull
    public SentryEnvelopeItemHeader getHeader() {
        return this.header;
    }

    @NotNull
    public static SentryEnvelopeItem fromSession(@NotNull final ISerializer serializer, @NotNull final Session session) throws IOException {
        Objects.requireNonNull(serializer, "ISerializer is required.");
        Objects.requireNonNull(session, "Session is required.");
        final CachedItem cachedItem = new CachedItem(new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$AeToAkDEk7zkHtPq_e8ceJed2n0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SentryEnvelopeItem.lambda$fromSession$0(ISerializer.this, session);
            }
        });
        SentryEnvelopeItemHeader itemHeader = new SentryEnvelopeItemHeader(SentryItemType.Session, new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$3lcB3rxvEThjz7k6DEVATfHEuSk
            @Override // java.util.concurrent.Callable
            public final Object call() {
                Integer valueOf;
                valueOf = Integer.valueOf(SentryEnvelopeItem.CachedItem.this.getBytes().length);
                return valueOf;
            }
        }, "application/json", null);
        return new SentryEnvelopeItem(itemHeader, new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$46-c1KPXIDINWJhUxMM1np_6D_o
            @Override // java.util.concurrent.Callable
            public final Object call() {
                byte[] bytes;
                bytes = SentryEnvelopeItem.CachedItem.this.getBytes();
                return bytes;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ byte[] lambda$fromSession$0(ISerializer serializer, Session session) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(stream, UTF_8));
            serializer.serialize((ISerializer) session, writer);
            byte[] byteArray = stream.toByteArray();
            writer.close();
            stream.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Nullable
    public SentryEvent getEvent(@NotNull ISerializer serializer) throws Exception {
        SentryEnvelopeItemHeader sentryEnvelopeItemHeader = this.header;
        if (sentryEnvelopeItemHeader == null || sentryEnvelopeItemHeader.getType() != SentryItemType.Event) {
            return null;
        }
        Reader eventReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(getData()), UTF_8));
        try {
            SentryEvent sentryEvent = (SentryEvent) serializer.deserialize(eventReader, SentryEvent.class);
            eventReader.close();
            return sentryEvent;
        } catch (Throwable th) {
            try {
                eventReader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @NotNull
    public static SentryEnvelopeItem fromEvent(@NotNull final ISerializer serializer, @NotNull final SentryBaseEvent event) throws IOException {
        Objects.requireNonNull(serializer, "ISerializer is required.");
        Objects.requireNonNull(event, "SentryEvent is required.");
        final CachedItem cachedItem = new CachedItem(new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$jxUHPkaLs3ntawrMEkpL6JjoX2Q
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SentryEnvelopeItem.lambda$fromEvent$3(ISerializer.this, event);
            }
        });
        SentryEnvelopeItemHeader itemHeader = new SentryEnvelopeItemHeader(SentryItemType.resolve(event), new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$t23M4jokC44fiqHGQ6sMi5rhekE
            @Override // java.util.concurrent.Callable
            public final Object call() {
                Integer valueOf;
                valueOf = Integer.valueOf(SentryEnvelopeItem.CachedItem.this.getBytes().length);
                return valueOf;
            }
        }, "application/json", null);
        return new SentryEnvelopeItem(itemHeader, new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$Ca10OJ81isgsejMCl9wvrttHHUA
            @Override // java.util.concurrent.Callable
            public final Object call() {
                byte[] bytes;
                bytes = SentryEnvelopeItem.CachedItem.this.getBytes();
                return bytes;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ byte[] lambda$fromEvent$3(ISerializer serializer, SentryBaseEvent event) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(stream, UTF_8));
            serializer.serialize((ISerializer) event, writer);
            byte[] byteArray = stream.toByteArray();
            writer.close();
            stream.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Nullable
    public SentryTransaction getTransaction(@NotNull ISerializer serializer) throws Exception {
        SentryEnvelopeItemHeader sentryEnvelopeItemHeader = this.header;
        if (sentryEnvelopeItemHeader == null || sentryEnvelopeItemHeader.getType() != SentryItemType.Transaction) {
            return null;
        }
        Reader eventReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(getData()), UTF_8));
        try {
            SentryTransaction sentryTransaction = (SentryTransaction) serializer.deserialize(eventReader, SentryTransaction.class);
            eventReader.close();
            return sentryTransaction;
        } catch (Throwable th) {
            try {
                eventReader.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static SentryEnvelopeItem fromUserFeedback(@NotNull final ISerializer serializer, @NotNull final UserFeedback userFeedback) {
        Objects.requireNonNull(serializer, "ISerializer is required.");
        Objects.requireNonNull(userFeedback, "UserFeedback is required.");
        final CachedItem cachedItem = new CachedItem(new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$K6sCC0lt5v8cfXSR29mb44pT39U
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SentryEnvelopeItem.lambda$fromUserFeedback$6(ISerializer.this, userFeedback);
            }
        });
        SentryEnvelopeItemHeader itemHeader = new SentryEnvelopeItemHeader(SentryItemType.UserFeedback, new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$aTOwaUMUn5sWhYph7ZTiJc_4g4g
            @Override // java.util.concurrent.Callable
            public final Object call() {
                Integer valueOf;
                valueOf = Integer.valueOf(SentryEnvelopeItem.CachedItem.this.getBytes().length);
                return valueOf;
            }
        }, "application/json", null);
        return new SentryEnvelopeItem(itemHeader, new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$VMJgxr772cgds-R9tfkB2pTuUFQ
            @Override // java.util.concurrent.Callable
            public final Object call() {
                byte[] bytes;
                bytes = SentryEnvelopeItem.CachedItem.this.getBytes();
                return bytes;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ byte[] lambda$fromUserFeedback$6(ISerializer serializer, UserFeedback userFeedback) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(stream, UTF_8));
            serializer.serialize((ISerializer) userFeedback, writer);
            byte[] byteArray = stream.toByteArray();
            writer.close();
            stream.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public static SentryEnvelopeItem fromAttachment(@NotNull final Attachment attachment, final long maxAttachmentSize) {
        final CachedItem cachedItem = new CachedItem(new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$Mhto7tiDqIFy9fIwehC5NTfi8CM
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return SentryEnvelopeItem.lambda$fromAttachment$9(Attachment.this, maxAttachmentSize);
            }
        });
        SentryEnvelopeItemHeader itemHeader = new SentryEnvelopeItemHeader(SentryItemType.Attachment, new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$f7m5PjFMTGhnRj2xwbku-d8JHhY
            @Override // java.util.concurrent.Callable
            public final Object call() {
                Integer valueOf;
                valueOf = Integer.valueOf(SentryEnvelopeItem.CachedItem.this.getBytes().length);
                return valueOf;
            }
        }, attachment.getContentType(), attachment.getFilename(), attachment.getAttachmentType());
        return new SentryEnvelopeItem(itemHeader, new Callable() { // from class: io.sentry.-$$Lambda$SentryEnvelopeItem$wqxgIQhh_23hH7Vyp31tJlCgO-0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                byte[] bytes;
                bytes = SentryEnvelopeItem.CachedItem.this.getBytes();
                return bytes;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ byte[] lambda$fromAttachment$9(Attachment attachment, long maxAttachmentSize) throws Exception {
        if (attachment.getBytes() != null) {
            if (attachment.getBytes().length > maxAttachmentSize) {
                throw new SentryEnvelopeException(String.format("Dropping attachment with filename '%s', because the size of the passed bytes with %d bytes is bigger than the maximum allowed attachment size of %d bytes.", attachment.getFilename(), Integer.valueOf(attachment.getBytes().length), Long.valueOf(maxAttachmentSize)));
            }
            return attachment.getBytes();
        } else if (attachment.getPathname() != null) {
            try {
                File file = new File(attachment.getPathname());
                if (!file.isFile()) {
                    throw new SentryEnvelopeException(String.format("Reading the attachment %s failed, because the file located at the path is not a file.", attachment.getPathname()));
                }
                if (!file.canRead()) {
                    throw new SentryEnvelopeException(String.format("Reading the attachment %s failed, because can't read the file.", attachment.getPathname()));
                }
                if (file.length() > maxAttachmentSize) {
                    throw new SentryEnvelopeException(String.format("Dropping attachment, because the size of the it located at '%s' with %d bytes is bigger than the maximum allowed attachment size of %d bytes.", attachment.getPathname(), Long.valueOf(file.length()), Long.valueOf(maxAttachmentSize)));
                }
                FileInputStream fileInputStream = new FileInputStream(attachment.getPathname());
                BufferedInputStream inputStream = new BufferedInputStream(fileInputStream);
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    while (true) {
                        int length = inputStream.read(bytes);
                        if (length != -1) {
                            outputStream.write(bytes, 0, length);
                        } else {
                            byte[] byteArray = outputStream.toByteArray();
                            outputStream.close();
                            inputStream.close();
                            fileInputStream.close();
                            return byteArray;
                        }
                    }
                } catch (Throwable th) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException | SecurityException e) {
                throw new SentryEnvelopeException(String.format("Reading the attachment %s failed.", attachment.getPathname()));
            }
        } else {
            throw new SentryEnvelopeException(String.format("Couldn't attach the attachment %s.\nPlease check that either bytes or a path is set.", attachment.getFilename()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class CachedItem {
        @Nullable
        private byte[] bytes;
        @Nullable
        private final Callable<byte[]> dataFactory;

        public CachedItem(@Nullable Callable<byte[]> dataFactory) {
            this.dataFactory = dataFactory;
        }

        @NotNull
        public byte[] getBytes() throws Exception {
            Callable<byte[]> callable;
            if (this.bytes == null && (callable = this.dataFactory) != null) {
                this.bytes = callable.call();
            }
            return orEmptyArray(this.bytes);
        }

        @NotNull
        private static byte[] orEmptyArray(@Nullable byte[] bytes) {
            return bytes != null ? bytes : new byte[0];
        }
    }
}
