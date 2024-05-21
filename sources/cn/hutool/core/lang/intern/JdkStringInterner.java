package cn.hutool.core.lang.intern;
/* loaded from: classes.dex */
public class JdkStringInterner implements Interner<String> {
    @Override // cn.hutool.core.lang.intern.Interner
    public String intern(String sample) {
        if (sample == null) {
            return null;
        }
        return sample.intern();
    }
}
