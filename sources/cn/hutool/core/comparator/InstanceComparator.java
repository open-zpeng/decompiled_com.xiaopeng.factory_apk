package cn.hutool.core.comparator;

import cn.hutool.core.lang.Assert;
import java.util.Comparator;
/* loaded from: classes.dex */
public class InstanceComparator<T> implements Comparator<T> {
    private final boolean atEndIfMiss;
    private final Class<?>[] instanceOrder;

    public InstanceComparator(Class<?>... instanceOrder) {
        this(false, instanceOrder);
    }

    public InstanceComparator(boolean atEndIfMiss, Class<?>... instanceOrder) {
        Assert.notNull(instanceOrder, "'instanceOrder' array must not be null", new Object[0]);
        this.atEndIfMiss = atEndIfMiss;
        this.instanceOrder = instanceOrder;
    }

    @Override // java.util.Comparator
    public int compare(T o1, T o2) {
        int i1 = getOrder(o1);
        int i2 = getOrder(o2);
        return Integer.compare(i1, i2);
    }

    private int getOrder(T object) {
        if (object != null) {
            int i = 0;
            while (true) {
                Class<?>[] clsArr = this.instanceOrder;
                if (i >= clsArr.length) {
                    break;
                } else if (!clsArr[i].isInstance(object)) {
                    i++;
                } else {
                    return i;
                }
            }
        }
        if (this.atEndIfMiss) {
            return this.instanceOrder.length;
        }
        return -1;
    }
}
