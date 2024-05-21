package com.xpeng.upso.proxy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Keep;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengPsoClientType;
import com.xpeng.upso.XpengSecretType;
import com.xpeng.upso.cduc.CduClientThread;
import com.xpeng.upso.proxy.ProxySocketClientRunnable;
import com.xpeng.upso.selftest.tboxClientTest.TboxClientThread;
import com.xpeng.upso.selftest.xpuClientTest.XpuClientThread;
import com.xpeng.upso.sentry.SentryReporter;
import com.xpeng.upso.utils.LogUtils;
import com.xpeng.upso.utils.ThreadUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
@Keep
/* loaded from: classes2.dex */
public class ProxyService extends Service {
    private static final String TAG = "Upso-Service";
    private static Context context;
    private static boolean useAndroidKeystore;
    private CduClientThread cduClientThread;
    private ProxyParamWrapper paramWrapper;
    private boolean presetStarted;
    private ServerSocketRunnable serverSocketRunnable;
    private TboxClientThread tboxClientThread;
    private boolean verifyStarted;
    private XpuClientThread xpuClientThread;
    private IBinder binder = null;
    private IUpsoEventCallback upsoEventCallback = null;
    private ConcurrentHashMap<Integer, ProxySocketClientRunnable> clientMap = new ConcurrentHashMap<>();
    private ProxySocketClientRunnable.IClientEventCallback mClientEventCallback = new ProxySocketClientRunnable.IClientEventCallback() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxyService$5ctSJs8-xLiSV55TXFI1lJ5Idp8
        @Override // com.xpeng.upso.proxy.ProxySocketClientRunnable.IClientEventCallback
        public final void onClientPresetStatusCallback(int i, String str, int i2) {
            ProxyService.this.lambda$new$0$ProxyService(i, str, i2);
        }
    };

    /* loaded from: classes2.dex */
    public interface IUpsoEventCallback {
        void onPresetStatus(String clientID, int statusCode);
    }

    public static Context getAppContext() {
        return context;
    }

    @Deprecated
    public synchronized void setParam(XpengCarModel carModel, XpengCarChipModel chipModel, XpengPsoClientType clientType) {
        this.paramWrapper = new ProxyParamWrapper();
        this.paramWrapper.setCarModel(carModel);
        this.paramWrapper.setChipModel(chipModel);
        this.paramWrapper.setClientType(clientType);
        this.paramWrapper.setSecretType(XpengSecretType.AES_32);
        if (checkParam() && !isServerReady()) {
            startProxyServer();
        }
    }

    public synchronized void setParam(ProxyParamWrapper param) {
        this.paramWrapper = param;
        ProxyConstants.setServerPort(this.paramWrapper.getProxyServerPort());
        if (checkParam() && !isServerReady()) {
            startProxyServer();
        }
    }

    public ProxyParamWrapper getParam() {
        return this.paramWrapper;
    }

    public synchronized void setPresetEventCallback(IUpsoEventCallback callback) {
        this.upsoEventCallback = callback;
    }

    public synchronized boolean startPreset() {
        LogUtils.d(TAG, "startPrest, use android keystore = " + useAndroidKeystore);
        if (checkParam()) {
            this.presetStarted = true;
            this.verifyStarted = false;
            return startImpl();
        }
        return false;
    }

    public synchronized boolean stop() {
        try {
            this.presetStarted = false;
            this.verifyStarted = false;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return true;
        }
        return stopImpl();
    }

    public synchronized boolean startVerify() {
        if (!checkParam()) {
            LogUtils.e(TAG, "checkParam fail");
            return false;
        }
        this.verifyStarted = true;
        this.presetStarted = false;
        return startImpl();
    }

    public synchronized int getConnectedSize() {
        return this.clientMap.size();
    }

    private boolean startImpl() {
        LogUtils.d(TAG, "clientType : " + this.paramWrapper.getClientType());
        ThreadUtil.runOnMainThreadDelay(new Runnable() { // from class: com.xpeng.upso.proxy.ProxyService.1
            @Override // java.lang.Runnable
            public void run() {
                ProxySocketClientRunnable current;
                synchronized (ProxyService.this) {
                    Iterator<Integer> keySets = ProxyService.this.clientMap.keySet().iterator();
                    while (true) {
                        if (!keySets.hasNext()) {
                            break;
                        }
                        current = (ProxySocketClientRunnable) ProxyService.this.clientMap.get(keySets.next());
                        if (current.getClientEcuDeviceType() == null || ProxyService.this.paramWrapper == null || current.getClientEcuDeviceType().equals(ProxyService.this.paramWrapper.getClientType().toString())) {
                            if (!ProxyService.this.presetStarted) {
                                if (ProxyService.this.verifyStarted) {
                                    LogUtils.d(ProxyService.TAG, "try to start OP_START_VERIFY");
                                    if (!current.isStarted() || current.opIsIdle()) {
                                        break;
                                    } else if (ProxyService.this.paramWrapper.getClientType().equals(XpengPsoClientType.XPU)) {
                                        current.startOp(2);
                                        break;
                                    }
                                }
                            } else {
                                LogUtils.d(ProxyService.TAG, "try to start OP_START_PRESET");
                                if (!current.isStarted()) {
                                    current.startOp(1);
                                    break;
                                }
                            }
                        }
                    }
                    current.startOp(2);
                }
            }
        }, 10L);
        return true;
    }

    public boolean startXpuClient() {
        this.paramWrapper.getClientType().equals(XpengPsoClientType.XPU);
        return false;
    }

    private boolean stopImpl() {
        clearClientMap();
        if (this.paramWrapper.getClientType().equals(XpengPsoClientType.CDU) || this.paramWrapper.getClientType().equals(XpengPsoClientType.TBOX)) {
            stopCdu();
            stopTBOX();
            stopXPU();
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearClientMap() {
        for (Integer key : this.clientMap.keySet()) {
            ProxySocketClientRunnable current = this.clientMap.get(key);
            current.stop();
        }
        LogUtils.i(TAG, "clear clientMap");
        this.clientMap.clear();
    }

    public synchronized void setUseAndroidKeystore(boolean use) {
        useAndroidKeystore = use;
    }

    public static synchronized boolean isUsingAndroidKeystore() {
        boolean z;
        synchronized (ProxyService.class) {
            z = useAndroidKeystore;
        }
        return z;
    }

    /* loaded from: classes2.dex */
    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public ProxyService getService() {
            return ProxyService.this;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        SentryReporter.init(this);
        LogUtils.d(TAG, "onCreate,ver=2.3.7");
        context = getApplicationContext();
        useAndroidKeystore = false;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        LogUtils.d(TAG, "onBind");
        if (this.binder == null) {
            this.binder = new LocalBinder();
        }
        return this.binder;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "onStartCommand");
        return 0;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy...");
        stop();
        closeServerSocket();
    }

    void startProxyServer() {
        closeServerSocket();
        this.serverSocketRunnable = new ServerSocketRunnable();
        new Thread(this.serverSocketRunnable).start();
    }

    boolean isServerReady() {
        ServerSocketRunnable serverSocketRunnable = this.serverSocketRunnable;
        return serverSocketRunnable != null && serverSocketRunnable.isServerReady;
    }

    private boolean checkParam() {
        ProxyParamWrapper proxyParamWrapper = this.paramWrapper;
        if (proxyParamWrapper == null) {
            LogUtils.e(TAG, "paramWrapper = null");
            return false;
        } else if (proxyParamWrapper.getChipModel() == null) {
            LogUtils.e(TAG, "ChipModel = null");
            return false;
        } else if (this.paramWrapper.getClientType() == null) {
            LogUtils.e(TAG, "ClientType = null");
            return false;
        } else if (this.paramWrapper.getSecretType() == null) {
            LogUtils.e(TAG, "SecretType = null");
            return false;
        } else {
            return true;
        }
    }

    private void startCdu(final int op) {
        stopCdu();
        ThreadUtil.runOnChildThreadDelay(new Runnable() { // from class: com.xpeng.upso.proxy.ProxyService.2
            @Override // java.lang.Runnable
            public void run() {
                ProxyService proxyService = ProxyService.this;
                proxyService.cduClientThread = new CduClientThread(proxyService.getApplicationContext(), op, ProxyService.this.paramWrapper);
                ProxyService.this.cduClientThread.start();
            }
        }, 200L);
    }

    private void stopCdu() {
        CduClientThread cduClientThread = this.cduClientThread;
        if (cduClientThread != null) {
            cduClientThread.cancel();
            this.cduClientThread = null;
        }
    }

    private void stopXPU() {
        XpuClientThread xpuClientThread = this.xpuClientThread;
        if (xpuClientThread != null) {
            xpuClientThread.cancel();
            this.xpuClientThread = null;
        }
    }

    private void startXPU(int op) {
        stopXPU();
        this.xpuClientThread = new XpuClientThread(getApplicationContext(), op, this.paramWrapper);
        this.xpuClientThread.start();
    }

    private void stopTBOX() {
        TboxClientThread tboxClientThread = this.tboxClientThread;
        if (tboxClientThread != null) {
            tboxClientThread.cancel();
            this.tboxClientThread = null;
        }
    }

    private void startTBOX(int op) {
        stopTBOX();
        this.tboxClientThread = new TboxClientThread(getApplicationContext(), op, this.paramWrapper);
        this.tboxClientThread.start();
    }

    private void closeServerSocket() {
        ServerSocketRunnable serverSocketRunnable = this.serverSocketRunnable;
        if (serverSocketRunnable != null) {
            serverSocketRunnable.cancel();
            this.serverSocketRunnable = null;
        }
    }

    public /* synthetic */ void lambda$new$0$ProxyService(int clientId, String deviceId, int statusCode) {
        LogUtils.d(TAG, "onClientPresetStatusCallback: client: " + clientId + " deviceId: " + deviceId + " statusCode: " + statusCode);
        boolean needRemove = false;
        if (statusCode >= 100 && statusCode <= 999) {
            needRemove = true;
        } else if (statusCode == 3) {
            needRemove = true;
        } else if (statusCode == 4) {
            needRemove = true;
        }
        if (needRemove) {
            stop();
            clearClientMap();
        }
        IUpsoEventCallback iUpsoEventCallback = this.upsoEventCallback;
        if (iUpsoEventCallback != null) {
            iUpsoEventCallback.onPresetStatus(deviceId, statusCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ServerSocketRunnable implements Runnable {
        private boolean isServerReady = false;
        private boolean isShutdown = false;
        private int runnableId = 0;
        private ServerSocket serverSocket;

        public ServerSocketRunnable() {
        }

        public synchronized void cancel() {
            LogUtils.i(ProxyService.TAG, "ServerSocketRunnable cancel");
            this.isShutdown = true;
            this.isServerReady = false;
            if (this.serverSocket != null) {
                try {
                    this.serverSocket.close();
                    LogUtils.i(ProxyService.TAG, "serverSocket close");
                } catch (Exception e) {
                }
                this.serverSocket = null;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            ServerSocket serverSocket;
            while (!this.isShutdown) {
                try {
                    this.serverSocket = new ServerSocket(ProxyConstants.SERVER_PORT);
                    this.isServerReady = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    this.isServerReady = false;
                }
                if (!this.isServerReady) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    LogUtils.d(ProxyService.TAG, "serverSocket isServerReady,prot=" + ProxyConstants.SERVER_PORT);
                    while (!this.isShutdown && (serverSocket = this.serverSocket) != null && !serverSocket.isClosed()) {
                        Socket socket = null;
                        try {
                            socket = this.serverSocket.accept();
                            LogUtils.d(ProxyService.TAG, "run: new client connected to this server");
                            this.runnableId++;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        if (socket != null) {
                            ProxyService.this.clearClientMap();
                            ProxySocketClientRunnable newClient = new ProxySocketClientRunnable(this.runnableId, ProxyService.this.paramWrapper, socket);
                            newClient.setClientEventCallback(ProxyService.this.mClientEventCallback);
                            ProxyService.this.clientMap.put(Integer.valueOf(this.runnableId), newClient);
                            synchronized (ProxyService.this) {
                                if (ProxyService.this.paramWrapper.getClientType().equals(XpengPsoClientType.XPU)) {
                                    ProxyService.this.presetStarted = true;
                                    ProxyService.this.verifyStarted = false;
                                    newClient.startOp(1);
                                }
                            }
                            ProxyThreadPool.getInstance().getClientPool().execute(newClient);
                        }
                    }
                }
            }
            LogUtils.d(ProxyService.TAG, "Sever socket Thread End");
        }
    }
}
