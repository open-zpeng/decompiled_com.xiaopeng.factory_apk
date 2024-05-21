package cn.hutool.core.convert.impl;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class ArrayConverter extends AbstractConverter<Object> {
    private static final long serialVersionUID = 1;
    private boolean ignoreElementError;
    private final Class<?> targetComponentType;
    private final Class<?> targetType;

    public ArrayConverter(Class<?> targetType) {
        this(targetType, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArrayConverter(Class<?> targetType, boolean ignoreElementError) {
        Class cls = targetType == null ? Object[].class : targetType;
        if (cls.isArray()) {
            this.targetType = cls;
            this.targetComponentType = cls.getComponentType();
        } else {
            this.targetComponentType = cls;
            this.targetType = ArrayUtil.getArrayType(cls);
        }
        this.ignoreElementError = ignoreElementError;
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    protected Object convertInternal(Object value) {
        return value.getClass().isArray() ? convertArrayToArray(value) : convertObjectToArray(value);
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<Object> getTargetType() {
        return this.targetType;
    }

    public void setIgnoreElementError(boolean ignoreElementError) {
        this.ignoreElementError = ignoreElementError;
    }

    private Object convertArrayToArray(Object array) {
        Class<?> valueComponentType = ArrayUtil.getComponentType(array);
        if (valueComponentType == this.targetComponentType) {
            return array;
        }
        int len = ArrayUtil.length(array);
        Object result = Array.newInstance(this.targetComponentType, len);
        for (int i = 0; i < len; i++) {
            Array.set(result, i, convertComponentType(Array.get(array, i)));
        }
        return result;
    }

    private Object convertObjectToArray(Object value) {
        if (value instanceof CharSequence) {
            if (this.targetComponentType == Character.TYPE || this.targetComponentType == Character.class) {
                return convertArrayToArray(value.toString().toCharArray());
            }
            String[] strings = StrUtil.splitToArray((CharSequence) value.toString(), ',');
            return convertArrayToArray(strings);
        } else if (value instanceof List) {
            List<?> list = (List) value;
            Object result = Array.newInstance(this.targetComponentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(result, i, convertComponentType(list.get(i)));
            }
            return result;
        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection) value;
            Object result2 = Array.newInstance(this.targetComponentType, collection.size());
            int i2 = 0;
            for (Object element : collection) {
                Array.set(result2, i2, convertComponentType(element));
                i2++;
            }
            return result2;
        } else if (value instanceof Iterable) {
            List<?> list2 = IterUtil.toList((Iterable) value);
            Object result3 = Array.newInstance(this.targetComponentType, list2.size());
            for (int i3 = 0; i3 < list2.size(); i3++) {
                Array.set(result3, i3, convertComponentType(list2.get(i3)));
            }
            return result3;
        } else if (value instanceof Iterator) {
            List<?> list3 = IterUtil.toList((Iterator) value);
            Object result4 = Array.newInstance(this.targetComponentType, list3.size());
            for (int i4 = 0; i4 < list3.size(); i4++) {
                Array.set(result4, i4, convertComponentType(list3.get(i4)));
            }
            return result4;
        } else if ((value instanceof Number) && Byte.TYPE == this.targetComponentType) {
            Object result5 = ByteUtil.numberToBytes((Number) value);
            return result5;
        } else if ((value instanceof Serializable) && Byte.TYPE == this.targetComponentType) {
            Object result6 = ObjectUtil.serialize(value);
            return result6;
        } else {
            Object result7 = convertToSingleElementArray(value);
            return result7;
        }
    }

    private Object[] convertToSingleElementArray(Object value) {
        Object[] singleElementArray = ArrayUtil.newArray(this.targetComponentType, 1);
        singleElementArray[0] = convertComponentType(value);
        return singleElementArray;
    }

    private Object convertComponentType(Object value) {
        return Convert.convertWithCheck(this.targetComponentType, value, null, this.ignoreElementError);
    }
}
