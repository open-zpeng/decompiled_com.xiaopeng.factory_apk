package com.xiaopeng.commonfunc.bean.factorytest;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class RadioBean {
    private String MuteState;
    private String bassGain;
    private String freqPoint;
    private String gBanlanceLevel;
    private String gFaderLevel;
    private String heroLoudnessOn;
    private String mTunerLevel;
    private String midGain;
    private String pInputSource;
    private String primaryVolume;
    private String radioCurrentBand;
    private String radioCurrentFreq;
    private String state;
    private String trebleGain;
    private String tunerpower;

    public String getPrimaryVolume() {
        return this.primaryVolume;
    }

    public void setPrimaryVolume(String primaryVolume) {
        this.primaryVolume = primaryVolume;
    }

    public String getRadioCurrentBand() {
        return this.radioCurrentBand;
    }

    public void setRadioCurrentBand(String radioCurrentBand) {
        this.radioCurrentBand = radioCurrentBand;
    }

    public String getRadioCurrentFreq() {
        return this.radioCurrentFreq;
    }

    public void setRadioCurrentFreq(String radioCurrentFreq) {
        this.radioCurrentFreq = radioCurrentFreq;
    }

    public String getFreqPoint() {
        return this.freqPoint;
    }

    public void setFreqPoint(String freqPoint) {
        this.freqPoint = freqPoint;
    }

    public String getPInputSource() {
        return this.pInputSource;
    }

    public void setPInputSource(String pInputSource) {
        this.pInputSource = pInputSource;
    }

    public String getTunerpower() {
        return this.tunerpower;
    }

    public void setTunerpower(String tunerpower) {
        this.tunerpower = tunerpower;
    }

    public String getBassGain() {
        return this.bassGain;
    }

    public void setBassGain(String bassGain) {
        this.bassGain = bassGain;
    }

    public String getMidGain() {
        return this.midGain;
    }

    public void setMidGain(String midGain) {
        this.midGain = midGain;
    }

    public String getTrebleGain() {
        return this.trebleGain;
    }

    public void setTrebleGain(String trebleGain) {
        this.trebleGain = trebleGain;
    }

    public String getGBanlanceLevel() {
        return this.gBanlanceLevel;
    }

    public void setGBanlanceLevel(String gBanlanceLevel) {
        this.gBanlanceLevel = gBanlanceLevel;
    }

    public String getGFaderLevel() {
        return this.gFaderLevel;
    }

    public void setGFaderLevel(String gFaderLevel) {
        this.gFaderLevel = gFaderLevel;
    }

    public String getHeroLoudnessOn() {
        return this.heroLoudnessOn;
    }

    public void setHeroLoudnessOn(String heroLoudnessOn) {
        this.heroLoudnessOn = heroLoudnessOn;
    }

    public String getMuteState() {
        return this.MuteState;
    }

    public void setMuteState(String MuteState) {
        this.MuteState = MuteState;
    }

    public String getMTunerLevel() {
        return this.mTunerLevel;
    }

    public void setMTunerLevel(String mTunerLevel) {
        this.mTunerLevel = mTunerLevel;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String toString() {
        return "current radio band='" + this.radioCurrentBand + CharPool.SINGLE_QUOTE + "\ncurrent radio freq ×100='" + this.radioCurrentFreq + CharPool.SINGLE_QUOTE + "\nfreqPoint='" + this.freqPoint + CharPool.SINGLE_QUOTE + "\npInputSource='" + this.pInputSource + CharPool.SINGLE_QUOTE + "\nradio on off status='" + this.tunerpower + CharPool.SINGLE_QUOTE + "\nbassGain='" + this.bassGain + CharPool.SINGLE_QUOTE + "\nmidGain='" + this.midGain + CharPool.SINGLE_QUOTE + "\ntrebleGain='" + this.trebleGain + CharPool.SINGLE_QUOTE + "\ngBanlanceLevel='" + this.gBanlanceLevel + CharPool.SINGLE_QUOTE + "\ngFaderLevel='" + this.gFaderLevel + CharPool.SINGLE_QUOTE + "\nheroLoudnessOn='" + this.heroLoudnessOn + CharPool.SINGLE_QUOTE + "\nMuteState='" + this.MuteState + CharPool.SINGLE_QUOTE + "\nmTunerLevel='" + this.mTunerLevel + CharPool.SINGLE_QUOTE + "\nstate='" + this.state + CharPool.SINGLE_QUOTE + "\n";
    }
}
