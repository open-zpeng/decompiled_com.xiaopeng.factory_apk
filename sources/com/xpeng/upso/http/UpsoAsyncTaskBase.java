package com.xpeng.upso.http;

import android.os.AsyncTask;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.http.UpsoRequestParam;
import com.xpeng.upso.http.UpsoResponse;
import com.xpeng.upso.utils.LogUtils;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public abstract class UpsoAsyncTaskBase<T1 extends UpsoRequestParam, T2 extends UpsoResponse> extends AsyncTask<UpsoRequestParam, Integer, String> {
    protected T1 a;
    protected String b;
    private String d;
    private IPresetCallback<T2> e;
    protected JSONObject c = new JSONObject();
    private MediaType f = MediaType.parse(UpsoConstants.REQUEST_MEDIA_TYPE);

    /* loaded from: classes2.dex */
    public interface IPresetCallback<T2> {
        void onPresetResponseCallback(T2 t2);
    }

    public UpsoAsyncTaskBase(T1 t1) {
        this.a = t1;
        try {
            this.c.put(UpsoConstants.REQUSET_KEY_PROJECT_ID, t1.chipModel.toString());
            this.c.put(UpsoConstants.REQUEST_KEY_VEHICLE_MODEL, t1.carModel.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private RequestBody b() {
        c();
        return RequestBody.create(this.f, this.c.toString());
    }

    protected abstract T2 a();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: a */
    public String doInBackground(UpsoRequestParam... upsoRequestParamArr) {
        Response execute;
        LogUtils.d("PresetAsyncTask", "doInBackground");
        d();
        Call newCall = new OkHttpClient.Builder().readTimeout(10L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url(this.d).post(b()).build());
        this.b = null;
        try {
            execute = newCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (execute.isSuccessful()) {
            LogUtils.d("PresetAsyncTask", "request successfully");
            this.b = execute.body().string();
            LogUtils.d("PresetAsyncTask", "testNetwork response = " + UpsoConfig.logLimited(this.b));
            return this.b;
        }
        this.b = null;
        return null;
    }

    protected abstract void c();

    protected abstract void d();

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        LogUtils.d("PresetAsyncTask", "onPreExecute");
    }

    public void setOnPresetCallback(IPresetCallback<T2> iPresetCallback) {
        this.e = iPresetCallback;
    }

    public void setUrl(String str) {
        this.d = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* renamed from: a */
    public void onPostExecute(String str) {
        super.onPostExecute(str);
        if (this.e != null) {
            this.e.onPresetResponseCallback(this.b != null ? a() : null);
        }
    }
}
