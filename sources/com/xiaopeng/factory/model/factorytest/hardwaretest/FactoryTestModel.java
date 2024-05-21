package com.xiaopeng.factory.model.factorytest.hardwaretest;

import android.text.TextUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.bean.car.AndroidDugReq;
import com.xiaopeng.commonfunc.model.ModeModel;
import com.xiaopeng.commonfunc.model.car.McuModel;
import com.xiaopeng.commonfunc.model.car.TboxModel;
import com.xiaopeng.commonfunc.utils.AtUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.atcommand.ResponseWriter;
import com.xiaopeng.lib.http.HttpsUtils;
import com.xiaopeng.lib.http.server.ServerBean;
import com.xiaopeng.lib.http.server.ServerCallback;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.lib.utils.config.CommonConfig;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class FactoryTestModel {
    public static final int SREEN_SELTEST = 21;
    private static final String TAG = "FactoryTestModel";
    public static final int TEST_BATTERY = 17;
    public static final int TEST_BLUETOOTH = 1;
    public static final int TEST_BRIGHTNESS = 5;
    public static final int TEST_CAMERA = 3;
    public static final int TEST_CAN = 15;
    public static final int TEST_DAB = 26;
    public static final int TEST_EEPROM = 14;
    public static final int TEST_FAN = 19;
    public static final int TEST_FEEDBACK = 24;
    public static final int TEST_GPS = 13;
    public static final int TEST_KEY = 25;
    public static final int TEST_LTE = 4;
    public static final int TEST_PHY = 22;
    public static final int TEST_PSU = 18;
    public static final int TEST_QUIET = 7;
    public static final int TEST_RADIO = 6;
    public static final int TEST_RECORD = 10;
    public static final int TEST_RGB = 9;
    public static final int TEST_SD = 12;
    public static final int TEST_SENSOR = 16;
    public static final int TEST_SOUND = 11;
    public static final int TEST_SPDIF = 23;
    public static final int TEST_TEMPERATURE = 20;
    public static final int TEST_TOUCH = 8;
    public static final int TEST_USB = 0;
    public static final int TEST_USB_BLUETOOTH = 27;
    public static final int TEST_WLAN = 2;
    private static final String URL_MCU_CHECK = CommonConfig.HTTP_HOST + "/biz/v3/vehicle/factoryMcuCheck";
    private ResponseWriter mResponseWriter;
    private final McuModel mMcuModel = new McuModel(TAG);
    private final TboxModel mTboxModel = new TboxModel(TAG);
    private ModeModel mModeModel = new ModeModel(MyApplication.getContext());

    public FactoryTestModel() {
    }

    public FactoryTestModel(ResponseWriter responseWriter) {
        this.mResponseWriter = responseWriter;
    }

    public void requestMcuCheck() {
        Map<String, String> param = new HashMap<>();
        param.put("vid", String.valueOf(SystemPropertyUtil.getVehicleId()));
        LogUtils.i(TAG, "requestMcuCheck" + param.toString());
        OkGo.getInstance().cancelTag(URL_MCU_CHECK);
        ((PostRequest) ((PostRequest) OkGo.post(URL_MCU_CHECK).headers("Client", CommonConfig.CAR_CLIENT_DEFAULT)).upJson(HttpsUtils.buildBody(param)).tag(URL_MCU_CHECK)).execute(new ServerCallback() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.FactoryTestModel.1
            @Override // com.lzy.okgo.callback.Callback
            public void onSuccess(Response<ServerBean> response) {
                if (response.body().getCode() != 200) {
                    FactoryTestModel.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.McuTest.CMD_NAME, "0"));
                    return;
                }
                String content = HttpsUtils.decodeBody(response.body());
                LogUtils.i(FactoryTestModel.TAG, content);
                if (TextUtils.isEmpty(content)) {
                    FactoryTestModel.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.McuTest.CMD_NAME, "0"));
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String result = jsonObject.getString(Constant.MCUTEST.MCU_SERVER_CONNECTION_RESULT_TAG);
                    if ("true".equalsIgnoreCase(result)) {
                        FactoryTestModel.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.McuTest.CMD_NAME, "0"));
                    } else {
                        FactoryTestModel.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.McuTest.CMD_NAME, "0"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override // com.lzy.okgo.callback.AbsCallback, com.lzy.okgo.callback.Callback
            public void onError(Response<ServerBean> response) {
                FactoryTestModel.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.McuTest.CMD_NAME, "0"));
            }
        });
    }

    public void enterAudioTestMode() {
        LogUtils.i(TAG, "enterAudioTestMode");
        this.mModeModel.enterAudioTestMode();
    }

    public void exitAudioTestMode() {
        LogUtils.i(TAG, "enterAudioTestMode");
        this.mModeModel.exitAudioTestMode();
    }

    public void enterTboxTestMode() {
        this.mModeModel.enterTboxTestMode();
    }

    public void exitTboxTestMode() {
        this.mModeModel.exitTboxTestMode();
    }

    public void enterMcuTestMode() {
        this.mModeModel.enterMcuTestMode();
    }

    public void exitMcuTestMode() {
        this.mModeModel.exitMcuTestMode();
    }

    public String getDtsVer() {
        return this.mModeModel.getDtsVer();
    }

    public boolean enterPowerTest() {
        AndroidDugReq androidDugReq = new AndroidDugReq();
        androidDugReq.dugSt = 2;
        LogUtils.i("enterMcuTestMode", androidDugReq.toString());
        this.mMcuModel.sendFactoryDugReqMsgToMcu(androidDugReq.packToIntArray());
        return true;
    }

    public boolean exitPowerTest() {
        AndroidDugReq androidDugReq = new AndroidDugReq();
        androidDugReq.dugSt = 1;
        LogUtils.i("enterMcuTestMode", androidDugReq.toString());
        this.mMcuModel.sendFactoryDugReqMsgToMcu(androidDugReq.packToIntArray());
        return true;
    }
}
