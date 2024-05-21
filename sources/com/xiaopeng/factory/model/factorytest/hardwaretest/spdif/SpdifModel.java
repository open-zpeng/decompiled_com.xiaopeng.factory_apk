package com.xiaopeng.factory.model.factorytest.hardwaretest.spdif;

import android.media.AudioManager;
import com.xiaopeng.factory.MyApplication;
/* loaded from: classes.dex */
public class SpdifModel {
    private static final String PARAMETER_PLAYBACK = "ftmtest=playback";
    private static final String PARAMETER_PLAYBACK_ANALOG_DEST = "dest=1";
    private static final String PARAMETER_PLAYBACK_DIGITAL_DEST = "dest=2";
    private static final String PARAMETER_PLAYBACK_START = "action=start";
    private static final String PARAMETER_PLAYBACK_STOP = "action=stop";
    private final AudioManager mAudioManager = (AudioManager) MyApplication.getContext().getSystemService("audio");

    public void startAnalogPlayback() {
        this.mAudioManager.setParameters("ftmtest=playback;action=start;dest=1");
    }

    public void startDigitalPlayback() {
        this.mAudioManager.setParameters("ftmtest=playback;action=start;dest=2");
    }

    public void stopPlayback() {
        this.mAudioManager.setParameters("ftmtest=playback;action=stop");
    }
}
