package com.xpeng.dolbyverifypso;

import android.pso.XpPsoException;
import androidx.annotation.Keep;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
@Keep
/* loaded from: classes2.dex */
public interface IDolbyVerify {
    public static final String MAIN_TAG = "Upso-";

    boolean checkCrypo() throws XpPsoException;

    boolean checkPreset() throws XpPsoException;

    void enableSensitiveLog(boolean z);

    void setParameters(XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel);
}
