package com.xpeng.upso.utils;

import androidx.annotation.Keep;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficStatsEntry;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.commons.lang3.BooleanUtils;
@Keep
/* loaded from: classes2.dex */
public class JSchSSH {
    private static final String TAG = JSchSSH.class.getCanonicalName();

    public static byte[] get(String host, String user, String psw, int port, String dir, String fileName) {
        byte[] data = null;
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession(user, host, port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", BooleanUtils.NO);
            session.setPassword(psw);
            session.setConfig(config);
            session.connect(10000);
            String str = TAG;
            LogUtils.d(str, host + " connection:" + session.isConnected());
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            OutputStream out = new ByteArrayOutputStream();
            channelSftp.cd(dir);
            channelSftp.get(fileName, out);
            data = ((ByteArrayOutputStream) out).toByteArray();
            channel.disconnect();
            session.disconnect();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return data;
        }
    }

    public String ShellCMD(String cmd, String ftpUserName, String ftpPassword, String ftpHost, int ftpPort) {
        try {
            JSch jsch = new JSch();
            try {
                Session session2 = jsch.getSession(ftpUserName, ftpHost, ftpPort);
                try {
                    session2.setPassword(ftpPassword);
                    session2.setTimeout(TrafficStatsEntry.FIRST_NETWORK_UID);
                    Properties config2 = new Properties();
                    config2.put("StrictHostKeyChecking", BooleanUtils.NO);
                    session2.setConfig(config2);
                    session2.connect();
                    LogUtils.i(TAG, "cnd=" + cmd);
                    ChannelShell chShell = (ChannelShell) session2.openChannel("shell");
                    chShell.connect();
                    InputStream inputStream = chShell.getInputStream();
                    OutputStream outputStream = chShell.getOutputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    String cmd5 = cmd + " \nexit\n";
                    outputStream.write(cmd5.getBytes());
                    outputStream.flush();
                    while (true) {
                        String line = in.readLine();
                        if (line == null) {
                            break;
                        }
                        JSch jsch2 = jsch;
                        LogUtils.i(TAG, line + "\r\n");
                        jsch = jsch2;
                    }
                } catch (JSchException e) {
                    e = e;
                    e.printStackTrace();
                    LogUtils.e(TAG, e.toString());
                    return "";
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    LogUtils.e(TAG, e.toString());
                    return "";
                }
            } catch (JSchException e3) {
                e = e3;
            } catch (IOException e4) {
                e = e4;
            }
        } catch (JSchException e5) {
            e = e5;
        } catch (IOException e6) {
            e = e6;
        }
        return "";
    }
}
