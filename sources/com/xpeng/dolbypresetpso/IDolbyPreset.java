package com.xpeng.dolbypresetpso;

import android.pso.XpPsoException;
import androidx.annotation.Keep;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
@Keep
/* loaded from: classes2.dex */
public interface IDolbyPreset {
    public static final String MAIN_TAG = "Upso-";

    void enableSensitiveLog(boolean z);

    String genAlias(XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel);

    boolean presetAesKeyByData(byte[] bArr) throws XpPsoException;

    boolean presetAesKeyByFile(String str) throws XpPsoException;

    void presetAesKeyFromNetwork(IDolbyPresetResultCallback iDolbyPresetResultCallback);

    void setParameters(DolbyPresetParam dolbyPresetParam);

    void setParameters(XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel);
}
