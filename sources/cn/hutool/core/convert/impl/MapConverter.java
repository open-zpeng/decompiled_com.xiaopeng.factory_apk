package cn.hutool.core.convert.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class MapConverter extends AbstractConverter<Map<?, ?>> {
    private static final long serialVersionUID = 1;
    private final Type keyType;
    private final Type mapType;
    private final Type valueType;

    public MapConverter(Type mapType) {
        this(mapType, TypeUtil.getTypeArgument(mapType, 0), TypeUtil.getTypeArgument(mapType, 1));
    }

    public MapConverter(Type mapType, Type keyType, Type valueType) {
        this.mapType = mapType;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public Map<?, ?> convertInternal(Object value) {
        if (!(value instanceof Map)) {
            if (BeanUtil.isBean(value.getClass())) {
                return convertInternal((Object) BeanUtil.beanToMap(value));
            }
            throw new UnsupportedOperationException(StrUtil.format("Unsupport toMap value type: {}", value.getClass().getName()));
        }
        Type[] typeArguments = TypeUtil.getTypeArguments(value.getClass());
        if (typeArguments != null && 2 == typeArguments.length && Objects.equals(this.keyType, typeArguments[0]) && Objects.equals(this.valueType, typeArguments[1])) {
            return (Map) value;
        }
        Map map = MapUtil.createMap(TypeUtil.getClass(this.mapType));
        convertMapToMap((Map) value, map);
        return map;
    }

    private void convertMapToMap(Map<?, ?> srcMap, Map<Object, Object> targetMap) {
        ConverterRegistry convert = ConverterRegistry.getInstance();
        for (Map.Entry<?, ?> entry : srcMap.entrySet()) {
            Object key = TypeUtil.isUnknown(this.keyType) ? entry.getKey() : convert.convert(this.keyType, entry.getKey());
            Object value = TypeUtil.isUnknown(this.valueType) ? entry.getValue() : convert.convert(this.valueType, entry.getValue());
            targetMap.put(key, value);
        }
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<Map<?, ?>> getTargetType() {
        return TypeUtil.getClass(this.mapType);
    }
}
