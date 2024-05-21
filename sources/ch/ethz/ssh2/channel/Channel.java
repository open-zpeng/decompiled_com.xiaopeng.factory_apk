package ch.ethz.ssh2.channel;
/* loaded from: classes.dex */
public class Channel {
    static final int CHANNEL_BUFFER_SIZE = 30000;
    static final int STATE_CLOSED = 4;
    static final int STATE_OPEN = 2;
    static final int STATE_OPENING = 1;
    final ChannelManager cm;
    String exit_signal;
    Integer exit_status;
    String hexX11FakeCookie;
    int localMaxPacketSize;
    int localWindow;
    int localID = -1;
    int remoteID = -1;
    final Object channelSendLock = new Object();
    boolean closeMessageSent = false;
    final byte[] msgWindowAdjust = new byte[9];
    int state = 1;
    boolean closeMessageRecv = false;
    int successCounter = 0;
    int failedCounter = 0;
    long remoteWindow = 0;
    int remoteMaxPacketSize = -1;
    final byte[] stdoutBuffer = new byte[30000];
    final byte[] stderrBuffer = new byte[30000];
    int stdoutReadpos = 0;
    int stdoutWritepos = 0;
    int stderrReadpos = 0;
    int stderrWritepos = 0;
    boolean EOF = false;
    private final Object reasonClosedLock = new Object();
    private String reasonClosed = null;
    final ChannelOutputStream stdinStream = new ChannelOutputStream(this);
    final ChannelInputStream stdoutStream = new ChannelInputStream(this, false);
    final ChannelInputStream stderrStream = new ChannelInputStream(this, true);

    public Channel(ChannelManager cm) {
        this.localWindow = 0;
        this.localMaxPacketSize = -1;
        this.cm = cm;
        this.localWindow = 30000;
        this.localMaxPacketSize = 33976;
    }

    public ChannelInputStream getStderrStream() {
        return this.stderrStream;
    }

    public ChannelOutputStream getStdinStream() {
        return this.stdinStream;
    }

    public ChannelInputStream getStdoutStream() {
        return this.stdoutStream;
    }

    public String getExitSignal() {
        String str;
        synchronized (this) {
            str = this.exit_signal;
        }
        return str;
    }

    public Integer getExitStatus() {
        Integer num;
        synchronized (this) {
            num = this.exit_status;
        }
        return num;
    }

    public String getReasonClosed() {
        String str;
        synchronized (this.reasonClosedLock) {
            str = this.reasonClosed;
        }
        return str;
    }

    public void setReasonClosed(String reasonClosed) {
        synchronized (this.reasonClosedLock) {
            if (this.reasonClosed == null) {
                this.reasonClosed = reasonClosed;
            }
        }
    }
}
