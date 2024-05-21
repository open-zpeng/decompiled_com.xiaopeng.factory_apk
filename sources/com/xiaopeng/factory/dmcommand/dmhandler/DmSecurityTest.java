package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.SecureBootUtil;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.presenter.security.TestSecurityPresenter;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes.dex */
public class DmSecurityTest extends DmCommandHandler {
    private final TestSecurityPresenter mTestSecurityPresenter;
    private static final boolean SUPPORT_DOLBY_SECRET_KEY = Support.Case.getEnabled(Support.Case.DOLBY_SECRET_KEY);
    private static final boolean SUPPORT_CDU_AES_KEY = Support.Case.getEnabled(Support.Case.CDU_AES_KEY);
    private static final boolean SUPPORT_TBOX_AES_KEY = Support.Case.getEnabled(Support.Case.TBOX_AES_KEY);

    public DmSecurityTest(Context context, DmResponseWriter dmResponseWriter) {
        super(context);
        this.CMD_NAME = DmUtil.SecurityTest.CMD_NAME;
        this.CLASS_NAME = "DmSecurityTest";
        this.mTestSecurityPresenter = new TestSecurityPresenter(dmResponseWriter);
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        resData = null;
        if (checkArgu(argu, new byte[]{1, 0})) {
            LogUtils.i(this.CMD_NAME, "verify CDU key");
            if (SecureBootUtil.SUPPORT_SECURE_BOOT && !this.mTestSecurityPresenter.getEfuseStatus()) {
                resData = responseNG(argu);
            } else {
                this.mTestSecurityPresenter.verifyCduKey();
            }
        } else if (checkArgu(argu, new byte[]{1, 1})) {
            LogUtils.i(this.CMD_NAME, "verify TBOX key");
            this.mTestSecurityPresenter.startTboxCertVerify();
        } else if (checkArgu(argu, new byte[]{1, 2})) {
            LogUtils.i(this.CMD_NAME, "verify WIFI key");
            this.mTestSecurityPresenter.verifyWifiKey();
        } else if (checkArgu(argu, new byte[]{1, 3})) {
            LogUtils.i(this.CMD_NAME, "verify PSU key");
            if (SecureBootUtil.SUPPORT_SECURE_BOOT && !this.mTestSecurityPresenter.getEfuseStatus()) {
                resData = responseNG(argu);
            } else {
                this.mTestSecurityPresenter.verifyPsuKey();
            }
        } else if (checkArgu(argu, new byte[]{1, 5})) {
            LogUtils.i(this.CLASS_NAME, "verify tbox aes key");
            if (!SUPPORT_TBOX_AES_KEY) {
                resData = responseNA(argu);
            } else {
                this.mTestSecurityPresenter.addAesKeyTask(5);
            }
        } else if (checkArgu(argu, new byte[]{1, 6})) {
            LogUtils.i(this.CLASS_NAME, "verify cdu aes key");
            if (!SUPPORT_CDU_AES_KEY) {
                resData = responseNA(argu);
            } else {
                this.mTestSecurityPresenter.addAesKeyTask(4);
            }
        } else if (checkArgu(argu, new byte[]{1, 7})) {
            LogUtils.i(this.CLASS_NAME, "get efuse status");
            resData = this.mTestSecurityPresenter.getEfuseStatus() ? responseOK(argu) : responseNG(argu);
        } else if (checkArgu(argu, new byte[]{1, 8})) {
            LogUtils.i(this.CLASS_NAME, "verify dolby key");
            if (!SUPPORT_DOLBY_SECRET_KEY) {
                resData = responseNA(argu);
            } else if (SecureBootUtil.SUPPORT_SECURE_BOOT && !this.mTestSecurityPresenter.getEfuseStatus()) {
                resData = responseNG(argu);
            } else {
                this.mTestSecurityPresenter.verifyDolbySecretKey();
            }
        } else {
            resData = responseNA(argu);
        }
        return resData;
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public void destroy() {
        super.destroy();
        this.mTestSecurityPresenter.disconnectCertWifi();
        this.mTestSecurityPresenter.onDestroy();
    }
}
