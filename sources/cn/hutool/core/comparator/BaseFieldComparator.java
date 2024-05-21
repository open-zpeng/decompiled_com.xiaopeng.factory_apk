package cn.hutool.core.comparator;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;
@Deprecated
/* loaded from: classes.dex */
public abstract class BaseFieldComparator<T> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = -3482464782340308755L;

    protected int compareItem(T o1, T o2, Field field) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return 1;
        }
        if (o2 == null) {
            return -1;
        }
        try {
            Comparable<?> v1 = (Comparable) ReflectUtil.getFieldValue(o1, field);
            Comparable<?> v2 = (Comparable) ReflectUtil.getFieldValue(o2, field);
            return compare(o1, o2, v1, v2);
        } catch (Exception e) {
            throw new ComparatorException(e);
        }
    }

    private int compare(T o1, T o2, Comparable fieldValue1, Comparable fieldValue2) {
        int result = ObjectUtil.compare(fieldValue1, fieldValue2);
        if (result == 0) {
            return CompareUtil.compare((Object) o1, (Object) o2, true);
        }
        return result;
    }
}
