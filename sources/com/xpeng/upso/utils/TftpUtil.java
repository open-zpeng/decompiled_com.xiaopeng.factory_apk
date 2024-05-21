package com.xpeng.upso.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.commons.net.tftp.TFTPClient;
import org.apache.commons.net.tftp.TFTPPacket;
/* loaded from: classes2.dex */
public final class TftpUtil {
    public static final int ASCII_MODE = 0;
    public static final int BINARY_MODE = 1;

    public static boolean send(int transferMode, String hostname, String localFilename, String remoteFilename, boolean verbose) {
        TFTPClient tftp;
        FileInputStream input;
        boolean closed;
        if (verbose) {
            tftp = new TFTPClient() { // from class: com.xpeng.upso.utils.TftpUtil.1
                @Override // org.apache.commons.net.tftp.TFTP
                protected void trace(String direction, TFTPPacket packet) {
                    PrintStream printStream = System.out;
                    printStream.println(direction + " " + packet);
                }
            };
        } else {
            tftp = new TFTPClient();
        }
        try {
            FileInputStream input2 = new FileInputStream(localFilename);
            input = input2;
        } catch (IOException e) {
            tftp.close();
            System.err.println("Error: could not open local file for reading.");
            System.err.println(e.getMessage());
            System.exit(1);
            input = null;
        }
        open(tftp);
        try {
            try {
                String[] parts = hostname.split(":");
                if (parts.length == 2) {
                    tftp.sendFile(remoteFilename, transferMode, input, parts[0], Integer.parseInt(parts[1]));
                } else {
                    tftp.sendFile(remoteFilename, transferMode, input, hostname);
                }
            } catch (UnknownHostException e2) {
                System.err.println("Error: could not resolve hostname.");
                System.err.println(e2.getMessage());
                System.exit(1);
            } catch (IOException e3) {
                System.err.println("Error: I/O exception occurred while sending file.");
                System.err.println(e3.getMessage());
                System.exit(1);
            }
            return closed;
        } finally {
            close(tftp, input);
        }
    }

    public static boolean receive(int transferMode, String hostname, String localFilename, String remoteFilename, boolean verbose) {
        TFTPClient tftp;
        FileOutputStream output;
        boolean closed;
        if (verbose) {
            tftp = new TFTPClient() { // from class: com.xpeng.upso.utils.TftpUtil.2
                @Override // org.apache.commons.net.tftp.TFTP
                protected void trace(String direction, TFTPPacket packet) {
                    PrintStream printStream = System.out;
                    printStream.println(direction + " " + packet);
                }
            };
        } else {
            tftp = new TFTPClient();
        }
        File file = new File(localFilename);
        try {
            FileOutputStream output2 = new FileOutputStream(file);
            output = output2;
        } catch (IOException e) {
            tftp.close();
            System.err.println("Error: could not open local file for writing.");
            System.err.println(e.getMessage());
            System.exit(1);
            output = null;
        }
        open(tftp);
        try {
            try {
                String[] parts = hostname.split(":");
                if (parts.length == 2) {
                    tftp.receiveFile(remoteFilename, transferMode, output, parts[0], Integer.parseInt(parts[1]));
                } else {
                    tftp.receiveFile(remoteFilename, transferMode, output, hostname);
                }
            } finally {
                close(tftp, output);
            }
        } catch (UnknownHostException e2) {
            System.err.println("Error: could not resolve hostname.");
            System.err.println(e2.getMessage());
            System.exit(1);
        } catch (IOException e3) {
            System.err.println("Error: I/O exception occurred while receiving file.");
            System.err.println(e3.getMessage());
            System.exit(1);
        }
        return closed;
    }

    private static boolean close(TFTPClient tftp, Closeable output) {
        tftp.close();
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                System.err.println("Error: error closing file.");
                System.err.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    private static void open(TFTPClient tftp) {
        try {
            tftp.open();
        } catch (SocketException e) {
            System.err.println("Error: could not open local UDP socket.");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
