package com.xiaopeng.factory.presenter.security;

import android.car.hardware.CarPropertyValue;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.bean.car.TboxBean;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import com.xiaopeng.commonfunc.model.car.CarEventChangedListener;
import com.xiaopeng.commonfunc.model.security.TboxKeyModel;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.commonfunc.utils.SecureBootUtil;
import com.xiaopeng.commonfunc.utils.SystemPropertyUtil;
import com.xiaopeng.commonfunc.utils.TestResultUtil;
import com.xiaopeng.commonfunc.utils.UIUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.R;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.model.security.CduKeyModel;
import com.xiaopeng.factory.model.security.CertVerifyModel;
import com.xiaopeng.factory.model.security.DolbySecretKeyModel;
import com.xiaopeng.factory.model.security.PsuKeyModel;
import com.xiaopeng.factory.model.security.WifiAdmin;
import com.xiaopeng.factory.model.security.WifiKeyModel;
import com.xiaopeng.factory.system.socket.FactoryClientConnector;
import com.xiaopeng.factory.view.security.ISecurityView;
import com.xiaopeng.factory.view.security.IV18CdukeyView;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.http.ICallback;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.xmlconfig.Support;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengPsoClientType;
import com.xpeng.upso.XpengSecretType;
import com.xpeng.upso.proxy.ProxyParamWrapper;
import com.xpeng.upso.proxy.ProxyService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class TestSecurityPresenter {
    public static final byte BRUSH_EFUSE = 26;
    private static final String CDU_CERT_SITE = "100.64.254.1";
    public static final boolean IS_FACTORY_BIN = SystemPropertyUtil.isFactoryBinary();
    private static final String RESPONSE_OK = "ok";
    private static final int STEP_GET_ICCID = 3;
    public static final int STEP_PRESET_CDU_AES_KEY = 6;
    public static final int STEP_PRESET_TBOX_AES_KEY = 7;
    private static final int STEP_REQUEST_INSTALLED_BY_TBOX = 1;
    private static final int STEP_REQUEST_VERIFIED_BY_TBOX = 2;
    public static final int STEP_VERIFIED_CDU_AES_KEY = 4;
    public static final int STEP_VERIFIED_TBOX_AES_KEY = 5;
    private static final String TAG = "TestSecurityPresenter";
    public static final int V18KEY_CHECKING = 1;
    public static final int V18KEY_CHECK_DEFAULT = -1;
    public static final int V18KEY_CHECK_FAIL = 0;
    public static final int V18KEY_CHECK_PASS = 2;
    private volatile boolean hasAesKeyTaskRunning;
    private LinkedBlockingQueue<Integer> mAesKeyQueue;
    private XpengCarChipModel mCarChipModel;
    private XpengCarModel mCarModel;
    private CduKeyModel mCduKeyModel;
    private DmResponseWriter mDmResponseWriter;
    private DolbySecretKeyModel mDolbySecretKeyModel;
    private ProxyParamWrapper mParamWrapper;
    private ProxyService mProxyService;
    private PsuKeyModel mPsuKeyModel;
    private ISecurityView mSecurityView;
    private TboxKeyModel mTboxKeyModel;
    private IV18CdukeyView mV18CdukeyView;
    private WifiAdmin mWifiAdmin;
    private WifiKeyModel mWifiKeyModel;
    private boolean mEfuseStatus = false;
    private boolean mEfuseFlag = false;
    private int STEP_AES_KEY = -1;
    private final Handler mHandler = new Handler() { // from class: com.xiaopeng.factory.presenter.security.TestSecurityPresenter.1
        @Override // android.os.Handler
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            if (i == 1) {
                TestSecurityPresenter.this.mTboxKeyModel.startTboxCertInstall();
            } else if (i == 2) {
                TestSecurityPresenter.this.mTboxKeyModel.startTboxCertVerify();
            } else if (i == 3) {
                TestSecurityPresenter.this.mTboxKeyModel.getIccid();
            } else if (i == 4) {
                TestSecurityPresenter.this.startVerifyAesKey();
            }
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.xiaopeng.factory.presenter.security.TestSecurityPresenter.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d(TestSecurityPresenter.TAG, "onServiceConnected");
            ProxyService.LocalBinder localBinder = (ProxyService.LocalBinder) service;
            TestSecurityPresenter.this.mProxyService = localBinder.getService();
            TestSecurityPresenter.this.setProxyServiceParam();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d(TestSecurityPresenter.TAG, "onServiceDisconnected");
        }
    };
    private ProxyService.IUpsoEventCallback persetEventCallback = new ProxyService.IUpsoEventCallback() { // from class: com.xiaopeng.factory.presenter.security.TestSecurityPresenter.3
        @Override // com.xpeng.upso.proxy.ProxyService.IUpsoEventCallback
        public void onPresetStatus(String clientID, int statusCode) {
            LogUtils.i(TestSecurityPresenter.TAG, "clientID " + clientID + " statusCode " + statusCode);
            if (statusCode == 1) {
                LogUtils.i(TestSecurityPresenter.TAG, TestSecurityPresenter.this.STEP_AES_KEY + " onClientConnected");
            } else if (statusCode == 2) {
                LogUtils.i(TestSecurityPresenter.TAG, TestSecurityPresenter.this.STEP_AES_KEY + " onPresetSuccess");
                if (TestSecurityPresenter.this.STEP_AES_KEY == 6) {
                    if (TestSecurityPresenter.this.mSecurityView != null) {
                        TestSecurityPresenter.this.mSecurityView.updateCduAesKeyExistView(true);
                    }
                    TestSecurityPresenter.this.response4Dm(true, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_06);
                } else if (TestSecurityPresenter.this.STEP_AES_KEY == 7) {
                    if (TestSecurityPresenter.this.mSecurityView != null) {
                        TestSecurityPresenter.this.mSecurityView.updateTboxAesKeyExistView(true);
                    }
                    TestSecurityPresenter.this.response4Dm(true, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_05);
                }
                TestSecurityPresenter.this.hasAesKeyTaskRunning = false;
                TestSecurityPresenter.this.handlerAesKeyTask();
            } else if (statusCode == 3) {
                LogUtils.i(TestSecurityPresenter.TAG, TestSecurityPresenter.this.STEP_AES_KEY + " onVerifySuccess");
                if (TestSecurityPresenter.this.STEP_AES_KEY == 4) {
                    TestSecurityPresenter.this.updateCduAesKeyView(true);
                    TestSecurityPresenter.this.response4Dm(true, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_06);
                } else if (TestSecurityPresenter.this.STEP_AES_KEY == 5) {
                    TestSecurityPresenter.this.updateTboxAesKeyView(true);
                    TestSecurityPresenter.this.response4Dm(true, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_05);
                }
                TestSecurityPresenter.this.mProxyService.stop();
                TestSecurityPresenter.this.hasAesKeyTaskRunning = false;
                TestSecurityPresenter.this.handlerAesKeyTask();
            } else if (statusCode == 106) {
                LogUtils.i(TestSecurityPresenter.TAG, TestSecurityPresenter.this.STEP_AES_KEY + " KEY_MISSING");
                if (TestSecurityPresenter.this.STEP_AES_KEY == 4) {
                    TestSecurityPresenter.this.updateCduAesKeyView(false);
                    TestSecurityPresenter.this.response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_06);
                } else if (TestSecurityPresenter.this.STEP_AES_KEY == 5) {
                    TestSecurityPresenter.this.updateTboxAesKeyView(false);
                    TestSecurityPresenter.this.response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_05);
                }
                TestSecurityPresenter.this.hasAesKeyTaskRunning = false;
                TestSecurityPresenter.this.handlerAesKeyTask();
            }
            if (statusCode >= 100 && statusCode <= 999) {
                LogUtils.i(TestSecurityPresenter.TAG, TestSecurityPresenter.this.STEP_AES_KEY + " onPresetOrVerifyFail");
                if (TestSecurityPresenter.this.STEP_AES_KEY == 6) {
                    TestSecurityPresenter.this.updateCduAesKeyView(false);
                    TestSecurityPresenter.this.response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_06);
                } else if (TestSecurityPresenter.this.STEP_AES_KEY == 7) {
                    TestSecurityPresenter.this.updateTboxAesKeyView(false);
                    TestSecurityPresenter.this.response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_05);
                } else if (TestSecurityPresenter.this.STEP_AES_KEY == 4) {
                    if (TestSecurityPresenter.this.mSecurityView != null) {
                        TestSecurityPresenter.this.mSecurityView.updateCduAesKeyVerifiedView(false);
                    }
                    TestSecurityPresenter.this.response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_06);
                } else if (TestSecurityPresenter.this.STEP_AES_KEY == 5) {
                    if (TestSecurityPresenter.this.mSecurityView != null) {
                        TestSecurityPresenter.this.mSecurityView.updateTboxAesKeyVerifiedView(false);
                    }
                    TestSecurityPresenter.this.response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_05);
                }
                TestSecurityPresenter.this.mProxyService.stop();
                TestSecurityPresenter.this.hasAesKeyTaskRunning = false;
                TestSecurityPresenter.this.handlerAesKeyTask();
            }
        }
    };
    private final FactoryClientConnector.ReceiverListener mFacSockReceiver = new FactoryClientConnector.ReceiverListener() { // from class: com.xiaopeng.factory.presenter.security.TestSecurityPresenter.4
        @Override // com.xiaopeng.factory.system.socket.FactoryClientConnector.ReceiverListener
        public void onReceiveData(byte[] data) {
            LogUtils.i(TestSecurityPresenter.TAG, "FacSock Receive data:" + DataHelp.byteArrayToHexStr(data, " "));
            byte[] cmd = null;
            if (data[0] == 26) {
                cmd = new byte[]{2, 7};
            }
            if (cmd != null && TestSecurityPresenter.this.mDmResponseWriter != null) {
                TestSecurityPresenter.this.mDmResponseWriter.write(data[2] == 0 ? DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, cmd) : DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, cmd));
            }
        }
    };
    private final CarEventChangedListener mEventChange = new CarEventChangedListener() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$uojpHGPh4ngwbQXr3v-VqWFUMmM
        @Override // com.xiaopeng.commonfunc.model.car.CarEventChangedListener
        public final void onChangeEvent(CarPropertyValue carPropertyValue) {
            TestSecurityPresenter.this.lambda$new$1$TestSecurityPresenter(carPropertyValue);
        }
    };

    public /* synthetic */ void lambda$new$1$TestSecurityPresenter(CarPropertyValue carPropertyValue) {
        byte[] responseNG;
        byte[] responseNG2;
        byte[] responseNG3;
        int id = carPropertyValue.getPropertyId();
        Object propertyValue = carPropertyValue.getValue();
        switch (id) {
            case 554700880:
                TboxBean tboxBean = (TboxBean) new Gson().fromJson((String) propertyValue, (Class<Object>) TboxBean.class);
                LogUtils.i(TAG, tboxBean.toString());
                if (tboxBean.getStatus() != 0) {
                    UIUtil.showToast(tboxBean.getStringValue1());
                    ISecurityView iSecurityView = this.mSecurityView;
                    if (iSecurityView != null) {
                        iSecurityView.updateTboxKeyView(false);
                        this.mSecurityView.updateTboxKeyVerifiedView(false);
                    }
                }
                int request = tboxBean.getRequest();
                if (request == 1) {
                    if (tboxBean.getStatus() == 0) {
                        if (this.mHandler.hasMessages(1)) {
                            this.mHandler.removeMessages(1);
                            this.mTboxKeyModel.reqTboxKey(MyApplication.getContext(), tboxBean.getStringValue2());
                            return;
                        } else if (this.mHandler.hasMessages(2)) {
                            this.mHandler.removeMessages(2);
                            this.mTboxKeyModel.requestGetTboxKey();
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                } else if (request != 2) {
                    if (request == 3) {
                        if (tboxBean.getStatus() == 0) {
                            final String tboxCertPath = tboxBean.getStringValue1();
                            final String md5 = tboxBean.getStringValue2();
                            ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$dgv2FB1Vd5wRmS9LmzFvszFBRdo
                                @Override // java.lang.Runnable
                                public final void run() {
                                    TestSecurityPresenter.this.lambda$new$0$TestSecurityPresenter(tboxCertPath, md5);
                                }
                            });
                            return;
                        }
                        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
                        if (dmResponseWriter != null) {
                            dmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_01));
                            return;
                        }
                        return;
                    }
                    return;
                } else {
                    DmResponseWriter dmResponseWriter2 = this.mDmResponseWriter;
                    if (dmResponseWriter2 != null) {
                        if (tboxBean.getStatus() == 0) {
                            responseNG = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_01);
                        } else {
                            responseNG = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_01);
                        }
                        dmResponseWriter2.write(responseNG);
                    }
                    if (tboxBean.getStatus() == 0 && this.mSecurityView != null) {
                        this.mTboxKeyModel.requestGetTboxKey();
                        return;
                    }
                    return;
                }
            case 557846538:
                int installRes = ((Integer) propertyValue).intValue();
                LogUtils.i(TAG, "ID_TBOX_CERT_INSTALL installRes:" + installRes);
                DmResponseWriter dmResponseWriter3 = this.mDmResponseWriter;
                if (dmResponseWriter3 != null) {
                    if (installRes == 0) {
                        responseNG2 = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_01);
                    } else {
                        responseNG2 = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_01);
                    }
                    dmResponseWriter3.write(responseNG2);
                }
                if (installRes == 0) {
                    if (this.mSecurityView != null) {
                        startTboxCertVerify();
                        return;
                    }
                    return;
                } else if (installRes == 1) {
                    UIUtil.showToast(MyApplication.getContext(), R.string.tbox_key_fail_unknown);
                    updateTboxView(false);
                    return;
                } else if (installRes != 2) {
                    if (installRes == 3) {
                        UIUtil.showToast(MyApplication.getContext(), R.string.tbox_key_fail_request_cerf);
                        updateTboxView(false);
                        return;
                    } else if (installRes == 4) {
                        UIUtil.showToast(MyApplication.getContext(), R.string.tbox_key_fail_parse_cerf);
                        updateTboxView(false);
                        return;
                    } else if (installRes == 5) {
                        UIUtil.showToast(MyApplication.getContext(), R.string.tbox_key_fail_verify_cerf);
                        updateTboxView(false);
                        return;
                    } else {
                        return;
                    }
                } else {
                    UIUtil.showToast(MyApplication.getContext(), R.string.tbox_key_fail_geticcid);
                    updateTboxView(false);
                    return;
                }
            case 557846539:
                int verifyRes = ((Integer) propertyValue).intValue();
                LogUtils.i(TAG, "ID_TBOX_CERT_VERIFY verifyRes:" + verifyRes);
                TestResultUtil.writeTestResult(TestResultItem.INDEX_TBOX_CERT, verifyRes == 0 ? TestResultItem.RESULT_PASS : TestResultItem.RESULT_FAIL);
                ISecurityView iSecurityView2 = this.mSecurityView;
                if (iSecurityView2 != null) {
                    iSecurityView2.updateTboxKeyView(verifyRes >= 0 && verifyRes < 10);
                    this.mSecurityView.updateTboxKeyVerifiedView(verifyRes == 0);
                }
                DmResponseWriter dmResponseWriter4 = this.mDmResponseWriter;
                if (dmResponseWriter4 != null) {
                    if (verifyRes == 0) {
                        responseNG3 = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_01);
                    } else {
                        responseNG3 = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_01);
                    }
                    dmResponseWriter4.write(responseNG3);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public /* synthetic */ void lambda$new$0$TestSecurityPresenter(String tboxCertPath, String md5) {
        if (this.mTboxKeyModel.copyTboxKeyToLocal(tboxCertPath, md5)) {
            this.mTboxKeyModel.verifyTboxCert(new ICallback<String, String>() { // from class: com.xiaopeng.factory.presenter.security.TestSecurityPresenter.5
                @Override // com.xiaopeng.lib.http.ICallback
                public void onSuccess(String data) {
                    TestSecurityPresenter.this.updateTboxView(true);
                    if (TestSecurityPresenter.this.mDmResponseWriter != null) {
                        TestSecurityPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_01));
                    }
                }

                @Override // com.xiaopeng.lib.http.ICallback
                public void onError(String err) {
                    TestSecurityPresenter.this.updateTboxView(false);
                    if (TestSecurityPresenter.this.mDmResponseWriter != null) {
                        TestSecurityPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_01));
                    }
                }
            });
            return;
        }
        updateTboxView(false);
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_01));
        }
    }

    public TestSecurityPresenter() {
        init();
    }

    public TestSecurityPresenter(ISecurityView view) {
        this.mSecurityView = view;
        init();
    }

    public TestSecurityPresenter(IV18CdukeyView view) {
        this.mV18CdukeyView = view;
        init();
    }

    public TestSecurityPresenter(DmResponseWriter dmResponseWriter) {
        init();
        this.mDmResponseWriter = dmResponseWriter;
    }

    private void init() {
        this.mCduKeyModel = new CduKeyModel();
        this.mWifiKeyModel = new WifiKeyModel();
        this.mPsuKeyModel = new PsuKeyModel();
        this.mTboxKeyModel = new TboxKeyModel(MyApplication.getContext());
        this.mWifiAdmin = new WifiAdmin(MyApplication.getContext());
        this.mCarChipModel = PsuKeyModel.getCarChipModel();
        this.mCarModel = PsuKeyModel.getCarModel();
        this.mDolbySecretKeyModel = new DolbySecretKeyModel(this.mCarModel, this.mCarChipModel);
        this.mParamWrapper = new ProxyParamWrapper();
        this.mAesKeyQueue = new LinkedBlockingQueue<>();
        Collection<Integer> ids = new ArrayList<>();
        ids.add(557846538);
        ids.add(557846539);
        ids.add(554700880);
        this.mTboxKeyModel.registerPropCallback(ids, this.mEventChange);
        Intent intent = new Intent(MyApplication.getContext(), ProxyService.class);
        boolean z = true;
        MyApplication.getContext().bindService(intent, this.mServiceConnection, 1);
        LogUtils.d(TAG, "start secret service");
        this.mEfuseStatus = SecureBootUtil.getEfuseStatus() > 0;
        if (!IS_FACTORY_BIN || !SecureBootUtil.SUPPORT_SECURE_BOOT || this.mEfuseStatus) {
            z = false;
        }
        this.mEfuseFlag = z;
        LogUtils.i(TAG, "Efuse Status = " + this.mEfuseStatus + ", Efuse flag = " + this.mEfuseFlag);
    }

    public void onDestroy() {
        this.mCduKeyModel.onDestroy();
        this.mTboxKeyModel.onDestroy();
        ProxyService proxyService = this.mProxyService;
        if (proxyService != null) {
            proxyService.stopSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTboxView(boolean res) {
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateTboxKeyView(res);
            this.mSecurityView.updateTboxKeyVerifiedView(res);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCduAesKeyView(boolean res) {
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateCduAesKeyExistView(res);
            this.mSecurityView.updateCduAesKeyVerifiedView(res);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTboxAesKeyView(boolean res) {
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateTboxAesKeyExistView(res);
            this.mSecurityView.updateTboxAesKeyVerifiedView(res);
        }
    }

    public void connectCertWifi() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$qlbNbcPJz5F7F5tC4IVhG1J4-SU
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$connectCertWifi$2$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$connectCertWifi$2$TestSecurityPresenter() {
        boolean connected = isCertWifiConnected();
        if (!connected) {
            connected = this.mWifiAdmin.waitConnectToServerWifi(Constant.CERT_WIFI_SSID, Constant.CERT_WIFI_PASS);
        }
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateWifiConnectionStatus(connected);
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(connected ? DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_00_00) : DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_00_00));
        }
    }

    public void disconnectCertWifi() {
        this.mWifiAdmin.closeServerWifi(Constant.CERT_WIFI_SSID);
    }

    public boolean isCertWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getContext().getSystemService("connectivity");
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected() && ni.getSubtype() == 2) || this.mWifiAdmin.isWifiConnected("\"xp-sec\"");
    }

    public void startTboxCertInstall() {
        this.mHandler.sendEmptyMessage(3);
        this.mHandler.sendEmptyMessageDelayed(1, 2500L);
    }

    public void startTboxCertVerify() {
        this.mHandler.sendEmptyMessage(3);
        this.mHandler.sendEmptyMessageDelayed(2, 2500L);
        TestResultUtil.writeTestResult(TestResultItem.INDEX_TBOX_CERT, TestResultItem.RESULT_ENTER);
    }

    public void getPsuKey() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$pOww0wazoD0EFSheEH98tStkJ2U
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$getPsuKey$4$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$getPsuKey$4$TestSecurityPresenter() {
        if (this.mEfuseFlag) {
            ISecurityView iSecurityView = this.mSecurityView;
            if (iSecurityView != null) {
                iSecurityView.updateNeedEfuseEnabledView();
            }
            DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
            if (dmResponseWriter != null) {
                dmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_03));
            }
        } else if (!isCertWifiConnected()) {
            ISecurityView iSecurityView2 = this.mSecurityView;
            if (iSecurityView2 != null) {
                iSecurityView2.updateWifiConnectionStatus(false);
            }
            DmResponseWriter dmResponseWriter2 = this.mDmResponseWriter;
            if (dmResponseWriter2 != null) {
                dmResponseWriter2.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_03));
            }
        } else {
            this.mPsuKeyModel.asyncGetKey(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$je8Dd3hzQUWDqU7fud4JPIE5uhU
                @Override // java.lang.Runnable
                public final void run() {
                    TestSecurityPresenter.this.lambda$getPsuKey$3$TestSecurityPresenter();
                }
            });
        }
    }

    public /* synthetic */ void lambda$getPsuKey$3$TestSecurityPresenter() {
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updatePsuKeyExistView();
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(checkPsuKeyExist() ? DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_03) : DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_03));
        }
    }

    public boolean checkPsuKeyExist() {
        boolean res = this.mPsuKeyModel.isPsuKeyExist();
        LogUtils.i(TAG, "checkPsuKeyExist res:" + res);
        return res;
    }

    public void verifyPsuKey() {
        LogUtils.i(TAG, "verifyPsuKey");
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$YbZ0sIKnp6cgByNtwQ5Dxtxe0EQ
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$verifyPsuKey$5$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$verifyPsuKey$5$TestSecurityPresenter() {
        byte[] responseNG;
        TestResultUtil.writeTestResult(234, TestResultItem.RESULT_ENTER);
        boolean result = this.mPsuKeyModel.verifyPsuKey();
        TestResultUtil.writeTestResult(234, result ? TestResultItem.RESULT_PASS : TestResultItem.RESULT_FAIL);
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updatePsuKeyVerifiedView(result);
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            if (result) {
                responseNG = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_03);
            } else {
                responseNG = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_03);
            }
            dmResponseWriter.write(responseNG);
        }
    }

    public void getWifiKey() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$Ud9JSp-mhFQn6QP1QFhkYKN9cq4
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$getWifiKey$7$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$getWifiKey$7$TestSecurityPresenter() {
        if (!isCertWifiConnected()) {
            ISecurityView iSecurityView = this.mSecurityView;
            if (iSecurityView != null) {
                iSecurityView.updateWifiConnectionStatus(false);
            }
            DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
            if (dmResponseWriter != null) {
                dmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_02));
                return;
            }
            return;
        }
        this.mWifiKeyModel.asyncGetKey(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$M5Gg4tk7Z1Vd4nJF0JAlz2_jtxY
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$getWifiKey$6$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$getWifiKey$6$TestSecurityPresenter() {
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateWifiKeyExistView();
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(checkWifiKeyExist() ? DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_02) : DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_02));
        }
    }

    public boolean checkWifiKeyExist() {
        boolean res = this.mWifiKeyModel.isWifiKeyExist();
        LogUtils.i(TAG, "checkWifiKeyExist res:" + res);
        return res;
    }

    public void verifyWifiKey() {
        TestResultUtil.writeTestResult(TestResultItem.INDEX_WIFI_CERT, TestResultItem.RESULT_ENTER);
        if (!checkWifiKeyExist() || SystemPropertyUtil.getHardwareId().length() <= 8) {
            TestResultUtil.writeTestResult(TestResultItem.INDEX_WIFI_CERT, TestResultItem.RESULT_FAIL);
            ISecurityView iSecurityView = this.mSecurityView;
            if (iSecurityView != null) {
                iSecurityView.updateWifiKeyVerifiedView(false);
            }
            DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
            if (dmResponseWriter != null) {
                dmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_02));
                return;
            }
            return;
        }
        String password = "jCmX14gy4XLe" + SystemPropertyUtil.getHardwareId().substring(0, 8);
        LogUtils.i(TAG, "verifyWifiKey password:" + password);
        new CertVerifyModel("/mnt/vendor/private/ck58l92i5/wf_c.png", "/mnt/vendor/private/ck58l92i5/wf_r_bks.png", Support.Url.getUrl(Support.Url.CHECK_WIFI_KEY), password).verifyCert(new ICallback<String, String>() { // from class: com.xiaopeng.factory.presenter.security.TestSecurityPresenter.6
            @Override // com.xiaopeng.lib.http.ICallback
            public void onSuccess(String s) {
                byte[] responseNG;
                int code = 0;
                try {
                    code = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                TestResultUtil.writeTestResult(TestResultItem.INDEX_WIFI_CERT, code == 200 ? TestResultItem.RESULT_PASS : TestResultItem.RESULT_FAIL);
                if (TestSecurityPresenter.this.mSecurityView != null) {
                    TestSecurityPresenter.this.mSecurityView.updateWifiKeyVerifiedView(code == 200);
                }
                if (TestSecurityPresenter.this.mDmResponseWriter != null) {
                    DmResponseWriter dmResponseWriter2 = TestSecurityPresenter.this.mDmResponseWriter;
                    if (code == 200) {
                        responseNG = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_02);
                    } else {
                        responseNG = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_02);
                    }
                    dmResponseWriter2.write(responseNG);
                }
            }

            @Override // com.xiaopeng.lib.http.ICallback
            public void onError(String s) {
                TestResultUtil.writeTestResult(TestResultItem.INDEX_WIFI_CERT, TestResultItem.RESULT_FAIL);
                if (TestSecurityPresenter.this.mSecurityView != null) {
                    TestSecurityPresenter.this.mSecurityView.updateWifiKeyVerifiedView(false);
                }
                if (TestSecurityPresenter.this.mDmResponseWriter != null) {
                    TestSecurityPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_02));
                }
            }
        });
    }

    public void saveCDUKey() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$jXVHaycJiaqCdbncrL1bNXGRGIY
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$saveCDUKey$8$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$saveCDUKey$8$TestSecurityPresenter() {
        String value = FileUtil.read(Constant.PATH_SDCARD + SystemPropertyUtil.getHardwareId());
        if (value != null) {
            try {
                this.mCduKeyModel.saveCDUKey(new JSONObject(value));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateCduKeyView();
        }
    }

    public void verifyCduKey() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$XKQ1y3KDTiy1BOCNMu3Hkt6d1Os
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$verifyCduKey$9$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$verifyCduKey$9$TestSecurityPresenter() {
        byte[] responseNG;
        TestResultUtil.writeTestResult(231, TestResultItem.RESULT_ENTER);
        boolean result = this.mCduKeyModel.verifyCduKey() && this.mCduKeyModel.verifyV18CduKey();
        TestResultUtil.writeTestResult(231, result ? TestResultItem.RESULT_PASS : TestResultItem.RESULT_FAIL);
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateCduKeyVerifiedView(result);
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            if (result) {
                responseNG = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_00);
            } else {
                responseNG = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_00);
            }
            dmResponseWriter.write(responseNG);
        }
    }

    public void changeV18CaCert() {
        byte[] responseNG;
        boolean result = this.mCduKeyModel.changeV18CaCert();
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            if (result) {
                responseNG = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_04);
            } else {
                responseNG = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_04);
            }
            dmResponseWriter.write(responseNG);
        }
    }

    public void verifyV18CduKey() {
        IV18CdukeyView iV18CdukeyView = this.mV18CdukeyView;
        if (iV18CdukeyView != null) {
            iV18CdukeyView.updateCheckView(1);
        }
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$R6OmPLTuyW-Y2wKQiIcqkonjzfI
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$verifyV18CduKey$10$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$verifyV18CduKey$10$TestSecurityPresenter() {
        TestResultUtil.writeTestResult(236, TestResultItem.RESULT_ENTER);
        this.mCduKeyModel.verifyV18CduKey(new Callback() { // from class: com.xiaopeng.factory.presenter.security.TestSecurityPresenter.7
            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onSuccess(IResponse iResponse) {
                byte[] responseNG;
                LogUtils.i(TestSecurityPresenter.TAG, "iResponse.body() : " + iResponse.body());
                boolean result = iResponse.body().contains(TestSecurityPresenter.RESPONSE_OK);
                TestResultUtil.writeTestResult(236, result ? TestResultItem.RESULT_PASS : TestResultItem.RESULT_FAIL);
                if (TestSecurityPresenter.this.mDmResponseWriter != null) {
                    DmResponseWriter dmResponseWriter = TestSecurityPresenter.this.mDmResponseWriter;
                    if (result) {
                        responseNG = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_04);
                    } else {
                        responseNG = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_04);
                    }
                    dmResponseWriter.write(responseNG);
                }
                if (TestSecurityPresenter.this.mV18CdukeyView != null) {
                    TestSecurityPresenter.this.mV18CdukeyView.updateCheckView(result ? 2 : 0);
                }
            }

            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onFailure(IResponse iResponse) {
                LogUtils.i(TestSecurityPresenter.TAG, "verifyCduKey fail:");
                if (TestSecurityPresenter.this.mDmResponseWriter != null) {
                    TestSecurityPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_04));
                }
                if (TestSecurityPresenter.this.mV18CdukeyView != null) {
                    TestSecurityPresenter.this.mV18CdukeyView.updateCheckView(0);
                }
            }
        });
    }

    public boolean checkCduKey() {
        return this.mCduKeyModel.isCduKeyExist();
    }

    public boolean checkCduKeyFile() {
        return this.mCduKeyModel.isCduKeyFileExist();
    }

    public String getInputHardwareId() {
        return this.mCduKeyModel.getInputHardwareId();
    }

    public void getCduKey(final String hardwareId) {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$9n6k5xw45yLVVS3dvy-D_BVznYs
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$getCduKey$12$TestSecurityPresenter(hardwareId);
            }
        });
    }

    public /* synthetic */ void lambda$getCduKey$12$TestSecurityPresenter(String hardwareId) {
        if (this.mEfuseFlag) {
            ISecurityView iSecurityView = this.mSecurityView;
            if (iSecurityView != null) {
                iSecurityView.updateNeedEfuseEnabledView();
            }
            DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
            if (dmResponseWriter != null) {
                dmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_00));
            }
        } else if (!isCertWifiConnected()) {
            ISecurityView iSecurityView2 = this.mSecurityView;
            if (iSecurityView2 != null) {
                iSecurityView2.updateWifiConnectionStatus(false);
            }
            DmResponseWriter dmResponseWriter2 = this.mDmResponseWriter;
            if (dmResponseWriter2 != null) {
                dmResponseWriter2.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_00));
            }
        } else {
            this.mCduKeyModel.asyncGetKey(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$qCQXNpdoYE3o-fGMMEJGryw3iFU
                @Override // java.lang.Runnable
                public final void run() {
                    TestSecurityPresenter.this.lambda$getCduKey$11$TestSecurityPresenter();
                }
            }, hardwareId);
        }
    }

    public /* synthetic */ void lambda$getCduKey$11$TestSecurityPresenter() {
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateCduKeyView();
            if (this.mCduKeyModel.getInputHardwareId() != null) {
                this.mSecurityView.updateCduKeyFileView();
            }
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(checkCduKey() ? DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_00) : DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_00));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProxyServiceParam() {
        ProxyService proxyService = this.mProxyService;
        if (proxyService != null) {
            proxyService.setPresetEventCallback(this.persetEventCallback);
            this.mParamWrapper.setCarModel(this.mCarModel);
            this.mParamWrapper.setChipModel(this.mCarChipModel);
            this.mParamWrapper.setSecretType(XpengSecretType.AES_32);
            this.mParamWrapper.setClientType(XpengPsoClientType.TBOX);
            this.mProxyService.setUseAndroidKeystore(false);
            this.mProxyService.setParam(this.mParamWrapper);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void response4Dm(boolean res, byte[] cmd, byte[] argus) {
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(res ? DmUtil.responseOK(cmd, argus) : DmUtil.responseNG(cmd, argus));
        }
    }

    public void startVerifyAesKey() {
        if (this.mProxyService == null) {
            this.mHandler.sendEmptyMessageDelayed(4, 50L);
        } else {
            addAesKeyTask(4);
        }
    }

    public void presetCduAesKey() {
        if (this.mProxyService != null && this.mCarModel != null) {
            this.hasAesKeyTaskRunning = true;
            this.STEP_AES_KEY = 6;
            LogUtils.i(TAG, "set STEP_AES_KEY " + this.STEP_AES_KEY);
            this.mParamWrapper.setClientType(XpengPsoClientType.CDU);
            this.mProxyService.setParam(this.mParamWrapper);
            this.mProxyService.startPreset();
        } else {
            LogUtils.i(TAG, "preset cdu aes key, ProxyService is not connect, or carModel is null");
            updateCduAesKeyView(false);
            response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_06);
        }
        handlerAesKeyTask();
    }

    public void verifyCduAesKey() {
        if (this.mProxyService != null && this.mCarModel != null) {
            this.hasAesKeyTaskRunning = true;
            this.STEP_AES_KEY = 4;
            LogUtils.i(TAG, "set STEP_AES_KEY " + this.STEP_AES_KEY);
            this.mParamWrapper.setClientType(XpengPsoClientType.CDU);
            this.mProxyService.setParam(this.mParamWrapper);
            this.mProxyService.startVerify();
            return;
        }
        LogUtils.i(TAG, "verify cdu aes key, ProxyService is not connect, or carModel is null");
        updateCduAesKeyView(false);
        response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_06);
        handlerAesKeyTask();
    }

    public void presetTboxAesKey() {
        ProxyService proxyService = this.mProxyService;
        if (proxyService != null && this.mCarModel != null && proxyService.getConnectedSize() > 0) {
            this.hasAesKeyTaskRunning = true;
            this.STEP_AES_KEY = 7;
            LogUtils.i(TAG, "set STEP_AES_KEY " + this.STEP_AES_KEY);
            this.mParamWrapper.setClientType(XpengPsoClientType.TBOX);
            this.mProxyService.setParam(this.mParamWrapper);
            this.mProxyService.startPreset();
            return;
        }
        LogUtils.i(TAG, "preset tbox aes key, ProxyService is not connect, or carModel is null");
        if (this.mProxyService != null) {
            LogUtils.i(TAG, "preset tbox aes key ProxyService Connected Size: " + this.mProxyService.getConnectedSize());
        }
        updateTboxAesKeyView(false);
        response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_02_05);
        handlerAesKeyTask();
    }

    public void verifyTboxAesKey() {
        ProxyService proxyService = this.mProxyService;
        if (proxyService != null && this.mCarModel != null && proxyService.getConnectedSize() > 0) {
            this.hasAesKeyTaskRunning = true;
            this.STEP_AES_KEY = 5;
            LogUtils.i(TAG, "set STEP_AES_KEY " + this.STEP_AES_KEY);
            this.mParamWrapper.setClientType(XpengPsoClientType.TBOX);
            this.mProxyService.setParam(this.mParamWrapper);
            this.mProxyService.startVerify();
            return;
        }
        LogUtils.i(TAG, "verify tbox aes key, ProxyService is not connect, or carModel is null");
        if (this.mProxyService != null) {
            LogUtils.i(TAG, "verify tbox aes key,ProxyService Connected Size: " + this.mProxyService.getConnectedSize());
        }
        updateTboxAesKeyView(false);
        response4Dm(false, DmUtil.SecurityTest.CMD_NAME, DmUtil.ARGU_01_05);
        handlerAesKeyTask();
    }

    public void addAesKeyTask(int type) {
        LogUtils.i(TAG, "add aes key task type " + type);
        this.mAesKeyQueue.offer(Integer.valueOf(type));
        handlerAesKeyTask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handlerAesKeyTask() {
        if (!this.mAesKeyQueue.isEmpty() && !this.hasAesKeyTaskRunning) {
            LogUtils.i(TAG, "handle aes key task type " + this.mAesKeyQueue.peek());
            int intValue = this.mAesKeyQueue.poll().intValue();
            if (intValue == 4) {
                verifyCduAesKey();
            } else if (intValue == 5) {
                verifyTboxAesKey();
            } else if (intValue == 6) {
                presetCduAesKey();
            } else if (intValue == 7) {
                presetTboxAesKey();
            }
        }
    }

    public void brushEfuse() {
        if (this.mEfuseStatus) {
            DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
            if (dmResponseWriter != null) {
                dmResponseWriter.write(DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, new byte[]{2, 7}));
                return;
            }
            return;
        }
        int4Dm();
        FactoryClientConnector.getInstance().send(new byte[]{BRUSH_EFUSE, 0});
    }

    public void int4Dm() {
        FactoryClientConnector.getInstance().setReceiverListener(this.mFacSockReceiver);
    }

    public boolean checkDolbySecretKey() {
        return this.mDolbySecretKeyModel.isDolbySecretKeyExist();
    }

    public void verifyDolbySecretKey() {
        LogUtils.i(TAG, "verifyDolbySecretKey");
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$lNJCC8LY0vlcGUZ0qDneuXNpv4I
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$verifyDolbySecretKey$13$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$verifyDolbySecretKey$13$TestSecurityPresenter() {
        boolean res = this.mDolbySecretKeyModel.verifyDolbySecretKey();
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateDolbySecretKeyVerifiedView(res);
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(res ? DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, new byte[]{1, 8}) : DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, new byte[]{1, 8}));
        }
    }

    public void presetDolbySecretKey() {
        LogUtils.i(TAG, "presetDolbySecretKey");
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$Z9BXL5kzQUKtW-HTQR55bsT3BTI
            @Override // java.lang.Runnable
            public final void run() {
                TestSecurityPresenter.this.lambda$presetDolbySecretKey$15$TestSecurityPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$presetDolbySecretKey$15$TestSecurityPresenter() {
        if (this.mEfuseFlag) {
            ISecurityView iSecurityView = this.mSecurityView;
            if (iSecurityView != null) {
                iSecurityView.updateNeedEfuseEnabledView();
            }
            DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
            if (dmResponseWriter != null) {
                dmResponseWriter.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, new byte[]{2, 8}));
            }
        } else if (!isCertWifiConnected()) {
            ISecurityView iSecurityView2 = this.mSecurityView;
            if (iSecurityView2 != null) {
                iSecurityView2.updateWifiConnectionStatus(false);
            }
            DmResponseWriter dmResponseWriter2 = this.mDmResponseWriter;
            if (dmResponseWriter2 != null) {
                dmResponseWriter2.write(DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, new byte[]{2, 8}));
            }
        } else {
            this.mDolbySecretKeyModel.asyncGetKey(new Runnable() { // from class: com.xiaopeng.factory.presenter.security.-$$Lambda$TestSecurityPresenter$rSG7hHW03sdgbsxw-Bg9yunvrUM
                @Override // java.lang.Runnable
                public final void run() {
                    TestSecurityPresenter.this.lambda$presetDolbySecretKey$14$TestSecurityPresenter();
                }
            });
        }
    }

    public /* synthetic */ void lambda$presetDolbySecretKey$14$TestSecurityPresenter() {
        byte[] responseNG;
        ISecurityView iSecurityView = this.mSecurityView;
        if (iSecurityView != null) {
            iSecurityView.updateDolbySecretKeyExistView();
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            if (checkDolbySecretKey()) {
                responseNG = DmUtil.responseOK(DmUtil.SecurityTest.CMD_NAME, new byte[]{2, 8});
            } else {
                responseNG = DmUtil.responseNG(DmUtil.SecurityTest.CMD_NAME, new byte[]{2, 8});
            }
            dmResponseWriter.write(responseNG);
        }
    }

    public boolean getEfuseFlag() {
        return this.mEfuseFlag;
    }

    public boolean getEfuseStatus() {
        return this.mEfuseStatus;
    }
}
