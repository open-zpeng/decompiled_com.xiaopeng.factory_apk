package com.xiaopeng.factory.presenter.indiv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.IndivHelper;
import com.xiaopeng.commonfunc.utils.SecureBootUtil;
import com.xiaopeng.commonfunc.utils.SystemPropertyUtil;
import com.xiaopeng.commonfunc.utils.TestResultUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.view.indiv.IIndivView;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.http.server.ServerBean;
import com.xiaopeng.lib.security.xmartv1.RandomKeySecurity;
import com.xiaopeng.lib.security.xmartv1.XmartV1Constants;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.lib.utils.config.CommonConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes2.dex */
public class IndivPresenter {
    public static final int INDIV_CHECKING = 1;
    public static final int INDIV_CHECK_DEFAULT = -1;
    public static final int INDIV_CHECK_FAIL = 0;
    public static final int INDIV_CHECK_PASS = 2;
    private static final String TAG = "IndivPresenter";
    private static final String TESTING_TEXT = "工厂检查indiv加解密状态";
    private DmResponseWriter mDmResponseWriter;
    private IIndivView mIndivView;
    private static final String URL_INDIV_TEST_ENCRYPTION = CommonConfig.HTTP_HOST + "/biz/v5/vehicle/factory/teeEncryptCheck";
    private static final String URL_INDIV_TEST_DECRYPTION = CommonConfig.HTTP_HOST + "/biz/v5/vehicle/factory/teeDecryptCheck";
    public static final boolean IS_FACTORY_BIN = SystemPropertyUtil.isFactoryBinary();
    private boolean mEfuseStatus = false;
    private boolean mEfuseFlag = false;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.factory.presenter.indiv.IndivPresenter.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            byte[] responseNG;
            String action = intent.getAction();
            if ("com.xiaopeng.action.SECURE_STORE_RELOAD".equals(action)) {
                LogUtils.i(IndivPresenter.TAG, "onReceive ACTION_BROADCAST_SECURE_STORE_RELOAD");
                IndivHelper.initMain(MyApplication.getContext());
                if (IndivPresenter.this.mIndivView != null) {
                    IndivPresenter.this.mIndivView.updateIndivButtonView();
                    IndivPresenter.this.checkIndiv();
                }
                if (IndivPresenter.this.mDmResponseWriter != null) {
                    DmResponseWriter dmResponseWriter = IndivPresenter.this.mDmResponseWriter;
                    if (IndivPresenter.this.isTeeInitSuccess()) {
                        responseNG = DmUtil.responseOK(DmUtil.IndivTest.CMD_NAME, DmUtil.ARGU_00_00);
                    } else {
                        responseNG = DmUtil.responseNG(DmUtil.IndivTest.CMD_NAME, DmUtil.ARGU_00_00);
                    }
                    dmResponseWriter.write(responseNG);
                }
            }
        }
    };

    public IndivPresenter(IIndivView indivView) {
        this.mIndivView = indivView;
    }

    public IndivPresenter(DmResponseWriter writer) {
        this.mDmResponseWriter = writer;
    }

    public void init() {
        boolean z = true;
        this.mEfuseStatus = SecureBootUtil.getEfuseStatus() > 0;
        if (!IS_FACTORY_BIN || !SecureBootUtil.SUPPORT_SECURE_BOOT || this.mEfuseStatus) {
            z = false;
        }
        this.mEfuseFlag = z;
        LogUtils.i(TAG, "Efuse Status = " + this.mEfuseStatus + ", Efuse flag = " + this.mEfuseFlag);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xiaopeng.action.SECURE_STORE_RELOAD");
        MyApplication.getContext().registerReceiver(this.mReceiver, filter);
    }

    public void deinit() {
        MyApplication.getContext().unregisterReceiver(this.mReceiver);
    }

    public boolean isTeeInitSuccess() {
        return IndivHelper.isInitSuccess();
    }

    public void clearLocalIndividualData() {
        IndivHelper.clearLocalIndividualData();
    }

    public void asyncInitIndivService() {
        IndivHelper.asyncInitIndivService();
    }

    public void checkIndiv() {
        IIndivView iIndivView = this.mIndivView;
        if (iIndivView != null) {
            iIndivView.updateIndivCheckView(1);
        }
        if (this.mEfuseFlag) {
            IIndivView iIndivView2 = this.mIndivView;
            if (iIndivView2 != null) {
                iIndivView2.updateNeedEfuseEnabledView();
            }
            DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
            if (dmResponseWriter != null) {
                dmResponseWriter.write(DmUtil.responseNG(DmUtil.IndivTest.CMD_NAME, DmUtil.ARGU_00_01));
                return;
            }
            return;
        }
        ThreadUtils.postBackground(new Runnable() { // from class: com.xiaopeng.factory.presenter.indiv.-$$Lambda$IndivPresenter$_O6RwpJRRVfa9XLum7epbri98GI
            @Override // java.lang.Runnable
            public final void run() {
                IndivPresenter.this.lambda$checkIndiv$0$IndivPresenter();
            }
        });
    }

    public /* synthetic */ void lambda$checkIndiv$0$IndivPresenter() {
        byte[] responseNG;
        TestResultUtil.writeTestResult(235, TestResultItem.RESULT_ENTER);
        boolean result = testEncryption() && testDecryption();
        IIndivView iIndivView = this.mIndivView;
        if (iIndivView != null) {
            iIndivView.updateIndivCheckView(result ? 2 : 0);
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            if (result) {
                responseNG = DmUtil.responseOK(DmUtil.IndivTest.CMD_NAME, DmUtil.ARGU_00_01);
            } else {
                responseNG = DmUtil.responseNG(DmUtil.IndivTest.CMD_NAME, DmUtil.ARGU_00_01);
            }
            dmResponseWriter.write(responseNG);
        }
        TestResultUtil.writeTestResult(235, result ? TestResultItem.RESULT_PASS : TestResultItem.RESULT_FAIL);
    }

    private boolean testEncryption() {
        EncryptCheckBean bean = new EncryptCheckBean();
        bean.rawData = TESTING_TEXT;
        bean.teeDataList = new ArrayList<>();
        for (int i = 0; i < XmartV1Constants.LOCAL_KEYS_NUM; i++) {
            bean.teeDataList.add(RandomKeySecurity.getInstance().encodeWithIndex(TESTING_TEXT, i));
        }
        String requestJson = new Gson().toJson(bean);
        LogUtils.i(TAG, "Encrypt check string:" + requestJson);
        try {
            IHttp http = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
            IResponse response = http.bizHelper().post(URL_INDIV_TEST_ENCRYPTION, requestJson).build().execute();
            ServerBean serverBean = DataHelp.getServerBean(response);
            boolean result = serverBean.getCode() == 200;
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean testDecryption() {
        EncryptCheckBean bean;
        Map<String, String> param = new HashMap<>(1);
        param.put(Constant.KEY_RAW_DATA, TESTING_TEXT);
        try {
            IHttp http = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
            IResponse response = http.bizHelper().post(URL_INDIV_TEST_DECRYPTION, new Gson().toJson(param)).build().execute();
            ServerBean serverBean = DataHelp.getServerBean(response);
            if (serverBean.getCode() != 200) {
                return false;
            }
            String data = serverBean.getData();
            if (!TextUtils.isEmpty(data) && (bean = (EncryptCheckBean) new Gson().fromJson(data, (Class<Object>) EncryptCheckBean.class)) != null && bean.teeDataList.size() == XmartV1Constants.LOCAL_KEYS_NUM) {
                for (int i = 0; i < bean.teeDataList.size(); i++) {
                    if (!TESTING_TEXT.equals(RandomKeySecurity.getInstance().decode(bean.teeDataList.get(i)))) {
                        LogUtils.i(TAG, "Decryption check failed with key: #" + i);
                        return false;
                    }
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class EncryptCheckBean {
        public String rawData;
        public ArrayList<String> teeDataList;

        private EncryptCheckBean() {
        }
    }

    public boolean getEfuseFlag() {
        return this.mEfuseFlag;
    }

    public boolean getEfuseStatus() {
        return this.mEfuseStatus;
    }
}
