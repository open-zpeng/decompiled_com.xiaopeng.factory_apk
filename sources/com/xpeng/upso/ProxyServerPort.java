package com.xpeng.upso;

import androidx.annotation.Keep;
import java.util.HashMap;
import java.util.Map;
@Keep
/* loaded from: classes2.dex */
public enum ProxyServerPort {
    CDU(0, "8765"),
    TBOX(1, "8765"),
    XPU(2, "8766");
    
    private static final Map<String, ProxyServerPort> MAP = new HashMap();
    private String desc;
    private int index;

    static {
        ProxyServerPort[] values;
        for (ProxyServerPort proxyServerPort : values()) {
            MAP.put(proxyServerPort.toString(), proxyServerPort);
        }
    }

    ProxyServerPort(int i, String str) {
        this.index = i;
        this.desc = str;
    }

    public static ProxyServerPort valueOfName(String str) {
        return MAP.get(str);
    }

    public int getIndex() {
        return Integer.parseInt(this.desc);
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
