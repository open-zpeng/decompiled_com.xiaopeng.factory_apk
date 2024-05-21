package com.xpeng.dolbypresetpso;

import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public enum DolbyErrorCode {
    ERR_OK(0, "ERR_OK"),
    ERR_NO_NETWORK_RESPONSE(1, "ERR_NO_NETWORK_RESPONSE"),
    ERR_HTTP_RESPONSE_CODE(2, "ERR_HTTP_RESPONSE_CODE"),
    ERR_NO_DATA(3, "ERR_NO_DATA"),
    ERR_KEY_DECODE(4, "ERR_KEY_DECODE"),
    ERR_PRESET_FAILED(5, "ERR_PRESET_FAILED");
    
    private String desc;
    private int index;

    DolbyErrorCode(int i, String str) {
        this.index = i;
        this.desc = str;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
