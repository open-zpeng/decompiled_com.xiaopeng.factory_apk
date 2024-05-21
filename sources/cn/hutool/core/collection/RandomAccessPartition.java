package cn.hutool.core.collection;

import java.util.List;
import java.util.RandomAccess;
/* loaded from: classes.dex */
public class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
    public RandomAccessPartition(List<T> list, int size) {
        super(list, size);
    }
}
