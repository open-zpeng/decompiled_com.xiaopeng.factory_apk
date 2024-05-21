package com.O000000o.O000000o.O00000Oo.O000000o;

import com.O000000o.O000000o.O000o00;
import com.O000000o.O000000o.O000o000;
import java.io.IOException;
import java.util.ArrayList;
/* compiled from: ObjectTypeAdapter.java */
/* loaded from: classes.dex */
public final class O0000Oo0 extends O000o000<Object> {
    public static final O000o00 O000000o = new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O0000Oo0.1
        @Override // com.O000000o.O000000o.O000o00
        public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o) {
            if (o000000o.O000000o() == Object.class) {
                return new O0000Oo0(o0000OOo);
            }
            return null;
        }
    };
    private final com.O000000o.O000000o.O0000OOo O00000Oo;

    private O0000Oo0(com.O000000o.O000000o.O0000OOo o0000OOo) {
        this.O00000Oo = o0000OOo;
    }

    @Override // com.O000000o.O000000o.O000o000
    public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Object obj) throws IOException {
        if (obj == null) {
            o00000o.O00000oo();
            return;
        }
        O000o000 O000000o2 = this.O00000Oo.O000000o((Class) obj.getClass());
        if (!(O000000o2 instanceof O0000Oo0)) {
            O000000o2.O000000o(o00000o, (com.O000000o.O000000o.O00000o.O00000o) obj);
            return;
        }
        o00000o.O00000o();
        o00000o.O00000oO();
    }

    @Override // com.O000000o.O000000o.O000o000
    public Object O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
        switch (o000000o.O00000oo()) {
            case BEGIN_ARRAY:
                ArrayList arrayList = new ArrayList();
                o000000o.O000000o();
                while (o000000o.O00000oO()) {
                    arrayList.add(O00000Oo(o000000o));
                }
                o000000o.O00000Oo();
                return arrayList;
            case BEGIN_OBJECT:
                com.O000000o.O000000o.O00000Oo.O0000Oo0 o0000Oo0 = new com.O000000o.O000000o.O00000Oo.O0000Oo0();
                o000000o.O00000o0();
                while (o000000o.O00000oO()) {
                    o0000Oo0.put(o000000o.O0000O0o(), O00000Oo(o000000o));
                }
                o000000o.O00000o();
                return o0000Oo0;
            case STRING:
                return o000000o.O0000OOo();
            case NUMBER:
                return Double.valueOf(o000000o.O0000OoO());
            case BOOLEAN:
                return Boolean.valueOf(o000000o.O0000Oo0());
            case NULL:
                o000000o.O0000Oo();
                return null;
            default:
                throw new IllegalStateException();
        }
    }
}
