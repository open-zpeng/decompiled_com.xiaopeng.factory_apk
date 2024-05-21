package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.SecureBootUtil;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.presenter.indiv.IndivPresenter;
/* loaded from: classes.dex */
public class DmIndivTest extends DmCommandHandler {
    private final IndivPresenter mIndivPresenter;

    public DmIndivTest(Context context, DmResponseWriter dmResponseWriter) {
        super(context);
        this.CMD_NAME = DmUtil.IndivTest.CMD_NAME;
        this.CLASS_NAME = "DmIndivTest";
        this.mIndivPresenter = new IndivPresenter(dmResponseWriter);
        this.mIndivPresenter.init();
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        resData = null;
        if (checkArgu(argu, new byte[]{0, 1})) {
            if (SecureBootUtil.SUPPORT_SECURE_BOOT && !this.mIndivPresenter.getEfuseStatus()) {
                resData = responseNG(argu);
            } else {
                this.mIndivPresenter.checkIndiv();
            }
        } else {
            resData = responseNA(argu);
        }
        return resData;
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public void destroy() {
        super.destroy();
        this.mIndivPresenter.deinit();
    }
}
