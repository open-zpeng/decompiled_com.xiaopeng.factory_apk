package cn.hutool.core.math;
/* loaded from: classes.dex */
public class BitStatusUtil {
    public static int add(int states, int stat) {
        check(states, stat);
        return states | stat;
    }

    public static boolean has(int states, int stat) {
        check(states, stat);
        return (states & stat) == stat;
    }

    public static int remove(int states, int stat) {
        check(states, stat);
        if (has(states, stat)) {
            return states ^ stat;
        }
        return states;
    }

    public static int clear() {
        return 0;
    }

    private static void check(int... args) {
        for (int arg : args) {
            if (arg < 0) {
                throw new IllegalArgumentException(arg + " 必须大于等于0");
            } else if ((arg & 1) == 1) {
                throw new IllegalArgumentException(arg + " 不是偶数");
            }
        }
    }
}
