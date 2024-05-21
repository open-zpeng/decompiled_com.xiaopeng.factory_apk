package com.xiaopeng.libconfig.ipc.bean;

import cn.hutool.core.text.CharPool;
/* loaded from: classes2.dex */
public class OnlineAccountBean {
    private String accessToken;
    private int grade;
    private String name;
    private boolean onLine;
    private String phone;
    private String pic;
    private String refreshToken;
    private long uid;

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isOnLine() {
        return this.onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String toString() {
        return "OnlineAccountBean{uid='" + this.uid + CharPool.SINGLE_QUOTE + ", name='" + this.name + CharPool.SINGLE_QUOTE + ", phone='" + this.phone + CharPool.SINGLE_QUOTE + ", onLine=" + this.onLine + ", accessToken='" + this.accessToken + CharPool.SINGLE_QUOTE + ", refreshToken='" + this.refreshToken + CharPool.SINGLE_QUOTE + ", grade=" + this.grade + ", pic='" + this.pic + CharPool.SINGLE_QUOTE + '}';
    }
}
