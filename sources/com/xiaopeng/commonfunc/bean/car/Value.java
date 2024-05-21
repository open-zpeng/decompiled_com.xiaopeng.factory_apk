package com.xiaopeng.commonfunc.bean.car;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class Value {
    private String s1;
    private String s2;

    public String getS1() {
        return this.s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return this.s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String toString() {
        return "{s1='" + this.s1 + CharPool.SINGLE_QUOTE + ", s2='" + this.s2 + CharPool.SINGLE_QUOTE + '}';
    }
}
