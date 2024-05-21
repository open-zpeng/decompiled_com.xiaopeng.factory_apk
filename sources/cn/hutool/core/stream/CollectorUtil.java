package cn.hutool.core.stream;

import java.util.Collections;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
/* loaded from: classes.dex */
public class CollectorUtil {
    public static <T> Collector<T, ?, String> joining(CharSequence delimiter) {
        return joining(delimiter, new Function() { // from class: cn.hutool.core.stream.-$$Lambda$4LiI1jOztFeQ3039WfZCt2dkQjI
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return obj.toString();
            }
        });
    }

    public static <T> Collector<T, ?, String> joining(CharSequence delimiter, Function<T, ? extends CharSequence> toStringFunc) {
        return joining(delimiter, "", "", toStringFunc);
    }

    public static <T> Collector<T, ?, String> joining(final CharSequence delimiter, final CharSequence prefix, final CharSequence suffix, final Function<T, ? extends CharSequence> toStringFunc) {
        return new SimpleCollector(new Supplier() { // from class: cn.hutool.core.stream.-$$Lambda$CollectorUtil$maKgG2D45xcy3BacKo4ZXbmP69s
            @Override // java.util.function.Supplier
            public final Object get() {
                return CollectorUtil.lambda$joining$0(delimiter, prefix, suffix);
            }
        }, new BiConsumer() { // from class: cn.hutool.core.stream.-$$Lambda$CollectorUtil$hl08-tACRpbKV7tqdn_VNKQ17x4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((StringJoiner) obj).add((CharSequence) toStringFunc.apply(obj2));
            }
        }, new BinaryOperator() { // from class: cn.hutool.core.stream.-$$Lambda$i0Jl5dMkfWphZviqg6QdkkWPWRI
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return ((StringJoiner) obj).merge((StringJoiner) obj2);
            }
        }, new Function() { // from class: cn.hutool.core.stream.-$$Lambda$okJigbB9kSn__oCZ5Do9uFNyF6A
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((StringJoiner) obj).toString();
            }
        }, Collections.emptySet());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ StringJoiner lambda$joining$0(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return new StringJoiner(delimiter, prefix, suffix);
    }
}
