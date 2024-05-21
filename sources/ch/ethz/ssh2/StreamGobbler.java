package ch.ethz.ssh2;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class StreamGobbler extends InputStream {
    private InputStream is;
    private Object synchronizer = new Object();
    private boolean isEOF = false;
    private boolean isClosed = false;
    private IOException exception = null;
    private byte[] buffer = new byte[2048];
    private int read_pos = 0;
    private int write_pos = 0;
    private GobblerThread t = new GobblerThread();

    /* loaded from: classes.dex */
    class GobblerThread extends Thread {
        GobblerThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] buff = new byte[8192];
            int space_available = 0;
            while (true) {
                try {
                    int avail = StreamGobbler.this.is.read(buff);
                    synchronized (StreamGobbler.this.synchronizer) {
                        if (avail <= 0) {
                            StreamGobbler.this.isEOF = true;
                            StreamGobbler.this.synchronizer.notifyAll();
                            return;
                        }
                        try {
                            space_available = StreamGobbler.this.buffer.length - StreamGobbler.this.write_pos;
                            if (space_available < avail) {
                                try {
                                    int unread_size = StreamGobbler.this.write_pos - StreamGobbler.this.read_pos;
                                    int need_space = unread_size + avail;
                                    byte[] new_buffer = StreamGobbler.this.buffer;
                                    if (need_space > StreamGobbler.this.buffer.length) {
                                        int inc = need_space / 3;
                                        int i = 256;
                                        if (inc >= 256) {
                                            i = inc;
                                        }
                                        int inc2 = i;
                                        new_buffer = new byte[need_space + (inc2 > 8192 ? 8192 : inc2)];
                                    }
                                    if (unread_size > 0) {
                                        System.arraycopy(StreamGobbler.this.buffer, StreamGobbler.this.read_pos, new_buffer, 0, unread_size);
                                    }
                                    StreamGobbler.this.buffer = new_buffer;
                                    StreamGobbler.this.read_pos = 0;
                                    StreamGobbler.this.write_pos = unread_size;
                                } catch (Throwable th) {
                                    th = th;
                                }
                            }
                            System.arraycopy(buff, 0, StreamGobbler.this.buffer, StreamGobbler.this.write_pos, avail);
                            StreamGobbler.this.write_pos += avail;
                            StreamGobbler.this.synchronizer.notifyAll();
                        } catch (Throwable th2) {
                            th = th2;
                        }
                        th = th2;
                        while (true) {
                            try {
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        }
                        throw th;
                    }
                    throw th;
                } catch (IOException e) {
                    synchronized (StreamGobbler.this.synchronizer) {
                        StreamGobbler.this.exception = e;
                        StreamGobbler.this.synchronizer.notifyAll();
                        return;
                    }
                }
            }
        }
    }

    public StreamGobbler(InputStream is) {
        this.is = is;
        this.t.setDaemon(true);
        this.t.start();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        synchronized (this.synchronizer) {
            try {
                try {
                    if (this.isClosed) {
                        throw new IOException("This StreamGobbler is closed.");
                    }
                    while (this.read_pos == this.write_pos) {
                        if (this.exception != null) {
                            throw this.exception;
                        }
                        if (this.isEOF) {
                            return -1;
                        }
                        try {
                            this.synchronizer.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    byte[] bArr = this.buffer;
                    int i = this.read_pos;
                    this.read_pos = i + 1;
                    int b = bArr[i] & 255;
                    return b;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        int i;
        synchronized (this.synchronizer) {
            if (this.isClosed) {
                throw new IOException("This StreamGobbler is closed.");
            }
            i = this.write_pos - this.read_pos;
        }
        return i;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.synchronizer) {
            if (this.isClosed) {
                return;
            }
            this.isClosed = true;
            this.isEOF = true;
            this.synchronizer.notifyAll();
            this.is.close();
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || off + len > b.length || off + len < 0 || off > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        synchronized (this.synchronizer) {
            try {
                try {
                    if (this.isClosed) {
                        throw new IOException("This StreamGobbler is closed.");
                    }
                    while (this.read_pos == this.write_pos) {
                        if (this.exception != null) {
                            throw this.exception;
                        }
                        if (this.isEOF) {
                            return -1;
                        }
                        try {
                            this.synchronizer.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    int avail = this.write_pos - this.read_pos;
                    int avail2 = avail > len ? len : avail;
                    System.arraycopy(this.buffer, this.read_pos, b, off, avail2);
                    this.read_pos += avail2;
                    return avail2;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }
}
