package cn.hutool.core.lang.hash;
/* loaded from: classes.dex */
public class Number128 {
    private long highValue;
    private long lowValue;

    public Number128(long lowValue, long highValue) {
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public long getLowValue() {
        return this.lowValue;
    }

    public long getHighValue() {
        return this.highValue;
    }

    public void setLowValue(long lowValue) {
        this.lowValue = lowValue;
    }

    public void setHighValue(long hiValue) {
        this.highValue = hiValue;
    }

    public long[] getLongArray() {
        return new long[]{this.lowValue, this.highValue};
    }
}
