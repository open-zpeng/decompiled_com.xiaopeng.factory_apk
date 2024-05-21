package com.xpeng.dolbypresetpso.http;

import android.os.AsyncTask;
import androidx.annotation.Keep;
import com.google.gson.Gson;
import com.xpeng.dolbypresetpso.DolbyPresetParam;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.aesserver.AesConstants;
import com.xpeng.upso.http.UpsoConstants;
import com.xpeng.upso.utils.LogUtils;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
@Keep
/* loaded from: classes2.dex */
public class PresetAsyncTask extends AsyncTask<DolbyPresetParam, Integer, PresetResponse> {
    private static final String TAG = "Upso-PresetAsyncTask";
    private DolbyPresetParam param;
    private a presetCallback;
    private String url = "http://pso-int.xiaopeng.com/api/dubi/aes/preset";

    /* loaded from: classes2.dex */
    public interface a {
        void a(PresetResponse presetResponse);
    }

    public PresetAsyncTask(DolbyPresetParam dolbyPresetParam) {
        this.param = dolbyPresetParam;
    }

    private RequestBody createRequestBody() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(UpsoConstants.REQUSET_KEY_PROJECT_ID, "CDU_DUBI");
            jSONObject.put(UpsoConstants.REQUEST_KEY_VEHICLE_MODEL, this.param.carModel.toString());
            jSONObject.put(AesConstants.REQUEST_PARAM_VERSION, "1.0.0");
            jSONObject.put("sn", "");
            jSONObject.put("command", "");
            LogUtils.i(TAG, "doInBackground,requestBody=" + jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
        }
        return RequestBody.create(MediaType.parse(UpsoConstants.REQUEST_MEDIA_TYPE), jSONObject.toString());
    }

    @Override // android.os.AsyncTask
    public void onPreExecute() {
        super.onPreExecute();
        LogUtils.d(TAG, "onPreExecute");
    }

    public void setOnPresetCallback(a aVar) {
        this.presetCallback = aVar;
    }

    @Override // android.os.AsyncTask
    public PresetResponse doInBackground(DolbyPresetParam... dolbyPresetParamArr) {
        LogUtils.i(TAG, "doInBackground");
        LogUtils.i(TAG, "doInBackground,url=" + this.url);
        try {
            String string = new OkHttpClient.Builder().readTimeout(10L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url(this.url).post(createRequestBody()).build()).execute().body().string();
            LogUtils.d(TAG, "testNetwork response = " + UpsoConfig.logLimited(string));
            PresetResponse presetResponse = (PresetResponse) new Gson().fromJson(string, (Class<Object>) PresetResponse.class);
            if (UpsoConfig.isLogEnabled()) {
                LogUtils.d(TAG, "testNetwork msg = " + presetResponse.projectId);
            }
            return presetResponse;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, e.toString());
            return null;
        }
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(PresetResponse presetResponse) {
        super.onPostExecute((PresetAsyncTask) presetResponse);
        a aVar = this.presetCallback;
        if (aVar != null) {
            aVar.a(presetResponse);
        }
    }
}
