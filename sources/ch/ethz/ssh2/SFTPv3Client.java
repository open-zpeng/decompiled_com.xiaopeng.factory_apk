package ch.ethz.ssh2;

import ch.ethz.ssh2.packets.TypesReader;
import ch.ethz.ssh2.packets.TypesWriter;
import cn.hutool.core.text.StrPool;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Vector;
/* loaded from: classes.dex */
public class SFTPv3Client {
    String charsetName;
    final Connection conn;
    final PrintStream debug;
    boolean flag_closed;
    InputStream is;
    int next_request_id;
    OutputStream os;
    int protocol_version;
    HashMap server_extensions;
    final Session sess;

    public SFTPv3Client(Connection conn, PrintStream debug) throws IOException {
        this.flag_closed = false;
        this.protocol_version = 0;
        this.server_extensions = new HashMap();
        this.next_request_id = 1000;
        this.charsetName = null;
        if (conn == null) {
            throw new IllegalArgumentException("Cannot accept null argument!");
        }
        this.conn = conn;
        this.debug = debug;
        if (debug != null) {
            debug.println("Opening session and starting SFTP subsystem.");
        }
        this.sess = conn.openSession();
        this.sess.startSubSystem("sftp");
        this.is = this.sess.getStdout();
        this.os = new BufferedOutputStream(this.sess.getStdin(), 2048);
        if (this.is == null || this.os == null) {
            throw new IOException("There is a problem with the streams of the underlying channel.");
        }
        init();
    }

    public SFTPv3Client(Connection conn) throws IOException {
        this(conn, null);
    }

    public void setCharset(String charset) throws IOException {
        if (charset == null) {
            this.charsetName = charset;
            return;
        }
        try {
            Charset.forName(charset);
            this.charsetName = charset;
        } catch (Exception e) {
            throw ((IOException) new IOException("This charset is not supported").initCause(e));
        }
    }

    public String getCharset() {
        return this.charsetName;
    }

    private final void checkHandleValidAndOpen(SFTPv3FileHandle handle) throws IOException {
        if (handle.client != this) {
            throw new IOException("The file handle was created with another SFTPv3FileHandle instance.");
        }
        if (handle.isClosed) {
            throw new IOException("The file handle is closed.");
        }
    }

    private final void sendMessage(int type, int requestId, byte[] msg, int off, int len) throws IOException {
        int msglen = len + 1;
        if (type != 1) {
            msglen += 4;
        }
        this.os.write(msglen >> 24);
        this.os.write(msglen >> 16);
        this.os.write(msglen >> 8);
        this.os.write(msglen);
        this.os.write(type);
        if (type != 1) {
            this.os.write(requestId >> 24);
            this.os.write(requestId >> 16);
            this.os.write(requestId >> 8);
            this.os.write(requestId);
        }
        this.os.write(msg, off, len);
        this.os.flush();
    }

    private final void sendMessage(int type, int requestId, byte[] msg) throws IOException {
        sendMessage(type, requestId, msg, 0, msg.length);
    }

    private final void readBytes(byte[] buff, int pos, int len) throws IOException {
        while (len > 0) {
            int count = this.is.read(buff, pos, len);
            if (count < 0) {
                throw new IOException("Unexpected end of sftp stream.");
            }
            if (count == 0 || count > len) {
                throw new IOException("Underlying stream implementation is bogus!");
            }
            len -= count;
            pos += count;
        }
    }

    private final byte[] receiveMessage(int maxlen) throws IOException {
        byte[] msglen = new byte[4];
        readBytes(msglen, 0, 4);
        int len = ((msglen[0] & 255) << 24) | ((msglen[1] & 255) << 16) | ((msglen[2] & 255) << 8) | (msglen[3] & 255);
        if (len > maxlen || len <= 0) {
            StringBuffer stringBuffer = new StringBuffer("Illegal sftp packet len: ");
            stringBuffer.append(len);
            throw new IOException(stringBuffer.toString());
        }
        byte[] msg = new byte[len];
        readBytes(msg, 0, len);
        return msg;
    }

    private final int generateNextRequestID() {
        int i;
        synchronized (this) {
            i = this.next_request_id;
            this.next_request_id = i + 1;
        }
        return i;
    }

    private final void closeHandle(byte[] handle) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(handle, 0, handle.length);
        sendMessage(4, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    private SFTPv3FileAttributes readAttrs(TypesReader tr) throws IOException {
        SFTPv3FileAttributes fa = new SFTPv3FileAttributes();
        int flags = tr.readUINT32();
        if ((flags & 1) != 0) {
            PrintStream printStream = this.debug;
            if (printStream != null) {
                printStream.println("SSH_FILEXFER_ATTR_SIZE");
            }
            fa.size = new Long(tr.readUINT64());
        }
        if ((flags & 2) != 0) {
            PrintStream printStream2 = this.debug;
            if (printStream2 != null) {
                printStream2.println("SSH_FILEXFER_ATTR_V3_UIDGID");
            }
            fa.uid = new Integer(tr.readUINT32());
            fa.gid = new Integer(tr.readUINT32());
        }
        if ((flags & 4) != 0) {
            PrintStream printStream3 = this.debug;
            if (printStream3 != null) {
                printStream3.println("SSH_FILEXFER_ATTR_PERMISSIONS");
            }
            fa.permissions = new Integer(tr.readUINT32());
        }
        if ((flags & 8) != 0) {
            PrintStream printStream4 = this.debug;
            if (printStream4 != null) {
                printStream4.println("SSH_FILEXFER_ATTR_V3_ACMODTIME");
            }
            fa.atime = new Integer(tr.readUINT32());
            fa.mtime = new Integer(tr.readUINT32());
        }
        if ((Integer.MIN_VALUE & flags) != 0) {
            int count = tr.readUINT32();
            PrintStream printStream5 = this.debug;
            if (printStream5 != null) {
                StringBuffer stringBuffer = new StringBuffer("SSH_FILEXFER_ATTR_EXTENDED (");
                stringBuffer.append(count);
                stringBuffer.append(")");
                printStream5.println(stringBuffer.toString());
            }
            while (count > 0) {
                tr.readByteString();
                tr.readByteString();
                count--;
            }
        }
        return fa;
    }

    public SFTPv3FileAttributes fstat(SFTPv3FileHandle handle) throws IOException {
        checkHandleValidAndOpen(handle);
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(handle.fileHandle, 0, handle.fileHandle.length);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_FSTAT...");
            this.debug.flush();
        }
        sendMessage(8, req_id, tw.getBytes());
        byte[] resp = receiveMessage(34000);
        PrintStream printStream2 = this.debug;
        if (printStream2 != null) {
            printStream2.println("Got REPLY.");
            this.debug.flush();
        }
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != req_id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t == 105) {
            return readAttrs(tr);
        }
        if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int errorCode = tr.readUINT32();
        throw new SFTPException(tr.readString(), errorCode);
    }

    private SFTPv3FileAttributes statBoth(String path, int statMethod) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(path, this.charsetName);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_STAT/SSH_FXP_LSTAT...");
            this.debug.flush();
        }
        sendMessage(statMethod, req_id, tw.getBytes());
        byte[] resp = receiveMessage(34000);
        PrintStream printStream2 = this.debug;
        if (printStream2 != null) {
            printStream2.println("Got REPLY.");
            this.debug.flush();
        }
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != req_id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t == 105) {
            return readAttrs(tr);
        }
        if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int errorCode = tr.readUINT32();
        throw new SFTPException(tr.readString(), errorCode);
    }

    public SFTPv3FileAttributes stat(String path) throws IOException {
        return statBoth(path, 17);
    }

    public SFTPv3FileAttributes lstat(String path) throws IOException {
        return statBoth(path, 7);
    }

    public String readLink(String path) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(path, this.charsetName);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_READLINK...");
            this.debug.flush();
        }
        sendMessage(19, req_id, tw.getBytes());
        byte[] resp = receiveMessage(34000);
        PrintStream printStream2 = this.debug;
        if (printStream2 != null) {
            printStream2.println("Got REPLY.");
            this.debug.flush();
        }
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != req_id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t == 104) {
            int count = tr.readUINT32();
            if (count != 1) {
                throw new IOException("The server sent an invalid SSH_FXP_NAME packet.");
            }
            return tr.readString(this.charsetName);
        } else if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        } else {
            int errorCode = tr.readUINT32();
            throw new SFTPException(tr.readString(), errorCode);
        }
    }

    private void expectStatusOKMessage(int id) throws IOException {
        byte[] resp = receiveMessage(34000);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Got REPLY.");
            this.debug.flush();
        }
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        int errorCode = tr.readUINT32();
        if (errorCode == 0) {
            return;
        }
        throw new SFTPException(tr.readString(), errorCode);
    }

    public void setstat(String path, SFTPv3FileAttributes attr) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(path, this.charsetName);
        tw.writeBytes(createAttrs(attr));
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_SETSTAT...");
            this.debug.flush();
        }
        sendMessage(9, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    public void fsetstat(SFTPv3FileHandle handle, SFTPv3FileAttributes attr) throws IOException {
        checkHandleValidAndOpen(handle);
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(handle.fileHandle, 0, handle.fileHandle.length);
        tw.writeBytes(createAttrs(attr));
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_FSETSTAT...");
            this.debug.flush();
        }
        sendMessage(10, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    public void createSymlink(String src, String target) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(target, this.charsetName);
        tw.writeString(src, this.charsetName);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_SYMLINK...");
            this.debug.flush();
        }
        sendMessage(20, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    public String canonicalPath(String path) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(path, this.charsetName);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_REALPATH...");
            this.debug.flush();
        }
        sendMessage(16, req_id, tw.getBytes());
        byte[] resp = receiveMessage(34000);
        PrintStream printStream2 = this.debug;
        if (printStream2 != null) {
            printStream2.println("Got REPLY.");
            this.debug.flush();
        }
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != req_id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t == 104) {
            int count = tr.readUINT32();
            if (count != 1) {
                throw new IOException("The server sent an invalid SSH_FXP_NAME packet.");
            }
            return tr.readString(this.charsetName);
        } else if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        } else {
            int errorCode = tr.readUINT32();
            throw new SFTPException(tr.readString(), errorCode);
        }
    }

    private final Vector scanDirectory(byte[] handle) throws IOException {
        Vector files = new Vector();
        while (true) {
            int req_id = generateNextRequestID();
            TypesWriter tw = new TypesWriter();
            tw.writeString(handle, 0, handle.length);
            PrintStream printStream = this.debug;
            if (printStream != null) {
                printStream.println("Sending SSH_FXP_READDIR...");
                this.debug.flush();
            }
            sendMessage(12, req_id, tw.getBytes());
            byte[] resp = receiveMessage(34000);
            PrintStream printStream2 = this.debug;
            if (printStream2 != null) {
                printStream2.println("Got REPLY.");
                this.debug.flush();
            }
            TypesReader tr = new TypesReader(resp);
            int t = tr.readByte();
            int rep_id = tr.readUINT32();
            if (rep_id != req_id) {
                throw new IOException("The server sent an invalid id field.");
            }
            if (t == 104) {
                int count = tr.readUINT32();
                PrintStream printStream3 = this.debug;
                if (printStream3 != null) {
                    StringBuffer stringBuffer = new StringBuffer("Parsing ");
                    stringBuffer.append(count);
                    stringBuffer.append(" name entries...");
                    printStream3.println(stringBuffer.toString());
                }
                while (count > 0) {
                    SFTPv3DirectoryEntry dirEnt = new SFTPv3DirectoryEntry();
                    dirEnt.filename = tr.readString(this.charsetName);
                    dirEnt.longEntry = tr.readString(this.charsetName);
                    dirEnt.attributes = readAttrs(tr);
                    files.addElement(dirEnt);
                    PrintStream printStream4 = this.debug;
                    if (printStream4 != null) {
                        StringBuffer stringBuffer2 = new StringBuffer("File: '");
                        stringBuffer2.append(dirEnt.filename);
                        stringBuffer2.append("'");
                        printStream4.println(stringBuffer2.toString());
                    }
                    count--;
                }
            } else if (t != 101) {
                StringBuffer stringBuffer3 = new StringBuffer("The SFTP server sent an unexpected packet type (");
                stringBuffer3.append(t);
                stringBuffer3.append(")");
                throw new IOException(stringBuffer3.toString());
            } else {
                int errorCode = tr.readUINT32();
                if (errorCode == 1) {
                    return files;
                }
                throw new SFTPException(tr.readString(), errorCode);
            }
        }
    }

    private final byte[] openDirectory(String path) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(path, this.charsetName);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_OPENDIR...");
            this.debug.flush();
        }
        sendMessage(11, req_id, tw.getBytes());
        byte[] resp = receiveMessage(34000);
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != req_id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t == 102) {
            PrintStream printStream2 = this.debug;
            if (printStream2 != null) {
                printStream2.println("Got SSH_FXP_HANDLE.");
                this.debug.flush();
            }
            byte[] handle = tr.readByteString();
            return handle;
        } else if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        } else {
            int errorCode = tr.readUINT32();
            String errorMessage = tr.readString();
            throw new SFTPException(errorMessage, errorCode);
        }
    }

    private final String expandString(byte[] b, int off, int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int c = b[off + i] & 255;
            if (c >= 32 && c <= 126) {
                sb.append((char) c);
            } else {
                StringBuffer stringBuffer = new StringBuffer("{0x");
                stringBuffer.append(Integer.toHexString(c));
                stringBuffer.append(StrPool.DELIM_END);
                sb.append(stringBuffer.toString());
            }
        }
        return sb.toString();
    }

    private void init() throws IOException {
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_INIT (3)...");
        }
        TypesWriter tw = new TypesWriter();
        tw.writeUINT32(3);
        sendMessage(1, 0, tw.getBytes());
        PrintStream printStream2 = this.debug;
        if (printStream2 != null) {
            printStream2.println("Waiting for SSH_FXP_VERSION...");
        }
        TypesReader tr = new TypesReader(receiveMessage(34000));
        int type = tr.readByte();
        if (type != 2) {
            StringBuffer stringBuffer = new StringBuffer("The server did not send a SSH_FXP_VERSION packet (got ");
            stringBuffer.append(type);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        this.protocol_version = tr.readUINT32();
        PrintStream printStream3 = this.debug;
        if (printStream3 != null) {
            StringBuffer stringBuffer2 = new StringBuffer("SSH_FXP_VERSION: protocol_version = ");
            stringBuffer2.append(this.protocol_version);
            printStream3.println(stringBuffer2.toString());
        }
        if (this.protocol_version != 3) {
            StringBuffer stringBuffer3 = new StringBuffer("Server version ");
            stringBuffer3.append(this.protocol_version);
            stringBuffer3.append(" is currently not supported");
            throw new IOException(stringBuffer3.toString());
        }
        while (tr.remain() != 0) {
            String name = tr.readString();
            byte[] value = tr.readByteString();
            this.server_extensions.put(name, value);
            PrintStream printStream4 = this.debug;
            if (printStream4 != null) {
                StringBuffer stringBuffer4 = new StringBuffer("SSH_FXP_VERSION: extension: ");
                stringBuffer4.append(name);
                stringBuffer4.append(" = '");
                stringBuffer4.append(expandString(value, 0, value.length));
                stringBuffer4.append("'");
                printStream4.println(stringBuffer4.toString());
            }
        }
    }

    public int getProtocolVersion() {
        return this.protocol_version;
    }

    public void close() {
        this.sess.close();
    }

    public Vector ls(String dirName) throws IOException {
        byte[] handle = openDirectory(dirName);
        Vector result = scanDirectory(handle);
        closeHandle(handle);
        return result;
    }

    public void mkdir(String dirName, int posixPermissions) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(dirName, this.charsetName);
        tw.writeUINT32(4);
        tw.writeUINT32(posixPermissions);
        sendMessage(14, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    public void rm(String fileName) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(fileName, this.charsetName);
        sendMessage(13, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    public void rmdir(String dirName) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(dirName, this.charsetName);
        sendMessage(15, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    public void mv(String oldPath, String newPath) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(oldPath, this.charsetName);
        tw.writeString(newPath, this.charsetName);
        sendMessage(18, req_id, tw.getBytes());
        expectStatusOKMessage(req_id);
    }

    public SFTPv3FileHandle openFileRO(String fileName) throws IOException {
        return openFile(fileName, 1, null);
    }

    public SFTPv3FileHandle openFileRW(String fileName) throws IOException {
        return openFile(fileName, 3, null);
    }

    public SFTPv3FileHandle createFile(String fileName) throws IOException {
        return createFile(fileName, null);
    }

    public SFTPv3FileHandle createFile(String fileName, SFTPv3FileAttributes attr) throws IOException {
        return openFile(fileName, 11, attr);
    }

    public SFTPv3FileHandle createFileTruncate(String fileName) throws IOException {
        return createFileTruncate(fileName, null);
    }

    public SFTPv3FileHandle createFileTruncate(String fileName, SFTPv3FileAttributes attr) throws IOException {
        return openFile(fileName, 27, attr);
    }

    private byte[] createAttrs(SFTPv3FileAttributes attr) {
        TypesWriter tw = new TypesWriter();
        int attrFlags = 0;
        if (attr == null) {
            tw.writeUINT32(0);
        } else {
            if (attr.size != null) {
                attrFlags = 0 | 1;
            }
            if (attr.uid != null && attr.gid != null) {
                attrFlags |= 2;
            }
            if (attr.permissions != null) {
                attrFlags |= 4;
            }
            if (attr.atime != null && attr.mtime != null) {
                attrFlags |= 8;
            }
            tw.writeUINT32(attrFlags);
            if (attr.size != null) {
                tw.writeUINT64(attr.size.longValue());
            }
            if (attr.uid != null && attr.gid != null) {
                tw.writeUINT32(attr.uid.intValue());
                tw.writeUINT32(attr.gid.intValue());
            }
            if (attr.permissions != null) {
                tw.writeUINT32(attr.permissions.intValue());
            }
            if (attr.atime != null && attr.mtime != null) {
                tw.writeUINT32(attr.atime.intValue());
                tw.writeUINT32(attr.mtime.intValue());
            }
        }
        return tw.getBytes();
    }

    private SFTPv3FileHandle openFile(String fileName, int flags, SFTPv3FileAttributes attr) throws IOException {
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(fileName, this.charsetName);
        tw.writeUINT32(flags);
        tw.writeBytes(createAttrs(attr));
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_OPEN...");
            this.debug.flush();
        }
        sendMessage(3, req_id, tw.getBytes());
        byte[] resp = receiveMessage(34000);
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != req_id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t == 102) {
            PrintStream printStream2 = this.debug;
            if (printStream2 != null) {
                printStream2.println("Got SSH_FXP_HANDLE.");
                this.debug.flush();
            }
            return new SFTPv3FileHandle(this, tr.readByteString());
        } else if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        } else {
            int errorCode = tr.readUINT32();
            String errorMessage = tr.readString();
            throw new SFTPException(errorMessage, errorCode);
        }
    }

    public int read(SFTPv3FileHandle handle, long fileOffset, byte[] dst, int dstoff, int len) throws IOException {
        checkHandleValidAndOpen(handle);
        if (len > 32768 || len <= 0) {
            throw new IllegalArgumentException("invalid len argument");
        }
        int req_id = generateNextRequestID();
        TypesWriter tw = new TypesWriter();
        tw.writeString(handle.fileHandle, 0, handle.fileHandle.length);
        tw.writeUINT64(fileOffset);
        tw.writeUINT32(len);
        PrintStream printStream = this.debug;
        if (printStream != null) {
            printStream.println("Sending SSH_FXP_READ...");
            this.debug.flush();
        }
        sendMessage(5, req_id, tw.getBytes());
        byte[] resp = receiveMessage(34000);
        TypesReader tr = new TypesReader(resp);
        int t = tr.readByte();
        int rep_id = tr.readUINT32();
        if (rep_id != req_id) {
            throw new IOException("The server sent an invalid id field.");
        }
        if (t == 103) {
            PrintStream printStream2 = this.debug;
            if (printStream2 != null) {
                printStream2.println("Got SSH_FXP_DATA...");
                this.debug.flush();
            }
            int readLen = tr.readUINT32();
            if (readLen < 0 || readLen > len) {
                throw new IOException("The server sent an invalid length field.");
            }
            tr.readBytes(dst, dstoff, readLen);
            return readLen;
        } else if (t != 101) {
            StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
            stringBuffer.append(t);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        } else {
            int errorCode = tr.readUINT32();
            if (errorCode == 1) {
                PrintStream printStream3 = this.debug;
                if (printStream3 != null) {
                    printStream3.println("Got SSH_FX_EOF.");
                    this.debug.flush();
                    return -1;
                }
                return -1;
            }
            String errorMessage = tr.readString();
            throw new SFTPException(errorMessage, errorCode);
        }
    }

    public void write(SFTPv3FileHandle handle, long fileOffset, byte[] src, int srcoff, int len) throws IOException {
        checkHandleValidAndOpen(handle);
        if (len < 0) {
            long fileOffset2 = fileOffset;
            int srcoff2 = srcoff;
            int len2 = len;
            while (len2 > 0) {
                int writeRequestLen = len2;
                if (writeRequestLen > 32768) {
                    writeRequestLen = 32768;
                }
                int req_id = generateNextRequestID();
                TypesWriter tw = new TypesWriter();
                tw.writeString(handle.fileHandle, 0, handle.fileHandle.length);
                tw.writeUINT64(fileOffset2);
                tw.writeString(src, srcoff2, writeRequestLen);
                PrintStream printStream = this.debug;
                if (printStream != null) {
                    printStream.println("Sending SSH_FXP_WRITE...");
                    this.debug.flush();
                }
                sendMessage(6, req_id, tw.getBytes());
                fileOffset2 += writeRequestLen;
                srcoff2 += writeRequestLen;
                len2 -= writeRequestLen;
                byte[] resp = receiveMessage(34000);
                TypesReader tr = new TypesReader(resp);
                int t = tr.readByte();
                int rep_id = tr.readUINT32();
                if (rep_id != req_id) {
                    throw new IOException("The server sent an invalid id field.");
                }
                if (t != 101) {
                    StringBuffer stringBuffer = new StringBuffer("The SFTP server sent an unexpected packet type (");
                    stringBuffer.append(t);
                    stringBuffer.append(")");
                    throw new IOException(stringBuffer.toString());
                }
                int errorCode = tr.readUINT32();
                if (errorCode != 0) {
                    String errorMessage = tr.readString();
                    throw new SFTPException(errorMessage, errorCode);
                }
            }
        }
    }

    public void closeFile(SFTPv3FileHandle handle) throws IOException {
        if (handle == null) {
            throw new IllegalArgumentException("the handle argument may not be null");
        }
        try {
            if (!handle.isClosed) {
                closeHandle(handle.fileHandle);
            }
        } finally {
            handle.isClosed = true;
        }
    }
}
