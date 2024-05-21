package cn.hutool.core.bean.copier;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.bean.PropDesc;
import cn.hutool.core.bean.copier.provider.BeanValueProvider;
import cn.hutool.core.bean.copier.provider.DynaBeanValueProvider;
import cn.hutool.core.bean.copier.provider.MapValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.copier.Copier;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class BeanCopier<T> implements Copier<T>, Serializable {
    private static final long serialVersionUID = 1;
    private final CopyOptions copyOptions;
    private final T dest;
    private final Type destType;
    private final Object source;

    public static <T> BeanCopier<T> create(Object source, T dest, CopyOptions copyOptions) {
        return create(source, dest, dest.getClass(), copyOptions);
    }

    public static <T> BeanCopier<T> create(Object source, T dest, Type destType, CopyOptions copyOptions) {
        return new BeanCopier<>(source, dest, destType, copyOptions);
    }

    public BeanCopier(Object source, T dest, Type destType, CopyOptions copyOptions) {
        this.source = source;
        this.dest = dest;
        this.destType = destType;
        this.copyOptions = copyOptions;
    }

    @Override // cn.hutool.core.lang.copier.Copier
    public T copy() {
        Object obj = this.source;
        if (obj != null) {
            if (obj instanceof ValueProvider) {
                valueProviderToBean((ValueProvider) obj, this.dest);
            } else if (obj instanceof DynaBean) {
                valueProviderToBean(new DynaBeanValueProvider((DynaBean) obj, this.copyOptions.ignoreError), this.dest);
            } else if (obj instanceof Map) {
                T t = this.dest;
                if (t instanceof Map) {
                    mapToMap((Map) obj, (Map) t);
                } else {
                    mapToBean((Map) obj, t);
                }
            } else {
                T t2 = this.dest;
                if (t2 instanceof Map) {
                    beanToMap(obj, (Map) t2);
                } else {
                    beanToBean(obj, t2);
                }
            }
        }
        return this.dest;
    }

    private void beanToBean(Object providerBean, Object destBean) {
        valueProviderToBean(new BeanValueProvider(providerBean, this.copyOptions.ignoreCase, this.copyOptions.ignoreError), destBean);
    }

    private void mapToBean(Map<?, ?> map, Object bean) {
        valueProviderToBean(new MapValueProvider(map, this.copyOptions.ignoreCase, this.copyOptions.ignoreError), bean);
    }

    private void mapToMap(Map source, Map dest) {
        if (dest != null && source != null) {
            dest.putAll(source);
        }
    }

    private void beanToMap(final Object bean, final Map targetMap) {
        final HashSet<String> ignoreSet = this.copyOptions.ignoreProperties != null ? CollUtil.newHashSet(this.copyOptions.ignoreProperties) : null;
        final CopyOptions copyOptions = this.copyOptions;
        BeanUtil.descForEach(bean.getClass(), new Consumer() { // from class: cn.hutool.core.bean.copier.-$$Lambda$BeanCopier$Xv_1Hx4afVep2f_QlT35gbi5p5I
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanCopier.lambda$beanToMap$0(CopyOptions.this, ignoreSet, bean, targetMap, (PropDesc) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$beanToMap$0(CopyOptions copyOptions, HashSet ignoreSet, Object bean, Map targetMap, PropDesc prop) {
        String key;
        if (!prop.isReadable(copyOptions.isTransientSupport())) {
            return;
        }
        String key2 = prop.getFieldName();
        if (CollUtil.contains(ignoreSet, key2) || (key = copyOptions.editFieldName(copyOptions.getMappedFieldName(key2, false))) == null) {
            return;
        }
        try {
            Object value = prop.getValue(bean);
            if (copyOptions.propertiesFilter != null && !copyOptions.propertiesFilter.test(prop.getField(), value)) {
                return;
            }
            if ((value == null && copyOptions.ignoreNullValue) || bean == value) {
                return;
            }
            targetMap.put(key, value);
        } catch (Exception e) {
            if (copyOptions.ignoreError) {
                return;
            }
            throw new BeanException(e, "Get value of [{}] error!", prop.getFieldName());
        }
    }

    private void valueProviderToBean(final ValueProvider<String> valueProvider, final Object bean) {
        Class<?> actualEditable;
        if (valueProvider == null) {
            return;
        }
        final CopyOptions copyOptions = this.copyOptions;
        Class<?> actualEditable2 = bean.getClass();
        if (copyOptions.editable == null) {
            actualEditable = actualEditable2;
        } else if (!copyOptions.editable.isInstance(bean)) {
            throw new IllegalArgumentException(StrUtil.format("Target class [{}] not assignable to Editable class [{}]", bean.getClass().getName(), copyOptions.editable.getName()));
        } else {
            Class<?> actualEditable3 = copyOptions.editable;
            actualEditable = actualEditable3;
        }
        final HashSet<String> ignoreSet = copyOptions.ignoreProperties != null ? CollUtil.newHashSet(copyOptions.ignoreProperties) : null;
        BeanUtil.descForEach(actualEditable, new Consumer() { // from class: cn.hutool.core.bean.copier.-$$Lambda$BeanCopier$-Mm0ExJ2TW-_JN1V29_Zf7TWRxc
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BeanCopier.this.lambda$valueProviderToBean$1$BeanCopier(ignoreSet, copyOptions, valueProvider, bean, (PropDesc) obj);
            }
        });
    }

    public /* synthetic */ void lambda$valueProviderToBean$1$BeanCopier(HashSet ignoreSet, CopyOptions copyOptions, ValueProvider valueProvider, Object bean, PropDesc prop) {
        String providerKey;
        if (!prop.isWritable(this.copyOptions.isTransientSupport())) {
            return;
        }
        String fieldName = prop.getFieldName();
        if (CollUtil.contains(ignoreSet, fieldName) || (providerKey = copyOptions.editFieldName(copyOptions.getMappedFieldName(fieldName, true))) == null || !valueProvider.containsKey(providerKey)) {
            return;
        }
        Type fieldType = TypeUtil.getActualType(this.destType, prop.getFieldType());
        Object value = valueProvider.value(providerKey, fieldType);
        if (copyOptions.propertiesFilter != null && !copyOptions.propertiesFilter.test(prop.getField(), value)) {
            return;
        }
        if ((value == null && copyOptions.ignoreNullValue) || bean == value) {
            return;
        }
        prop.setValue(bean, value, copyOptions.ignoreNullValue, copyOptions.ignoreError);
    }
}
