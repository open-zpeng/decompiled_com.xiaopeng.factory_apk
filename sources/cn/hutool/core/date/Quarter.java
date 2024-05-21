package cn.hutool.core.date;
/* loaded from: classes.dex */
public enum Quarter {
    Q1(1),
    Q2(2),
    Q3(3),
    Q4(4);
    
    private final int value;

    Quarter(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Quarter of(int intValue) {
        if (intValue != 1) {
            if (intValue != 2) {
                if (intValue != 3) {
                    if (intValue == 4) {
                        return Q4;
                    }
                    return null;
                }
                return Q3;
            }
            return Q2;
        }
        return Q1;
    }
}
