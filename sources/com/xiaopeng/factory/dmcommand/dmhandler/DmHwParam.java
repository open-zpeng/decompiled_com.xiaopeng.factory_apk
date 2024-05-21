package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.commonfunc.utils.SystemPropertyUtil;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.HWAndSWInfoPresenter;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes.dex */
public class DmHwParam extends DmCommandHandler {
    private final HWAndSWInfoPresenter mHWAndSWInfoPresenter;

    public DmHwParam(Context context, DmResponseWriter responseWriter) {
        super(context);
        this.CMD_NAME = DmUtil.HwParam.CMD_NAME;
        this.CLASS_NAME = "DmHwParam";
        this.mHWAndSWInfoPresenter = new HWAndSWInfoPresenter(responseWriter);
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        resData = null;
        if (checkArgu(argu, new byte[]{1, 0})) {
            long size = (512 * FileUtil.readLongVal(Support.Path.getFilePath(Support.Path.ROM_DISK_SIZE))) / 1000000000;
            if (size > 240) {
                size = 256;
            } else if (size > 120) {
                size = 128;
            }
            resData = responseWithValue(argu, DataHelp.strToByteArray(String.valueOf(size)));
        } else if (checkArgu(argu, new byte[]{1, 1})) {
            String model = FileUtil.read(Support.Path.getFilePath(Support.Path.ROM_DISK_MODEL));
            resData = responseWithValue(argu, DataHelp.strToByteArray(model));
        } else if (checkArgu(argu, new byte[]{2, 0})) {
            this.mHWAndSWInfoPresenter.requestCarCode();
        } else if (checkArgu(argu, new byte[]{2, 1})) {
            String pn = SystemPropertyUtil.getPartNumber();
            resData = responseWithValue(argu, DataHelp.strToByteArray(pn));
        } else if (checkArgu(argu, new byte[]{2, 2})) {
            String cduid = SystemPropertyUtil.getHardwareId();
            resData = responseWithValue(argu, DataHelp.strToByteArray(cduid));
        } else if (checkArgu(argu, new byte[]{2, 3})) {
            String iccid = SystemPropertyUtil.getIccid();
            resData = responseWithValue(argu, DataHelp.strToByteArray(iccid));
        } else if (checkArgu(argu, new byte[]{2, 4})) {
            String swver = BuildInfoUtils.getSystemVersion();
            resData = responseWithValue(argu, DataHelp.strToByteArray(swver));
        } else if (checkArgu(argu, new byte[]{2, 5})) {
            String hwver = SystemPropertyUtil.getHwVersion();
            resData = responseWithValue(argu, DataHelp.strToByteArray(hwver));
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
