package cn.hutool.core.bean;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.annotation.PropIgnore;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
/* loaded from: classes.dex */
public class PropDesc {
    final Field field;
    protected Method getter;
    protected Method setter;

    public PropDesc(Field field, Method getter, Method setter) {
        this.field = field;
        this.getter = ClassUtil.setAccessible(getter);
        this.setter = ClassUtil.setAccessible(setter);
    }

    public String getFieldName() {
        return ReflectUtil.getFieldName(this.field);
    }

    public String getRawFieldName() {
        Field field = this.field;
        if (field == null) {
            return null;
        }
        return field.getName();
    }

    public Field getField() {
        return this.field;
    }

    public Type getFieldType() {
        Field field = this.field;
        if (field != null) {
            return TypeUtil.getType(field);
        }
        return findPropType(this.getter, this.setter);
    }

    public Class<?> getFieldClass() {
        Field field = this.field;
        if (field != null) {
            return TypeUtil.getClass(field);
        }
        return findPropClass(this.getter, this.setter);
    }

    public Method getGetter() {
        return this.getter;
    }

    public Method getSetter() {
        return this.setter;
    }

    public boolean isReadable(boolean checkTransient) {
        if (this.getter != null || ModifierUtil.isPublic(this.field)) {
            if (checkTransient && isTransientForGet()) {
                return false;
            }
            return !isIgnoreGet();
        }
        return false;
    }

    public Object getValue(Object bean) {
        Method method = this.getter;
        if (method != null) {
            return ReflectUtil.invoke(bean, method, new Object[0]);
        }
        if (ModifierUtil.isPublic(this.field)) {
            return ReflectUtil.getFieldValue(bean, this.field);
        }
        return null;
    }

    public Object getValue(Object bean, Type targetType, boolean ignoreError) {
        Object result = null;
        try {
            result = getValue(bean);
        } catch (Exception e) {
            if (!ignoreError) {
                throw new BeanException(e, "Get value of [{}] error!", getFieldName());
            }
        }
        if (result != null && targetType != null) {
            return Convert.convertWithCheck(targetType, result, null, ignoreError);
        }
        return result;
    }

    public boolean isWritable(boolean checkTransient) {
        if (this.setter != null || ModifierUtil.isPublic(this.field)) {
            if (checkTransient && isTransientForSet()) {
                return false;
            }
            return !isIgnoreSet();
        }
        return false;
    }

    public PropDesc setValue(Object bean, Object value) {
        Method method = this.setter;
        if (method != null) {
            ReflectUtil.invoke(bean, method, value);
        } else if (ModifierUtil.isPublic(this.field)) {
            ReflectUtil.setFieldValue(bean, this.field, value);
        }
        return this;
    }

    public PropDesc setValue(Object bean, Object value, boolean ignoreNull, boolean ignoreError) {
        if (ignoreNull && value == null) {
            return this;
        }
        if (value != null) {
            Class<?> propClass = getFieldClass();
            if (!propClass.isInstance(value)) {
                value = Convert.convertWithCheck(propClass, value, null, ignoreError);
            }
        }
        if (value != null || !ignoreNull) {
            try {
                setValue(bean, value);
            } catch (Exception e) {
                if (!ignoreError) {
                    throw new BeanException(e, "Set value of [{}] error!", getFieldName());
                }
            }
        }
        return this;
    }

    private Type findPropType(Method getter, Method setter) {
        Type type = null;
        if (getter != null) {
            type = TypeUtil.getReturnType(getter);
        }
        if (type == null && setter != null) {
            Type type2 = TypeUtil.getParamType(setter, 0);
            return type2;
        }
        return type;
    }

    private Class<?> findPropClass(Method getter, Method setter) {
        Class<?> type = null;
        if (getter != null) {
            type = TypeUtil.getReturnClass(getter);
        }
        if (type == null && setter != null) {
            Class<?> type2 = TypeUtil.getFirstParamClass(setter);
            return type2;
        }
        return type;
    }

    private boolean isIgnoreSet() {
        return AnnotationUtil.hasAnnotation(this.field, PropIgnore.class) || AnnotationUtil.hasAnnotation(this.setter, PropIgnore.class);
    }

    private boolean isIgnoreGet() {
        return AnnotationUtil.hasAnnotation(this.field, PropIgnore.class) || AnnotationUtil.hasAnnotation(this.getter, PropIgnore.class);
    }

    private boolean isTransientForGet() {
        Method method;
        boolean isTransient = ModifierUtil.hasModifier(this.field, ModifierUtil.ModifierType.TRANSIENT);
        if (!isTransient && (method = this.getter) != null) {
            boolean isTransient2 = ModifierUtil.hasModifier(method, ModifierUtil.ModifierType.TRANSIENT);
            if (!isTransient2) {
                return AnnotationUtil.hasAnnotation(this.getter, Transient.class);
            }
            return isTransient2;
        }
        return isTransient;
    }

    private boolean isTransientForSet() {
        Method method;
        boolean isTransient = ModifierUtil.hasModifier(this.field, ModifierUtil.ModifierType.TRANSIENT);
        if (!isTransient && (method = this.setter) != null) {
            boolean isTransient2 = ModifierUtil.hasModifier(method, ModifierUtil.ModifierType.TRANSIENT);
            if (!isTransient2) {
                return AnnotationUtil.hasAnnotation(this.setter, Transient.class);
            }
            return isTransient2;
        }
        return isTransient;
    }
}
