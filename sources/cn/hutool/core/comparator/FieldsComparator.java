package cn.hutool.core.comparator;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import java.lang.reflect.Field;
import java.util.Comparator;
/* loaded from: classes.dex */
public class FieldsComparator<T> extends NullComparator<T> {
    private static final long serialVersionUID = 8649196282886500803L;

    public FieldsComparator(Class<T> beanClass, String... fieldNames) {
        this(true, beanClass, fieldNames);
    }

    public FieldsComparator(boolean nullGreater, final Class<T> beanClass, final String... fieldNames) {
        super(nullGreater, new Comparator() { // from class: cn.hutool.core.comparator.-$$Lambda$FieldsComparator$OmR6xx22A_S8AX3Lb2tp3nS2bek
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return FieldsComparator.lambda$new$0(fieldNames, beanClass, obj, obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$new$0(String[] fieldNames, Class beanClass, Object a, Object b) {
        for (String fieldName : fieldNames) {
            Field field = ClassUtil.getDeclaredField(beanClass, fieldName);
            Assert.notNull(field, "Field [{}] not found in Class [{}]", fieldName, beanClass.getName());
            int compare = new FieldComparator(field).compare(a, b);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }
}
