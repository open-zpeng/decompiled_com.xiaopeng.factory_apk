package com.xiaopeng.factory.system.service;

import android.app.Service;
import android.car.hardware.CarPropertyValue;
import android.content.Intent;
import android.os.IBinder;
import com.xiaopeng.commonfunc.model.car.CarEventChangedListener;
import com.xiaopeng.commonfunc.model.car.XpDiagnosticModel;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.dmcommand.DmParser;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.FactoryTestPresenter;
import com.xiaopeng.factory.system.socket.FactoryClientConnector;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.ArrayList;
import java.util.Collection;
/* loaded from: classes2.dex */
public class DmCommandService extends Service {
    private static final String TAG = "DmCommandService";
    private DmParser mDmParser;
    private DmResponseWriter mDmResponseWriter;
    private FactoryTestPresenter mFactoryTestPresenter;
    private XpDiagnosticModel mXpDiagnosticModel;
    private boolean mIsReadyMsgSent = false;
    private final CarEventChangedListener mEventChange = new CarEventChangedListener() { // from class: com.xiaopeng.factory.system.service.-$$Lambda$DmCommandService$J0PcqU4pv-P_Gf_4fhCHeKYm0aE
        @Override // com.xiaopeng.commonfunc.model.car.CarEventChangedListener
        public final void onChangeEvent(CarPropertyValue carPropertyValue) {
            DmCommandService.this.lambda$new$0$DmCommandService(carPropertyValue);
        }
    };

    public /* synthetic */ void lambda$new$0$DmCommandService(CarPropertyValue carPropertyValue) {
        DmResponseWriter dmResponseWriter;
        int id = carPropertyValue.getPropertyId();
        Object propertyValue = carPropertyValue.getValue();
        if (id == 1) {
            LogUtils.i(TAG, "CAR_MANAGER_INIT");
            if (!this.mIsReadyMsgSent && (dmResponseWriter = this.mDmResponseWriter) != null) {
                this.mIsReadyMsgSent = dmResponseWriter.write(DmUtil.responseIMReady());
                return;
            }
            return;
        }
        switch (id) {
            case 560993284:
                if (propertyValue instanceof byte[]) {
                    byte[] value = (byte[]) propertyValue;
                    LogUtils.i(TAG, "ID_MCU_DIAG_REQUEST, value: " + DataHelp.byteArrayToHexStr(value));
                    if (DataHelp.checkBytes(value, DmUtil.ENTER_EOL_CMD)) {
                        initDmParserForAutoTest();
                        return;
                    } else if (DataHelp.checkBytes(value, DmUtil.QUIT_EOL_CMD)) {
                        deinitDmParser();
                        return;
                    } else {
                        DmParser dmParser = this.mDmParser;
                        if (dmParser != null) {
                            dmParser.process(value, this.mDmResponseWriter);
                            return;
                        } else {
                            LogUtils.i(TAG, "please enter EOL session");
                            return;
                        }
                    }
                }
                return;
            case 560993285:
                LogUtils.i(TAG, "ID_MCU_DIAG_REQUEST");
                return;
            default:
                return;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        DmResponseWriter dmResponseWriter;
        super.onCreate();
        this.mXpDiagnosticModel = new XpDiagnosticModel(TAG);
        Collection<Integer> ids = new ArrayList<>();
        ids.add(560993284);
        ids.add(560993285);
        this.mXpDiagnosticModel.registerPropCallback(ids, this.mEventChange);
        if (this.mDmResponseWriter == null) {
            this.mDmResponseWriter = new DmResponseWriter(this.mXpDiagnosticModel);
        }
        if (!this.mIsReadyMsgSent && (dmResponseWriter = this.mDmResponseWriter) != null) {
            this.mIsReadyMsgSent = dmResponseWriter.write(DmUtil.responseIMReady());
        }
        this.mFactoryTestPresenter = new FactoryTestPresenter();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void initDmParserForAutoTest() {
        LogUtils.i(TAG, "initDmParserForAutoTest");
        this.mFactoryTestPresenter.enterAudioTestMode();
        FactoryClientConnector.getInstance().creatConnection();
        if (this.mDmParser == null) {
            this.mDmParser = new DmParser();
            this.mDmParser.registerDmComandHandler(MyApplication.getContext(), this.mDmResponseWriter);
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(DmUtil.responseEnterEOLSuccess());
        }
    }

    private void deinitDmParser() {
        LogUtils.i(TAG, "deinitDmParser");
        this.mFactoryTestPresenter.exitAudioTestMode();
        FactoryClientConnector.getInstance().disconnect();
        DmParser dmParser = this.mDmParser;
        if (dmParser != null) {
            dmParser.unregisterDmComandHandler();
            this.mDmParser = null;
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(DmUtil.responseQuitEOLSuccess());
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        LogUtils.i(TAG, "onDestroy");
        super.onDestroy();
        this.mDmResponseWriter = null;
        deinitDmParser();
        this.mXpDiagnosticModel.onDestroy();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }
}
