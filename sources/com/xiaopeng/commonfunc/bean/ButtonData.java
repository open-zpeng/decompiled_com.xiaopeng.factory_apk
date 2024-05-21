package com.xiaopeng.commonfunc.bean;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class ButtonData {
    private String mTextString;
    private Class mTriggerClass;

    public ButtonData(String mTextString, Class mTriggerClass) {
        this.mTextString = mTextString;
        this.mTriggerClass = mTriggerClass;
    }

    public String getTextString() {
        return this.mTextString;
    }

    public void setTextString(String mTextString) {
        this.mTextString = mTextString;
    }

    public Class getTriggerClass() {
        return this.mTriggerClass;
    }

    public void setTriggerClass(Class mTriggerClass) {
        this.mTriggerClass = mTriggerClass;
    }

    public String toString() {
        return "ButtonData{mTextString='" + this.mTextString + CharPool.SINGLE_QUOTE + ", mTriggerClass=" + this.mTriggerClass + '}';
    }
}
