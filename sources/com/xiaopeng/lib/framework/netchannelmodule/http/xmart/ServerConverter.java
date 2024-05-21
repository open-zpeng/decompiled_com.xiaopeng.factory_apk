package com.xiaopeng.lib.framework.netchannelmodule.http.xmart;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.text.CharSequenceUtil;
import com.lzy.okgo.convert.Converter;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class ServerConverter implements Converter<ServerBean> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.lzy.okgo.convert.Converter
    public ServerBean convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            throw new IllegalStateException(CharSequenceUtil.NULL);
        }
        ServerBean bean = new ServerBean();
        JSONObject jsonObject = new JSONObject(body.string());
        bean.code(jsonObject.getInt("code"));
        try {
            bean.data(jsonObject.getString("data"));
        } catch (Throwable th) {
        }
        try {
            bean.message(jsonObject.getString(NotificationCompat.CATEGORY_MESSAGE));
        } catch (Throwable th2) {
        }
        return bean;
    }
}
