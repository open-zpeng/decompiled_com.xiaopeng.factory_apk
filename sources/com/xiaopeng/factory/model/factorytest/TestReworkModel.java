package com.xiaopeng.factory.model.factorytest;

import com.google.gson.Gson;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.http.ICallback;
import com.xiaopeng.lib.utils.SystemPropertyUtil;
import com.xiaopeng.lib.utils.config.CommonConfig;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class TestReworkModel {
    private final String URL_REWORK = CommonConfig.HTTP_HOST + "/biz/v5/cdu/rework";

    public void requestRework(final ICallback callback) {
        Map<String, String> param = new HashMap<>();
        param.put("cduId", SystemPropertyUtil.getHardwareId());
        IHttp http = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        http.cancelTag(this.URL_REWORK);
        http.bizHelper().post(this.URL_REWORK, new Gson().toJson(param)).build().tag(this.URL_REWORK).execute(new Callback() { // from class: com.xiaopeng.factory.model.factorytest.TestReworkModel.1
            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onSuccess(IResponse iResponse) {
                if (iResponse.code() == 200) {
                    ICallback iCallback = callback;
                    if (iCallback != null) {
                        iCallback.onSuccess(null);
                        return;
                    }
                    return;
                }
                ICallback iCallback2 = callback;
                if (iCallback2 != null) {
                    iCallback2.onError(null);
                }
            }

            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onFailure(IResponse iResponse) {
                ICallback iCallback = callback;
                if (iCallback != null) {
                    iCallback.onError(null);
                }
            }
        });
    }
}
