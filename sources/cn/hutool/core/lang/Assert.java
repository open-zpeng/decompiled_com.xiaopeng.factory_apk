package cn.hutool.core.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Map;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class Assert {
    public static <X extends Throwable> void isTrue(boolean expression, Supplier<? extends X> supplier) throws Throwable {
        if (!expression) {
            throw supplier.get();
        }
    }

    public static void isTrue(boolean expression, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        isTrue(expression, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$aNzHIfkrl9Ae3dPrI8GZ2J3x1oA
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$isTrue$0(errorMsgTemplate, params);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$isTrue$0(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static void isTrue(boolean expression) throws IllegalArgumentException {
        isTrue(expression, "[Assertion failed] - this expression must be true", new Object[0]);
    }

    public static <X extends Throwable> void isFalse(boolean expression, Supplier<X> errorSupplier) throws Throwable {
        if (expression) {
            throw errorSupplier.get();
        }
    }

    public static void isFalse(boolean expression, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        isFalse(expression, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$2QYyc1k3NyS_3Puak52wih49sQ8
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$isFalse$1(errorMsgTemplate, params);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$isFalse$1(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static void isFalse(boolean expression) throws IllegalArgumentException {
        isFalse(expression, "[Assertion failed] - this expression must be false", new Object[0]);
    }

    public static <X extends Throwable> void isNull(Object object, Supplier<X> errorSupplier) throws Throwable {
        if (object != null) {
            throw errorSupplier.get();
        }
    }

    public static void isNull(Object object, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        isNull(object, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$TEf1AsBncv-qFpSTA3EzjTFEDy0
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$isNull$2(errorMsgTemplate, params);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$isNull$2(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static void isNull(Object object) throws IllegalArgumentException {
        isNull(object, "[Assertion failed] - the object argument must be null", new Object[0]);
    }

    public static <T, X extends Throwable> T notNull(T object, Supplier<X> errorSupplier) throws Throwable {
        if (object == null) {
            throw errorSupplier.get();
        }
        return object;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$notNull$3(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static <T> T notNull(T object, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (T) notNull(object, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$eSa13geDXfUFtcOuHoGzKFFBDCo
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$notNull$3(errorMsgTemplate, params);
            }
        });
    }

    public static <T> T notNull(T object) throws IllegalArgumentException {
        return (T) notNull(object, "[Assertion failed] - this argument is required; it must not be null", new Object[0]);
    }

    public static <T extends CharSequence, X extends Throwable> T notEmpty(T text, Supplier<X> errorSupplier) throws Throwable {
        if (StrUtil.isEmpty(text)) {
            throw errorSupplier.get();
        }
        return text;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$notEmpty$4(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static <T extends CharSequence> T notEmpty(T text, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (T) notEmpty(text, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$P5-JM-jZEH9y4DG2JccBD-H4ON8
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$notEmpty$4(errorMsgTemplate, params);
            }
        });
    }

    public static <T extends CharSequence> T notEmpty(T text) throws IllegalArgumentException {
        return (T) notEmpty(text, "[Assertion failed] - this String argument must have length; it must not be null or empty", new Object[0]);
    }

    public static <T extends CharSequence, X extends Throwable> T notBlank(T text, Supplier<X> errorMsgSupplier) throws Throwable {
        if (StrUtil.isBlank(text)) {
            throw errorMsgSupplier.get();
        }
        return text;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$notBlank$5(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static <T extends CharSequence> T notBlank(T text, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (T) notBlank(text, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$Hw0PU-HCQ2lr8-vWM7aZPW42f_s
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$notBlank$5(errorMsgTemplate, params);
            }
        });
    }

    public static <T extends CharSequence> T notBlank(T text) throws IllegalArgumentException {
        return (T) notBlank(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank", new Object[0]);
    }

    public static <T extends CharSequence, X extends Throwable> T notContain(CharSequence textToSearch, T substring, Supplier<X> errorSupplier) throws Throwable {
        if (StrUtil.contains(textToSearch, substring)) {
            throw errorSupplier.get();
        }
        return substring;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$notContain$6(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static String notContain(String textToSearch, String substring, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (String) notContain(textToSearch, substring, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$neZBnzBKeh9CUS5VD2Hyb2GZBfo
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$notContain$6(errorMsgTemplate, params);
            }
        });
    }

    public static String notContain(String textToSearch, String substring) throws IllegalArgumentException {
        return notContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [{}]", substring);
    }

    public static <T, X extends Throwable> T[] notEmpty(T[] array, Supplier<X> errorSupplier) throws Throwable {
        if (ArrayUtil.isEmpty((Object[]) array)) {
            throw errorSupplier.get();
        }
        return array;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$notEmpty$7(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static <T> T[] notEmpty(T[] array, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (T[]) notEmpty(array, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$_p_g2Ym8azoWKPI5M83MTBsq_lo
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$notEmpty$7(errorMsgTemplate, params);
            }
        });
    }

    public static <T> T[] notEmpty(T[] array) throws IllegalArgumentException {
        return (T[]) notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element", new Object[0]);
    }

    public static <T, X extends Throwable> T[] noNullElements(T[] array, Supplier<X> errorSupplier) throws Throwable {
        if (ArrayUtil.hasNull(array)) {
            throw errorSupplier.get();
        }
        return array;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$noNullElements$8(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static <T> T[] noNullElements(T[] array, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (T[]) noNullElements(array, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$9-Fxitl4kpdxIh05KZhlQe6cHvc
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$noNullElements$8(errorMsgTemplate, params);
            }
        });
    }

    public static <T> T[] noNullElements(T[] array) throws IllegalArgumentException {
        return (T[]) noNullElements(array, "[Assertion failed] - this array must not contain any null elements", new Object[0]);
    }

    public static <E, T extends Iterable<E>, X extends Throwable> T notEmpty(T collection, Supplier<X> errorSupplier) throws Throwable {
        if (CollUtil.isEmpty((Iterable<?>) collection)) {
            throw errorSupplier.get();
        }
        return collection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$notEmpty$9(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (T) notEmpty(collection, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$u_OwfFvZtV_FI0MshpDngilJtLg
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$notEmpty$9(errorMsgTemplate, params);
            }
        });
    }

    public static <E, T extends Iterable<E>> T notEmpty(T collection) throws IllegalArgumentException {
        return (T) notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element", new Object[0]);
    }

    public static <K, V, T extends Map<K, V>, X extends Throwable> T notEmpty(T map, Supplier<X> errorSupplier) throws Throwable {
        if (MapUtil.isEmpty(map)) {
            throw errorSupplier.get();
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ IllegalArgumentException lambda$notEmpty$10(String errorMsgTemplate, Object[] params) {
        return new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map, final String errorMsgTemplate, final Object... params) throws IllegalArgumentException {
        return (T) notEmpty(map, new Supplier() { // from class: cn.hutool.core.lang.-$$Lambda$Assert$K6ip-qgquMT_g_0sTizazwqnXQ0
            @Override // java.util.function.Supplier
            public final Object get() {
                return Assert.lambda$notEmpty$10(errorMsgTemplate, params);
            }
        });
    }

    public static <K, V, T extends Map<K, V>> T notEmpty(T map) throws IllegalArgumentException {
        return (T) notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry", new Object[0]);
    }

    public static <T> T isInstanceOf(Class<?> type, T obj) {
        return (T) isInstanceOf(type, obj, "Object [{}] is not instanceof [{}]", obj, type);
    }

    public static <T> T isInstanceOf(Class<?> type, T obj, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        notNull(type, "Type to check against must not be null", new Object[0]);
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
        return obj;
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) throws IllegalArgumentException {
        isAssignable(superType, subType, "{} is not assignable to {})", subType, superType);
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        notNull(superType, "Type to check against must not be null", new Object[0]);
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    public static void state(boolean expression, Supplier<String> errorMsgSupplier) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(errorMsgSupplier.get());
        }
    }

    public static void state(boolean expression, String errorMsgTemplate, Object... params) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    public static void state(boolean expression) throws IllegalStateException {
        state(expression, "[Assertion failed] - this state invariant must be true", new Object[0]);
    }

    public static int checkIndex(int index, int size) throws IllegalArgumentException, IndexOutOfBoundsException {
        return checkIndex(index, size, "[Assertion failed]", new Object[0]);
    }

    public static int checkIndex(int index, int size, String errorMsgTemplate, Object... params) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badIndexMsg(index, size, errorMsgTemplate, params));
        }
        return index;
    }

    public static int checkBetween(int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", Integer.valueOf(min), Integer.valueOf(max)));
        }
        return value;
    }

    public static long checkBetween(long value, long min, long max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", Long.valueOf(min), Long.valueOf(max)));
        }
        return value;
    }

    public static double checkBetween(double value, double min, double max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", Double.valueOf(min), Double.valueOf(max)));
        }
        return value;
    }

    public static Number checkBetween(Number value, Number min, Number max) {
        notNull(value);
        notNull(min);
        notNull(max);
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        if (valueDouble < minDouble || valueDouble > maxDouble) {
            throw new IllegalArgumentException(StrUtil.format("Length must be between {} and {}.", min, max));
        }
        return value;
    }

    private static String badIndexMsg(int index, int size, String desc, Object... params) {
        if (index < 0) {
            return StrUtil.format("{} ({}) must not be negative", StrUtil.format(desc, params), Integer.valueOf(index));
        }
        if (size >= 0) {
            return StrUtil.format("{} ({}) must be less than size ({})", StrUtil.format(desc, params), Integer.valueOf(index), Integer.valueOf(size));
        }
        throw new IllegalArgumentException("negative size: " + size);
    }
}
