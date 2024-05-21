package com.xpeng.upso;

import androidx.annotation.Keep;
import com.xiaopeng.xmlconfig.Support;
import java.util.HashMap;
import java.util.Map;
@Keep
/* loaded from: classes2.dex */
public enum XpengPsoClientType {
    CDU(0, "CDU"),
    TBOX(1, "TBOX"),
    XPU(2, Support.Properties.XPU);
    
    private static final Map<String, XpengPsoClientType> MAP = new HashMap();
    private String desc;
    private int index;

    static {
        XpengPsoClientType[] values;
        for (XpengPsoClientType xpengPsoClientType : values()) {
            MAP.put(xpengPsoClientType.toString(), xpengPsoClientType);
        }
    }

    XpengPsoClientType(int i, String str) {
        this.index = i;
        this.desc = str;
    }

    public static XpengPsoClientType valueOfName(String str) {
        return MAP.get(str);
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
