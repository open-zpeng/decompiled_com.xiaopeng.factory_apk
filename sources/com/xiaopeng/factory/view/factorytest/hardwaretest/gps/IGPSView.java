package com.xiaopeng.factory.view.factorytest.hardwaretest.gps;

import android.location.GpsSatellite;
import java.util.List;
/* loaded from: classes2.dex */
public interface IGPSView {
    void onSensorChanged(int i, float[] fArr, long j, float f);

    void updateGPSStatus(int i, List<GpsSatellite> list);

    void updateLocation(String str);

    void updateOnOffInfo();
}
