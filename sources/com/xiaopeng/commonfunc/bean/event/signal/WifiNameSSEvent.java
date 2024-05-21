package com.xiaopeng.commonfunc.bean.event.signal;

import cn.hutool.core.text.CharPool;
import com.xiaopeng.commonfunc.Constant;
/* loaded from: classes.dex */
public class WifiNameSSEvent {
    private String ip;
    private String name;
    private String speed;
    private String strength;

    public WifiNameSSEvent() {
        this.name = Constant.UNKNOWN_STRING;
        this.strength = Constant.UNKNOWN_STRING;
        this.speed = Constant.UNKNOWN_STRING;
        this.ip = Constant.UNKNOWN_STRING;
    }

    public WifiNameSSEvent(String name, String strength, String speed, String ip) {
        this.name = name;
        this.strength = strength;
        this.speed = speed;
        this.ip = ip;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrength() {
        return this.strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getSpeed() {
        return this.speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String toString() {
        return "WifiNameSSEvent{name='" + this.name + CharPool.SINGLE_QUOTE + ", strength='" + this.strength + CharPool.SINGLE_QUOTE + ", speed='" + this.speed + CharPool.SINGLE_QUOTE + ", ip='" + this.ip + CharPool.SINGLE_QUOTE + '}';
    }
}
