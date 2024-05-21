package com.xiaopeng.libbluetooth;

import android.util.SparseArray;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class AbsA2DPControlCallback {
    /* JADX INFO: Access modifiers changed from: protected */
    public void onA2DPFeatureSupport(boolean isSupportMetaData, boolean isSupportPlayStatus) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onA2DPBrowsingCallback(int cur, int total, int errorCode, int complete, String title, String artist, String itemType) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onA2DPMetaData(int dataType, int attributeId, String metaData) {
    }

    protected void onA2DPMetaData(SparseArray<String> songMessages) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onA2DPPlayBackPos(int position) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onA2DPPlayStatus(int playStatus) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onA2DPStreamStatus(int streamStatus) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onAVRCPFeatureSupport(boolean... supportFeatures) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onAVRCPBrowsingEvent(int eventId) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onAVRCPChangedEvent(int attributeId, int attributeValue) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onAVRCPSupportedEvent(int attributeId, List<Integer> allowAttributes) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBindSuccess() {
    }
}
