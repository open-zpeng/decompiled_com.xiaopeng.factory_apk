package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.presenter.navi.NaviPresenter;
import com.xiaopeng.factory.presenter.security.TestSecurityPresenter;
/* loaded from: classes.dex */
public class DmNavi extends DmCommandHandler {
    private final DmResponseWriter mDmResponseWriter;
    private final NaviPresenter mNaviPresenter;

    public DmNavi(Context context, DmResponseWriter dmResponseWriter) {
        super(context);
        this.CMD_NAME = new byte[]{-64, TestSecurityPresenter.BRUSH_EFUSE};
        this.CLASS_NAME = "DmNavi";
        this.mNaviPresenter = new NaviPresenter(dmResponseWriter);
        this.mDmResponseWriter = dmResponseWriter;
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        resData = null;
        if (checkArgu(argu, new byte[]{0, 0})) {
            this.mNaviPresenter.genUuid();
        } else if (checkArgu(argu, new byte[]{0, 1})) {
            String uuid = this.mNaviPresenter.readUuid();
            if (uuid != null) {
                resData = responseWithValue(argu, DataHelp.strToByteArray(uuid));
            } else {
                resData = responseNG(argu);
            }
        } else {
            resData = responseNA(argu);
        }
        return resData;
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public void destroy() {
        super.destroy();
    }
}
