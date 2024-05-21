package com.xiaopeng.factory.model.factorytest.hardwaretest;

import android.media.AudioManager;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.factory.MyApplication;
import org.apache.commons.lang3.BooleanUtils;
/* loaded from: classes.dex */
public class LoopbackModel {
    private final String PARAMETER_MIC2SPK = "mic2spklb=";
    private final String PARAMETER_ON = BooleanUtils.ON;
    private final String PARAMETER_OFF = BooleanUtils.OFF;
    private final String PARAMETER_LOOPBACK = "ftmtest=loopback";
    private final String PARAMETER_LOOPBACK_START = "action=start";
    private final String PARAMETER_LOOPBACK_STOP = "action=stop";
    private final String PARAMETER_LOOPBACK_SRC = "source=";
    private final String PARAMETER_LOOPBACK_DEST = "dest=";
    private final AudioManager mAudioManager = (AudioManager) MyApplication.getContext().getSystemService("audio");

    public void startLoopAudio() {
        this.mAudioManager.setParameters("mic2spklb=on");
    }

    public void startLoopBack(int src, int dest) {
        AudioManager audioManager = this.mAudioManager;
        audioManager.setParameters("ftmtest=loopback;action=start;source=" + src + Constant.SEMICOLON_STRING + "dest=" + dest);
    }

    public void stopLoopAudio() {
        this.mAudioManager.setParameters("ftmtest=loopback;action=stop");
    }
}
