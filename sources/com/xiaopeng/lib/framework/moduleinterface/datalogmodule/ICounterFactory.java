package com.xiaopeng.lib.framework.moduleinterface.datalogmodule;

import android.content.Context;
/* loaded from: classes2.dex */
public interface ICounterFactory {
    ICounter createDailyCounter(Context context, String name);

    ICounter createHourlyCounter(Context context, String name);
}