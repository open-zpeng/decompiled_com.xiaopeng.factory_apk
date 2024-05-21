package com.jcraft.jsch;

import io.sentry.cache.EnvelopeCache;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Vector;
/* loaded from: classes.dex */
public abstract class Channel implements Runnable {
    static final int SSH_MSG_CHANNEL_OPEN_CONFIRMATION = 91;
    static final int SSH_MSG_CHANNEL_OPEN_FAILURE = 92;
    static final int SSH_MSG_CHANNEL_WINDOW_ADJUST = 93;
    static final int SSH_OPEN_ADMINISTRATIVELY_PROHIBITED = 1;
    static final int SSH_OPEN_CONNECT_FAILED = 2;
    static final int SSH_OPEN_RESOURCE_SHORTAGE = 4;
    static final int SSH_OPEN_UNKNOWN_CHANNEL_TYPE = 3;
    static int index = 0;
    private static Vector pool = new Vector();
    int id;
    private Session session;
    volatile int recipient = -1;
    protected byte[] type = Util.str2byte("foo");
    volatile int lwsize_max = 1048576;
    volatile int lwsize = this.lwsize_max;
    volatile int lmpsize = 16384;
    volatile long rwsize = 0;
    volatile int rmpsize = 0;

    /* renamed from: io  reason: collision with root package name */
    IO f211io = null;
    Thread thread = null;
    volatile boolean eof_local = false;
    volatile boolean eof_remote = false;
    volatile boolean close = false;
    volatile boolean connected = false;
    volatile boolean open_confirmation = false;
    volatile int exitstatus = -1;
    volatile int reply = 0;
    volatile int connectTimeout = 0;
    int notifyme = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Channel getChannel(String type) {
        if (type.equals(EnvelopeCache.PREFIX_CURRENT_SESSION_FILE)) {
            return new ChannelSession();
        }
        if (type.equals("shell")) {
            return new ChannelShell();
        }
        if (type.equals("exec")) {
            return new ChannelExec();
        }
        if (type.equals("x11")) {
            return new ChannelX11();
        }
        if (type.equals("auth-agent@openssh.com")) {
            return new ChannelAgentForwarding();
        }
        if (type.equals("direct-tcpip")) {
            return new ChannelDirectTCPIP();
        }
        if (type.equals("forwarded-tcpip")) {
            return new ChannelForwardedTCPIP();
        }
        if (type.equals("sftp")) {
            return new ChannelSftp();
        }
        if (type.equals("subsystem")) {
            return new ChannelSubsystem();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Channel getChannel(int id, Session session) {
        synchronized (pool) {
            for (int i = 0; i < pool.size(); i++) {
                Channel c = (Channel) pool.elementAt(i);
                if (c.id == id && c.session == session) {
                    return c;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void del(Channel c) {
        synchronized (pool) {
            pool.removeElement(c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Channel() {
        synchronized (pool) {
            int i = index;
            index = i + 1;
            this.id = i;
            pool.addElement(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setRecipient(int foo) {
        this.recipient = foo;
        if (this.notifyme > 0) {
            notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRecipient() {
        return this.recipient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init() throws JSchException {
    }

    public void connect() throws JSchException {
        connect(0);
    }

    public void connect(int connectTimeout) throws JSchException {
        this.connectTimeout = connectTimeout;
        try {
            sendChannelOpen();
            start();
        } catch (Exception e) {
            this.connected = false;
            disconnect();
            if (e instanceof JSchException) {
                throw ((JSchException) e);
            }
            throw new JSchException(e.toString(), e);
        }
    }

    public void setXForwarding(boolean foo) {
    }

    public void start() throws JSchException {
    }

    public boolean isEOF() {
        return this.eof_remote;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getData(Buffer buf) {
        setRecipient(buf.getInt());
        setRemoteWindowSize(buf.getUInt());
        setRemotePacketSize(buf.getInt());
    }

    public void setInputStream(InputStream in) {
        this.f211io.setInputStream(in, false);
    }

    public void setInputStream(InputStream in, boolean dontclose) {
        this.f211io.setInputStream(in, dontclose);
    }

    public void setOutputStream(OutputStream out) {
        this.f211io.setOutputStream(out, false);
    }

    public void setOutputStream(OutputStream out, boolean dontclose) {
        this.f211io.setOutputStream(out, dontclose);
    }

    public void setExtOutputStream(OutputStream out) {
        this.f211io.setExtOutputStream(out, false);
    }

    public void setExtOutputStream(OutputStream out, boolean dontclose) {
        this.f211io.setExtOutputStream(out, dontclose);
    }

    public InputStream getInputStream() throws IOException {
        int max_input_buffer_size = 32768;
        try {
            max_input_buffer_size = Integer.parseInt(getSession().getConfig("max_input_buffer_size"));
        } catch (Exception e) {
        }
        PipedInputStream in = new MyPipedInputStream(this, 32768, max_input_buffer_size);
        boolean resizable = 32768 < max_input_buffer_size;
        this.f211io.setOutputStream(new PassiveOutputStream(in, resizable), false);
        return in;
    }

    public InputStream getExtInputStream() throws IOException {
        int max_input_buffer_size = 32768;
        try {
            max_input_buffer_size = Integer.parseInt(getSession().getConfig("max_input_buffer_size"));
        } catch (Exception e) {
        }
        PipedInputStream in = new MyPipedInputStream(this, 32768, max_input_buffer_size);
        boolean resizable = 32768 < max_input_buffer_size;
        this.f211io.setExtOutputStream(new PassiveOutputStream(in, resizable), false);
        return in;
    }

    public OutputStream getOutputStream() throws IOException {
        OutputStream out = new OutputStream() { // from class: com.jcraft.jsch.Channel.1
            private int dataLen = 0;
            private Buffer buffer = null;
            private Packet packet = null;
            private boolean closed = false;
            byte[] b = new byte[1];

            private synchronized void init() throws IOException {
                this.buffer = new Buffer(Channel.this.rmpsize);
                this.packet = new Packet(this.buffer);
                byte[] _buf = this.buffer.buffer;
                if ((_buf.length - 14) - 128 <= 0) {
                    this.buffer = null;
                    this.packet = null;
                    throw new IOException("failed to initialize the channel.");
                }
            }

            @Override // java.io.OutputStream
            public void write(int w) throws IOException {
                byte[] bArr = this.b;
                bArr[0] = (byte) w;
                write(bArr, 0, 1);
            }

            @Override // java.io.OutputStream
            public void write(byte[] buf, int s, int l) throws IOException {
                if (this.packet == null) {
                    init();
                }
                if (this.closed) {
                    throw new IOException("Already closed");
                }
                byte[] _buf = this.buffer.buffer;
                int _bufl = _buf.length;
                while (l > 0) {
                    int _l = l;
                    int i = this.dataLen;
                    if (l > (_bufl - (i + 14)) - 128) {
                        _l = (_bufl - (i + 14)) - 128;
                    }
                    if (_l <= 0) {
                        flush();
                    } else {
                        System.arraycopy(buf, s, _buf, this.dataLen + 14, _l);
                        this.dataLen += _l;
                        s += _l;
                        l -= _l;
                    }
                }
            }

            @Override // java.io.OutputStream, java.io.Flushable
            public void flush() throws IOException {
                if (this.closed) {
                    throw new IOException("Already closed");
                }
                if (this.dataLen == 0) {
                    return;
                }
                this.packet.reset();
                this.buffer.putByte((byte) 94);
                this.buffer.putInt(Channel.this.recipient);
                this.buffer.putInt(this.dataLen);
                this.buffer.skip(this.dataLen);
                try {
                    int foo = this.dataLen;
                    this.dataLen = 0;
                    synchronized (channel) {
                        if (!channel.close) {
                            Channel.this.getSession().write(this.packet, channel, foo);
                        }
                    }
                } catch (Exception e) {
                    close();
                    throw new IOException(e.toString());
                }
            }

            @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                if (this.packet == null) {
                    try {
                        init();
                    } catch (IOException e) {
                        return;
                    }
                }
                if (this.closed) {
                    return;
                }
                if (this.dataLen > 0) {
                    flush();
                }
                channel.eof();
                this.closed = true;
            }
        };
        return out;
    }

    /* loaded from: classes.dex */
    class MyPipedInputStream extends PipedInputStream {
        private int BUFFER_SIZE;
        private int max_buffer_size;

        MyPipedInputStream() throws IOException {
            this.BUFFER_SIZE = 1024;
            this.max_buffer_size = this.BUFFER_SIZE;
        }

        MyPipedInputStream(int size) throws IOException {
            this.BUFFER_SIZE = 1024;
            this.max_buffer_size = this.BUFFER_SIZE;
            this.buffer = new byte[size];
            this.BUFFER_SIZE = size;
            this.max_buffer_size = size;
        }

        MyPipedInputStream(Channel channel, int size, int max_buffer_size) throws IOException {
            this(size);
            this.max_buffer_size = max_buffer_size;
        }

        MyPipedInputStream(PipedOutputStream out) throws IOException {
            super(out);
            this.BUFFER_SIZE = 1024;
            this.max_buffer_size = this.BUFFER_SIZE;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public MyPipedInputStream(PipedOutputStream out, int size) throws IOException {
            super(out);
            this.BUFFER_SIZE = 1024;
            this.max_buffer_size = this.BUFFER_SIZE;
            this.buffer = new byte[size];
            this.BUFFER_SIZE = size;
        }

        public synchronized void updateReadSide() throws IOException {
            if (available() != 0) {
                return;
            }
            this.in = 0;
            this.out = 0;
            byte[] bArr = this.buffer;
            int i = this.in;
            this.in = i + 1;
            bArr[i] = 0;
            read();
        }

        private int freeSpace() {
            if (this.out < this.in) {
                int size = this.buffer.length - this.in;
                return size;
            } else if (this.in >= this.out) {
                return 0;
            } else {
                if (this.in != -1) {
                    int size2 = this.out - this.in;
                    return size2;
                }
                int size3 = this.buffer.length;
                return size3;
            }
        }

        synchronized void checkSpace(int len) throws IOException {
            int size = freeSpace();
            if (size < len) {
                int datasize = this.buffer.length - size;
                int foo = this.buffer.length;
                while (foo - datasize < len) {
                    foo *= 2;
                }
                if (foo > this.max_buffer_size) {
                    foo = this.max_buffer_size;
                }
                if (foo - datasize < len) {
                    return;
                }
                byte[] tmp = new byte[foo];
                if (this.out < this.in) {
                    System.arraycopy(this.buffer, 0, tmp, 0, this.buffer.length);
                } else if (this.in < this.out) {
                    if (this.in != -1) {
                        System.arraycopy(this.buffer, 0, tmp, 0, this.in);
                        System.arraycopy(this.buffer, this.out, tmp, tmp.length - (this.buffer.length - this.out), this.buffer.length - this.out);
                        this.out = tmp.length - (this.buffer.length - this.out);
                    }
                } else if (this.in == this.out) {
                    System.arraycopy(this.buffer, 0, tmp, 0, this.buffer.length);
                    this.in = this.buffer.length;
                }
                this.buffer = tmp;
            } else if (this.buffer.length == size && size > this.BUFFER_SIZE) {
                int i = size / 2;
                if (i < this.BUFFER_SIZE) {
                    i = this.BUFFER_SIZE;
                }
                this.buffer = new byte[i];
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLocalWindowSizeMax(int foo) {
        this.lwsize_max = foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLocalWindowSize(int foo) {
        this.lwsize = foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLocalPacketSize(int foo) {
        this.lmpsize = foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setRemoteWindowSize(long foo) {
        this.rwsize = foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addRemoteWindowSize(long foo) {
        this.rwsize += foo;
        if (this.notifyme > 0) {
            notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRemotePacketSize(int foo) {
        this.rmpsize = foo;
    }

    @Override // java.lang.Runnable
    public void run() {
    }

    void write(byte[] foo) throws IOException {
        write(foo, 0, foo.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write(byte[] foo, int s, int l) throws IOException {
        try {
            this.f211io.put(foo, s, l);
        } catch (NullPointerException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void write_ext(byte[] foo, int s, int l) throws IOException {
        try {
            this.f211io.put_ext(foo, s, l);
        } catch (NullPointerException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void eof_remote() {
        this.eof_remote = true;
        try {
            this.f211io.out_close();
        } catch (NullPointerException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void eof() {
        if (this.eof_local) {
            return;
        }
        this.eof_local = true;
        int i = getRecipient();
        if (i == -1) {
            return;
        }
        try {
            Buffer buf = new Buffer(100);
            Packet packet = new Packet(buf);
            packet.reset();
            buf.putByte((byte) 96);
            buf.putInt(i);
            synchronized (this) {
                if (!this.close) {
                    getSession().write(packet);
                }
            }
        } catch (Exception e) {
        }
    }

    void close() {
        if (this.close) {
            return;
        }
        this.close = true;
        this.eof_remote = true;
        this.eof_local = true;
        int i = getRecipient();
        if (i == -1) {
            return;
        }
        try {
            Buffer buf = new Buffer(100);
            Packet packet = new Packet(buf);
            packet.reset();
            buf.putByte((byte) 97);
            buf.putInt(i);
            synchronized (this) {
                getSession().write(packet);
            }
        } catch (Exception e) {
        }
    }

    public boolean isClosed() {
        return this.close;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void disconnect(Session session) {
        int count = 0;
        synchronized (pool) {
            try {
                Channel[] channels = new Channel[pool.size()];
                for (int i = 0; i < pool.size(); i++) {
                    try {
                        Channel c = (Channel) pool.elementAt(i);
                        if (c.session == session) {
                            int count2 = count + 1;
                            try {
                                channels[count] = c;
                                count = count2;
                            } catch (Exception e) {
                                count = count2;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                    } catch (Exception e2) {
                    }
                }
                for (int i2 = 0; i2 < count; i2++) {
                    channels[i2].disconnect();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public void disconnect() {
        try {
            synchronized (this) {
                if (this.connected) {
                    this.connected = false;
                    close();
                    this.eof_local = true;
                    this.eof_remote = true;
                    this.thread = null;
                    try {
                        if (this.f211io != null) {
                            this.f211io.close();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } finally {
            del(this);
        }
    }

    public boolean isConnected() {
        Session _session = this.session;
        return _session != null && _session.isConnected() && this.connected;
    }

    public void sendSignal(String signal) throws Exception {
        RequestSignal request = new RequestSignal();
        request.setSignal(signal);
        request.request(getSession(), this);
    }

    /* loaded from: classes.dex */
    class PassiveInputStream extends MyPipedInputStream {
        PipedOutputStream out;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PassiveInputStream(PipedOutputStream out, int size) throws IOException {
            super(out, size);
            this.out = out;
        }

        PassiveInputStream(PipedOutputStream out) throws IOException {
            super(out);
            this.out = out;
        }

        @Override // java.io.PipedInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            PipedOutputStream pipedOutputStream = this.out;
            if (pipedOutputStream != null) {
                pipedOutputStream.close();
            }
            this.out = null;
        }
    }

    /* loaded from: classes.dex */
    class PassiveOutputStream extends PipedOutputStream {
        private MyPipedInputStream _sink;

        PassiveOutputStream(PipedInputStream in, boolean resizable_buffer) throws IOException {
            super(in);
            this._sink = null;
            if (resizable_buffer && (in instanceof MyPipedInputStream)) {
                this._sink = (MyPipedInputStream) in;
            }
        }

        @Override // java.io.PipedOutputStream, java.io.OutputStream
        public void write(int b) throws IOException {
            MyPipedInputStream myPipedInputStream = this._sink;
            if (myPipedInputStream != null) {
                myPipedInputStream.checkSpace(1);
            }
            super.write(b);
        }

        @Override // java.io.PipedOutputStream, java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            MyPipedInputStream myPipedInputStream = this._sink;
            if (myPipedInputStream != null) {
                myPipedInputStream.checkSpace(len);
            }
            super.write(b, off, len);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setExitStatus(int status) {
        this.exitstatus = status;
    }

    public int getExitStatus() {
        return this.exitstatus;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() throws JSchException {
        Session _session = this.session;
        if (_session == null) {
            throw new JSchException("session is not available");
        }
        return _session;
    }

    public int getId() {
        return this.id;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendOpenConfirmation() throws Exception {
        Buffer buf = new Buffer(100);
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 91);
        buf.putInt(getRecipient());
        buf.putInt(this.id);
        buf.putInt(this.lwsize);
        buf.putInt(this.lmpsize);
        getSession().write(packet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendOpenFailure(int reasoncode) {
        try {
            Buffer buf = new Buffer(100);
            Packet packet = new Packet(buf);
            packet.reset();
            buf.putByte((byte) 92);
            buf.putInt(getRecipient());
            buf.putInt(reasoncode);
            buf.putString(Util.str2byte("open failed"));
            buf.putString(Util.empty);
            getSession().write(packet);
        } catch (Exception e) {
        }
    }

    protected Packet genChannelOpenPacket() {
        Buffer buf = new Buffer(100);
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 90);
        buf.putString(this.type);
        buf.putInt(this.id);
        buf.putInt(this.lwsize);
        buf.putInt(this.lmpsize);
        return packet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendChannelOpen() throws Exception {
        Session _session = getSession();
        if (!_session.isConnected()) {
            throw new JSchException("session is down");
        }
        Packet packet = genChannelOpenPacket();
        _session.write(packet);
        long start = System.currentTimeMillis();
        long timeout = this.connectTimeout;
        int retry = timeout != 0 ? 1 : 2000;
        synchronized (this) {
            while (getRecipient() == -1 && _session.isConnected() && retry > 0) {
                if (timeout > 0 && System.currentTimeMillis() - start > timeout) {
                    retry = 0;
                } else {
                    long t = timeout == 0 ? 10L : timeout;
                    try {
                        this.notifyme = 1;
                        wait(t);
                        this.notifyme = 0;
                    } catch (InterruptedException e) {
                        this.notifyme = 0;
                    } catch (Throwable th) {
                        this.notifyme = 0;
                        throw th;
                    }
                    retry--;
                }
            }
        }
        if (!_session.isConnected()) {
            throw new JSchException("session is down");
        }
        if (getRecipient() == -1) {
            throw new JSchException("channel is not opened.");
        }
        if (!this.open_confirmation) {
            throw new JSchException("channel is not opened.");
        }
        this.connected = true;
    }
}
