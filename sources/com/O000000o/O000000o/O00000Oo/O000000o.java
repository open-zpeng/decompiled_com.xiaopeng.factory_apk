package com.O000000o.O000000o.O00000Oo;
/* compiled from: $Gson$Preconditions.java */
/* loaded from: classes.dex */
public final class O000000o {
    public static <T> T O000000o(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static void O000000o(boolean z) {
        if (!z) {
            throw new IllegalArgumentException();
        }
    }
}
