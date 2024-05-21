package cn.hutool.core.comparator;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Field;
import java.util.function.Function;
/* loaded from: classes.dex */
public class FieldComparator<T> extends FuncComparator<T> {
    private static final long serialVersionUID = 9157326766723846313L;

    public FieldComparator(Class<T> beanClass, String fieldName) {
        this(getNonNullField(beanClass, fieldName));
    }

    public FieldComparator(Field field) {
        this(true, field);
    }

    public FieldComparator(boolean nullGreater, final Field field) {
        super(nullGreater, new Function() { // from class: cn.hutool.core.comparator.-$$Lambda$FieldComparator$9in4vWRi4Ms-5X81cNAgDsOQx8o
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return FieldComparator.lambda$new$0(field, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Comparable lambda$new$0(Field field, Object bean) {
        return (Comparable) ReflectUtil.getFieldValue(bean, (Field) Assert.notNull(field, "Field must be not null!", new Object[0]));
    }

    private static Field getNonNullField(Class<?> beanClass, String fieldName) {
        Field field = ClassUtil.getDeclaredField(beanClass, fieldName);
        if (field == null) {
            throw new IllegalArgumentException(StrUtil.format("Field [{}] not found in Class [{}]", fieldName, beanClass.getName()));
        }
        return field;
    }
}
