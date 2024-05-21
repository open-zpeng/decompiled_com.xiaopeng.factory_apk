package cn.hutool.core.lang;

import java.lang.Number;
/* loaded from: classes.dex */
public class DefaultSegment<T extends Number> implements Segment<T> {
    protected T endIndex;
    protected T startIndex;

    public DefaultSegment(T startIndex, T endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override // cn.hutool.core.lang.Segment
    public T getStartIndex() {
        return this.startIndex;
    }

    @Override // cn.hutool.core.lang.Segment
    public T getEndIndex() {
        return this.endIndex;
    }
}
