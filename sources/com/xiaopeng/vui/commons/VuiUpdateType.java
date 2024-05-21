package com.xiaopeng.vui.commons;
/* loaded from: classes2.dex */
public enum VuiUpdateType {
    UPDATE_VIEW("view"),
    UPDATE_VIEW_ATTRIBUTE("attribute");
    
    private String type;

    VuiUpdateType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
