package cn.hutool.core.comparator;

import cn.hutool.core.bean.BeanUtil;
import java.util.function.Function;
/* loaded from: classes.dex */
public class PropertyComparator<T> extends FuncComparator<T> {
    private static final long serialVersionUID = 9157326766723846313L;

    public PropertyComparator(String property) {
        this(property, true);
    }

    public PropertyComparator(final String property, boolean isNullGreater) {
        super(isNullGreater, new Function() { // from class: cn.hutool.core.comparator.-$$Lambda$PropertyComparator$tydm6wcH5zB602wte6bL5hUM68U
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PropertyComparator.lambda$new$0(property, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Comparable lambda$new$0(String property, Object bean) {
        return (Comparable) BeanUtil.getProperty(bean, property);
    }
}
