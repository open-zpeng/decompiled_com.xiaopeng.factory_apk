package cn.hutool.core.bean;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class BeanDesc implements Serializable {
    private static final long serialVersionUID = 1;
    private final Class<?> beanClass;
    private final Map<String, PropDesc> propMap = new LinkedHashMap();

    public BeanDesc(Class<?> beanClass) {
        Assert.notNull(beanClass);
        this.beanClass = beanClass;
        init();
    }

    public String getName() {
        return this.beanClass.getName();
    }

    public String getSimpleName() {
        return this.beanClass.getSimpleName();
    }

    public Map<String, PropDesc> getPropMap(boolean ignoreCase) {
        return ignoreCase ? new CaseInsensitiveMap(1.0f, this.propMap) : this.propMap;
    }

    public Collection<PropDesc> getProps() {
        return this.propMap.values();
    }

    public PropDesc getProp(String fieldName) {
        return this.propMap.get(fieldName);
    }

    public Field getField(String fieldName) {
        PropDesc desc = this.propMap.get(fieldName);
        if (desc == null) {
            return null;
        }
        return desc.getField();
    }

    public Method getGetter(String fieldName) {
        PropDesc desc = this.propMap.get(fieldName);
        if (desc == null) {
            return null;
        }
        return desc.getGetter();
    }

    public Method getSetter(String fieldName) {
        PropDesc desc = this.propMap.get(fieldName);
        if (desc == null) {
            return null;
        }
        return desc.getSetter();
    }

    private BeanDesc init() {
        Field[] fields;
        Method[] methods = ReflectUtil.getMethods(this.beanClass);
        for (Field field : ReflectUtil.getFields(this.beanClass)) {
            if (!ModifierUtil.isStatic(field)) {
                PropDesc prop = createProp(field, methods);
                this.propMap.putIfAbsent(prop.getFieldName(), prop);
            }
        }
        return this;
    }

    private PropDesc createProp(Field field, Method[] methods) {
        PropDesc prop = findProp(field, methods, false);
        if (prop.getter == null || prop.setter == null) {
            PropDesc propIgnoreCase = findProp(field, methods, true);
            if (prop.getter == null) {
                prop.getter = propIgnoreCase.getter;
            }
            if (prop.setter == null) {
                prop.setter = propIgnoreCase.setter;
            }
        }
        return prop;
    }

    private PropDesc findProp(Field field, Method[] methods, boolean ignoreCase) {
        String fieldName = field.getName();
        Class<?> fieldType = field.getType();
        boolean isBooleanField = BooleanUtil.isBoolean(fieldType);
        Method getter = null;
        Method setter = null;
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length <= 1) {
                String methodName = method.getName();
                if (parameterTypes.length == 0) {
                    if (isMatchGetter(methodName, fieldName, isBooleanField, ignoreCase)) {
                        getter = method;
                    }
                } else if (isMatchSetter(methodName, fieldName, isBooleanField, ignoreCase)) {
                    setter = method;
                }
                if (getter != null && setter != null) {
                    break;
                }
            }
        }
        return new PropDesc(field, getter, setter);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0062, code lost:
        if (("is" + r0).equals(r6) != false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isMatchGetter(java.lang.String r6, java.lang.String r7, boolean r8, boolean r9) {
        /*
            r5 = this;
            if (r9 == 0) goto Lc
            java.lang.String r6 = r6.toLowerCase()
            java.lang.String r0 = r7.toLowerCase()
            r7 = r0
            goto L10
        Lc:
            java.lang.String r0 = cn.hutool.core.util.StrUtil.upperFirst(r7)
        L10:
            java.lang.String r1 = "get"
            boolean r2 = r6.startsWith(r1)
            r3 = 0
            java.lang.String r4 = "is"
            if (r2 != 0) goto L22
            boolean r2 = r6.startsWith(r4)
            if (r2 != 0) goto L22
            return r3
        L22:
            java.lang.String r2 = "getclass"
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L2b
            return r3
        L2b:
            if (r8 == 0) goto L7b
            boolean r2 = r7.startsWith(r4)
            r3 = 1
            if (r2 == 0) goto L65
            boolean r2 = r6.equals(r7)
            if (r2 != 0) goto L64
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            boolean r2 = r2.equals(r6)
            if (r2 != 0) goto L64
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r4)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L7b
        L64:
            return r3
        L65:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r4)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L7b
            return r3
        L7b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r1)
            r2.append(r0)
            java.lang.String r1 = r2.toString()
            boolean r1 = r1.equals(r6)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hutool.core.bean.BeanDesc.isMatchGetter(java.lang.String, java.lang.String, boolean, boolean):boolean");
    }

    private boolean isMatchSetter(String methodName, String fieldName, boolean isBooleanField, boolean ignoreCase) {
        String handledFieldName;
        if (ignoreCase) {
            methodName = methodName.toLowerCase();
            handledFieldName = fieldName.toLowerCase();
            fieldName = handledFieldName;
        } else {
            handledFieldName = StrUtil.upperFirst(fieldName);
        }
        if (!methodName.startsWith("set")) {
            return false;
        }
        if (isBooleanField && fieldName.startsWith("is")) {
            if (!("set" + StrUtil.removePrefix(fieldName, "is")).equals(methodName)) {
                if (("set" + handledFieldName).equals(methodName)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return ("set" + fieldName).equals(methodName);
    }
}
