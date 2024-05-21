package com.xpeng.upso.utils;

import androidx.annotation.Keep;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.ServerHostKeyVerifier;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
@Keep
/* loaded from: classes2.dex */
public class GmScp {
    private static final String TAG = GmScp.class.getSimpleName();
    private static volatile GmScp scpTboxInstance;
    private static volatile GmScp scpXpuInstance;
    private Connection connection;
    private String host;
    private boolean isAuthed = false;
    private String pass;
    private int port;
    private SCPClient scpClient;
    private String user;

    private GmScp(String user, String pass, String host, int port) {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.port = port;
    }

    public void connect() {
        this.connection = new Connection(this.host, this.port);
        try {
            ServerHostKeyVerifier verifier = new ServerHostKeyVerifier() { // from class: com.xpeng.upso.utils.GmScp.1
                @Override // ch.ethz.ssh2.ServerHostKeyVerifier
                public boolean verifyServerHostKey(String hostname, int port, String serverHostKeyAlgorithm, byte[] serverHostKey) throws Exception {
                    return true;
                }
            };
            this.connection.connect(verifier, 10000, 0);
            this.isAuthed = this.connection.authenticateWithPassword(this.user, this.pass);
            this.scpClient = this.connection.createSCPClient();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            close();
        }
    }

    public void close() {
        this.connection.close();
    }

    public boolean getIsAuthed() {
        return this.isAuthed;
    }

    public void putFile(String filePath, String aimPath) {
        try {
            if (this.scpClient != null) {
                this.scpClient.put(filePath, aimPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
        }
    }

    public void getFile(String filePath, OutputStream target) {
        try {
            if (this.scpClient != null) {
                this.scpClient.get(filePath, target);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
        }
    }

    public static byte[] get(String host, String user, String psw, int port, String path) {
        try {
            GmScp scp = new GmScp(user, psw, host, port);
            scp.connect();
            OutputStream out = new ByteArrayOutputStream();
            if (scp.getIsAuthed()) {
                scp.getFile(path, out);
                byte[] logData = ((ByteArrayOutputStream) out).toByteArray();
                String str = TAG;
                LogUtils.i(str, "get log ,len=" + logData.length);
                scp.close();
                return logData;
            }
            String str2 = TAG;
            LogUtils.i(str2, "scp Authed=" + scp.getIsAuthed());
            scp.close();
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }
}
