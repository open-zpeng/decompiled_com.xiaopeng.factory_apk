package com.xiaopeng.factory.system.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import com.xiaopeng.factory.system.service.DmCommandService;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes2.dex */
public class AppBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AppBroadcastReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.i(TAG, "Action = " + action);
        if (action == null) {
            return;
        }
        char c = 65535;
        int hashCode = action.hashCode();
        if (hashCode != -1636124229) {
            if (hashCode == 798292259 && action.equals("android.intent.action.BOOT_COMPLETED")) {
                c = 1;
            }
        } else if (action.equals("com.xiaopeng.action.SECURE_STORE_RELOAD")) {
            c = 0;
        }
        if (c != 0 && c == 1) {
            context.startServiceAsUser(new Intent(context, DmCommandService.class), UserHandle.CURRENT);
        }
    }
}
