package com.xiaopeng.commonfunc.bean.factorytest;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class FacRadioBean {
    private int bassGain;
    private int freqPoint;
    private int gBanlanceLevel;
    private int gFaderLevel;
    private int heroLoudnessOn;
    private int mMuteState;
    private int mTunerLevel;
    private int midGain;
    private int pInputSource;
    private int primaryVolume;
    private int radioCurrentBand;
    private int radioCurrentFreq;
    private int state;
    private int trebleGain;
    private int tunerpower;

    public int getPrimaryVolume() {
        return this.primaryVolume;
    }

    public int getRadioCurrentBand() {
        return this.radioCurrentBand;
    }

    public int getRadioCurrentFreq() {
        return this.radioCurrentFreq;
    }

    public int getFreqPoint() {
        return this.freqPoint;
    }

    public int getpInputSource() {
        return this.pInputSource;
    }

    public int getTunerpower() {
        return this.tunerpower;
    }

    public int getBassGain() {
        return this.bassGain;
    }

    public int getMidGain() {
        return this.midGain;
    }

    public int getTrebleGain() {
        return this.trebleGain;
    }

    public int getgBanlanceLevel() {
        return this.gBanlanceLevel;
    }

    public int getgFaderLevel() {
        return this.gFaderLevel;
    }

    public int getHeroLoudnessOn() {
        return this.heroLoudnessOn;
    }

    public int getMuteState() {
        return this.mMuteState;
    }

    public int getmTunerLevel() {
        return this.mTunerLevel;
    }

    public int getState() {
        return this.state;
    }

    public String toString() {
        return "当前收音机band='" + this.radioCurrentBand + CharPool.SINGLE_QUOTE + "\n当前收音机频点×100='" + this.radioCurrentFreq + CharPool.SINGLE_QUOTE + "\nfreqPoint='" + this.freqPoint + CharPool.SINGLE_QUOTE + "\npInputSource='" + this.pInputSource + CharPool.SINGLE_QUOTE + "\n收音机打开状态='" + this.tunerpower + CharPool.SINGLE_QUOTE + "\nbassGain='" + this.bassGain + CharPool.SINGLE_QUOTE + "\nmidGain='" + this.midGain + CharPool.SINGLE_QUOTE + "\ntrebleGain='" + this.trebleGain + CharPool.SINGLE_QUOTE + "\ngBanlanceLevel='" + this.gBanlanceLevel + CharPool.SINGLE_QUOTE + "\ngFaderLevel='" + this.gFaderLevel + CharPool.SINGLE_QUOTE + "\nheroLoudnessOn='" + this.heroLoudnessOn + CharPool.SINGLE_QUOTE + "\nmMuteState='" + this.mMuteState + CharPool.SINGLE_QUOTE + "\nmTunerLevel='" + this.mTunerLevel + CharPool.SINGLE_QUOTE + "\nstate='" + this.state + CharPool.SINGLE_QUOTE + "\n";
    }
}
