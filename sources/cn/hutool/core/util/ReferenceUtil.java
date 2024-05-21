package cn.hutool.core.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class ReferenceUtil {

    /* loaded from: classes.dex */
    public enum ReferenceType {
        SOFT,
        WEAK,
        PHANTOM
    }

    public static <T> Reference<T> create(ReferenceType type, T referent) {
        return create(type, referent, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: cn.hutool.core.util.ReferenceUtil$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$hutool$core$util$ReferenceUtil$ReferenceType = new int[ReferenceType.values().length];

        static {
            try {
                $SwitchMap$cn$hutool$core$util$ReferenceUtil$ReferenceType[ReferenceType.SOFT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$hutool$core$util$ReferenceUtil$ReferenceType[ReferenceType.WEAK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$cn$hutool$core$util$ReferenceUtil$ReferenceType[ReferenceType.PHANTOM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public static <T> Reference<T> create(ReferenceType type, T referent, ReferenceQueue<T> queue) {
        int i = AnonymousClass1.$SwitchMap$cn$hutool$core$util$ReferenceUtil$ReferenceType[type.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    return new PhantomReference(referent, queue);
                }
                return null;
            }
            return new WeakReference(referent);
        }
        return new SoftReference(referent);
    }
}
