package io.sentry.instrumentation.file;

import io.sentry.HubAdapter;
import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.instrumentation.file.FileIOSpanManager;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryFileOutputStream extends FileOutputStream {
    @NotNull
    private final FileOutputStream delegate;
    @NotNull
    private final FileIOSpanManager spanManager;

    public SentryFileOutputStream(@Nullable String name) throws FileNotFoundException {
        this(name != null ? new File(name) : null, HubAdapter.getInstance());
    }

    public SentryFileOutputStream(@Nullable String name, boolean append) throws FileNotFoundException {
        this(init(name != null ? new File(name) : null, append, null, HubAdapter.getInstance()));
    }

    public SentryFileOutputStream(@Nullable File file) throws FileNotFoundException {
        this(file, HubAdapter.getInstance());
    }

    public SentryFileOutputStream(@Nullable File file, boolean append) throws FileNotFoundException {
        this(init(file, append, null, HubAdapter.getInstance()));
    }

    public SentryFileOutputStream(@NotNull FileDescriptor fdObj) {
        this(init(fdObj, null, HubAdapter.getInstance()), fdObj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryFileOutputStream(@Nullable File file, @NotNull IHub hub) throws FileNotFoundException {
        this(init(file, false, null, hub));
    }

    private SentryFileOutputStream(@NotNull FileOutputStreamInitData data, @NotNull FileDescriptor fd) {
        super(fd);
        this.spanManager = new FileIOSpanManager(data.span, data.file, data.isSendDefaultPii);
        this.delegate = data.delegate;
    }

    private SentryFileOutputStream(@NotNull FileOutputStreamInitData data) throws FileNotFoundException {
        super(data.file, data.append);
        this.spanManager = new FileIOSpanManager(data.span, data.file, data.isSendDefaultPii);
        this.delegate = data.delegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FileOutputStreamInitData init(@Nullable File file, boolean append, @Nullable FileOutputStream delegate, @NotNull IHub hub) throws FileNotFoundException {
        ISpan span = FileIOSpanManager.startSpan(hub, "file.write");
        if (delegate == null) {
            delegate = new FileOutputStream(file);
        }
        return new FileOutputStreamInitData(file, append, span, delegate, hub.getOptions().isSendDefaultPii());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FileOutputStreamInitData init(@NotNull FileDescriptor fd, @Nullable FileOutputStream delegate, @NotNull IHub hub) {
        ISpan span = FileIOSpanManager.startSpan(hub, "file.write");
        if (delegate == null) {
            delegate = new FileOutputStream(fd);
        }
        return new FileOutputStreamInitData(null, false, span, delegate, hub.getOptions().isSendDefaultPii());
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(final int b) throws IOException {
        this.spanManager.performIO(new FileIOSpanManager.FileIOCallable() { // from class: io.sentry.instrumentation.file.-$$Lambda$SentryFileOutputStream$OpKrPQIimwV2QIV57Q0IEV8SHfw
            @Override // io.sentry.instrumentation.file.FileIOSpanManager.FileIOCallable
            public final Object call() {
                return SentryFileOutputStream.this.lambda$write$0$SentryFileOutputStream(b);
            }
        });
    }

    public /* synthetic */ Integer lambda$write$0$SentryFileOutputStream(int b) throws IOException {
        this.delegate.write(b);
        return 1;
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(final byte[] b) throws IOException {
        this.spanManager.performIO(new FileIOSpanManager.FileIOCallable() { // from class: io.sentry.instrumentation.file.-$$Lambda$SentryFileOutputStream$c9i5Cmfihe3xDtj3bKz93FdGDMQ
            @Override // io.sentry.instrumentation.file.FileIOSpanManager.FileIOCallable
            public final Object call() {
                return SentryFileOutputStream.this.lambda$write$1$SentryFileOutputStream(b);
            }
        });
    }

    public /* synthetic */ Integer lambda$write$1$SentryFileOutputStream(byte[] b) throws IOException {
        this.delegate.write(b);
        return Integer.valueOf(b.length);
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.spanManager.performIO(new FileIOSpanManager.FileIOCallable() { // from class: io.sentry.instrumentation.file.-$$Lambda$SentryFileOutputStream$oC2q7IyylRUI38X3gYyFX5q9IC4
            @Override // io.sentry.instrumentation.file.FileIOSpanManager.FileIOCallable
            public final Object call() {
                return SentryFileOutputStream.this.lambda$write$2$SentryFileOutputStream(b, off, len);
            }
        });
    }

    public /* synthetic */ Integer lambda$write$2$SentryFileOutputStream(byte[] b, int off, int len) throws IOException {
        this.delegate.write(b, off, len);
        return Integer.valueOf(len);
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.spanManager.finish(this.delegate);
    }

    /* loaded from: classes2.dex */
    public static final class Factory {
        public static FileOutputStream create(@NotNull FileOutputStream delegate, @Nullable String name) throws FileNotFoundException {
            return new SentryFileOutputStream(SentryFileOutputStream.init(name != null ? new File(name) : null, false, delegate, HubAdapter.getInstance()));
        }

        public static FileOutputStream create(@NotNull FileOutputStream delegate, @Nullable String name, boolean append) throws FileNotFoundException {
            return new SentryFileOutputStream(SentryFileOutputStream.init(name != null ? new File(name) : null, append, delegate, HubAdapter.getInstance()));
        }

        public static FileOutputStream create(@NotNull FileOutputStream delegate, @Nullable File file) throws FileNotFoundException {
            return new SentryFileOutputStream(SentryFileOutputStream.init(file, false, delegate, HubAdapter.getInstance()));
        }

        public static FileOutputStream create(@NotNull FileOutputStream delegate, @Nullable File file, boolean append) throws FileNotFoundException {
            return new SentryFileOutputStream(SentryFileOutputStream.init(file, append, delegate, HubAdapter.getInstance()));
        }

        public static FileOutputStream create(@NotNull FileOutputStream delegate, @NotNull FileDescriptor fdObj) {
            return new SentryFileOutputStream(SentryFileOutputStream.init(fdObj, delegate, HubAdapter.getInstance()), fdObj);
        }
    }
}
