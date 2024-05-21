package com.xiaopeng.factory.model.factorytest.hardwaretest.gps;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import com.xiaopeng.lib.utils.LogUtils;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
/* loaded from: classes.dex */
public class UDPClient {
    private static final int ADR_COMMAND_DISABLE_ADR = 0;
    private static final int ADR_COMMAND_ENABLE_ADR = 1;
    private static final int ADR_COMMAND_IDLE = -1;
    private static final int CLIENT_PORT = 29948;
    private static final int ERROR_RETRY_TIMEOUT = 5000;
    private static final int FAILURE_REASON_CREATE_SOCKET = 1;
    private static final int FAILURE_REASON_DISCONNECT_FROM_SERVER = 5;
    private static final int FAILURE_REASON_GET_SERVER_HOST_NAME = 2;
    private static final int FAILURE_REASON_SEND_ADR_CLOSE_COMMAND = 7;
    private static final int FAILURE_REASON_SEND_ADR_OPEN_COMMAND = 8;
    private static final int FAILURE_REASON_SEND_SERVER_START_COMMAND = 4;
    private static final int FAILURE_REASON_SEND_SERVER_STOP_COMMAND = 6;
    private static final int FAILURE_REASON_SET_SOCKET_TIME_OUT = 3;
    private static final int FIRST_TIME_RETRY_MAX_NUM = 600;
    private static final float INVALID_TEMPERATURE = -274.0f;
    private static final int RECEIVE_BUF_MAX_SIZE = 1024;
    private static final int RECEIVE_PACKAGE_RETRY_MAX_NUM = 10;
    private static final int SERVER_PORT = 29946;
    private static final int SOCKET_TIMEOUT = 500;
    private static final String TAG = "UDPClient";
    private static UDPClient sUDPClient;
    private Handler mHandler;
    private UDPClientListener mListener;
    private HandlerThread mThread;
    private DatagramSocket mUDPSocket;
    private static final byte[] START_COMMAND = {88, 1, 16};
    private static final byte[] STOP_COMMAND = {88, 3};
    private static final byte[] PING_COMMAND = {88, 0};
    private static final byte[] UBX_CLOSE_ADR = {-75, 98, 6, WlanPresenter.WL_RX_NF3219, 44, 0, -1, -1, 76, 102, -64, 0, 0, 0, 0, 0, 5, 24, 12, 0, 0, 0, 0, 0, 75, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -92, -13};
    private static final byte[] UBX_OPEN_ADR = {-75, 98, 6, WlanPresenter.WL_RX_NF3219, 44, 0, -1, -1, 76, 102, -64, 0, 0, 0, 0, 0, 5, 24, 12, 0, 0, 0, 0, 0, 75, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, -91, -8};
    private boolean mIsStarted = false;
    private int mNeedSendADRCommand = -1;
    private int mFailureCounter = 0;
    private int mFailureReason = 0;
    private final Runnable mSocketHandlerRunnable = new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.gps.UDPClient.1
        @Override // java.lang.Runnable
        public void run() {
            int i;
            int i2;
            DatagramPacket mReceivedPacket;
            DatagramPacket mSendPacket;
            DatagramPacket mReceivedPacket2;
            boolean z;
            byte[] buf;
            do {
                LogUtils.i(UDPClient.TAG, "Failure counter = " + UDPClient.this.mFailureCounter + ", Failure reason = " + UDPClient.this.mFailureReason);
                int i3 = 1024;
                byte[] buf2 = new byte[1024];
                int i4 = 1;
                try {
                    UDPClient.this.mUDPSocket = new DatagramSocket((SocketAddress) null);
                    UDPClient.this.mUDPSocket.setReuseAddress(true);
                    UDPClient.this.mUDPSocket.bind(new InetSocketAddress(UDPClient.CLIENT_PORT));
                    try {
                        InetAddress mAddress = InetAddress.getByName("127.0.0.1");
                        DatagramPacket mSendPacket2 = new DatagramPacket(UDPClient.START_COMMAND, 0, 3, mAddress, UDPClient.SERVER_PORT);
                        DatagramPacket mReceivedPacket3 = new DatagramPacket(buf2, 1024);
                        try {
                            UDPClient.this.mUDPSocket.setSoTimeout(500);
                            boolean receivedResponse = false;
                            int tries = 0;
                            while (!receivedResponse && tries < UDPClient.FIRST_TIME_RETRY_MAX_NUM) {
                                try {
                                    UDPClient.this.mUDPSocket.send(mSendPacket2);
                                    try {
                                        UDPClient.this.mUDPSocket.receive(mReceivedPacket3);
                                        if (!mReceivedPacket3.getAddress().equals(mAddress)) {
                                            LogUtils.e(UDPClient.TAG, "Received packet from an unknown source: " + mReceivedPacket3.getAddress());
                                        } else {
                                            receivedResponse = true;
                                        }
                                    } catch (InterruptedIOException e) {
                                        tries++;
                                        LogUtils.i(UDPClient.TAG, "Time out," + (UDPClient.FIRST_TIME_RETRY_MAX_NUM - tries) + " more tries...");
                                    } catch (IOException e2) {
                                        e2.printStackTrace();
                                    }
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                            if (!receivedResponse) {
                                LogUtils.e(UDPClient.TAG, "Server not response, try again");
                                try {
                                    Thread.sleep(5000L);
                                } catch (InterruptedException except) {
                                    LogUtils.w(UDPClient.TAG, except.getMessage());
                                }
                                UDPClient.this.mUDPSocket.close();
                                UDPClient.access$008(UDPClient.this);
                                UDPClient.this.mFailureReason = 4;
                            } else {
                                LogUtils.i(UDPClient.TAG, "Received data " + (new String(mReceivedPacket3.getData(), 0, mReceivedPacket3.getLength()) + " from: " + mReceivedPacket3.getAddress().getHostAddress() + ":" + mReceivedPacket3.getPort()));
                                mReceivedPacket3.setLength(1024);
                                LogUtils.i(UDPClient.TAG, "Getting into main handler loop of udp client socket");
                                int tries2 = 0;
                                DatagramPacket mSendPacket3 = mSendPacket2;
                                while (true) {
                                    if (!UDPClient.this.mIsStarted) {
                                        i = 6;
                                        i2 = 5;
                                        break;
                                    }
                                    if (UDPClient.this.mNeedSendADRCommand != 0) {
                                        mReceivedPacket = mReceivedPacket3;
                                        if (i4 != UDPClient.this.mNeedSendADRCommand) {
                                            mSendPacket = mSendPacket3;
                                        } else {
                                            DatagramPacket mSendPacket4 = new DatagramPacket(UDPClient.UBX_OPEN_ADR, 0, UDPClient.UBX_OPEN_ADR.length, mAddress, UDPClient.SERVER_PORT);
                                            try {
                                                UDPClient.this.mUDPSocket.send(mSendPacket4);
                                                UDPClient.this.mNeedSendADRCommand = -1;
                                                mSendPacket = mSendPacket4;
                                            } catch (IOException e4) {
                                                LogUtils.i(UDPClient.TAG, "Failed to send ADR open command");
                                                UDPClient.access$008(UDPClient.this);
                                                UDPClient.this.mFailureReason = 8;
                                                i2 = 5;
                                                i = 6;
                                            }
                                        }
                                    } else {
                                        mReceivedPacket = mReceivedPacket3;
                                        DatagramPacket mSendPacket5 = new DatagramPacket(UDPClient.UBX_CLOSE_ADR, 0, UDPClient.UBX_CLOSE_ADR.length, mAddress, UDPClient.SERVER_PORT);
                                        try {
                                            UDPClient.this.mUDPSocket.send(mSendPacket5);
                                            UDPClient.this.mNeedSendADRCommand = -1;
                                            mSendPacket = mSendPacket5;
                                        } catch (IOException e5) {
                                            LogUtils.i(UDPClient.TAG, "Failed to send ADR close command");
                                            UDPClient.access$008(UDPClient.this);
                                            UDPClient.this.mFailureReason = 7;
                                            i2 = 5;
                                            i = 6;
                                        }
                                    }
                                    mReceivedPacket.setLength(i3);
                                    try {
                                        UDPClient.this.mUDPSocket.receive(mReceivedPacket);
                                        tries2 = 0;
                                        byte[] data = mReceivedPacket.getData();
                                        String str_receive = new String(mReceivedPacket.getData(), 0, mReceivedPacket.getLength());
                                        if (2 == mReceivedPacket.getLength() && UDPClient.PING_COMMAND[0] == data[0] && UDPClient.PING_COMMAND[i4] == data[i4]) {
                                            try {
                                            } catch (IOException e6) {
                                                e = e6;
                                            }
                                            try {
                                                DatagramPacket mSendPacket6 = new DatagramPacket(UDPClient.PING_COMMAND, 0, 2, mAddress, UDPClient.SERVER_PORT);
                                                UDPClient.this.mUDPSocket.send(mSendPacket6);
                                                mReceivedPacket3 = mReceivedPacket;
                                                mSendPacket3 = mSendPacket6;
                                            } catch (IOException e7) {
                                                e = e7;
                                                e.printStackTrace();
                                                i2 = 5;
                                                i = 6;
                                                UDPClient.this.mUDPSocket.send(new DatagramPacket(UDPClient.STOP_COMMAND, 0, 2, mAddress, UDPClient.SERVER_PORT));
                                                LogUtils.i(UDPClient.TAG, "Re-connect to the GPS HAL's UDP server");
                                                UDPClient.this.mUDPSocket.close();
                                                UDPClient.access$008(UDPClient.this);
                                                UDPClient.this.mFailureReason = i2;
                                                if (!UDPClient.this.mIsStarted) {
                                                    LogUtils.e(UDPClient.TAG, "Exit the mSocketHandlerRunnable thread, error occurred.");
                                                }
                                            }
                                        } else {
                                            if (!TextUtils.isEmpty(str_receive) && UDPClient.this.mListener != null) {
                                                String[] sensorParts = str_receive.split(",");
                                                float temperature = UDPClient.INVALID_TEMPERATURE;
                                                try {
                                                    try {
                                                        int type = Integer.parseInt(sensorParts[0]);
                                                        float x = Float.parseFloat(sensorParts[i4]);
                                                        float y = Float.parseFloat(sensorParts[2]);
                                                        float z2 = Float.parseFloat(sensorParts[3]);
                                                        long t = Long.parseLong(sensorParts[4]);
                                                        if (6 == sensorParts.length) {
                                                            try {
                                                                temperature = Float.parseFloat(sensorParts[5]);
                                                            } catch (NumberFormatException e8) {
                                                                e8.printStackTrace();
                                                                buf = buf2;
                                                                mReceivedPacket2 = mReceivedPacket;
                                                                z = false;
                                                            }
                                                        }
                                                        z = false;
                                                        float[] values = {x, y, z2};
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append("type:");
                                                        sb.append(type);
                                                        sb.append(",x:");
                                                        sb.append(x);
                                                        sb.append(",y:");
                                                        sb.append(y);
                                                        sb.append(",z:");
                                                        sb.append(z2);
                                                        sb.append(",t:");
                                                        buf = buf2;
                                                        sb.append(t);
                                                        mReceivedPacket2 = mReceivedPacket;
                                                        sb.append(",temperature:");
                                                        sb.append(temperature);
                                                        LogUtils.i(UDPClient.TAG, sb.toString());
                                                        if (UDPClient.this.mListener != null) {
                                                            UDPClient.this.mListener.onSensorChanged(type, values, t, temperature);
                                                        }
                                                    } catch (NumberFormatException e9) {
                                                        e = e9;
                                                        buf = buf2;
                                                        mReceivedPacket2 = mReceivedPacket;
                                                        z = false;
                                                        e.printStackTrace();
                                                        mSendPacket3 = mSendPacket;
                                                        buf2 = buf;
                                                        mReceivedPacket3 = mReceivedPacket2;
                                                        i3 = 1024;
                                                        i4 = 1;
                                                    }
                                                } catch (NumberFormatException e10) {
                                                    e = e10;
                                                    buf = buf2;
                                                    mReceivedPacket2 = mReceivedPacket;
                                                    z = false;
                                                }
                                            } else {
                                                mReceivedPacket2 = mReceivedPacket;
                                                z = false;
                                                buf = buf2;
                                            }
                                            mSendPacket3 = mSendPacket;
                                            buf2 = buf;
                                            mReceivedPacket3 = mReceivedPacket2;
                                            i3 = 1024;
                                            i4 = 1;
                                        }
                                    } catch (SocketTimeoutException e11) {
                                        byte[] buf3 = buf2;
                                        DatagramPacket mReceivedPacket4 = mReceivedPacket;
                                        i2 = 5;
                                        i = 6;
                                        LogUtils.e(UDPClient.TAG, "Time out during receiving from the UDP server");
                                        int tries3 = tries2 + 1;
                                        if (tries2 < 10) {
                                            LogUtils.i(UDPClient.TAG, "try again");
                                            tries2 = tries3;
                                            mSendPacket3 = mSendPacket;
                                            buf2 = buf3;
                                            mReceivedPacket3 = mReceivedPacket4;
                                            i3 = 1024;
                                            i4 = 1;
                                        } else {
                                            LogUtils.i(UDPClient.TAG, "reach to max try times, go out of main handler");
                                            break;
                                        }
                                    } catch (IOException e12) {
                                        i2 = 5;
                                        i = 6;
                                        e12.printStackTrace();
                                    }
                                }
                                try {
                                    UDPClient.this.mUDPSocket.send(new DatagramPacket(UDPClient.STOP_COMMAND, 0, 2, mAddress, UDPClient.SERVER_PORT));
                                    LogUtils.i(UDPClient.TAG, "Re-connect to the GPS HAL's UDP server");
                                    UDPClient.this.mUDPSocket.close();
                                    UDPClient.access$008(UDPClient.this);
                                    UDPClient.this.mFailureReason = i2;
                                } catch (IOException e13) {
                                    e13.printStackTrace();
                                    LogUtils.e(UDPClient.TAG, "Send STOP_COMMAND failed, try again");
                                    try {
                                        Thread.sleep(5000L);
                                    } catch (InterruptedException except2) {
                                        LogUtils.w(UDPClient.TAG, except2.getMessage());
                                    }
                                    UDPClient.this.mUDPSocket.close();
                                    UDPClient.access$008(UDPClient.this);
                                    UDPClient.this.mFailureReason = i;
                                }
                            }
                        } catch (SocketException e14) {
                            LogUtils.e(UDPClient.TAG, "Can not setSoTimeout");
                            e14.printStackTrace();
                            try {
                                Thread.sleep(5000L);
                            } catch (InterruptedException except3) {
                                LogUtils.w(UDPClient.TAG, except3.getMessage());
                            }
                            UDPClient.this.mUDPSocket.close();
                            UDPClient.access$008(UDPClient.this);
                            UDPClient.this.mFailureReason = 3;
                        }
                    } catch (UnknownHostException e15) {
                        LogUtils.e(UDPClient.TAG, "Can not getByName 127.0.0.1");
                        e15.printStackTrace();
                        try {
                            Thread.sleep(5000L);
                        } catch (InterruptedException except4) {
                            LogUtils.w(UDPClient.TAG, except4.getMessage());
                        }
                        UDPClient.this.mUDPSocket.close();
                        UDPClient.access$008(UDPClient.this);
                        UDPClient.this.mFailureReason = 2;
                    }
                } catch (SocketException e16) {
                    LogUtils.e(UDPClient.TAG, "Can not create udp socket with port: 29948");
                    e16.printStackTrace();
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException except5) {
                        LogUtils.w(UDPClient.TAG, except5.getMessage());
                    }
                    UDPClient.access$008(UDPClient.this);
                    UDPClient.this.mFailureReason = 1;
                }
            } while (!UDPClient.this.mIsStarted);
            LogUtils.e(UDPClient.TAG, "Exit the mSocketHandlerRunnable thread, error occurred.");
        }
    };

    /* loaded from: classes.dex */
    public interface UDPClientListener {
        void onSensorChanged(int i, float[] fArr, long j, float f);
    }

    static /* synthetic */ int access$008(UDPClient x0) {
        int i = x0.mFailureCounter;
        x0.mFailureCounter = i + 1;
        return i;
    }

    public static UDPClient getInstance() {
        if (sUDPClient == null) {
            LogUtils.i(TAG, "create UDPClient Instance");
            sUDPClient = new UDPClient();
        }
        return sUDPClient;
    }

    public void setUDPClientListener(UDPClientListener listener) {
        this.mListener = listener;
    }

    public void enableADR(boolean enable) {
        if (enable) {
            this.mNeedSendADRCommand = 1;
        } else {
            this.mNeedSendADRCommand = 0;
        }
    }

    public void open() {
        if (this.mIsStarted) {
            return;
        }
        this.mThread = new HandlerThread("UDP");
        this.mThread.start();
        this.mHandler = new Handler(this.mThread.getLooper());
        this.mIsStarted = true;
        LogUtils.i(TAG, "Try to open UDP socket");
        this.mHandler.post(this.mSocketHandlerRunnable);
    }

    public void close() {
        if (this.mIsStarted) {
            this.mIsStarted = false;
            this.mUDPSocket.close();
            this.mHandler.removeCallbacks(this.mSocketHandlerRunnable);
            this.mThread.quitSafely();
            this.mListener = null;
        }
    }
}
