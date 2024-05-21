package ch.ethz.ssh2.channel;

import ch.ethz.ssh2.log.Logger;
import ch.ethz.ssh2.packets.PacketChannelOpenConfirmation;
import ch.ethz.ssh2.packets.PacketChannelOpenFailure;
import ch.ethz.ssh2.packets.PacketGlobalCancelForwardRequest;
import ch.ethz.ssh2.packets.PacketGlobalForwardRequest;
import ch.ethz.ssh2.packets.PacketOpenDirectTCPIPChannel;
import ch.ethz.ssh2.packets.PacketOpenSessionChannel;
import ch.ethz.ssh2.packets.PacketSessionExecCommand;
import ch.ethz.ssh2.packets.PacketSessionPtyRequest;
import ch.ethz.ssh2.packets.PacketSessionStartShell;
import ch.ethz.ssh2.packets.PacketSessionSubsystemRequest;
import ch.ethz.ssh2.packets.PacketSessionX11Request;
import ch.ethz.ssh2.packets.Packets;
import ch.ethz.ssh2.packets.TypesReader;
import ch.ethz.ssh2.transport.MessageHandler;
import ch.ethz.ssh2.transport.TransportManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.lang3.CharEncoding;
/* loaded from: classes.dex */
public class ChannelManager implements MessageHandler {
    static /* synthetic */ Class class$0;
    private static final Logger log;
    private TransportManager tm;
    private HashMap x11_magic_cookies = new HashMap();
    private Vector channels = new Vector();
    private int nextLocalChannel = 100;
    private boolean shutdown = false;
    private int globalSuccessCounter = 0;
    private int globalFailedCounter = 0;
    private HashMap remoteForwardings = new HashMap();
    private Vector listenerThreads = new Vector();
    private boolean listenerThreadsAllowed = true;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.channel.ChannelManager");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    public ChannelManager(TransportManager tm) {
        this.tm = tm;
        tm.registerMessageHandler(this, 80, 100);
    }

    private Channel getChannel(int id) {
        synchronized (this.channels) {
            for (int i = 0; i < this.channels.size(); i++) {
                Channel c = (Channel) this.channels.elementAt(i);
                if (c.localID == id) {
                    return c;
                }
            }
            return null;
        }
    }

    private void removeChannel(int id) {
        synchronized (this.channels) {
            int i = 0;
            while (true) {
                if (i >= this.channels.size()) {
                    break;
                }
                Channel c = (Channel) this.channels.elementAt(i);
                if (c.localID != id) {
                    i++;
                } else {
                    this.channels.removeElementAt(i);
                    break;
                }
            }
        }
    }

    private int addChannel(Channel c) {
        int i;
        synchronized (this.channels) {
            this.channels.addElement(c);
            i = this.nextLocalChannel;
            this.nextLocalChannel = i + 1;
        }
        return i;
    }

    private void waitUntilChannelOpen(Channel c) throws IOException {
        synchronized (c) {
            while (c.state == 1) {
                try {
                    c.wait();
                } catch (InterruptedException e) {
                }
            }
            if (c.state != 2) {
                removeChannel(c.localID);
                String detail = c.getReasonClosed();
                if (detail == null) {
                    StringBuffer stringBuffer = new StringBuffer("state: ");
                    stringBuffer.append(c.state);
                    detail = stringBuffer.toString();
                }
                StringBuffer stringBuffer2 = new StringBuffer("Could not open channel (");
                stringBuffer2.append(detail);
                stringBuffer2.append(")");
                throw new IOException(stringBuffer2.toString());
            }
        }
    }

    private final void waitForGlobalSuccessOrFailure() throws IOException {
        synchronized (this.channels) {
            while (this.globalSuccessCounter == 0 && this.globalFailedCounter == 0) {
                if (this.shutdown) {
                    throw new IOException("The connection is being shutdown");
                }
                try {
                    this.channels.wait();
                } catch (InterruptedException e) {
                }
            }
            if (this.globalFailedCounter != 0) {
                throw new IOException("The server denied the request (did you enable port forwarding?)");
            }
            if (this.globalSuccessCounter == 0) {
                throw new IOException("Illegal state.");
            }
        }
    }

    private final void waitForChannelSuccessOrFailure(Channel c) throws IOException {
        synchronized (c) {
            while (c.successCounter == 0 && c.failedCounter == 0) {
                if (c.state != 2) {
                    String detail = c.getReasonClosed();
                    if (detail == null) {
                        StringBuffer stringBuffer = new StringBuffer("state: ");
                        stringBuffer.append(c.state);
                        detail = stringBuffer.toString();
                    }
                    StringBuffer stringBuffer2 = new StringBuffer("This SSH2 channel is not open (");
                    stringBuffer2.append(detail);
                    stringBuffer2.append(")");
                    throw new IOException(stringBuffer2.toString());
                }
                try {
                    c.wait();
                } catch (InterruptedException e) {
                }
            }
            if (c.failedCounter != 0) {
                throw new IOException("The server denied the request.");
            }
        }
    }

    public void registerX11Cookie(String hexFakeCookie, X11ServerData data) {
        synchronized (this.x11_magic_cookies) {
            this.x11_magic_cookies.put(hexFakeCookie, data);
        }
    }

    public void unRegisterX11Cookie(String hexFakeCookie, boolean killChannels) {
        if (hexFakeCookie == null) {
            throw new IllegalStateException("hexFakeCookie may not be null");
        }
        synchronized (this.x11_magic_cookies) {
            this.x11_magic_cookies.remove(hexFakeCookie);
        }
        if (!killChannels) {
            return;
        }
        if (log.isEnabled()) {
            log.log(50, "Closing all X11 channels for the given fake cookie");
        }
        synchronized (this.channels) {
            try {
                try {
                    Vector channel_copy = (Vector) this.channels.clone();
                    for (int i = 0; i < channel_copy.size(); i++) {
                        Channel c = (Channel) channel_copy.elementAt(i);
                        synchronized (c) {
                            if (hexFakeCookie.equals(c.hexX11FakeCookie)) {
                                try {
                                    closeChannel(c, "Closing X11 channel since the corresponding session is closing", true);
                                } catch (IOException e) {
                                }
                            }
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public X11ServerData checkX11Cookie(String hexFakeCookie) {
        synchronized (this.x11_magic_cookies) {
            if (hexFakeCookie != null) {
                return (X11ServerData) this.x11_magic_cookies.get(hexFakeCookie);
            }
            return null;
        }
    }

    public void closeAllChannels() {
        if (log.isEnabled()) {
            log.log(50, "Closing all channels");
        }
        synchronized (this.channels) {
            try {
                try {
                    Vector channel_copy = (Vector) this.channels.clone();
                    for (int i = 0; i < channel_copy.size(); i++) {
                        Channel c = (Channel) channel_copy.elementAt(i);
                        try {
                            closeChannel(c, "Closing all channels", true);
                        } catch (IOException e) {
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public void closeChannel(Channel c, String reason, boolean force) throws IOException {
        byte[] msg = new byte[5];
        synchronized (c) {
            if (force) {
                c.state = 4;
                c.EOF = true;
            }
            c.setReasonClosed(reason);
            msg[0] = 97;
            msg[1] = (byte) (c.remoteID >> 24);
            msg[2] = (byte) (c.remoteID >> 16);
            msg[3] = (byte) (c.remoteID >> 8);
            msg[4] = (byte) c.remoteID;
            c.notifyAll();
        }
        synchronized (c.channelSendLock) {
            if (c.closeMessageSent) {
                return;
            }
            this.tm.sendMessage(msg);
            c.closeMessageSent = true;
            if (log.isEnabled()) {
                Logger logger = log;
                StringBuffer stringBuffer = new StringBuffer("Sent SSH_MSG_CHANNEL_CLOSE (channel ");
                stringBuffer.append(c.localID);
                stringBuffer.append(")");
                logger.log(50, stringBuffer.toString());
            }
        }
    }

    public void sendEOF(Channel c) throws IOException {
        byte[] msg = new byte[5];
        synchronized (c) {
            if (c.state != 2) {
                return;
            }
            msg[0] = 96;
            msg[1] = (byte) (c.remoteID >> 24);
            msg[2] = (byte) (c.remoteID >> 16);
            msg[3] = (byte) (c.remoteID >> 8);
            msg[4] = (byte) c.remoteID;
            synchronized (c.channelSendLock) {
                if (c.closeMessageSent) {
                    return;
                }
                this.tm.sendMessage(msg);
                if (log.isEnabled()) {
                    Logger logger = log;
                    StringBuffer stringBuffer = new StringBuffer("Sent EOF (Channel ");
                    stringBuffer.append(c.localID);
                    stringBuffer.append("/");
                    stringBuffer.append(c.remoteID);
                    stringBuffer.append(")");
                    logger.log(50, stringBuffer.toString());
                }
            }
        }
    }

    public void sendOpenConfirmation(Channel c) throws IOException {
        synchronized (c) {
            if (c.state != 1) {
                return;
            }
            c.state = 2;
            PacketChannelOpenConfirmation pcoc = new PacketChannelOpenConfirmation(c.remoteID, c.localID, c.localWindow, c.localMaxPacketSize);
            synchronized (c.channelSendLock) {
                if (c.closeMessageSent) {
                    return;
                }
                this.tm.sendMessage(pcoc.getPayload());
            }
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:67:? -> B:62:0x0114). Please submit an issue!!! */
    public void sendData(Channel c, byte[] buffer, int pos, int len) throws IOException {
        int thislen;
        int pos2 = pos;
        for (int len2 = len; len2 > 0; len2 -= thislen) {
            synchronized (c) {
                while (c.state != 4) {
                    try {
                    } catch (Throwable th) {
                        th = th;
                    }
                    if (c.state != 2) {
                        StringBuffer stringBuffer = new StringBuffer("SSH channel in strange state. (");
                        stringBuffer.append(c.state);
                        stringBuffer.append(")");
                        throw new IOException(stringBuffer.toString());
                    }
                    if (c.remoteWindow != 0) {
                        thislen = c.remoteWindow >= ((long) len2) ? len2 : (int) c.remoteWindow;
                        int estimatedMaxDataLen = c.remoteMaxPacketSize - (this.tm.getPacketOverheadEstimate() + 9);
                        int estimatedMaxDataLen2 = estimatedMaxDataLen <= 0 ? 1 : estimatedMaxDataLen;
                        if (thislen > estimatedMaxDataLen2) {
                            thislen = estimatedMaxDataLen2;
                        }
                        try {
                            c.remoteWindow -= thislen;
                            byte[] msg = new byte[thislen + 9];
                            try {
                                msg[0] = 94;
                                msg[1] = (byte) (c.remoteID >> 24);
                                msg[2] = (byte) (c.remoteID >> 16);
                                msg[3] = (byte) (c.remoteID >> 8);
                                msg[4] = (byte) c.remoteID;
                                msg[5] = (byte) (thislen >> 24);
                                msg[6] = (byte) (thislen >> 16);
                                msg[7] = (byte) (thislen >> 8);
                                msg[8] = (byte) thislen;
                                try {
                                    System.arraycopy(buffer, pos2, msg, 9, thislen);
                                    synchronized (c.channelSendLock) {
                                        if (c.closeMessageSent) {
                                            StringBuffer stringBuffer2 = new StringBuffer("SSH channel is closed. (");
                                            stringBuffer2.append(c.getReasonClosed());
                                            stringBuffer2.append(")");
                                            throw new IOException(stringBuffer2.toString());
                                        }
                                        this.tm.sendMessage(msg);
                                    }
                                    pos2 += thislen;
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } else {
                        try {
                            try {
                                c.wait();
                            } catch (InterruptedException e) {
                            }
                        } catch (Throwable th5) {
                            th = th5;
                        }
                    }
                    throw th;
                }
                StringBuffer stringBuffer3 = new StringBuffer("SSH channel is closed. (");
                stringBuffer3.append(c.getReasonClosed());
                stringBuffer3.append(")");
                throw new IOException(stringBuffer3.toString());
            }
        }
    }

    public int requestGlobalForward(String bindAddress, int bindPort, String targetAddress, int targetPort) throws IOException {
        RemoteForwardingData rfd = new RemoteForwardingData();
        rfd.bindAddress = bindAddress;
        rfd.bindPort = bindPort;
        rfd.targetAddress = targetAddress;
        rfd.targetPort = targetPort;
        synchronized (this.remoteForwardings) {
            try {
                try {
                    Integer key = new Integer(bindPort);
                    if (this.remoteForwardings.get(key) != null) {
                        StringBuffer stringBuffer = new StringBuffer("There is already a forwarding for remote port ");
                        stringBuffer.append(bindPort);
                        throw new IOException(stringBuffer.toString());
                    }
                    this.remoteForwardings.put(key, rfd);
                    synchronized (this.channels) {
                        this.globalFailedCounter = 0;
                        this.globalSuccessCounter = 0;
                    }
                    PacketGlobalForwardRequest pgf = new PacketGlobalForwardRequest(true, bindAddress, bindPort);
                    this.tm.sendMessage(pgf.getPayload());
                    if (log.isEnabled()) {
                        Logger logger = log;
                        StringBuffer stringBuffer2 = new StringBuffer("Requesting a remote forwarding ('");
                        stringBuffer2.append(bindAddress);
                        stringBuffer2.append("', ");
                        stringBuffer2.append(bindPort);
                        stringBuffer2.append(")");
                        logger.log(50, stringBuffer2.toString());
                    }
                    try {
                        waitForGlobalSuccessOrFailure();
                        return bindPort;
                    } catch (IOException e) {
                        synchronized (this.remoteForwardings) {
                            this.remoteForwardings.remove(rfd);
                            throw e;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public void requestCancelGlobalForward(int bindPort) throws IOException {
        RemoteForwardingData rfd;
        synchronized (this.remoteForwardings) {
            rfd = (RemoteForwardingData) this.remoteForwardings.get(new Integer(bindPort));
            if (rfd == null) {
                StringBuffer stringBuffer = new StringBuffer("Sorry, there is no known remote forwarding for remote port ");
                stringBuffer.append(bindPort);
                throw new IOException(stringBuffer.toString());
            }
        }
        synchronized (this.channels) {
            this.globalFailedCounter = 0;
            this.globalSuccessCounter = 0;
        }
        PacketGlobalCancelForwardRequest pgcf = new PacketGlobalCancelForwardRequest(true, rfd.bindAddress, rfd.bindPort);
        this.tm.sendMessage(pgcf.getPayload());
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer2 = new StringBuffer("Requesting cancelation of remote forward ('");
            stringBuffer2.append(rfd.bindAddress);
            stringBuffer2.append("', ");
            stringBuffer2.append(rfd.bindPort);
            stringBuffer2.append(")");
            logger.log(50, stringBuffer2.toString());
        }
        waitForGlobalSuccessOrFailure();
        synchronized (this.remoteForwardings) {
            this.remoteForwardings.remove(rfd);
        }
    }

    public void registerThread(IChannelWorkerThread thr) throws IOException {
        synchronized (this.listenerThreads) {
            if (!this.listenerThreadsAllowed) {
                throw new IOException("Too late, this connection is closed.");
            }
            this.listenerThreads.addElement(thr);
        }
    }

    public Channel openDirectTCPIPChannel(String host_to_connect, int port_to_connect, String originator_IP_address, int originator_port) throws IOException {
        Channel c = new Channel(this);
        synchronized (c) {
            c.localID = addChannel(c);
        }
        PacketOpenDirectTCPIPChannel dtc = new PacketOpenDirectTCPIPChannel(c.localID, c.localWindow, c.localMaxPacketSize, host_to_connect, port_to_connect, originator_IP_address, originator_port);
        this.tm.sendMessage(dtc.getPayload());
        waitUntilChannelOpen(c);
        return c;
    }

    public Channel openSessionChannel() throws IOException {
        Channel c = new Channel(this);
        synchronized (c) {
            c.localID = addChannel(c);
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("Sending SSH_MSG_CHANNEL_OPEN (Channel ");
            stringBuffer.append(c.localID);
            stringBuffer.append(")");
            logger.log(50, stringBuffer.toString());
        }
        PacketOpenSessionChannel smo = new PacketOpenSessionChannel(c.localID, c.localWindow, c.localMaxPacketSize);
        this.tm.sendMessage(smo.getPayload());
        waitUntilChannelOpen(c);
        return c;
    }

    public void requestPTY(Channel c, String term, int term_width_characters, int term_height_characters, int term_width_pixels, int term_height_pixels, byte[] terminal_modes) throws IOException {
        synchronized (c) {
            try {
                if (c.state == 2) {
                    PacketSessionPtyRequest spr = new PacketSessionPtyRequest(c.remoteID, true, term, term_width_characters, term_height_characters, term_width_pixels, term_height_pixels, terminal_modes);
                    try {
                        c.failedCounter = 0;
                        c.successCounter = 0;
                        synchronized (c.channelSendLock) {
                            try {
                                try {
                                    if (c.closeMessageSent) {
                                        StringBuffer stringBuffer = new StringBuffer("Cannot request PTY on this channel (");
                                        stringBuffer.append(c.getReasonClosed());
                                        stringBuffer.append(")");
                                        throw new IOException(stringBuffer.toString());
                                    }
                                    this.tm.sendMessage(spr.getPayload());
                                    try {
                                        waitForChannelSuccessOrFailure(c);
                                        return;
                                    } catch (IOException e) {
                                        throw ((IOException) new IOException("PTY request failed").initCause(e));
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } else {
                    try {
                        StringBuffer stringBuffer2 = new StringBuffer("Cannot request PTY on this channel (");
                        stringBuffer2.append(c.getReasonClosed());
                        stringBuffer2.append(")");
                        throw new IOException(stringBuffer2.toString());
                    } catch (Throwable th4) {
                        th = th4;
                    }
                }
            } catch (Throwable th5) {
                th = th5;
            }
            while (true) {
                try {
                    break;
                } catch (Throwable th6) {
                    th = th6;
                }
            }
            throw th;
        }
    }

    public void requestX11(Channel c, boolean singleConnection, String x11AuthenticationProtocol, String x11AuthenticationCookie, int x11ScreenNumber) throws IOException {
        synchronized (c) {
            try {
                try {
                    if (c.state != 2) {
                        StringBuffer stringBuffer = new StringBuffer("Cannot request X11 on this channel (");
                        stringBuffer.append(c.getReasonClosed());
                        stringBuffer.append(")");
                        throw new IOException(stringBuffer.toString());
                    }
                    PacketSessionX11Request psr = new PacketSessionX11Request(c.remoteID, true, singleConnection, x11AuthenticationProtocol, x11AuthenticationCookie, x11ScreenNumber);
                    c.failedCounter = 0;
                    c.successCounter = 0;
                    synchronized (c.channelSendLock) {
                        if (c.closeMessageSent) {
                            StringBuffer stringBuffer2 = new StringBuffer("Cannot request X11 on this channel (");
                            stringBuffer2.append(c.getReasonClosed());
                            stringBuffer2.append(")");
                            throw new IOException(stringBuffer2.toString());
                        }
                        this.tm.sendMessage(psr.getPayload());
                    }
                    if (log.isEnabled()) {
                        Logger logger = log;
                        StringBuffer stringBuffer3 = new StringBuffer("Requesting X11 forwarding (Channel ");
                        stringBuffer3.append(c.localID);
                        stringBuffer3.append("/");
                        stringBuffer3.append(c.remoteID);
                        stringBuffer3.append(")");
                        logger.log(50, stringBuffer3.toString());
                    }
                    try {
                        waitForChannelSuccessOrFailure(c);
                    } catch (IOException e) {
                        throw ((IOException) new IOException("The X11 request failed.").initCause(e));
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public void requestSubSystem(Channel c, String subSystemName) throws IOException {
        synchronized (c) {
            try {
                try {
                    if (c.state != 2) {
                        StringBuffer stringBuffer = new StringBuffer("Cannot request subsystem on this channel (");
                        stringBuffer.append(c.getReasonClosed());
                        stringBuffer.append(")");
                        throw new IOException(stringBuffer.toString());
                    }
                    PacketSessionSubsystemRequest ssr = new PacketSessionSubsystemRequest(c.remoteID, true, subSystemName);
                    c.failedCounter = 0;
                    c.successCounter = 0;
                    synchronized (c.channelSendLock) {
                        if (c.closeMessageSent) {
                            StringBuffer stringBuffer2 = new StringBuffer("Cannot request subsystem on this channel (");
                            stringBuffer2.append(c.getReasonClosed());
                            stringBuffer2.append(")");
                            throw new IOException(stringBuffer2.toString());
                        }
                        this.tm.sendMessage(ssr.getPayload());
                    }
                    try {
                        waitForChannelSuccessOrFailure(c);
                    } catch (IOException e) {
                        throw ((IOException) new IOException("The subsystem request failed.").initCause(e));
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public void requestExecCommand(Channel c, String cmd) throws IOException {
        synchronized (c) {
            try {
                try {
                    if (c.state != 2) {
                        StringBuffer stringBuffer = new StringBuffer("Cannot execute command on this channel (");
                        stringBuffer.append(c.getReasonClosed());
                        stringBuffer.append(")");
                        throw new IOException(stringBuffer.toString());
                    }
                    PacketSessionExecCommand sm = new PacketSessionExecCommand(c.remoteID, true, cmd);
                    c.failedCounter = 0;
                    c.successCounter = 0;
                    synchronized (c.channelSendLock) {
                        if (c.closeMessageSent) {
                            StringBuffer stringBuffer2 = new StringBuffer("Cannot execute command on this channel (");
                            stringBuffer2.append(c.getReasonClosed());
                            stringBuffer2.append(")");
                            throw new IOException(stringBuffer2.toString());
                        }
                        this.tm.sendMessage(sm.getPayload());
                    }
                    if (log.isEnabled()) {
                        Logger logger = log;
                        StringBuffer stringBuffer3 = new StringBuffer("Executing command (channel ");
                        stringBuffer3.append(c.localID);
                        stringBuffer3.append(", '");
                        stringBuffer3.append(cmd);
                        stringBuffer3.append("')");
                        logger.log(50, stringBuffer3.toString());
                    }
                    try {
                        waitForChannelSuccessOrFailure(c);
                    } catch (IOException e) {
                        throw ((IOException) new IOException("The execute request failed.").initCause(e));
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public void requestShell(Channel c) throws IOException {
        synchronized (c) {
            try {
                try {
                    if (c.state != 2) {
                        StringBuffer stringBuffer = new StringBuffer("Cannot start shell on this channel (");
                        stringBuffer.append(c.getReasonClosed());
                        stringBuffer.append(")");
                        throw new IOException(stringBuffer.toString());
                    }
                    PacketSessionStartShell sm = new PacketSessionStartShell(c.remoteID, true);
                    c.failedCounter = 0;
                    c.successCounter = 0;
                    synchronized (c.channelSendLock) {
                        if (c.closeMessageSent) {
                            StringBuffer stringBuffer2 = new StringBuffer("Cannot start shell on this channel (");
                            stringBuffer2.append(c.getReasonClosed());
                            stringBuffer2.append(")");
                            throw new IOException(stringBuffer2.toString());
                        }
                        this.tm.sendMessage(sm.getPayload());
                    }
                    try {
                        waitForChannelSuccessOrFailure(c);
                    } catch (IOException e) {
                        throw ((IOException) new IOException("The shell request failed.").initCause(e));
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public void msgChannelExtendedData(byte[] msg, int msglen) throws IOException {
        if (msglen <= 13) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_EXTENDED_DATA message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int id = ((msg[1] & 255) << 24) | ((msg[2] & 255) << 16) | ((msg[3] & 255) << 8) | (msg[4] & 255);
        int dataType = ((msg[5] & 255) << 24) | ((msg[6] & 255) << 16) | ((msg[7] & 255) << 8) | (msg[8] & 255);
        int len = ((msg[11] & 255) << 8) | ((msg[9] & 255) << 24) | ((msg[10] & 255) << 16) | (msg[12] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_EXTENDED_DATA message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        } else if (dataType != 1) {
            StringBuffer stringBuffer3 = new StringBuffer("SSH_MSG_CHANNEL_EXTENDED_DATA message has unknown type (");
            stringBuffer3.append(dataType);
            stringBuffer3.append(")");
            throw new IOException(stringBuffer3.toString());
        } else if (len != msglen - 13) {
            StringBuffer stringBuffer4 = new StringBuffer("SSH_MSG_CHANNEL_EXTENDED_DATA message has wrong len (calculated ");
            stringBuffer4.append(msglen - 13);
            stringBuffer4.append(", got ");
            stringBuffer4.append(len);
            stringBuffer4.append(")");
            throw new IOException(stringBuffer4.toString());
        } else {
            if (log.isEnabled()) {
                Logger logger = log;
                StringBuffer stringBuffer5 = new StringBuffer("Got SSH_MSG_CHANNEL_EXTENDED_DATA (channel ");
                stringBuffer5.append(id);
                stringBuffer5.append(", ");
                stringBuffer5.append(len);
                stringBuffer5.append(")");
                logger.log(80, stringBuffer5.toString());
            }
            synchronized (c) {
                if (c.state == 4) {
                    return;
                }
                if (c.state != 2) {
                    StringBuffer stringBuffer6 = new StringBuffer("Got SSH_MSG_CHANNEL_EXTENDED_DATA, but channel is not in correct state (");
                    stringBuffer6.append(c.state);
                    stringBuffer6.append(")");
                    throw new IOException(stringBuffer6.toString());
                } else if (c.localWindow < len) {
                    throw new IOException("Remote sent too much data, does not fit into window.");
                } else {
                    c.localWindow -= len;
                    System.arraycopy(msg, 13, c.stderrBuffer, c.stderrWritepos, len);
                    c.stderrWritepos += len;
                    c.notifyAll();
                }
            }
        }
    }

    public int waitForCondition(Channel c, long timeout, int condition_mask) {
        long end_time = 0;
        boolean end_time_set = false;
        synchronized (c) {
            while (true) {
                int current_cond = 0;
                int stdoutAvail = c.stdoutWritepos - c.stdoutReadpos;
                int stderrAvail = c.stderrWritepos - c.stderrReadpos;
                if (stdoutAvail > 0) {
                    current_cond = 0 | 4;
                }
                if (stderrAvail > 0) {
                    current_cond |= 8;
                }
                if (c.EOF) {
                    current_cond |= 16;
                }
                if (c.getExitStatus() != null) {
                    current_cond |= 32;
                }
                if (c.getExitSignal() != null) {
                    current_cond |= 64;
                }
                if (c.state == 4) {
                    return current_cond | 2 | 16;
                } else if ((current_cond & condition_mask) != 0) {
                    return current_cond;
                } else {
                    if (timeout > 0) {
                        if (!end_time_set) {
                            long end_time2 = System.currentTimeMillis() + timeout;
                            end_time_set = true;
                            end_time = end_time2;
                        } else {
                            long end_time3 = System.currentTimeMillis();
                            timeout = end_time - end_time3;
                            if (timeout <= 0) {
                                return current_cond | 1;
                            }
                        }
                    }
                    if (timeout > 0) {
                        try {
                            c.wait(timeout);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        c.wait();
                    }
                }
            }
        }
    }

    public int getAvailable(Channel c, boolean extended) throws IOException {
        int avail;
        synchronized (c) {
            int i = 0;
            try {
                if (extended) {
                    avail = c.stderrWritepos - c.stderrReadpos;
                } else {
                    int avail2 = c.stdoutWritepos;
                    avail = avail2 - c.stdoutReadpos;
                }
                if (avail > 0) {
                    i = avail;
                } else {
                    try {
                        if (c.EOF) {
                            i = -1;
                        }
                    } catch (Throwable th) {
                        th = th;
                        while (true) {
                            try {
                                break;
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        }
                        throw th;
                    }
                }
                return i;
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0028, code lost:
        if (r18 != false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x002a, code lost:
        if (r10 <= r21) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x002c, code lost:
        r12 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002e, code lost:
        r12 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
        r5 = r12;
        java.lang.System.arraycopy(r17.stdoutBuffer, r17.stdoutReadpos, r19, r20, r5);
        r17.stdoutReadpos += r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0040, code lost:
        if (r17.stdoutReadpos == r17.stdoutWritepos) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0042, code lost:
        java.lang.System.arraycopy(r17.stdoutBuffer, r17.stdoutReadpos, r17.stdoutBuffer, 0, r17.stdoutWritepos - r17.stdoutReadpos);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0050, code lost:
        r17.stdoutWritepos -= r17.stdoutReadpos;
        r17.stdoutReadpos = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x005a, code lost:
        if (r11 <= r21) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005c, code lost:
        r0 = r21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005e, code lost:
        r0 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005f, code lost:
        r5 = r0;
        java.lang.System.arraycopy(r17.stderrBuffer, r17.stderrReadpos, r19, r20, r5);
        r17.stderrReadpos += r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0070, code lost:
        if (r17.stderrReadpos == r17.stderrWritepos) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0072, code lost:
        java.lang.System.arraycopy(r17.stderrBuffer, r17.stderrReadpos, r17.stderrBuffer, 0, r17.stderrWritepos - r17.stderrReadpos);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0080, code lost:
        r17.stderrWritepos -= r17.stderrReadpos;
        r17.stderrReadpos = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008c, code lost:
        if (r17.state == 2) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x008f, code lost:
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0094, code lost:
        if (r17.localWindow >= 15000) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0096, code lost:
        r0 = java.lang.Math.min(30000 - r17.stdoutWritepos, 30000 - r17.stderrWritepos);
        r6 = r0 - r17.localWindow;
        r17.localWindow = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00a8, code lost:
        r0 = r17.remoteID;
        r0 = r17.localID;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00af, code lost:
        if (r6 <= 0) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b7, code lost:
        if (ch.ethz.ssh2.channel.ChannelManager.log.isEnabled() == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b9, code lost:
        r0 = ch.ethz.ssh2.channel.ChannelManager.log;
        r11 = new java.lang.StringBuffer("Sending SSH_MSG_CHANNEL_WINDOW_ADJUST (channel ");
        r11.append(r0);
        r11.append(", ");
        r11.append(r6);
        r11.append(")");
        r0.log(80, r11.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00db, code lost:
        r10 = r17.channelSendLock;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00dd, code lost:
        monitor-enter(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00de, code lost:
        r0 = r17.msgWindowAdjust;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00e3, code lost:
        r0[0] = 93;
        r0[1] = (byte) (r0 >> 24);
        r0[2] = (byte) (r0 >> 16);
        r0[3] = (byte) (r0 >> 8);
        r0[4] = (byte) r0;
        r0[5] = (byte) (r6 >> 24);
        r0[6] = (byte) (r6 >> 16);
        r0[7] = (byte) (r6 >> 8);
        r0[8] = (byte) r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0114, code lost:
        if (r17.closeMessageSent != false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0118, code lost:
        r16.tm.sendMessage(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0120, code lost:
        monitor-exit(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0122, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0126, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x012b, code lost:
        monitor-exit(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x012c, code lost:
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x012d, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0131, code lost:
        return r5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int getChannelData(ch.ethz.ssh2.channel.Channel r17, boolean r18, byte[] r19, int r20, int r21) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 336
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.ethz.ssh2.channel.ChannelManager.getChannelData(ch.ethz.ssh2.channel.Channel, boolean, byte[], int, int):int");
    }

    public void msgChannelData(byte[] msg, int msglen) throws IOException {
        if (msglen <= 9) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_DATA message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int id = ((msg[1] & 255) << 24) | ((msg[2] & 255) << 16) | ((msg[3] & 255) << 8) | (msg[4] & 255);
        int len = (msg[8] & 255) | ((msg[5] & 255) << 24) | ((msg[6] & 255) << 16) | ((msg[7] & 255) << 8);
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_DATA message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        } else if (len != msglen - 9) {
            StringBuffer stringBuffer3 = new StringBuffer("SSH_MSG_CHANNEL_DATA message has wrong len (calculated ");
            stringBuffer3.append(msglen - 9);
            stringBuffer3.append(", got ");
            stringBuffer3.append(len);
            stringBuffer3.append(")");
            throw new IOException(stringBuffer3.toString());
        } else {
            if (log.isEnabled()) {
                Logger logger = log;
                StringBuffer stringBuffer4 = new StringBuffer("Got SSH_MSG_CHANNEL_DATA (channel ");
                stringBuffer4.append(id);
                stringBuffer4.append(", ");
                stringBuffer4.append(len);
                stringBuffer4.append(")");
                logger.log(80, stringBuffer4.toString());
            }
            synchronized (c) {
                if (c.state == 4) {
                    return;
                }
                if (c.state != 2) {
                    StringBuffer stringBuffer5 = new StringBuffer("Got SSH_MSG_CHANNEL_DATA, but channel is not in correct state (");
                    stringBuffer5.append(c.state);
                    stringBuffer5.append(")");
                    throw new IOException(stringBuffer5.toString());
                } else if (c.localWindow < len) {
                    throw new IOException("Remote sent too much data, does not fit into window.");
                } else {
                    c.localWindow -= len;
                    System.arraycopy(msg, 9, c.stdoutBuffer, c.stdoutWritepos, len);
                    c.stdoutWritepos += len;
                    c.notifyAll();
                }
            }
        }
    }

    public void msgChannelWindowAdjust(byte[] msg, int msglen) throws IOException {
        if (msglen != 9) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_WINDOW_ADJUST message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int id = ((msg[1] & 255) << 24) | ((msg[2] & 255) << 16) | ((msg[3] & 255) << 8) | (msg[4] & 255);
        int windowChange = ((msg[5] & 255) << 24) | ((msg[6] & 255) << 16) | ((msg[7] & 255) << 8) | (msg[8] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_WINDOW_ADJUST message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        }
        synchronized (c) {
            c.remoteWindow += windowChange & InternalZipConstants.ZIP_64_SIZE_LIMIT;
            if (c.remoteWindow > InternalZipConstants.ZIP_64_SIZE_LIMIT) {
                c.remoteWindow = InternalZipConstants.ZIP_64_SIZE_LIMIT;
            }
            c.notifyAll();
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer3 = new StringBuffer("Got SSH_MSG_CHANNEL_WINDOW_ADJUST (channel ");
            stringBuffer3.append(id);
            stringBuffer3.append(", ");
            stringBuffer3.append(windowChange);
            stringBuffer3.append(")");
            logger.log(80, stringBuffer3.toString());
        }
    }

    public void msgChannelOpen(byte[] msg, int msglen) throws IOException {
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        String channelType = tr.readString();
        int remoteID = tr.readUINT32();
        int remoteWindow = tr.readUINT32();
        int remoteMaxPacketSize = tr.readUINT32();
        if ("x11".equals(channelType)) {
            synchronized (this.x11_magic_cookies) {
                if (this.x11_magic_cookies.size() == 0) {
                    PacketChannelOpenFailure pcof = new PacketChannelOpenFailure(remoteID, 1, "X11 forwarding not activated", "");
                    this.tm.sendAsynchronousMessage(pcof.getPayload());
                    if (log.isEnabled()) {
                        log.log(20, "Unexpected X11 request, denying it!");
                    }
                    return;
                }
                String remoteOriginatorAddress = tr.readString();
                int remoteOriginatorPort = tr.readUINT32();
                Channel c = new Channel(this);
                synchronized (c) {
                    c.remoteID = remoteID;
                    c.remoteWindow = InternalZipConstants.ZIP_64_SIZE_LIMIT & remoteWindow;
                    c.remoteMaxPacketSize = remoteMaxPacketSize;
                    c.localID = addChannel(c);
                }
                RemoteX11AcceptThread rxat = new RemoteX11AcceptThread(c, remoteOriginatorAddress, remoteOriginatorPort);
                rxat.setDaemon(true);
                rxat.start();
            }
        } else if ("forwarded-tcpip".equals(channelType)) {
            String remoteConnectedAddress = tr.readString();
            int remoteConnectedPort = tr.readUINT32();
            String remoteOriginatorAddress2 = tr.readString();
            int remoteOriginatorPort2 = tr.readUINT32();
            synchronized (this.remoteForwardings) {
                try {
                    RemoteForwardingData rfd = (RemoteForwardingData) this.remoteForwardings.get(new Integer(remoteConnectedPort));
                    try {
                        if (rfd != null) {
                            Channel c2 = new Channel(this);
                            synchronized (c2) {
                                try {
                                    c2.remoteID = remoteID;
                                    c2.remoteWindow = remoteWindow & InternalZipConstants.ZIP_64_SIZE_LIMIT;
                                    c2.remoteMaxPacketSize = remoteMaxPacketSize;
                                    c2.localID = addChannel(c2);
                                } catch (Throwable th) {
                                    th = th;
                                    while (true) {
                                        try {
                                            break;
                                        } catch (Throwable th2) {
                                            th = th2;
                                        }
                                    }
                                    throw th;
                                }
                            }
                            RemoteAcceptThread rat = new RemoteAcceptThread(c2, remoteConnectedAddress, remoteConnectedPort, remoteOriginatorAddress2, remoteOriginatorPort2, rfd.targetAddress, rfd.targetPort);
                            rat.setDaemon(true);
                            rat.start();
                            return;
                        }
                        PacketChannelOpenFailure pcof2 = new PacketChannelOpenFailure(remoteID, 1, "No thanks, unknown port in forwarded-tcpip request", "");
                        this.tm.sendAsynchronousMessage(pcof2.getPayload());
                        if (log.isEnabled()) {
                            log.log(20, "Unexpected forwarded-tcpip request, denying it!");
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        while (true) {
                            try {
                                break;
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }
        } else {
            PacketChannelOpenFailure pcof3 = new PacketChannelOpenFailure(remoteID, 3, "Unknown channel type", "");
            this.tm.sendAsynchronousMessage(pcof3.getPayload());
            if (log.isEnabled()) {
                Logger logger = log;
                StringBuffer stringBuffer = new StringBuffer("The peer tried to open an unsupported channel type (");
                stringBuffer.append(channelType);
                stringBuffer.append(")");
                logger.log(20, stringBuffer.toString());
            }
        }
    }

    public void msgChannelRequest(byte[] msg, int msglen) throws IOException {
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        int id = tr.readUINT32();
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer = new StringBuffer("Unexpected SSH_MSG_CHANNEL_REQUEST message for non-existent channel ");
            stringBuffer.append(id);
            throw new IOException(stringBuffer.toString());
        }
        String type = tr.readString(CharEncoding.US_ASCII);
        boolean wantReply = tr.readBoolean();
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer2 = new StringBuffer("Got SSH_MSG_CHANNEL_REQUEST (channel ");
            stringBuffer2.append(id);
            stringBuffer2.append(", '");
            stringBuffer2.append(type);
            stringBuffer2.append("')");
            logger.log(80, stringBuffer2.toString());
        }
        if (type.equals("exit-status")) {
            if (wantReply) {
                throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message, 'want reply' is true");
            }
            int exit_status = tr.readUINT32();
            if (tr.remain() != 0) {
                throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message");
            }
            synchronized (c) {
                c.exit_status = new Integer(exit_status);
                c.notifyAll();
            }
            if (log.isEnabled()) {
                Logger logger2 = log;
                StringBuffer stringBuffer3 = new StringBuffer("Got EXIT STATUS (channel ");
                stringBuffer3.append(id);
                stringBuffer3.append(", status ");
                stringBuffer3.append(exit_status);
                stringBuffer3.append(")");
                logger2.log(50, stringBuffer3.toString());
            }
        } else if (type.equals("exit-signal")) {
            if (wantReply) {
                throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message, 'want reply' is true");
            }
            String signame = tr.readString(CharEncoding.US_ASCII);
            tr.readBoolean();
            tr.readString();
            tr.readString();
            if (tr.remain() != 0) {
                throw new IOException("Badly formatted SSH_MSG_CHANNEL_REQUEST message");
            }
            synchronized (c) {
                c.exit_signal = signame;
                c.notifyAll();
            }
            if (log.isEnabled()) {
                Logger logger3 = log;
                StringBuffer stringBuffer4 = new StringBuffer("Got EXIT SIGNAL (channel ");
                stringBuffer4.append(id);
                stringBuffer4.append(", signal ");
                stringBuffer4.append(signame);
                stringBuffer4.append(")");
                logger3.log(50, stringBuffer4.toString());
            }
        } else {
            if (wantReply) {
                byte[] reply = {100, (byte) (c.remoteID >> 24), (byte) (c.remoteID >> 16), (byte) (c.remoteID >> 8), (byte) c.remoteID};
                this.tm.sendAsynchronousMessage(reply);
            }
            if (log.isEnabled()) {
                Logger logger4 = log;
                StringBuffer stringBuffer5 = new StringBuffer("Channel request '");
                stringBuffer5.append(type);
                stringBuffer5.append("' is not known, ignoring it");
                logger4.log(50, stringBuffer5.toString());
            }
        }
    }

    public void msgChannelEOF(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_EOF message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int id = ((msg[1] & 255) << 24) | ((msg[2] & 255) << 16) | ((msg[3] & 255) << 8) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_EOF message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        }
        synchronized (c) {
            c.EOF = true;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer3 = new StringBuffer("Got SSH_MSG_CHANNEL_EOF (channel ");
            stringBuffer3.append(id);
            stringBuffer3.append(")");
            logger.log(50, stringBuffer3.toString());
        }
    }

    public void msgChannelClose(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_CLOSE message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int id = ((msg[1] & 255) << 24) | ((msg[2] & 255) << 16) | ((msg[3] & 255) << 8) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_CLOSE message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        }
        synchronized (c) {
            c.EOF = true;
            c.state = 4;
            c.setReasonClosed("Close requested by remote");
            c.closeMessageRecv = true;
            removeChannel(c.localID);
            c.notifyAll();
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer3 = new StringBuffer("Got SSH_MSG_CHANNEL_CLOSE (channel ");
            stringBuffer3.append(id);
            stringBuffer3.append(")");
            logger.log(50, stringBuffer3.toString());
        }
    }

    public void msgChannelSuccess(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_SUCCESS message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int id = ((msg[1] & 255) << 24) | ((msg[2] & 255) << 16) | ((msg[3] & 255) << 8) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_SUCCESS message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        }
        synchronized (c) {
            c.successCounter++;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer3 = new StringBuffer("Got SSH_MSG_CHANNEL_SUCCESS (channel ");
            stringBuffer3.append(id);
            stringBuffer3.append(")");
            logger.log(80, stringBuffer3.toString());
        }
    }

    public void msgChannelFailure(byte[] msg, int msglen) throws IOException {
        if (msglen != 5) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_FAILURE message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int id = ((msg[1] & 255) << 24) | ((msg[2] & 255) << 16) | ((msg[3] & 255) << 8) | (msg[4] & 255);
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_FAILURE message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        }
        synchronized (c) {
            c.failedCounter++;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer3 = new StringBuffer("Got SSH_MSG_CHANNEL_FAILURE (channel ");
            stringBuffer3.append(id);
            stringBuffer3.append(")");
            logger.log(50, stringBuffer3.toString());
        }
    }

    public void msgChannelOpenConfirmation(byte[] msg, int msglen) throws IOException {
        PacketChannelOpenConfirmation sm = new PacketChannelOpenConfirmation(msg, 0, msglen);
        Channel c = getChannel(sm.recipientChannelID);
        if (c == null) {
            StringBuffer stringBuffer = new StringBuffer("Unexpected SSH_MSG_CHANNEL_OPEN_CONFIRMATION message for non-existent channel ");
            stringBuffer.append(sm.recipientChannelID);
            throw new IOException(stringBuffer.toString());
        }
        synchronized (c) {
            if (c.state != 1) {
                StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_OPEN_CONFIRMATION message for channel ");
                stringBuffer2.append(sm.recipientChannelID);
                throw new IOException(stringBuffer2.toString());
            }
            c.remoteID = sm.senderChannelID;
            c.remoteWindow = sm.initialWindowSize & InternalZipConstants.ZIP_64_SIZE_LIMIT;
            c.remoteMaxPacketSize = sm.maxPacketSize;
            c.state = 2;
            c.notifyAll();
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer3 = new StringBuffer("Got SSH_MSG_CHANNEL_OPEN_CONFIRMATION (channel ");
            stringBuffer3.append(sm.recipientChannelID);
            stringBuffer3.append(" / remote: ");
            stringBuffer3.append(sm.senderChannelID);
            stringBuffer3.append(")");
            logger.log(50, stringBuffer3.toString());
        }
    }

    public void msgChannelOpenFailure(byte[] msg, int msglen) throws IOException {
        String reasonCodeSymbolicName;
        if (msglen < 5) {
            StringBuffer stringBuffer = new StringBuffer("SSH_MSG_CHANNEL_OPEN_FAILURE message has wrong size (");
            stringBuffer.append(msglen);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        int id = tr.readUINT32();
        Channel c = getChannel(id);
        if (c == null) {
            StringBuffer stringBuffer2 = new StringBuffer("Unexpected SSH_MSG_CHANNEL_OPEN_FAILURE message for non-existent channel ");
            stringBuffer2.append(id);
            throw new IOException(stringBuffer2.toString());
        }
        int reasonCode = tr.readUINT32();
        String description = tr.readString("UTF-8");
        if (reasonCode == 1) {
            reasonCodeSymbolicName = "SSH_OPEN_ADMINISTRATIVELY_PROHIBITED";
        } else if (reasonCode == 2) {
            reasonCodeSymbolicName = "SSH_OPEN_CONNECT_FAILED";
        } else if (reasonCode != 3) {
            if (reasonCode == 4) {
                reasonCodeSymbolicName = "SSH_OPEN_RESOURCE_SHORTAGE";
            } else {
                StringBuffer stringBuffer3 = new StringBuffer("UNKNOWN REASON CODE (");
                stringBuffer3.append(reasonCode);
                stringBuffer3.append(")");
                reasonCodeSymbolicName = stringBuffer3.toString();
            }
        } else {
            reasonCodeSymbolicName = "SSH_OPEN_UNKNOWN_CHANNEL_TYPE";
        }
        StringBuffer descriptionBuffer = new StringBuffer();
        descriptionBuffer.append(description);
        for (int i = 0; i < descriptionBuffer.length(); i++) {
            char cc = descriptionBuffer.charAt(i);
            if (cc < ' ' || cc > '~') {
                descriptionBuffer.setCharAt(i, (char) 65533);
            }
        }
        synchronized (c) {
            c.EOF = true;
            c.state = 4;
            StringBuffer stringBuffer4 = new StringBuffer("The server refused to open the channel (");
            stringBuffer4.append(reasonCodeSymbolicName);
            stringBuffer4.append(", '");
            stringBuffer4.append(descriptionBuffer.toString());
            stringBuffer4.append("')");
            c.setReasonClosed(stringBuffer4.toString());
            c.notifyAll();
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer5 = new StringBuffer("Got SSH_MSG_CHANNEL_OPEN_FAILURE (channel ");
            stringBuffer5.append(id);
            stringBuffer5.append(")");
            logger.log(50, stringBuffer5.toString());
        }
    }

    public void msgGlobalRequest(byte[] msg, int msglen) throws IOException {
        TypesReader tr = new TypesReader(msg, 0, msglen);
        tr.readByte();
        String requestName = tr.readString();
        boolean wantReply = tr.readBoolean();
        if (wantReply) {
            byte[] reply_failure = {82};
            this.tm.sendAsynchronousMessage(reply_failure);
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("Got SSH_MSG_GLOBAL_REQUEST (");
            stringBuffer.append(requestName);
            stringBuffer.append(")");
            logger.log(80, stringBuffer.toString());
        }
    }

    public void msgGlobalSuccess() throws IOException {
        synchronized (this.channels) {
            this.globalSuccessCounter++;
            this.channels.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_REQUEST_SUCCESS");
        }
    }

    public void msgGlobalFailure() throws IOException {
        synchronized (this.channels) {
            this.globalFailedCounter++;
            this.channels.notifyAll();
        }
        if (log.isEnabled()) {
            log.log(80, "Got SSH_MSG_REQUEST_FAILURE");
        }
    }

    @Override // ch.ethz.ssh2.transport.MessageHandler
    public void handleMessage(byte[] msg, int msglen) throws IOException {
        if (msg == null) {
            if (log.isEnabled()) {
                log.log(50, "HandleMessage: got shutdown");
            }
            synchronized (this.listenerThreads) {
                for (int i = 0; i < this.listenerThreads.size(); i++) {
                    IChannelWorkerThread lat = (IChannelWorkerThread) this.listenerThreads.elementAt(i);
                    lat.stopWorking();
                }
                this.listenerThreadsAllowed = false;
            }
            synchronized (this.channels) {
                this.shutdown = true;
                for (int i2 = 0; i2 < this.channels.size(); i2++) {
                    Channel c = (Channel) this.channels.elementAt(i2);
                    synchronized (c) {
                        c.EOF = true;
                        c.state = 4;
                        c.setReasonClosed("The connection is being shutdown");
                        c.closeMessageRecv = true;
                        c.notifyAll();
                    }
                }
                this.channels.setSize(0);
                this.channels.trimToSize();
                this.channels.notifyAll();
            }
            return;
        }
        byte b = msg[0];
        switch (b) {
            case 80:
                msgGlobalRequest(msg, msglen);
                return;
            case Packets.SSH_MSG_REQUEST_SUCCESS /* 81 */:
                msgGlobalSuccess();
                return;
            case Packets.SSH_MSG_REQUEST_FAILURE /* 82 */:
                msgGlobalFailure();
                return;
            default:
                switch (b) {
                    case 90:
                        msgChannelOpen(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_OPEN_CONFIRMATION /* 91 */:
                        msgChannelOpenConfirmation(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_OPEN_FAILURE /* 92 */:
                        msgChannelOpenFailure(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_WINDOW_ADJUST /* 93 */:
                        msgChannelWindowAdjust(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_DATA /* 94 */:
                        msgChannelData(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_EXTENDED_DATA /* 95 */:
                        msgChannelExtendedData(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_EOF /* 96 */:
                        msgChannelEOF(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_CLOSE /* 97 */:
                        msgChannelClose(msg, msglen);
                        return;
                    case Packets.SSH_MSG_CHANNEL_REQUEST /* 98 */:
                        msgChannelRequest(msg, msglen);
                        return;
                    case 99:
                        msgChannelSuccess(msg, msglen);
                        return;
                    case 100:
                        msgChannelFailure(msg, msglen);
                        return;
                    default:
                        StringBuffer stringBuffer = new StringBuffer("Cannot handle unknown channel message ");
                        stringBuffer.append(msg[0] & 255);
                        throw new IOException(stringBuffer.toString());
                }
        }
    }
}
