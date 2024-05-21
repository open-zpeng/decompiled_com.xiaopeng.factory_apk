package cn.hutool.core.bean.copier.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.PropDesc;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Type;
import java.util.Map;
/* loaded from: classes.dex */
public class BeanValueProvider implements ValueProvider<String> {
    private final boolean ignoreError;
    private final Object source;
    final Map<String, PropDesc> sourcePdMap;

    public BeanValueProvider(Object bean, boolean ignoreCase, boolean ignoreError) {
        this.source = bean;
        this.ignoreError = ignoreError;
        this.sourcePdMap = BeanUtil.getBeanDesc(this.source.getClass()).getPropMap(ignoreCase);
    }

    @Override // cn.hutool.core.bean.copier.ValueProvider
    public Object value(String key, Type valueType) {
        PropDesc sourcePd = getPropDesc(key, valueType);
        if (sourcePd == null) {
            return null;
        }
        Object result = sourcePd.getValue(this.source, valueType, this.ignoreError);
        return result;
    }

    @Override // cn.hutool.core.bean.copier.ValueProvider
    public boolean containsKey(String key) {
        PropDesc sourcePd = getPropDesc(key, null);
        return sourcePd != null && sourcePd.isReadable(false);
    }

    private PropDesc getPropDesc(String key, Type valueType) {
        PropDesc sourcePd = this.sourcePdMap.get(key);
        if (sourcePd == null) {
            if (valueType == null || Boolean.class == valueType || Boolean.TYPE == valueType) {
                return this.sourcePdMap.get(StrUtil.upperFirstAndAddPre(key, "is"));
            }
            return sourcePd;
        }
        return sourcePd;
    }
}
