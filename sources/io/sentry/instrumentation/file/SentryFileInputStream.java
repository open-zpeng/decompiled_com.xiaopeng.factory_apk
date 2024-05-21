package io.sentry.instrumentation.file;

import io.sentry.HubAdapter;
import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.instrumentation.file.FileIOSpanManager;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryFileInputStream extends FileInputStream {
    @NotNull
    private final FileInputStream delegate;
    @NotNull
    private final FileIOSpanManager spanManager;

    public SentryFileInputStream(@Nullable String name) throws FileNotFoundException {
        this(name != null ? new File(name) : null, HubAdapter.getInstance());
    }

    public SentryFileInputStream(@Nullable File file) throws FileNotFoundException {
        this(file, HubAdapter.getInstance());
    }

    public SentryFileInputStream(@NotNull FileDescriptor fdObj) {
        this(fdObj, HubAdapter.getInstance());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryFileInputStream(@Nullable File file, @NotNull IHub hub) throws FileNotFoundException {
        this(init(file, (FileInputStream) null, hub));
    }

    SentryFileInputStream(@NotNull FileDescriptor fdObj, @NotNull IHub hub) {
        this(init(fdObj, (FileInputStream) null, hub), fdObj);
    }

    private SentryFileInputStream(@NotNull FileInputStreamInitData data, @NotNull FileDescriptor fd) {
        super(fd);
        this.spanManager = new FileIOSpanManager(data.span, data.file, data.isSendDefaultPii);
        this.delegate = data.delegate;
    }

    private SentryFileInputStream(@NotNull FileInputStreamInitData data) throws FileNotFoundException {
        super(data.file);
        this.spanManager = new FileIOSpanManager(data.span, data.file, data.isSendDefaultPii);
        this.delegate = data.delegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FileInputStreamInitData init(@Nullable File file, @Nullable FileInputStream delegate, @NotNull IHub hub) throws FileNotFoundException {
        ISpan span = FileIOSpanManager.startSpan(hub, "file.read");
        if (delegate == null) {
            delegate = new FileInputStream(file);
        }
        return new FileInputStreamInitData(file, span, delegate, hub.getOptions().isSendDefaultPii());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FileInputStreamInitData init(@NotNull FileDescriptor fd, @Nullable FileInputStream delegate, @NotNull IHub hub) {
        ISpan span = FileIOSpanManager.startSpan(hub, "file.read");
        if (delegate == null) {
            delegate = new FileInputStream(fd);
        }
        return new FileInputStreamInitData(null, span, delegate, hub.getOptions().isSendDefaultPii());
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read() throws IOException {
        final AtomicInteger result = new AtomicInteger(0);
        this.spanManager.performIO(new FileIOSpanManager.FileIOCallable() { // from class: io.sentry.instrumentation.file.-$$Lambda$SentryFileInputStream$tOQVR0RFO2LEyOyxBHDqpaFJTbI
            @Override // io.sentry.instrumentation.file.FileIOSpanManager.FileIOCallable
            public final Object call() {
                return SentryFileInputStream.this.lambda$read$0$SentryFileInputStream(result);
            }
        });
        return result.get();
    }

    public /* synthetic */ Integer lambda$read$0$SentryFileInputStream(AtomicInteger result) throws IOException {
        int res = this.delegate.read();
        result.set(res);
        return Integer.valueOf(res != -1 ? 1 : 0);
    }

    public /* synthetic */ Integer lambda$read$1$SentryFileInputStream(byte[] b) throws IOException {
        return Integer.valueOf(this.delegate.read(b));
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read(final byte[] b) throws IOException {
        return ((Integer) this.spanManager.performIO(new FileIOSpanManager.FileIOCallable() { // from class: io.sentry.instrumentation.file.-$$Lambda$SentryFileInputStream$oyOduFqiv_2wfAYOPanks2L51Uk
            @Override // io.sentry.instrumentation.file.FileIOSpanManager.FileIOCallable
            public final Object call() {
                return SentryFileInputStream.this.lambda$read$1$SentryFileInputStream(b);
            }
        })).intValue();
    }

    public /* synthetic */ Integer lambda$read$2$SentryFileInputStream(byte[] b, int off, int len) throws IOException {
        return Integer.valueOf(this.delegate.read(b, off, len));
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return ((Integer) this.spanManager.performIO(new FileIOSpanManager.FileIOCallable() { // from class: io.sentry.instrumentation.file.-$$Lambda$SentryFileInputStream$lLBfGS-fVAYAqEw-WzCjhPp4OVM
            @Override // io.sentry.instrumentation.file.FileIOSpanManager.FileIOCallable
            public final Object call() {
                return SentryFileInputStream.this.lambda$read$2$SentryFileInputStream(b, off, len);
            }
        })).intValue();
    }

    public /* synthetic */ Long lambda$skip$3$SentryFileInputStream(long n) throws IOException {
        return Long.valueOf(this.delegate.skip(n));
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public long skip(final long n) throws IOException {
        return ((Long) this.spanManager.performIO(new FileIOSpanManager.FileIOCallable() { // from class: io.sentry.instrumentation.file.-$$Lambda$SentryFileInputStream$1EtB5RK7CphBG1nuxs2G8LSKutg
            @Override // io.sentry.instrumentation.file.FileIOSpanManager.FileIOCallable
            public final Object call() {
                return SentryFileInputStream.this.lambda$skip$3$SentryFileInputStream(n);
            }
        })).longValue();
    }

    @Override // java.io.FileInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.spanManager.finish(this.delegate);
    }

    /* loaded from: classes2.dex */
    public static final class Factory {
        public static FileInputStream create(@NotNull FileInputStream delegate, @Nullable String name) throws FileNotFoundException {
            return new SentryFileInputStream(SentryFileInputStream.init(name != null ? new File(name) : null, delegate, HubAdapter.getInstance()));
        }

        public static FileInputStream create(@NotNull FileInputStream delegate, @Nullable File file) throws FileNotFoundException {
            return new SentryFileInputStream(SentryFileInputStream.init(file, delegate, HubAdapter.getInstance()));
        }

        public static FileInputStream create(@NotNull FileInputStream delegate, @NotNull FileDescriptor descriptor) {
            return new SentryFileInputStream(SentryFileInputStream.init(descriptor, delegate, HubAdapter.getInstance()), descriptor);
        }

        static FileInputStream create(@NotNull FileInputStream delegate, @Nullable File file, @NotNull IHub hub) throws FileNotFoundException {
            return new SentryFileInputStream(SentryFileInputStream.init(file, delegate, hub));
        }
    }
}
