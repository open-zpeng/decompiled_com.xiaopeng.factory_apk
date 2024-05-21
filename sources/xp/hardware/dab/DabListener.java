package xp.hardware.dab;

import java.util.ArrayList;
/* loaded from: classes2.dex */
public interface DabListener {
    void onAnnouncementEnterCompleted(int i);

    void onAnnouncementEnterRequest(int i);

    void onAnnouncementExit(int i);

    void onDabChipReady();

    void onDabInfoChanged(DabInfo dabInfo);

    void onProgramListChanged(int i, ArrayList<DabInfo> arrayList);

    void onSlideShowChanged(int i, String str);

    void onSnrChanged(int i, int i2);

    void onStationListChanged(int i, ArrayList<DabInfo> arrayList);
}
