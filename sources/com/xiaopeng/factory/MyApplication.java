package com.xiaopeng.factory;

import android.app.Application;
import android.content.Context;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import com.xiaopeng.commonfunc.utils.CarHelper;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.commonfunc.utils.IndivHelper;
import com.xiaopeng.commonfunc.utils.TestResultUtil;
import com.xiaopeng.datalog.DataLogModuleEntry;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficeStaFlagInterceptor;
import com.xiaopeng.lib.http.HttpsUtils;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ProcessUtils;
import com.xiaopeng.lib.utils.view.UIUtils;
import com.xiaopeng.xmlconfig.XMLDataStorage;
import com.xiaopeng.xui.Xui;
import org.greenrobot.eventbus.EventBus;
/* loaded from: classes.dex */
public class MyApplication extends Application {
    private static final String TAG = "FactoryTest";
    public static MyApplication mApp = null;
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override // android.app.Application
    public void onCreate() {
        LogUtils.i(TAG, "onCreate ");
        super.onCreate();
        long time = System.currentTimeMillis();
        sContext = getApplicationContext();
        mApp = this;
        parseXmlConfig();
        CarHelper.init(sContext);
        initTestResultFile();
        if (getPackageName().endsWith(ProcessUtils.getCurProcessName())) {
            try {
                UIUtils.init(this);
                Xui.init(this);
                IndivHelper.initMain(this);
                HttpsUtils.init(this, true);
                registerModule();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            LogUtils.i(TAG, "onCreate time = " + (System.currentTimeMillis() - time));
        }
    }

    private void parseXmlConfig() {
        if (!XMLDataStorage.instance().parseXML(this)) {
            LogUtils.e(TAG, "XML config parsing was failed");
        }
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        if (getPackageName().endsWith(ProcessUtils.getCurProcessName())) {
            HttpsUtils.destroy();
            CarHelper.deinit();
        }
    }

    private void registerModule() {
        removeEventBusLog();
        Module.register(NetworkChannelsEntry.class, new NetworkChannelsEntry());
        Module.register(DataLogModuleEntry.class, new DataLogModuleEntry(this));
        IHttp http = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        http.config().applicationContext(this).addInterceptor(new TrafficeStaFlagInterceptor()).enableLogging().apply();
    }

    private void initTestResultFile() {
        if (!FileUtil.isExistFilePath(TestResultUtil.PATH_TEST_RESULT)) {
            LogUtils.e(TAG, "test result file not exist /mnt/vendor/private/factory/test_result");
            TestResultUtil.createTestResultFile(TestResultItem.RESULT_NOTEST);
        }
        if (!FileUtil.isExistFilePath(TestResultUtil.PATH_AGING_TEST_RECORD)) {
            LogUtils.e(TAG, "aging test record file not exist /mnt/vendor/private/factory/aging_test_record");
            TestResultUtil.createAgingTestResultFile();
        }
    }

    private void removeEventBusLog() {
        try {
            EventBus.builder().sendNoSubscriberEvent(false).logNoSubscriberMessages(false).logSubscriberExceptions(false).installDefaultEventBus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
