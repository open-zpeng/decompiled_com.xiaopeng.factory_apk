package com.xiaopeng.xui.utils;

import android.view.View;
import androidx.annotation.NonNull;
/* loaded from: classes2.dex */
public class XInputUtils {
    public static void ignoreHiddenInput(@NonNull View view) {
        view.setTag(268435456, 1001);
    }
}
