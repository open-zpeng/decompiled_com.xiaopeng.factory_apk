package ch.ethz.ssh2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public class SCPClient {
    Connection conn;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class LenNamePair {
        String filename;
        long length;

        LenNamePair() {
        }
    }

    public SCPClient(Connection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("Cannot accept null argument!");
        }
        this.conn = conn;
    }

    private void readResponse(InputStream is) throws IOException {
        int c = is.read();
        if (c == 0) {
            return;
        }
        if (c == -1) {
            throw new IOException("Remote scp terminated unexpectedly.");
        }
        if (c != 1 && c != 2) {
            throw new IOException("Remote scp sent illegal error code.");
        }
        if (c == 2) {
            throw new IOException("Remote scp terminated with error.");
        }
        String err = receiveLine(is);
        StringBuffer stringBuffer = new StringBuffer("Remote scp terminated with error (");
        stringBuffer.append(err);
        stringBuffer.append(").");
        throw new IOException(stringBuffer.toString());
    }

    private String receiveLine(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer(30);
        while (sb.length() <= 8192) {
            int c = is.read();
            if (c < 0) {
                throw new IOException("Remote scp terminated unexpectedly.");
            }
            if (c != 10) {
                sb.append((char) c);
            } else {
                return sb.toString();
            }
        }
        throw new IOException("Remote scp sent a too long line");
    }

    private LenNamePair parseCLine(String line) throws IOException {
        if (line.length() < 8) {
            throw new IOException("Malformed C line sent by remote SCP binary, line too short.");
        }
        if (line.charAt(4) != ' ' || line.charAt(5) == ' ') {
            throw new IOException("Malformed C line sent by remote SCP binary.");
        }
        int length_name_sep = line.indexOf(32, 5);
        if (length_name_sep == -1) {
            throw new IOException("Malformed C line sent by remote SCP binary.");
        }
        String length_substring = line.substring(5, length_name_sep);
        String name_substring = line.substring(length_name_sep + 1);
        if (length_substring.length() <= 0 || name_substring.length() <= 0) {
            throw new IOException("Malformed C line sent by remote SCP binary.");
        }
        if (length_substring.length() + 6 + name_substring.length() != line.length()) {
            throw new IOException("Malformed C line sent by remote SCP binary.");
        }
        try {
            long len = Long.parseLong(length_substring);
            if (len < 0) {
                throw new IOException("Malformed C line sent by remote SCP binary, illegal file length.");
            }
            LenNamePair lnp = new LenNamePair();
            lnp.length = len;
            lnp.filename = name_substring;
            return lnp;
        } catch (NumberFormatException e) {
            throw new IOException("Malformed C line sent by remote SCP binary, cannot parse file length.");
        }
    }

    private void sendBytes(Session sess, byte[] data, String fileName, String mode) throws IOException {
        OutputStream os = sess.getStdin();
        InputStream is = new BufferedInputStream(sess.getStdout(), 512);
        readResponse(is);
        StringBuffer stringBuffer = new StringBuffer("C");
        stringBuffer.append(mode);
        stringBuffer.append(" ");
        stringBuffer.append(data.length);
        stringBuffer.append(" ");
        stringBuffer.append(fileName);
        stringBuffer.append("\n");
        String cline = stringBuffer.toString();
        os.write(cline.getBytes());
        os.flush();
        readResponse(is);
        os.write(data, 0, data.length);
        os.write(0);
        os.flush();
        readResponse(is);
        os.write("E\n".getBytes());
        os.flush();
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void sendFiles(ch.ethz.ssh2.Session r18, java.lang.String[] r19, java.lang.String[] r20, java.lang.String r21) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 228
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.ethz.ssh2.SCPClient.sendFiles(ch.ethz.ssh2.Session, java.lang.String[], java.lang.String[], java.lang.String):void");
    }

    private void receiveFiles(Session sess, OutputStream[] targets) throws IOException {
        int c;
        String line;
        int trans;
        byte[] buffer = new byte[8192];
        OutputStream os = new BufferedOutputStream(sess.getStdin(), 512);
        InputStream is = new BufferedInputStream(sess.getStdout(), 40000);
        os.write(0);
        os.flush();
        for (OutputStream outputStream : targets) {
            do {
                c = is.read();
                if (c < 0) {
                    throw new IOException("Remote scp terminated unexpectedly.");
                }
                line = receiveLine(is);
            } while (c == 84);
            if (c == 1 || c == 2) {
                StringBuffer stringBuffer = new StringBuffer("Remote SCP error: ");
                stringBuffer.append(line);
                throw new IOException(stringBuffer.toString());
            } else if (c != 67) {
                StringBuffer stringBuffer2 = new StringBuffer("Remote SCP error: ");
                stringBuffer2.append((char) c);
                stringBuffer2.append(line);
                throw new IOException(stringBuffer2.toString());
            } else {
                LenNamePair lnp = parseCLine(line);
                os.write(0);
                os.flush();
                long remain = lnp.length;
                while (remain > 0) {
                    if (remain > buffer.length) {
                        trans = buffer.length;
                    } else {
                        trans = (int) remain;
                    }
                    int this_time_received = is.read(buffer, 0, trans);
                    if (this_time_received >= 0) {
                        outputStream.write(buffer, 0, this_time_received);
                        remain -= this_time_received;
                    } else {
                        throw new IOException("Remote scp terminated connection unexpectedly");
                    }
                }
                readResponse(is);
                os.write(0);
                os.flush();
            }
        }
    }

    private void receiveFiles(Session sess, String[] files, String target) throws IOException {
        int c;
        String line;
        int trans;
        byte[] buffer = new byte[8192];
        OutputStream os = new BufferedOutputStream(sess.getStdin(), 512);
        InputStream is = new BufferedInputStream(sess.getStdout(), 40000);
        os.write(0);
        os.flush();
        for (int i = 0; i < files.length; i++) {
            do {
                c = is.read();
                if (c < 0) {
                    throw new IOException("Remote scp terminated unexpectedly.");
                }
                line = receiveLine(is);
            } while (c == 84);
            if (c == 1 || c == 2) {
                StringBuffer stringBuffer = new StringBuffer("Remote SCP error: ");
                stringBuffer.append(line);
                throw new IOException(stringBuffer.toString());
            } else if (c != 67) {
                StringBuffer stringBuffer2 = new StringBuffer("Remote SCP error: ");
                stringBuffer2.append((char) c);
                stringBuffer2.append(line);
                throw new IOException(stringBuffer2.toString());
            } else {
                LenNamePair lnp = parseCLine(line);
                os.write(0);
                os.flush();
                StringBuffer stringBuffer3 = new StringBuffer(String.valueOf(target));
                stringBuffer3.append(File.separatorChar);
                stringBuffer3.append(lnp.filename);
                File f = new File(stringBuffer3.toString());
                FileOutputStream fop = null;
                try {
                    fop = new FileOutputStream(f);
                    long remain = lnp.length;
                    while (remain > 0) {
                        if (remain > buffer.length) {
                            trans = buffer.length;
                        } else {
                            trans = (int) remain;
                        }
                        int this_time_received = is.read(buffer, 0, trans);
                        if (this_time_received < 0) {
                            throw new IOException("Remote scp terminated connection unexpectedly");
                        }
                        fop.write(buffer, 0, this_time_received);
                        remain -= this_time_received;
                    }
                    fop.close();
                    readResponse(is);
                    os.write(0);
                    os.flush();
                } catch (Throwable th) {
                    if (fop != null) {
                        fop.close();
                    }
                    throw th;
                }
            }
        }
    }

    public void put(String localFile, String remoteTargetDirectory) throws IOException {
        put(new String[]{localFile}, remoteTargetDirectory, "0600");
    }

    public void put(String[] localFiles, String remoteTargetDirectory) throws IOException {
        put(localFiles, remoteTargetDirectory, "0600");
    }

    public void put(String localFile, String remoteTargetDirectory, String mode) throws IOException {
        put(new String[]{localFile}, remoteTargetDirectory, mode);
    }

    public void put(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) throws IOException {
        put(new String[]{localFile}, new String[]{remoteFileName}, remoteTargetDirectory, mode);
    }

    public void put(byte[] data, String remoteFileName, String remoteTargetDirectory) throws IOException {
        put(data, remoteFileName, remoteTargetDirectory, "0600");
    }

    public void put(byte[] data, String remoteFileName, String remoteTargetDirectory, String mode) throws IOException {
        Session sess = null;
        if (remoteFileName == null || remoteTargetDirectory == null || mode == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (mode.length() != 4) {
            throw new IllegalArgumentException("Invalid mode.");
        }
        for (int i = 0; i < mode.length(); i++) {
            if (!Character.isDigit(mode.charAt(i))) {
                throw new IllegalArgumentException("Invalid mode.");
            }
        }
        String remoteTargetDirectory2 = remoteTargetDirectory.trim();
        String remoteTargetDirectory3 = remoteTargetDirectory2.length() > 0 ? remoteTargetDirectory2 : ".";
        StringBuffer stringBuffer = new StringBuffer("scp -t -d ");
        stringBuffer.append(remoteTargetDirectory3);
        String cmd = stringBuffer.toString();
        try {
            try {
                sess = this.conn.openSession();
                sess.execCommand(cmd);
                sendBytes(sess, data, remoteFileName, mode);
                sess.close();
            } catch (IOException e) {
                throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
            }
        } catch (Throwable th) {
            if (sess != null) {
                sess.close();
            }
            throw th;
        }
    }

    public void put(String[] localFiles, String remoteTargetDirectory, String mode) throws IOException {
        put(localFiles, (String[]) null, remoteTargetDirectory, mode);
    }

    public void put(String[] localFiles, String[] remoteFiles, String remoteTargetDirectory, String mode) throws IOException {
        Session sess = null;
        if (localFiles == null || remoteTargetDirectory == null || mode == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (mode.length() != 4) {
            throw new IllegalArgumentException("Invalid mode.");
        }
        for (int i = 0; i < mode.length(); i++) {
            if (!Character.isDigit(mode.charAt(i))) {
                throw new IllegalArgumentException("Invalid mode.");
            }
        }
        int i2 = localFiles.length;
        if (i2 == 0) {
            return;
        }
        String remoteTargetDirectory2 = remoteTargetDirectory.trim();
        String remoteTargetDirectory3 = remoteTargetDirectory2.length() > 0 ? remoteTargetDirectory2 : ".";
        StringBuffer stringBuffer = new StringBuffer("scp -t -d ");
        stringBuffer.append(remoteTargetDirectory3);
        String cmd = stringBuffer.toString();
        for (String str : localFiles) {
            if (str == null) {
                throw new IllegalArgumentException("Cannot accept null filename.");
            }
        }
        try {
            try {
                sess = this.conn.openSession();
                sess.execCommand(cmd);
                sendFiles(sess, localFiles, remoteFiles, mode);
                sess.close();
            } catch (IOException e) {
                throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
            }
        } catch (Throwable th) {
            if (sess != null) {
                sess.close();
            }
            throw th;
        }
    }

    public void get(String remoteFile, String localTargetDirectory) throws IOException {
        get(new String[]{remoteFile}, localTargetDirectory);
    }

    public void get(String remoteFile, OutputStream target) throws IOException {
        get(new String[]{remoteFile}, new OutputStream[]{target});
    }

    private void get(String[] remoteFiles, OutputStream[] targets) throws IOException {
        Session sess = null;
        if (remoteFiles == null || targets == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (remoteFiles.length != targets.length) {
            throw new IllegalArgumentException("Length of arguments does not match.");
        }
        if (remoteFiles.length == 0) {
            return;
        }
        String cmd = "scp -f";
        for (int i = 0; i < remoteFiles.length; i++) {
            if (remoteFiles[i] == null) {
                throw new IllegalArgumentException("Cannot accept null filename.");
            }
            String tmp = remoteFiles[i].trim();
            if (tmp.length() == 0) {
                throw new IllegalArgumentException("Cannot accept empty filename.");
            }
            StringBuffer stringBuffer = new StringBuffer(String.valueOf(cmd));
            stringBuffer.append(" ");
            stringBuffer.append(tmp);
            cmd = stringBuffer.toString();
        }
        try {
            try {
                sess = this.conn.openSession();
                sess.execCommand(cmd);
                receiveFiles(sess, targets);
                sess.close();
            } catch (IOException e) {
                throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
            }
        } catch (Throwable th) {
            if (sess != null) {
                sess.close();
            }
            throw th;
        }
    }

    public void get(String[] remoteFiles, String localTargetDirectory) throws IOException {
        Session sess = null;
        if (remoteFiles == null || localTargetDirectory == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (remoteFiles.length == 0) {
            return;
        }
        String cmd = "scp -f";
        for (int i = 0; i < remoteFiles.length; i++) {
            try {
                if (remoteFiles[i] == null) {
                    throw new IllegalArgumentException("Cannot accept null filename.");
                }
                String tmp = remoteFiles[i].trim();
                if (tmp.length() == 0) {
                    throw new IllegalArgumentException("Cannot accept empty filename.");
                }
                StringBuffer stringBuffer = new StringBuffer(String.valueOf(cmd));
                stringBuffer.append(" ");
                stringBuffer.append(tmp);
                cmd = stringBuffer.toString();
            } catch (Throwable th) {
                if (sess != null) {
                    sess.close();
                }
                throw th;
            }
        }
        try {
            sess = this.conn.openSession();
            sess.execCommand(cmd);
            receiveFiles(sess, remoteFiles, localTargetDirectory);
            sess.close();
        } catch (IOException e) {
            throw ((IOException) new IOException("Error during SCP transfer.").initCause(e));
        }
    }
}
