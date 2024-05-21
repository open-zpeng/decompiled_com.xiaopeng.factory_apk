package ch.ethz.ssh2;

import ch.ethz.ssh2.channel.Channel;
import ch.ethz.ssh2.channel.ChannelManager;
import ch.ethz.ssh2.channel.X11ServerData;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
/* loaded from: classes.dex */
public class Session {
    ChannelManager cm;

    /* renamed from: cn  reason: collision with root package name */
    Channel f209cn;
    final SecureRandom rnd;
    boolean flag_pty_requested = false;
    boolean flag_x11_requested = false;
    boolean flag_execution_started = false;
    boolean flag_closed = false;
    String x11FakeCookie = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Session(ChannelManager cm, SecureRandom rnd) throws IOException {
        this.cm = cm;
        this.f209cn = cm.openSessionChannel();
        this.rnd = rnd;
    }

    public void requestDumbPTY() throws IOException {
        requestPTY("dumb", 0, 0, 0, 0, null);
    }

    public void requestPTY(String term) throws IOException {
        requestPTY(term, 0, 0, 0, 0, null);
    }

    public void requestPTY(String term, int term_width_characters, int term_height_characters, int term_width_pixels, int term_height_pixels, byte[] terminal_modes) throws IOException {
        byte[] terminal_modes2;
        if (term == null) {
            throw new IllegalArgumentException("TERM cannot be null.");
        }
        if (terminal_modes != null && terminal_modes.length > 0) {
            if (terminal_modes[terminal_modes.length - 1] != 0) {
                throw new IOException("Illegal terminal modes description, does not end in zero byte");
            }
            terminal_modes2 = terminal_modes;
        } else {
            terminal_modes2 = new byte[1];
        }
        synchronized (this) {
            if (this.flag_closed) {
                throw new IOException("This session is closed.");
            }
            if (this.flag_pty_requested) {
                throw new IOException("A PTY was already requested.");
            }
            if (this.flag_execution_started) {
                throw new IOException("Cannot request PTY at this stage anymore, a remote execution has already started.");
            }
            this.flag_pty_requested = true;
        }
        this.cm.requestPTY(this.f209cn, term, term_width_characters, term_height_characters, term_width_pixels, term_height_pixels, terminal_modes2);
    }

    public void requestX11Forwarding(String hostname, int port, byte[] cookie, boolean singleConnection) throws IOException {
        String hexEncodedFakeCookie;
        String stringBuffer;
        if (hostname == null) {
            throw new IllegalArgumentException("hostname argument may not be null");
        }
        synchronized (this) {
            if (this.flag_closed) {
                throw new IOException("This session is closed.");
            }
            if (this.flag_x11_requested) {
                throw new IOException("X11 forwarding was already requested.");
            }
            if (this.flag_execution_started) {
                throw new IOException("Cannot request X11 forwarding at this stage anymore, a remote execution has already started.");
            }
            this.flag_x11_requested = true;
        }
        X11ServerData x11data = new X11ServerData();
        x11data.hostname = hostname;
        x11data.port = port;
        x11data.x11_magic_cookie = cookie;
        byte[] fakeCookie = new byte[16];
        do {
            this.rnd.nextBytes(fakeCookie);
            StringBuffer tmp = new StringBuffer(32);
            for (byte b : fakeCookie) {
                String digit2 = Integer.toHexString(b & 255);
                if (digit2.length() == 2) {
                    stringBuffer = digit2;
                } else {
                    StringBuffer stringBuffer2 = new StringBuffer("0");
                    stringBuffer2.append(digit2);
                    stringBuffer = stringBuffer2.toString();
                }
                tmp.append(stringBuffer);
            }
            hexEncodedFakeCookie = tmp.toString();
        } while (this.cm.checkX11Cookie(hexEncodedFakeCookie) != null);
        this.cm.requestX11(this.f209cn, singleConnection, "MIT-MAGIC-COOKIE-1", hexEncodedFakeCookie, 0);
        synchronized (this) {
            if (!this.flag_closed) {
                this.x11FakeCookie = hexEncodedFakeCookie;
                this.cm.registerX11Cookie(hexEncodedFakeCookie, x11data);
            }
        }
    }

    public void execCommand(String cmd) throws IOException {
        if (cmd == null) {
            throw new IllegalArgumentException("cmd argument may not be null");
        }
        synchronized (this) {
            if (this.flag_closed) {
                throw new IOException("This session is closed.");
            }
            if (this.flag_execution_started) {
                throw new IOException("A remote execution has already started.");
            }
            this.flag_execution_started = true;
        }
        this.cm.requestExecCommand(this.f209cn, cmd);
    }

    public void startShell() throws IOException {
        synchronized (this) {
            if (this.flag_closed) {
                throw new IOException("This session is closed.");
            }
            if (this.flag_execution_started) {
                throw new IOException("A remote execution has already started.");
            }
            this.flag_execution_started = true;
        }
        this.cm.requestShell(this.f209cn);
    }

    public void startSubSystem(String name) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("name argument may not be null");
        }
        synchronized (this) {
            if (this.flag_closed) {
                throw new IOException("This session is closed.");
            }
            if (this.flag_execution_started) {
                throw new IOException("A remote execution has already started.");
            }
            this.flag_execution_started = true;
        }
        this.cm.requestSubSystem(this.f209cn, name);
    }

    public InputStream getStdout() {
        return this.f209cn.getStdoutStream();
    }

    public InputStream getStderr() {
        return this.f209cn.getStderrStream();
    }

    public OutputStream getStdin() {
        return this.f209cn.getStdinStream();
    }

    public int waitUntilDataAvailable(long timeout) throws IOException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must not be negative!");
        }
        int conditions = this.cm.waitForCondition(this.f209cn, timeout, 28);
        if ((conditions & 1) != 0) {
            return -1;
        }
        if ((conditions & 12) != 0) {
            return 1;
        }
        if ((conditions & 16) != 0) {
            return 0;
        }
        StringBuffer stringBuffer = new StringBuffer("Unexpected condition result (");
        stringBuffer.append(conditions);
        stringBuffer.append(")");
        throw new IllegalStateException(stringBuffer.toString());
    }

    public int waitForCondition(int condition_set, long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be non-negative!");
        }
        return this.cm.waitForCondition(this.f209cn, timeout, condition_set);
    }

    public Integer getExitStatus() {
        return this.f209cn.getExitStatus();
    }

    public String getExitSignal() {
        return this.f209cn.getExitSignal();
    }

    public void close() {
        synchronized (this) {
            if (this.flag_closed) {
                return;
            }
            this.flag_closed = true;
            if (this.x11FakeCookie != null) {
                this.cm.unRegisterX11Cookie(this.x11FakeCookie, true);
            }
            try {
                this.cm.closeChannel(this.f209cn, "Closed due to user request", true);
            } catch (IOException e) {
            }
        }
    }
}
