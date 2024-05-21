package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class DmCommandHandler {
    protected String CLASS_NAME = "DmCommandHandler";
    protected byte[] CMD_NAME = {0, 0};
    protected Context context;

    public DmCommandHandler(Context context) {
        this.context = context;
    }

    public synchronized byte[] handleCommand(byte[] argu) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] responseWithValue(byte[] arg, byte[] value) {
        return DmUtil.responseWithValue(this.CMD_NAME, arg, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] responseNG(byte[] arg) {
        return DmUtil.responseNG(this.CMD_NAME, arg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] responseOK(byte[] arg) {
        return DmUtil.responseOK(this.CMD_NAME, arg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] responseNA(byte[] arg) {
        return DmUtil.responseNA(this.CMD_NAME, arg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean checkArgu(byte[] argu, byte[] argv) {
        for (int i = 0; i < argv.length; i++) {
            if (argu[i] != argv[i]) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<String> parseArgu(byte[] argu) {
        List<String> res = new ArrayList<>();
        int length = argu[2] & 255;
        int index = 3;
        while (true) {
            int paramCount = length - 1;
            if (length <= 0 || index >= argu.length) {
                break;
            }
            int length2 = argu[index] & 255;
            res.add(DataHelp.byteArrayToStr(DataHelp.byteSub(argu, index + 1, length2)));
            index += length2 + 1;
            length = paramCount;
        }
        LogUtils.i(this.CLASS_NAME, "parseArgu res:" + DataHelp.listToString(res, ","));
        return res;
    }

    public void destroy() {
        LogUtils.i(this.CLASS_NAME, "destroy");
    }
}
