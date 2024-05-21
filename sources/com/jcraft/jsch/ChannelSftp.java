package com.jcraft.jsch;

import com.jcraft.jsch.Channel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Hashtable;
import java.util.Vector;
/* loaded from: classes.dex */
public class ChannelSftp extends ChannelSession {
    public static final int APPEND = 2;
    private static final int LOCAL_MAXIMUM_PACKET_SIZE = 32768;
    private static final int LOCAL_WINDOW_SIZE_MAX = 2097152;
    private static final int MAX_MSG_LENGTH = 262144;
    public static final int OVERWRITE = 0;
    public static final int RESUME = 1;
    private static final int SSH_FILEXFER_ATTR_ACMODTIME = 8;
    private static final int SSH_FILEXFER_ATTR_EXTENDED = Integer.MIN_VALUE;
    private static final int SSH_FILEXFER_ATTR_PERMISSIONS = 4;
    private static final int SSH_FILEXFER_ATTR_SIZE = 1;
    private static final int SSH_FILEXFER_ATTR_UIDGID = 2;
    private static final int SSH_FXF_APPEND = 4;
    private static final int SSH_FXF_CREAT = 8;
    private static final int SSH_FXF_EXCL = 32;
    private static final int SSH_FXF_READ = 1;
    private static final int SSH_FXF_TRUNC = 16;
    private static final int SSH_FXF_WRITE = 2;
    private static final byte SSH_FXP_ATTRS = 105;
    private static final byte SSH_FXP_CLOSE = 4;
    private static final byte SSH_FXP_DATA = 103;
    private static final byte SSH_FXP_EXTENDED = -56;
    private static final byte SSH_FXP_EXTENDED_REPLY = -55;
    private static final byte SSH_FXP_FSETSTAT = 10;
    private static final byte SSH_FXP_FSTAT = 8;
    private static final byte SSH_FXP_HANDLE = 102;
    private static final byte SSH_FXP_INIT = 1;
    private static final byte SSH_FXP_LSTAT = 7;
    private static final byte SSH_FXP_MKDIR = 14;
    private static final byte SSH_FXP_NAME = 104;
    private static final byte SSH_FXP_OPEN = 3;
    private static final byte SSH_FXP_OPENDIR = 11;
    private static final byte SSH_FXP_READ = 5;
    private static final byte SSH_FXP_READDIR = 12;
    private static final byte SSH_FXP_READLINK = 19;
    private static final byte SSH_FXP_REALPATH = 16;
    private static final byte SSH_FXP_REMOVE = 13;
    private static final byte SSH_FXP_RENAME = 18;
    private static final byte SSH_FXP_RMDIR = 15;
    private static final byte SSH_FXP_SETSTAT = 9;
    private static final byte SSH_FXP_STAT = 17;
    private static final byte SSH_FXP_STATUS = 101;
    private static final byte SSH_FXP_SYMLINK = 20;
    private static final byte SSH_FXP_VERSION = 2;
    private static final byte SSH_FXP_WRITE = 6;
    public static final int SSH_FX_BAD_MESSAGE = 5;
    public static final int SSH_FX_CONNECTION_LOST = 7;
    public static final int SSH_FX_EOF = 1;
    public static final int SSH_FX_FAILURE = 4;
    public static final int SSH_FX_NO_CONNECTION = 6;
    public static final int SSH_FX_NO_SUCH_FILE = 2;
    public static final int SSH_FX_OK = 0;
    public static final int SSH_FX_OP_UNSUPPORTED = 8;
    public static final int SSH_FX_PERMISSION_DENIED = 3;
    private static final String UTF8 = "UTF-8";
    private static final String file_separator = File.separator;
    private static final char file_separatorc = File.separatorChar;
    private static boolean fs_is_bs;
    private Buffer buf;
    private String cwd;
    private String home;
    private String lcwd;
    private Buffer obuf;
    private Packet opacket;
    private Packet packet;
    private boolean interactive = false;
    private int seq = 1;
    private int[] ackid = new int[1];
    private int client_version = 3;
    private int server_version = 3;
    private String version = String.valueOf(this.client_version);
    private Hashtable extensions = null;
    private InputStream io_in = null;
    private boolean extension_posix_rename = false;
    private boolean extension_statvfs = false;
    private boolean extension_hardlink = false;
    private String fEncoding = "UTF-8";
    private boolean fEncoding_is_utf8 = true;
    private RequestQueue rq = new RequestQueue(16);

    /* loaded from: classes.dex */
    public interface LsEntrySelector {
        public static final int BREAK = 1;
        public static final int CONTINUE = 0;

        int select(LsEntry lsEntry);
    }

    @Override // com.jcraft.jsch.ChannelSession, com.jcraft.jsch.Channel, java.lang.Runnable
    public /* bridge */ /* synthetic */ void run() {
        super.run();
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setAgentForwarding(boolean x0) {
        super.setAgentForwarding(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setEnv(String x0, String x1) {
        super.setEnv(x0, x1);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setEnv(Hashtable x0) {
        super.setEnv(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setEnv(byte[] x0, byte[] x1) {
        super.setEnv(x0, x1);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setPty(boolean x0) {
        super.setPty(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setPtySize(int x0, int x1, int x2, int x3) {
        super.setPtySize(x0, x1, x2, x3);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setPtyType(String x0) {
        super.setPtyType(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setPtyType(String x0, int x1, int x2, int x3, int x4) {
        super.setPtyType(x0, x1, x2, x3, x4);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setTerminalMode(byte[] x0) {
        super.setTerminalMode(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession, com.jcraft.jsch.Channel
    public /* bridge */ /* synthetic */ void setXForwarding(boolean x0) {
        super.setXForwarding(x0);
    }

    static {
        fs_is_bs = ((byte) File.separatorChar) == 92;
    }

    public void setBulkRequests(int bulk_requests) throws JSchException {
        if (bulk_requests > 0) {
            this.rq = new RequestQueue(bulk_requests);
            return;
        }
        throw new JSchException("setBulkRequests: " + bulk_requests + " must be greater than 0.");
    }

    public int getBulkRequests() {
        return this.rq.size();
    }

    public ChannelSftp() {
        setLocalWindowSizeMax(2097152);
        setLocalWindowSize(2097152);
        setLocalPacketSize(32768);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.Channel
    public void init() {
    }

    @Override // com.jcraft.jsch.Channel
    public void start() throws JSchException {
        try {
            PipedOutputStream pos = new PipedOutputStream();
            this.f211io.setOutputStream(pos);
            PipedInputStream pis = new Channel.MyPipedInputStream(pos, this.rmpsize);
            this.f211io.setInputStream(pis);
            this.io_in = this.f211io.in;
            if (this.io_in == null) {
                throw new JSchException("channel is down");
            }
            Request request = new RequestSftp();
            request.request(getSession(), this);
            this.buf = new Buffer(this.lmpsize);
            this.packet = new Packet(this.buf);
            this.obuf = new Buffer(this.rmpsize);
            this.opacket = new Packet(this.obuf);
            sendINIT();
            Header header = header(this.buf, new Header());
            int length = header.length;
            int i = 4;
            if (length > 262144) {
                throw new SftpException(4, "Received message is too long: " + length);
            }
            int i2 = header.type;
            this.server_version = header.rid;
            this.extensions = new Hashtable();
            if (length > 0) {
                fill(this.buf, length);
                while (length > 0) {
                    byte[] extension_name = this.buf.getString();
                    int length2 = length - (extension_name.length + i);
                    byte[] extension_data = this.buf.getString();
                    length = length2 - (extension_data.length + i);
                    this.extensions.put(Util.byte2str(extension_name), Util.byte2str(extension_data));
                    i = 4;
                }
            }
            if (this.extensions.get("posix-rename@openssh.com") != null && this.extensions.get("posix-rename@openssh.com").equals("1")) {
                this.extension_posix_rename = true;
            }
            if (this.extensions.get("statvfs@openssh.com") != null && this.extensions.get("statvfs@openssh.com").equals("2")) {
                this.extension_statvfs = true;
            }
            if (this.extensions.get("hardlink@openssh.com") != null && this.extensions.get("hardlink@openssh.com").equals("1")) {
                this.extension_hardlink = true;
            }
            this.lcwd = new File(".").getCanonicalPath();
        } catch (Exception e) {
            if (e instanceof JSchException) {
                throw ((JSchException) e);
            }
            if (e instanceof Throwable) {
                throw new JSchException(e.toString(), e);
            }
            throw new JSchException(e.toString());
        }
    }

    public void quit() {
        disconnect();
    }

    public void exit() {
        disconnect();
    }

    public void lcd(String path) throws SftpException {
        String path2 = localAbsolutePath(path);
        if (new File(path2).isDirectory()) {
            try {
                path2 = new File(path2).getCanonicalPath();
            } catch (Exception e) {
            }
            this.lcwd = path2;
            return;
        }
        throw new SftpException(2, "No such directory");
    }

    public void cd(String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            String path2 = isUnique(remoteAbsolutePath(path));
            byte[] str = _realpath(path2);
            SftpATTRS attr = _stat(str);
            if ((attr.getFlags() & 4) == 0) {
                throw new SftpException(4, "Can't change directory: " + path2);
            } else if (!attr.isDir()) {
                throw new SftpException(4, "Can't change directory: " + path2);
            } else {
                setCwd(Util.byte2str(str, this.fEncoding));
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void put(String src, String dst) throws SftpException {
        put(src, dst, (SftpProgressMonitor) null, 0);
    }

    public void put(String src, String dst, int mode) throws SftpException {
        put(src, dst, (SftpProgressMonitor) null, mode);
    }

    public void put(String src, String dst, SftpProgressMonitor monitor) throws SftpException {
        put(src, dst, monitor, 0);
    }

    public void put(String src, String dst, SftpProgressMonitor monitor, int mode) throws SftpException {
        String dst2;
        StringBuffer dstsb;
        String _dst;
        String _dst2;
        String _src;
        StringBuffer dstsb2;
        int j;
        int ii;
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            String src2 = localAbsolutePath(src);
            try {
                String dst3 = remoteAbsolutePath(dst);
                try {
                    Vector v = glob_remote(dst3);
                    int vsize = v.size();
                    if (vsize != 1) {
                        if (vsize == 0) {
                            if (isPattern(dst3)) {
                                throw new SftpException(4, dst3);
                            }
                            Util.unquote(dst3);
                        }
                        throw new SftpException(4, v.toString());
                    }
                    String dst4 = (String) v.elementAt(0);
                    boolean isRemoteDir = isRemoteDir(dst4);
                    Vector v2 = glob_local(src2);
                    int vsize2 = v2.size();
                    if (isRemoteDir) {
                        if (!dst4.endsWith("/")) {
                            dst4 = dst4 + "/";
                        }
                        StringBuffer dstsb3 = new StringBuffer(dst4);
                        dst2 = dst4;
                        dstsb = dstsb3;
                    } else if (vsize2 <= 1) {
                        dst2 = dst4;
                        dstsb = null;
                    } else {
                        throw new SftpException(4, "Copying multiple files, but the destination is missing or a file.");
                    }
                    int j2 = 0;
                    while (j2 < vsize2) {
                        try {
                            String _src2 = (String) v2.elementAt(j2);
                            if (isRemoteDir) {
                                int i = _src2.lastIndexOf(file_separatorc);
                                if (fs_is_bs && (ii = _src2.lastIndexOf(47)) != -1 && ii > i) {
                                    i = ii;
                                }
                                if (i == -1) {
                                    dstsb.append(_src2);
                                } else {
                                    dstsb.append(_src2.substring(i + 1));
                                }
                                String _dst3 = dstsb.toString();
                                dstsb.delete(dst2.length(), _dst3.length());
                                _dst = _dst3;
                            } else {
                                String _dst4 = dst2;
                                _dst = _dst4;
                            }
                            long size_of_dst = 0;
                            if (mode == 1) {
                                try {
                                    SftpATTRS attr = _stat(_dst);
                                    size_of_dst = attr.getSize();
                                } catch (Exception e) {
                                }
                                long size_of_src = new File(_src2).length();
                                if (size_of_src < size_of_dst) {
                                    throw new SftpException(4, "failed to resume for " + _dst);
                                } else if (size_of_src == size_of_dst) {
                                    return;
                                }
                            }
                            if (monitor == null) {
                                _dst2 = _dst;
                                _src = _src2;
                                dstsb2 = dstsb;
                                j = j2;
                            } else {
                                long size_of_dst2 = size_of_dst;
                                _dst2 = _dst;
                                _src = _src2;
                                dstsb2 = dstsb;
                                j = j2;
                                monitor.init(0, _src2, _dst2, new File(_src2).length());
                                if (mode == 1) {
                                    monitor.count(size_of_dst2);
                                }
                            }
                            FileInputStream fis = null;
                            try {
                                try {
                                    fis = new FileInputStream(_src);
                                    _put(fis, _dst2, monitor, mode);
                                    fis.close();
                                    j2 = j + 1;
                                    dstsb = dstsb2;
                                } catch (Throwable th) {
                                    th = th;
                                    if (fis != null) {
                                        fis.close();
                                    }
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            if (e instanceof SftpException) {
                                throw ((SftpException) e);
                            }
                            if (e instanceof Throwable) {
                                throw new SftpException(4, e.toString(), e);
                            }
                            throw new SftpException(4, e.toString());
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                }
            } catch (Exception e4) {
                e = e4;
            }
        } catch (Exception e5) {
            e = e5;
        }
    }

    public void put(InputStream src, String dst) throws SftpException {
        put(src, dst, (SftpProgressMonitor) null, 0);
    }

    public void put(InputStream src, String dst, int mode) throws SftpException {
        put(src, dst, (SftpProgressMonitor) null, mode);
    }

    public void put(InputStream src, String dst, SftpProgressMonitor monitor) throws SftpException {
        put(src, dst, monitor, 0);
    }

    public void put(InputStream src, String dst, SftpProgressMonitor monitor, int mode) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            String dst2 = remoteAbsolutePath(dst);
            Vector v = glob_remote(dst2);
            int vsize = v.size();
            if (vsize != 1) {
                if (vsize == 0) {
                    if (isPattern(dst2)) {
                        throw new SftpException(4, dst2);
                    }
                    Util.unquote(dst2);
                }
                throw new SftpException(4, v.toString());
            }
            String dst3 = (String) v.elementAt(0);
            if (monitor != null) {
                monitor.init(0, "-", dst3, -1L);
            }
            _put(src, dst3, monitor, mode);
        } catch (Exception e) {
            if (e instanceof SftpException) {
                if (((SftpException) e).id == 4 && isRemoteDir(dst)) {
                    throw new SftpException(4, dst + " is a directory");
                }
                throw ((SftpException) e);
            } else if (e instanceof Throwable) {
                throw new SftpException(4, e.toString(), e);
            } else {
                throw new SftpException(4, e.toString());
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0098, code lost:
        r1 = r36.buf.getInt();
        throwStatusError(r36.buf, r1);
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x02cb A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0143 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0271 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0222 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003f A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0058 A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005c A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00af A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d0 A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00d8 A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x011c A[Catch: Exception -> 0x02ea, TryCatch #1 {Exception -> 0x02ea, blocks: (B:3:0x000b, B:14:0x0036, B:17:0x003f, B:18:0x0055, B:20:0x0058, B:22:0x005f, B:27:0x007f, B:28:0x0095, B:30:0x0098, B:31:0x00a3, B:33:0x00af, B:39:0x00c6, B:41:0x00d0, B:43:0x00f0, B:45:0x0114, B:47:0x011c, B:95:0x02b9, B:97:0x02be, B:100:0x02c6, B:102:0x02cb, B:103:0x02ce, B:57:0x015c, B:59:0x0166, B:85:0x0222, B:87:0x0245, B:89:0x0271, B:63:0x017b, B:65:0x0181, B:67:0x0189, B:69:0x0191, B:74:0x01a7, B:77:0x01b7, B:78:0x01d9, B:79:0x01e5, B:80:0x0208, B:90:0x029e, B:92:0x02b0, B:42:0x00d8, B:38:0x00c4, B:21:0x005c), top: B:119:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0151  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void _put(java.io.InputStream r37, java.lang.String r38, com.jcraft.jsch.SftpProgressMonitor r39, int r40) throws com.jcraft.jsch.SftpException {
        /*
            Method dump skipped, instructions count: 781
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp._put(java.io.InputStream, java.lang.String, com.jcraft.jsch.SftpProgressMonitor, int):void");
    }

    public OutputStream put(String dst) throws SftpException {
        return put(dst, (SftpProgressMonitor) null, 0);
    }

    public OutputStream put(String dst, int mode) throws SftpException {
        return put(dst, (SftpProgressMonitor) null, mode);
    }

    public OutputStream put(String dst, SftpProgressMonitor monitor, int mode) throws SftpException {
        return put(dst, monitor, mode, 0L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x007a, code lost:
        r4 = r18.buf.getInt();
        throwStatusError(r18.buf, r4);
     */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004d A[Catch: Exception -> 0x00ba, TryCatch #2 {Exception -> 0x00ba, blocks: (B:7:0x001a, B:9:0x0020, B:19:0x0041, B:21:0x004d, B:23:0x0054, B:28:0x0072, B:29:0x0077, B:31:0x007a, B:32:0x0085, B:22:0x0051, B:43:0x00a3, B:44:0x00b9), top: B:65:0x001a }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0051 A[Catch: Exception -> 0x00ba, TryCatch #2 {Exception -> 0x00ba, blocks: (B:7:0x001a, B:9:0x0020, B:19:0x0041, B:21:0x004d, B:23:0x0054, B:28:0x0072, B:29:0x0077, B:31:0x007a, B:32:0x0085, B:22:0x0051, B:43:0x00a3, B:44:0x00b9), top: B:65:0x001a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.io.OutputStream put(java.lang.String r19, final com.jcraft.jsch.SftpProgressMonitor r20, int r21, long r22) throws com.jcraft.jsch.SftpException {
        /*
            Method dump skipped, instructions count: 224
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp.put(java.lang.String, com.jcraft.jsch.SftpProgressMonitor, int, long):java.io.OutputStream");
    }

    public void get(String src, String dst) throws SftpException {
        get(src, dst, null, 0);
    }

    public void get(String src, String dst, SftpProgressMonitor monitor) throws SftpException {
        get(src, dst, monitor, 0);
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x01c6 A[Catch: Exception -> 0x01cb, TryCatch #4 {Exception -> 0x01cb, blocks: (B:61:0x015e, B:63:0x0164, B:65:0x0173, B:77:0x01aa, B:85:0x01c6, B:87:0x01ca), top: B:136:0x015e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void get(java.lang.String r28, java.lang.String r29, com.jcraft.jsch.SftpProgressMonitor r30, int r31) throws com.jcraft.jsch.SftpException {
        /*
            Method dump skipped, instructions count: 608
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp.get(java.lang.String, java.lang.String, com.jcraft.jsch.SftpProgressMonitor, int):void");
    }

    public void get(String src, OutputStream dst) throws SftpException {
        get(src, dst, null, 0, 0L);
    }

    public void get(String src, OutputStream dst, SftpProgressMonitor monitor) throws SftpException {
        get(src, dst, monitor, 0, 0L);
    }

    public void get(String src, OutputStream dst, SftpProgressMonitor monitor, int mode, long skip) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            String src2 = isUnique(remoteAbsolutePath(src));
            if (monitor != null) {
                SftpATTRS attr = _stat(src2);
                monitor.init(1, src2, "??", attr.getSize());
                if (mode == 1) {
                    monitor.count(skip);
                }
            }
            _get(src2, dst, monitor, mode, skip);
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:90:0x01f5  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0207  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void _get(java.lang.String r30, java.io.OutputStream r31, com.jcraft.jsch.SftpProgressMonitor r32, int r33, long r34) throws com.jcraft.jsch.SftpException {
        /*
            Method dump skipped, instructions count: 523
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp._get(java.lang.String, java.io.OutputStream, com.jcraft.jsch.SftpProgressMonitor, int, long):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RequestQueue {
        int count;
        int head;
        Request[] rrq;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class OutOfOrderException extends Exception {
            long offset;

            OutOfOrderException(long offset) {
                this.offset = offset;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class Request {
            int id;
            long length;
            long offset;

            Request() {
            }
        }

        RequestQueue(int size) {
            this.rrq = null;
            this.rrq = new Request[size];
            int i = 0;
            while (true) {
                Request[] requestArr = this.rrq;
                if (i < requestArr.length) {
                    requestArr[i] = new Request();
                    i++;
                } else {
                    init();
                    return;
                }
            }
        }

        void init() {
            this.count = 0;
            this.head = 0;
        }

        void add(int id, long offset, int length) {
            if (this.count == 0) {
                this.head = 0;
            }
            int tail = this.head + this.count;
            Request[] requestArr = this.rrq;
            if (tail >= requestArr.length) {
                tail -= requestArr.length;
            }
            Request[] requestArr2 = this.rrq;
            requestArr2[tail].id = id;
            requestArr2[tail].offset = offset;
            requestArr2[tail].length = length;
            this.count++;
        }

        Request get(int id) throws OutOfOrderException, SftpException {
            this.count--;
            int i = this.head;
            this.head++;
            if (this.head == this.rrq.length) {
                this.head = 0;
            }
            if (this.rrq[i].id != id) {
                long offset = getOffset();
                boolean find = false;
                int j = 0;
                while (true) {
                    Request[] requestArr = this.rrq;
                    if (j >= requestArr.length) {
                        break;
                    } else if (requestArr[j].id != id) {
                        j++;
                    } else {
                        find = true;
                        this.rrq[j].id = 0;
                        break;
                    }
                }
                if (find) {
                    throw new OutOfOrderException(offset);
                }
                throw new SftpException(4, "RequestQueue: unknown request id " + id);
            }
            Request[] requestArr2 = this.rrq;
            requestArr2[i].id = 0;
            return requestArr2[i];
        }

        int count() {
            return this.count;
        }

        int size() {
            return this.rrq.length;
        }

        void cancel(Header header, Buffer buf) throws IOException {
            int _count = this.count;
            for (int i = 0; i < _count; i++) {
                header = ChannelSftp.this.header(buf, header);
                int length = header.length;
                int j = 0;
                while (true) {
                    Request[] requestArr = this.rrq;
                    if (j < requestArr.length) {
                        if (requestArr[j].id != header.rid) {
                            j++;
                        } else {
                            this.rrq[j].id = 0;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                ChannelSftp.this.skip(length);
            }
            init();
        }

        long getOffset() {
            long result = Long.MAX_VALUE;
            int i = 0;
            while (true) {
                Request[] requestArr = this.rrq;
                if (i < requestArr.length) {
                    if (requestArr[i].id != 0 && result > this.rrq[i].offset) {
                        result = this.rrq[i].offset;
                    }
                    i++;
                } else {
                    return result;
                }
            }
        }
    }

    public InputStream get(String src) throws SftpException {
        return get(src, (SftpProgressMonitor) null, 0L);
    }

    public InputStream get(String src, SftpProgressMonitor monitor) throws SftpException {
        return get(src, monitor, 0L);
    }

    public InputStream get(String src, int mode) throws SftpException {
        return get(src, (SftpProgressMonitor) null, 0L);
    }

    public InputStream get(String src, SftpProgressMonitor monitor, int mode) throws SftpException {
        return get(src, monitor, 0L);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0094  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.io.InputStream get(java.lang.String r18, final com.jcraft.jsch.SftpProgressMonitor r19, final long r20) throws com.jcraft.jsch.SftpException {
        /*
            r17 = this;
            r7 = r17
            java.lang.String r8 = ""
            r9 = 4
            java.io.InputStream r0 = r7.io_in     // Catch: java.lang.Exception -> L8d
            com.jcraft.jsch.Channel$MyPipedInputStream r0 = (com.jcraft.jsch.Channel.MyPipedInputStream) r0     // Catch: java.lang.Exception -> L8d
            r0.updateReadSide()     // Catch: java.lang.Exception -> L8d
            java.lang.String r0 = r17.remoteAbsolutePath(r18)     // Catch: java.lang.Exception -> L8d
            r1 = r0
            java.lang.String r0 = r7.isUnique(r1)     // Catch: java.lang.Exception -> L8a
            r5 = r0
            java.lang.String r0 = r7.fEncoding     // Catch: java.lang.Exception -> L87
            byte[] r0 = com.jcraft.jsch.Util.str2byte(r5, r0)     // Catch: java.lang.Exception -> L87
            com.jcraft.jsch.SftpATTRS r1 = r7._stat(r0)     // Catch: java.lang.Exception -> L87
            r16 = r1
            if (r19 == 0) goto L35
            r11 = 1
            java.lang.String r13 = "??"
            long r14 = r16.getSize()     // Catch: java.lang.Exception -> L32
            r10 = r19
            r12 = r5
            r10.init(r11, r12, r13, r14)     // Catch: java.lang.Exception -> L32
            goto L35
        L32:
            r0 = move-exception
            r14 = r5
            goto L90
        L35:
            r7.sendOPENR(r0)     // Catch: java.lang.Exception -> L87
            com.jcraft.jsch.ChannelSftp$Header r1 = new com.jcraft.jsch.ChannelSftp$Header     // Catch: java.lang.Exception -> L87
            r1.<init>()     // Catch: java.lang.Exception -> L87
            com.jcraft.jsch.Buffer r2 = r7.buf     // Catch: java.lang.Exception -> L87
            com.jcraft.jsch.ChannelSftp$Header r2 = r7.header(r2, r1)     // Catch: java.lang.Exception -> L87
            r10 = r2
            int r1 = r10.length     // Catch: java.lang.Exception -> L87
            r11 = r1
            int r1 = r10.type     // Catch: java.lang.Exception -> L87
            r12 = r1
            com.jcraft.jsch.Buffer r1 = r7.buf     // Catch: java.lang.Exception -> L87
            r7.fill(r1, r11)     // Catch: java.lang.Exception -> L87
            r1 = 101(0x65, float:1.42E-43)
            if (r12 == r1) goto L5e
            r2 = 102(0x66, float:1.43E-43)
            if (r12 != r2) goto L58
            goto L5e
        L58:
            com.jcraft.jsch.SftpException r1 = new com.jcraft.jsch.SftpException     // Catch: java.lang.Exception -> L32
            r1.<init>(r9, r8)     // Catch: java.lang.Exception -> L32
            throw r1     // Catch: java.lang.Exception -> L32
        L5e:
            if (r12 != r1) goto L6b
            com.jcraft.jsch.Buffer r1 = r7.buf     // Catch: java.lang.Exception -> L32
            int r1 = r1.getInt()     // Catch: java.lang.Exception -> L32
            com.jcraft.jsch.Buffer r2 = r7.buf     // Catch: java.lang.Exception -> L32
            r7.throwStatusError(r2, r1)     // Catch: java.lang.Exception -> L32
        L6b:
            com.jcraft.jsch.Buffer r1 = r7.buf     // Catch: java.lang.Exception -> L87
            byte[] r6 = r1.getString()     // Catch: java.lang.Exception -> L87
            com.jcraft.jsch.ChannelSftp$RequestQueue r1 = r7.rq     // Catch: java.lang.Exception -> L87
            r1.init()     // Catch: java.lang.Exception -> L87
            com.jcraft.jsch.ChannelSftp$2 r13 = new com.jcraft.jsch.ChannelSftp$2     // Catch: java.lang.Exception -> L87
            r1 = r13
            r2 = r17
            r3 = r20
            r14 = r5
            r5 = r19
            r1.<init>()     // Catch: java.lang.Exception -> L85
            r1 = r13
            return r1
        L85:
            r0 = move-exception
            goto L90
        L87:
            r0 = move-exception
            r14 = r5
            goto L90
        L8a:
            r0 = move-exception
            r14 = r1
            goto L90
        L8d:
            r0 = move-exception
            r14 = r18
        L90:
            boolean r1 = r0 instanceof com.jcraft.jsch.SftpException
            if (r1 != 0) goto La4
            boolean r1 = r0 instanceof java.lang.Throwable
            if (r1 == 0) goto L9e
            com.jcraft.jsch.SftpException r1 = new com.jcraft.jsch.SftpException
            r1.<init>(r9, r8, r0)
            throw r1
        L9e:
            com.jcraft.jsch.SftpException r1 = new com.jcraft.jsch.SftpException
            r1.<init>(r9, r8)
            throw r1
        La4:
            r1 = r0
            com.jcraft.jsch.SftpException r1 = (com.jcraft.jsch.SftpException) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp.get(java.lang.String, com.jcraft.jsch.SftpProgressMonitor, long):java.io.InputStream");
    }

    public Vector ls(String path) throws SftpException {
        final Vector v = new Vector();
        LsEntrySelector selector = new LsEntrySelector() { // from class: com.jcraft.jsch.ChannelSftp.3
            @Override // com.jcraft.jsch.ChannelSftp.LsEntrySelector
            public int select(LsEntry entry) {
                v.addElement(entry);
                return 0;
            }
        };
        ls(path, selector);
        return v;
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x0261  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0273  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void ls(java.lang.String r25, com.jcraft.jsch.ChannelSftp.LsEntrySelector r26) throws com.jcraft.jsch.SftpException {
        /*
            Method dump skipped, instructions count: 631
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp.ls(java.lang.String, com.jcraft.jsch.ChannelSftp$LsEntrySelector):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0048, code lost:
        r6 = r10.buf.getInt();
        r7 = null;
        r8 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0050, code lost:
        if (r8 >= r6) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0052, code lost:
        r7 = r10.buf.getString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x005b, code lost:
        if (r10.server_version > 3) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x005d, code lost:
        r10.buf.getString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0062, code lost:
        com.jcraft.jsch.SftpATTRS.getATTR(r10.buf);
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
        return com.jcraft.jsch.Util.byte2str(r7, r10.fEncoding);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String readlink(java.lang.String r11) throws com.jcraft.jsch.SftpException {
        /*
            r10 = this;
            java.lang.String r0 = ""
            r1 = 4
            int r2 = r10.server_version     // Catch: java.lang.Exception -> L89
            r3 = 3
            if (r2 < r3) goto L7f
            java.io.InputStream r2 = r10.io_in     // Catch: java.lang.Exception -> L89
            com.jcraft.jsch.Channel$MyPipedInputStream r2 = (com.jcraft.jsch.Channel.MyPipedInputStream) r2     // Catch: java.lang.Exception -> L89
            r2.updateReadSide()     // Catch: java.lang.Exception -> L89
            java.lang.String r2 = r10.remoteAbsolutePath(r11)     // Catch: java.lang.Exception -> L89
            r11 = r2
            java.lang.String r2 = r10.isUnique(r11)     // Catch: java.lang.Exception -> L89
            r11 = r2
            java.lang.String r2 = r10.fEncoding     // Catch: java.lang.Exception -> L89
            byte[] r2 = com.jcraft.jsch.Util.str2byte(r11, r2)     // Catch: java.lang.Exception -> L89
            r10.sendREADLINK(r2)     // Catch: java.lang.Exception -> L89
            com.jcraft.jsch.ChannelSftp$Header r2 = new com.jcraft.jsch.ChannelSftp$Header     // Catch: java.lang.Exception -> L89
            r2.<init>()     // Catch: java.lang.Exception -> L89
            com.jcraft.jsch.Buffer r4 = r10.buf     // Catch: java.lang.Exception -> L89
            com.jcraft.jsch.ChannelSftp$Header r4 = r10.header(r4, r2)     // Catch: java.lang.Exception -> L89
            r2 = r4
            int r4 = r2.length     // Catch: java.lang.Exception -> L89
            int r5 = r2.type     // Catch: java.lang.Exception -> L89
            com.jcraft.jsch.Buffer r6 = r10.buf     // Catch: java.lang.Exception -> L89
            r10.fill(r6, r4)     // Catch: java.lang.Exception -> L89
            r6 = 101(0x65, float:1.42E-43)
            r7 = 104(0x68, float:1.46E-43)
            if (r5 == r6) goto L46
            if (r5 != r7) goto L40
            goto L46
        L40:
            com.jcraft.jsch.SftpException r3 = new com.jcraft.jsch.SftpException     // Catch: java.lang.Exception -> L89
            r3.<init>(r1, r0)     // Catch: java.lang.Exception -> L89
            throw r3     // Catch: java.lang.Exception -> L89
        L46:
            if (r5 != r7) goto L71
            com.jcraft.jsch.Buffer r6 = r10.buf     // Catch: java.lang.Exception -> L89
            int r6 = r6.getInt()     // Catch: java.lang.Exception -> L89
            r7 = 0
            r8 = 0
        L50:
            if (r8 >= r6) goto L6a
            com.jcraft.jsch.Buffer r9 = r10.buf     // Catch: java.lang.Exception -> L89
            byte[] r9 = r9.getString()     // Catch: java.lang.Exception -> L89
            r7 = r9
            int r9 = r10.server_version     // Catch: java.lang.Exception -> L89
            if (r9 > r3) goto L62
            com.jcraft.jsch.Buffer r9 = r10.buf     // Catch: java.lang.Exception -> L89
            r9.getString()     // Catch: java.lang.Exception -> L89
        L62:
            com.jcraft.jsch.Buffer r9 = r10.buf     // Catch: java.lang.Exception -> L89
            com.jcraft.jsch.SftpATTRS.getATTR(r9)     // Catch: java.lang.Exception -> L89
            int r8 = r8 + 1
            goto L50
        L6a:
            java.lang.String r3 = r10.fEncoding     // Catch: java.lang.Exception -> L89
            java.lang.String r0 = com.jcraft.jsch.Util.byte2str(r7, r3)     // Catch: java.lang.Exception -> L89
            return r0
        L71:
            com.jcraft.jsch.Buffer r3 = r10.buf     // Catch: java.lang.Exception -> L89
            int r3 = r3.getInt()     // Catch: java.lang.Exception -> L89
            com.jcraft.jsch.Buffer r6 = r10.buf     // Catch: java.lang.Exception -> L89
            r10.throwStatusError(r6, r3)     // Catch: java.lang.Exception -> L89
            r0 = 0
            return r0
        L7f:
            com.jcraft.jsch.SftpException r2 = new com.jcraft.jsch.SftpException     // Catch: java.lang.Exception -> L89
            r3 = 8
            java.lang.String r4 = "The remote sshd is too old to support symlink operation."
            r2.<init>(r3, r4)     // Catch: java.lang.Exception -> L89
            throw r2     // Catch: java.lang.Exception -> L89
        L89:
            r2 = move-exception
            boolean r3 = r2 instanceof com.jcraft.jsch.SftpException
            if (r3 != 0) goto L9e
            boolean r3 = r2 instanceof java.lang.Throwable
            if (r3 == 0) goto L98
            com.jcraft.jsch.SftpException r3 = new com.jcraft.jsch.SftpException
            r3.<init>(r1, r0, r2)
            throw r3
        L98:
            com.jcraft.jsch.SftpException r3 = new com.jcraft.jsch.SftpException
            r3.<init>(r1, r0)
            throw r3
        L9e:
            r0 = r2
            com.jcraft.jsch.SftpException r0 = (com.jcraft.jsch.SftpException) r0
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp.readlink(java.lang.String):java.lang.String");
    }

    public void symlink(String oldpath, String newpath) throws SftpException {
        String oldpath2;
        if (this.server_version < 3) {
            throw new SftpException(8, "The remote sshd is too old to support symlink operation.");
        }
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            String _oldpath = remoteAbsolutePath(oldpath);
            String newpath2 = remoteAbsolutePath(newpath);
            String _oldpath2 = isUnique(_oldpath);
            int i = 0;
            if (oldpath.charAt(0) != '/') {
                String cwd = getCwd();
                int length = cwd.length();
                if (!cwd.endsWith("/")) {
                    i = 1;
                }
                oldpath2 = _oldpath2.substring(length + i);
            } else {
                oldpath2 = _oldpath2;
            }
            if (isPattern(newpath2)) {
                throw new SftpException(4, newpath2);
            }
            sendSYMLINK(Util.str2byte(oldpath2, this.fEncoding), Util.str2byte(Util.unquote(newpath2), this.fEncoding));
            Header header = header(this.buf, new Header());
            int length2 = header.length;
            int type = header.type;
            fill(this.buf, length2);
            if (type != 101) {
                throw new SftpException(4, "");
            }
            int i2 = this.buf.getInt();
            if (i2 == 0) {
                return;
            }
            throwStatusError(this.buf, i2);
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void hardlink(String oldpath, String newpath) throws SftpException {
        String oldpath2;
        if (!this.extension_hardlink) {
            throw new SftpException(8, "hardlink@openssh.com is not supported");
        }
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            String _oldpath = remoteAbsolutePath(oldpath);
            String newpath2 = remoteAbsolutePath(newpath);
            String _oldpath2 = isUnique(_oldpath);
            int i = 0;
            if (oldpath.charAt(0) != '/') {
                String cwd = getCwd();
                int length = cwd.length();
                if (!cwd.endsWith("/")) {
                    i = 1;
                }
                oldpath2 = _oldpath2.substring(length + i);
            } else {
                oldpath2 = _oldpath2;
            }
            if (isPattern(newpath2)) {
                throw new SftpException(4, newpath2);
            }
            sendHARDLINK(Util.str2byte(oldpath2, this.fEncoding), Util.str2byte(Util.unquote(newpath2), this.fEncoding));
            Header header = header(this.buf, new Header());
            int length2 = header.length;
            int type = header.type;
            fill(this.buf, length2);
            if (type != 101) {
                throw new SftpException(4, "");
            }
            int i2 = this.buf.getInt();
            if (i2 == 0) {
                return;
            }
            throwStatusError(this.buf, i2);
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void rename(String oldpath, String newpath) throws SftpException {
        String newpath2;
        if (this.server_version < 2) {
            throw new SftpException(8, "The remote sshd is too old to support rename operation.");
        }
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            String oldpath2 = remoteAbsolutePath(oldpath);
            String newpath3 = remoteAbsolutePath(newpath);
            String oldpath3 = isUnique(oldpath2);
            Vector v = glob_remote(newpath3);
            int vsize = v.size();
            if (vsize >= 2) {
                throw new SftpException(4, v.toString());
            }
            if (vsize == 1) {
                newpath2 = (String) v.elementAt(0);
            } else if (isPattern(newpath3)) {
                throw new SftpException(4, newpath3);
            } else {
                newpath2 = Util.unquote(newpath3);
            }
            sendRENAME(Util.str2byte(oldpath3, this.fEncoding), Util.str2byte(newpath2, this.fEncoding));
            Header header = header(this.buf, new Header());
            int length = header.length;
            int type = header.type;
            fill(this.buf, length);
            if (type != 101) {
                throw new SftpException(4, "");
            }
            int i = this.buf.getInt();
            if (i == 0) {
                return;
            }
            throwStatusError(this.buf, i);
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void rm(String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            Vector v = glob_remote(remoteAbsolutePath(path));
            int vsize = v.size();
            Header header = new Header();
            for (int j = 0; j < vsize; j++) {
                String path2 = (String) v.elementAt(j);
                sendREMOVE(Util.str2byte(path2, this.fEncoding));
                header = header(this.buf, header);
                int length = header.length;
                int type = header.type;
                fill(this.buf, length);
                if (type != 101) {
                    throw new SftpException(4, "");
                }
                int i = this.buf.getInt();
                if (i != 0) {
                    throwStatusError(this.buf, i);
                }
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private boolean isRemoteDir(String path) {
        try {
            sendSTAT(Util.str2byte(path, this.fEncoding));
            Header header = header(this.buf, new Header());
            int length = header.length;
            int type = header.type;
            fill(this.buf, length);
            if (type != 105) {
                return false;
            }
            SftpATTRS attr = SftpATTRS.getATTR(this.buf);
            return attr.isDir();
        } catch (Exception e) {
            return false;
        }
    }

    public void chgrp(int gid, String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            Vector v = glob_remote(remoteAbsolutePath(path));
            int vsize = v.size();
            for (int j = 0; j < vsize; j++) {
                String path2 = (String) v.elementAt(j);
                SftpATTRS attr = _stat(path2);
                attr.setFLAGS(0);
                attr.setUIDGID(attr.uid, gid);
                _setStat(path2, attr);
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void chown(int uid, String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            Vector v = glob_remote(remoteAbsolutePath(path));
            int vsize = v.size();
            for (int j = 0; j < vsize; j++) {
                String path2 = (String) v.elementAt(j);
                SftpATTRS attr = _stat(path2);
                attr.setFLAGS(0);
                attr.setUIDGID(uid, attr.gid);
                _setStat(path2, attr);
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void chmod(int permissions, String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            Vector v = glob_remote(remoteAbsolutePath(path));
            int vsize = v.size();
            for (int j = 0; j < vsize; j++) {
                String path2 = (String) v.elementAt(j);
                SftpATTRS attr = _stat(path2);
                attr.setFLAGS(0);
                attr.setPERMISSIONS(permissions);
                _setStat(path2, attr);
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void setMtime(String path, int mtime) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            Vector v = glob_remote(remoteAbsolutePath(path));
            int vsize = v.size();
            for (int j = 0; j < vsize; j++) {
                String path2 = (String) v.elementAt(j);
                SftpATTRS attr = _stat(path2);
                attr.setFLAGS(0);
                attr.setACMODTIME(attr.getATime(), mtime);
                _setStat(path2, attr);
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void rmdir(String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            Vector v = glob_remote(remoteAbsolutePath(path));
            int vsize = v.size();
            Header header = new Header();
            for (int j = 0; j < vsize; j++) {
                String path2 = (String) v.elementAt(j);
                sendRMDIR(Util.str2byte(path2, this.fEncoding));
                header = header(this.buf, header);
                int length = header.length;
                int type = header.type;
                fill(this.buf, length);
                if (type != 101) {
                    throw new SftpException(4, "");
                }
                int i = this.buf.getInt();
                if (i != 0) {
                    throwStatusError(this.buf, i);
                }
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public void mkdir(String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            sendMKDIR(Util.str2byte(remoteAbsolutePath(path), this.fEncoding), null);
            Header header = header(this.buf, new Header());
            int length = header.length;
            int type = header.type;
            fill(this.buf, length);
            if (type != 101) {
                throw new SftpException(4, "");
            }
            int i = this.buf.getInt();
            if (i == 0) {
                return;
            }
            throwStatusError(this.buf, i);
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public SftpATTRS stat(String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            return _stat(isUnique(remoteAbsolutePath(path)));
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private SftpATTRS _stat(byte[] path) throws SftpException {
        try {
            sendSTAT(path);
            Header header = header(this.buf, new Header());
            int length = header.length;
            int type = header.type;
            fill(this.buf, length);
            if (type != 105) {
                if (type == 101) {
                    int i = this.buf.getInt();
                    throwStatusError(this.buf, i);
                }
                throw new SftpException(4, "");
            }
            SftpATTRS attr = SftpATTRS.getATTR(this.buf);
            return attr;
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private SftpATTRS _stat(String path) throws SftpException {
        return _stat(Util.str2byte(path, this.fEncoding));
    }

    public SftpStatVFS statVFS(String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            return _statVFS(isUnique(remoteAbsolutePath(path)));
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private SftpStatVFS _statVFS(byte[] path) throws SftpException {
        if (!this.extension_statvfs) {
            throw new SftpException(8, "statvfs@openssh.com is not supported");
        }
        try {
            sendSTATVFS(path);
            Header header = header(this.buf, new Header());
            int length = header.length;
            int type = header.type;
            fill(this.buf, length);
            if (type != 201) {
                if (type == 101) {
                    int i = this.buf.getInt();
                    throwStatusError(this.buf, i);
                }
                throw new SftpException(4, "");
            }
            SftpStatVFS stat = SftpStatVFS.getStatVFS(this.buf);
            return stat;
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private SftpStatVFS _statVFS(String path) throws SftpException {
        return _statVFS(Util.str2byte(path, this.fEncoding));
    }

    public SftpATTRS lstat(String path) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            return _lstat(isUnique(remoteAbsolutePath(path)));
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private SftpATTRS _lstat(String path) throws SftpException {
        try {
            sendLSTAT(Util.str2byte(path, this.fEncoding));
            Header header = header(this.buf, new Header());
            int length = header.length;
            int type = header.type;
            fill(this.buf, length);
            if (type != 105) {
                if (type == 101) {
                    int i = this.buf.getInt();
                    throwStatusError(this.buf, i);
                }
                throw new SftpException(4, "");
            }
            SftpATTRS attr = SftpATTRS.getATTR(this.buf);
            return attr;
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private byte[] _realpath(String path) throws SftpException, IOException, Exception {
        sendREALPATH(Util.str2byte(path, this.fEncoding));
        Header header = header(this.buf, new Header());
        int length = header.length;
        int type = header.type;
        fill(this.buf, length);
        if (type != 101 && type != 104) {
            throw new SftpException(4, "");
        }
        if (type == 101) {
            int i = this.buf.getInt();
            throwStatusError(this.buf, i);
        }
        int i2 = this.buf.getInt();
        byte[] str = null;
        while (true) {
            int i3 = i2 - 1;
            if (i2 > 0) {
                str = this.buf.getString();
                if (this.server_version <= 3) {
                    this.buf.getString();
                }
                SftpATTRS.getATTR(this.buf);
                i2 = i3;
            } else {
                return str;
            }
        }
    }

    public void setStat(String path, SftpATTRS attr) throws SftpException {
        try {
            ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
            Vector v = glob_remote(remoteAbsolutePath(path));
            int vsize = v.size();
            for (int j = 0; j < vsize; j++) {
                String path2 = (String) v.elementAt(j);
                _setStat(path2, attr);
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    private void _setStat(String path, SftpATTRS attr) throws SftpException {
        try {
            sendSETSTAT(Util.str2byte(path, this.fEncoding), attr);
            Header header = header(this.buf, new Header());
            int length = header.length;
            int type = header.type;
            fill(this.buf, length);
            if (type != 101) {
                throw new SftpException(4, "");
            }
            int i = this.buf.getInt();
            if (i != 0) {
                throwStatusError(this.buf, i);
            }
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    public String pwd() throws SftpException {
        return getCwd();
    }

    public String lpwd() {
        return this.lcwd;
    }

    public String version() {
        return this.version;
    }

    public String getHome() throws SftpException {
        if (this.home == null) {
            try {
                ((Channel.MyPipedInputStream) this.io_in).updateReadSide();
                byte[] _home = _realpath("");
                this.home = Util.byte2str(_home, this.fEncoding);
            } catch (Exception e) {
                if (e instanceof SftpException) {
                    throw ((SftpException) e);
                }
                if (e instanceof Throwable) {
                    throw new SftpException(4, "", e);
                }
                throw new SftpException(4, "");
            }
        }
        return this.home;
    }

    private String getCwd() throws SftpException {
        if (this.cwd == null) {
            this.cwd = getHome();
        }
        return this.cwd;
    }

    private void setCwd(String cwd) {
        this.cwd = cwd;
    }

    private void read(byte[] buf, int s, int l) throws IOException, SftpException {
        while (l > 0) {
            int i = this.io_in.read(buf, s, l);
            if (i <= 0) {
                throw new SftpException(4, "");
            }
            s += i;
            l -= i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkStatus(int[] ackid, Header header) throws IOException, SftpException {
        Header header2 = header(this.buf, header);
        int length = header2.length;
        int type = header2.type;
        if (ackid != null) {
            ackid[0] = header2.rid;
        }
        fill(this.buf, length);
        if (type != 101) {
            throw new SftpException(4, "");
        }
        int i = this.buf.getInt();
        if (i != 0) {
            throwStatusError(this.buf, i);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean _sendCLOSE(byte[] handle, Header header) throws Exception {
        sendCLOSE(handle);
        return checkStatus(null, header);
    }

    private void sendINIT() throws Exception {
        this.packet.reset();
        putHEAD((byte) 1, 5);
        this.buf.putInt(3);
        getSession().write(this.packet, this, 9);
    }

    private void sendREALPATH(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_REALPATH, path);
    }

    private void sendSTAT(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_STAT, path);
    }

    private void sendSTATVFS(byte[] path) throws Exception {
        sendPacketPath((byte) 0, path, "statvfs@openssh.com");
    }

    private void sendLSTAT(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_LSTAT, path);
    }

    private void sendFSTAT(byte[] handle) throws Exception {
        sendPacketPath(SSH_FXP_FSTAT, handle);
    }

    private void sendSETSTAT(byte[] path, SftpATTRS attr) throws Exception {
        this.packet.reset();
        putHEAD(SSH_FXP_SETSTAT, path.length + 9 + attr.length());
        Buffer buffer = this.buf;
        int i = this.seq;
        this.seq = i + 1;
        buffer.putInt(i);
        this.buf.putString(path);
        attr.dump(this.buf);
        getSession().write(this.packet, this, path.length + 9 + attr.length() + 4);
    }

    private void sendREMOVE(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_REMOVE, path);
    }

    private void sendMKDIR(byte[] path, SftpATTRS attr) throws Exception {
        this.packet.reset();
        putHEAD(SSH_FXP_MKDIR, path.length + 9 + (attr != null ? attr.length() : 4));
        Buffer buffer = this.buf;
        int i = this.seq;
        this.seq = i + 1;
        buffer.putInt(i);
        this.buf.putString(path);
        if (attr != null) {
            attr.dump(this.buf);
        } else {
            this.buf.putInt(0);
        }
        getSession().write(this.packet, this, path.length + 9 + (attr != null ? attr.length() : 4) + 4);
    }

    private void sendRMDIR(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_RMDIR, path);
    }

    private void sendSYMLINK(byte[] p1, byte[] p2) throws Exception {
        sendPacketPath(SSH_FXP_SYMLINK, p1, p2);
    }

    private void sendHARDLINK(byte[] p1, byte[] p2) throws Exception {
        sendPacketPath((byte) 0, p1, p2, "hardlink@openssh.com");
    }

    private void sendREADLINK(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_READLINK, path);
    }

    private void sendOPENDIR(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_OPENDIR, path);
    }

    private void sendREADDIR(byte[] path) throws Exception {
        sendPacketPath(SSH_FXP_READDIR, path);
    }

    private void sendRENAME(byte[] p1, byte[] p2) throws Exception {
        sendPacketPath(SSH_FXP_RENAME, p1, p2, this.extension_posix_rename ? "posix-rename@openssh.com" : null);
    }

    private void sendCLOSE(byte[] path) throws Exception {
        sendPacketPath((byte) 4, path);
    }

    private void sendOPENR(byte[] path) throws Exception {
        sendOPEN(path, 1);
    }

    private void sendOPENW(byte[] path) throws Exception {
        sendOPEN(path, 26);
    }

    private void sendOPENA(byte[] path) throws Exception {
        sendOPEN(path, 10);
    }

    private void sendOPEN(byte[] path, int mode) throws Exception {
        this.packet.reset();
        putHEAD((byte) 3, path.length + 17);
        Buffer buffer = this.buf;
        int i = this.seq;
        this.seq = i + 1;
        buffer.putInt(i);
        this.buf.putString(path);
        this.buf.putInt(mode);
        this.buf.putInt(0);
        getSession().write(this.packet, this, path.length + 17 + 4);
    }

    private void sendPacketPath(byte fxp, byte[] path) throws Exception {
        sendPacketPath(fxp, path, (String) null);
    }

    private void sendPacketPath(byte fxp, byte[] path, String extension) throws Exception {
        this.packet.reset();
        int len = path.length + 9;
        if (extension == null) {
            putHEAD(fxp, len);
            Buffer buffer = this.buf;
            int i = this.seq;
            this.seq = i + 1;
            buffer.putInt(i);
        } else {
            len += extension.length() + 4;
            putHEAD(SSH_FXP_EXTENDED, len);
            Buffer buffer2 = this.buf;
            int i2 = this.seq;
            this.seq = i2 + 1;
            buffer2.putInt(i2);
            this.buf.putString(Util.str2byte(extension));
        }
        this.buf.putString(path);
        getSession().write(this.packet, this, len + 4);
    }

    private void sendPacketPath(byte fxp, byte[] p1, byte[] p2) throws Exception {
        sendPacketPath(fxp, p1, p2, null);
    }

    private void sendPacketPath(byte fxp, byte[] p1, byte[] p2, String extension) throws Exception {
        this.packet.reset();
        int len = p1.length + 13 + p2.length;
        if (extension == null) {
            putHEAD(fxp, len);
            Buffer buffer = this.buf;
            int i = this.seq;
            this.seq = i + 1;
            buffer.putInt(i);
        } else {
            len += extension.length() + 4;
            putHEAD(SSH_FXP_EXTENDED, len);
            Buffer buffer2 = this.buf;
            int i2 = this.seq;
            this.seq = i2 + 1;
            buffer2.putInt(i2);
            this.buf.putString(Util.str2byte(extension));
        }
        this.buf.putString(p1);
        this.buf.putString(p2);
        getSession().write(this.packet, this, len + 4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sendWRITE(byte[] handle, long offset, byte[] data, int start, int length) throws Exception {
        int _length = length;
        this.opacket.reset();
        if (this.obuf.buffer.length < this.obuf.index + 13 + 21 + handle.length + length + 128) {
            _length = this.obuf.buffer.length - ((((this.obuf.index + 13) + 21) + handle.length) + 128);
        }
        putHEAD(this.obuf, (byte) 6, handle.length + 21 + _length);
        Buffer buffer = this.obuf;
        int i = this.seq;
        this.seq = i + 1;
        buffer.putInt(i);
        this.obuf.putString(handle);
        this.obuf.putLong(offset);
        if (this.obuf.buffer != data) {
            this.obuf.putString(data, start, _length);
        } else {
            this.obuf.putInt(_length);
            this.obuf.skip(_length);
        }
        getSession().write(this.opacket, this, handle.length + 21 + _length + 4);
        return _length;
    }

    private void sendREAD(byte[] handle, long offset, int length) throws Exception {
        sendREAD(handle, offset, length, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendREAD(byte[] handle, long offset, int length, RequestQueue rrq) throws Exception {
        this.packet.reset();
        putHEAD((byte) 5, handle.length + 21);
        Buffer buffer = this.buf;
        int i = this.seq;
        this.seq = i + 1;
        buffer.putInt(i);
        this.buf.putString(handle);
        this.buf.putLong(offset);
        this.buf.putInt(length);
        getSession().write(this.packet, this, handle.length + 21 + 4);
        if (rrq != null) {
            rrq.add(this.seq - 1, offset, length);
        }
    }

    private void putHEAD(Buffer buf, byte type, int length) throws Exception {
        buf.putByte((byte) 94);
        buf.putInt(this.recipient);
        buf.putInt(length + 4);
        buf.putInt(length);
        buf.putByte(type);
    }

    private void putHEAD(byte type, int length) throws Exception {
        putHEAD(this.buf, type, length);
    }

    private Vector glob_remote(String _path) throws Exception {
        byte[][] _pattern_utf8;
        byte[] handle;
        String pdir;
        ChannelSftp channelSftp = this;
        Vector v = new Vector();
        int i = 0;
        int foo = _path.lastIndexOf(47);
        if (foo < 0) {
            v.addElement(Util.unquote(_path));
            return v;
        }
        String dir = _path.substring(0, foo == 0 ? 1 : foo);
        String _pattern = _path.substring(foo + 1);
        String dir2 = Util.unquote(dir);
        byte[][] _pattern_utf82 = new byte[1];
        boolean pattern_has_wildcard = channelSftp.isPattern(_pattern, _pattern_utf82);
        if (!pattern_has_wildcard) {
            if (!dir2.equals("/")) {
                dir2 = dir2 + "/";
            }
            v.addElement(dir2 + Util.unquote(_pattern));
            return v;
        }
        byte[] pattern = _pattern_utf82[0];
        channelSftp.sendOPENDIR(Util.str2byte(dir2, channelSftp.fEncoding));
        Header header = channelSftp.header(channelSftp.buf, new Header());
        int length = header.length;
        int type = header.type;
        channelSftp.fill(channelSftp.buf, length);
        if (type != 101 && type != 102) {
            throw new SftpException(4, "");
        }
        if (type == 101) {
            i = channelSftp.buf.getInt();
            channelSftp.throwStatusError(channelSftp.buf, i);
        }
        byte[] handle2 = channelSftp.buf.getString();
        String pdir2 = null;
        while (true) {
            channelSftp.sendREADDIR(handle2);
            header = channelSftp.header(channelSftp.buf, header);
            int length2 = header.length;
            int type2 = header.type;
            if (type2 != 101 && type2 != 104) {
                throw new SftpException(4, "");
            }
            if (type2 == 101) {
                channelSftp.fill(channelSftp.buf, length2);
                if (channelSftp._sendCLOSE(handle2, header)) {
                    return v;
                }
                return null;
            }
            channelSftp.buf.rewind();
            int i2 = i;
            int foo2 = foo;
            channelSftp.fill(channelSftp.buf.buffer, 0, 4);
            int length3 = length2 - 4;
            int count = channelSftp.buf.getInt();
            channelSftp.buf.reset();
            while (true) {
                if (count <= 0) {
                    _pattern_utf8 = _pattern_utf82;
                    handle = handle2;
                    i = i2;
                    break;
                }
                if (length3 <= 0) {
                    _pattern_utf8 = _pattern_utf82;
                    handle = handle2;
                } else {
                    channelSftp.buf.shift();
                    int j = channelSftp.buf.buffer.length > channelSftp.buf.index + length3 ? length3 : channelSftp.buf.buffer.length - channelSftp.buf.index;
                    _pattern_utf8 = _pattern_utf82;
                    handle = handle2;
                    int i3 = channelSftp.io_in.read(channelSftp.buf.buffer, channelSftp.buf.index, j);
                    if (i3 <= 0) {
                        i = i3;
                        break;
                    }
                    channelSftp.buf.index += i3;
                    length3 -= i3;
                    i2 = i3;
                }
                byte[] filename = channelSftp.buf.getString();
                if (channelSftp.server_version <= 3) {
                    channelSftp.buf.getString();
                }
                SftpATTRS.getATTR(channelSftp.buf);
                byte[] _filename = filename;
                String f = null;
                int length4 = length3;
                if (!channelSftp.fEncoding_is_utf8) {
                    f = Util.byte2str(filename, channelSftp.fEncoding);
                    _filename = Util.str2byte(f, "UTF-8");
                }
                boolean found = Util.glob(pattern, _filename);
                if (found) {
                    if (f == null) {
                        f = Util.byte2str(filename, channelSftp.fEncoding);
                    }
                    if (pdir2 != null) {
                        pdir = pdir2;
                    } else if (dir2.endsWith("/")) {
                        pdir = dir2;
                    } else {
                        pdir = dir2 + "/";
                    }
                    v.addElement(pdir + f);
                    pdir2 = pdir;
                }
                count--;
                channelSftp = this;
                _pattern_utf82 = _pattern_utf8;
                handle2 = handle;
                length3 = length4;
            }
            channelSftp = this;
            foo = foo2;
            _pattern_utf82 = _pattern_utf8;
            handle2 = handle;
        }
    }

    private boolean isPattern(byte[] path) {
        int length = path.length;
        int i = 0;
        while (i < length) {
            if (path[i] == 42 || path[i] == 63) {
                return true;
            }
            if (path[i] == 92 && i + 1 < length) {
                i++;
            }
            i++;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0040, code lost:
        if (com.jcraft.jsch.ChannelSftp.fs_is_bs == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0042, code lost:
        r1 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0044, code lost:
        r1 = com.jcraft.jsch.Util.unquote(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0048, code lost:
        r0.addElement(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x004b, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0066, code lost:
        if (com.jcraft.jsch.ChannelSftp.fs_is_bs == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0068, code lost:
        r1 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x006a, code lost:
        r1 = com.jcraft.jsch.Util.unquote(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x006e, code lost:
        r0.addElement(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0071, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.Vector glob_local(java.lang.String r12) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 217
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSftp.glob_local(java.lang.String):java.util.Vector");
    }

    private void throwStatusError(Buffer buf, int i) throws SftpException {
        if (this.server_version >= 3 && buf.getLength() >= 4) {
            byte[] str = buf.getString();
            throw new SftpException(i, Util.byte2str(str, "UTF-8"));
        }
        throw new SftpException(i, "Failure");
    }

    private static boolean isLocalAbsolutePath(String path) {
        return new File(path).isAbsolute();
    }

    @Override // com.jcraft.jsch.Channel
    public void disconnect() {
        super.disconnect();
    }

    private boolean isPattern(String path, byte[][] utf8) {
        byte[] _path = Util.str2byte(path, "UTF-8");
        if (utf8 != null) {
            utf8[0] = _path;
        }
        return isPattern(_path);
    }

    private boolean isPattern(String path) {
        return isPattern(path, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fill(Buffer buf, int len) throws IOException {
        buf.reset();
        fill(buf.buffer, 0, len);
        buf.skip(len);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int fill(byte[] buf, int s, int len) throws IOException {
        while (len > 0) {
            int i = this.io_in.read(buf, s, len);
            if (i <= 0) {
                throw new IOException("inputstream is closed");
            }
            s += i;
            len -= i;
        }
        return s - s;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void skip(long foo) throws IOException {
        while (foo > 0) {
            long bar = this.io_in.skip(foo);
            if (bar > 0) {
                foo -= bar;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Header {
        int length;
        int rid;
        int type;

        Header() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Header header(Buffer buf, Header header) throws IOException {
        buf.rewind();
        fill(buf.buffer, 0, 9);
        header.length = buf.getInt() - 5;
        header.type = buf.getByte() & 255;
        header.rid = buf.getInt();
        return header;
    }

    private String remoteAbsolutePath(String path) throws SftpException {
        if (path.charAt(0) == '/') {
            return path;
        }
        String cwd = getCwd();
        if (cwd.endsWith("/")) {
            return cwd + path;
        }
        return cwd + "/" + path;
    }

    private String localAbsolutePath(String path) {
        if (isLocalAbsolutePath(path)) {
            return path;
        }
        if (this.lcwd.endsWith(file_separator)) {
            return this.lcwd + path;
        }
        return this.lcwd + file_separator + path;
    }

    private String isUnique(String path) throws SftpException, Exception {
        Vector v = glob_remote(path);
        if (v.size() != 1) {
            throw new SftpException(4, path + " is not unique: " + v.toString());
        }
        return (String) v.elementAt(0);
    }

    public int getServerVersion() throws SftpException {
        if (!isConnected()) {
            throw new SftpException(4, "The channel is not connected.");
        }
        return this.server_version;
    }

    public void setFilenameEncoding(String encoding) throws SftpException {
        int sversion = getServerVersion();
        if (3 <= sversion && sversion <= 5 && !encoding.equals("UTF-8")) {
            throw new SftpException(4, "The encoding can not be changed for this sftp server.");
        }
        if (encoding.equals("UTF-8")) {
            encoding = "UTF-8";
        }
        this.fEncoding = encoding;
        this.fEncoding_is_utf8 = this.fEncoding.equals("UTF-8");
    }

    public String getExtension(String key) {
        Hashtable hashtable = this.extensions;
        if (hashtable == null) {
            return null;
        }
        return (String) hashtable.get(key);
    }

    public String realpath(String path) throws SftpException {
        try {
            byte[] _path = _realpath(remoteAbsolutePath(path));
            return Util.byte2str(_path, this.fEncoding);
        } catch (Exception e) {
            if (e instanceof SftpException) {
                throw ((SftpException) e);
            }
            if (e instanceof Throwable) {
                throw new SftpException(4, "", e);
            }
            throw new SftpException(4, "");
        }
    }

    /* loaded from: classes.dex */
    public class LsEntry implements Comparable {
        private SftpATTRS attrs;
        private String filename;
        private String longname;

        LsEntry(String filename, String longname, SftpATTRS attrs) {
            setFilename(filename);
            setLongname(longname);
            setAttrs(attrs);
        }

        public String getFilename() {
            return this.filename;
        }

        void setFilename(String filename) {
            this.filename = filename;
        }

        public String getLongname() {
            return this.longname;
        }

        void setLongname(String longname) {
            this.longname = longname;
        }

        public SftpATTRS getAttrs() {
            return this.attrs;
        }

        void setAttrs(SftpATTRS attrs) {
            this.attrs = attrs;
        }

        public String toString() {
            return this.longname;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object o) throws ClassCastException {
            if (o instanceof LsEntry) {
                return this.filename.compareTo(((LsEntry) o).getFilename());
            }
            throw new ClassCastException("a decendent of LsEntry must be given.");
        }
    }
}
