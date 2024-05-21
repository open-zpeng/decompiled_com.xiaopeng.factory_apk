package cn.hutool.core.bean;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import java.io.Serializable;
import java.util.Map;
/* loaded from: classes.dex */
public class DynaBean extends CloneSupport<DynaBean> implements Serializable {
    private static final long serialVersionUID = 1;
    private final Object bean;
    private final Class<?> beanClass;

    public static DynaBean create(Object bean) {
        return new DynaBean(bean);
    }

    public static DynaBean create(Class<?> beanClass) {
        return new DynaBean(beanClass);
    }

    public static DynaBean create(Class<?> beanClass, Object... params) {
        return new DynaBean(beanClass, params);
    }

    public DynaBean(Class<?> beanClass, Object... params) {
        this(ReflectUtil.newInstance(beanClass, params));
    }

    public DynaBean(Class<?> beanClass) {
        this(ReflectUtil.newInstance(beanClass, new Object[0]));
    }

    public DynaBean(Object bean) {
        Assert.notNull(bean);
        bean = bean instanceof DynaBean ? ((DynaBean) bean).getBean() : bean;
        this.bean = bean;
        this.beanClass = ClassUtil.getClass(bean);
    }

    public <T> T get(String fieldName) throws BeanException {
        if (Map.class.isAssignableFrom(this.beanClass)) {
            return (T) ((Map) this.bean).get(fieldName);
        }
        PropDesc prop = BeanUtil.getBeanDesc(this.beanClass).getProp(fieldName);
        if (prop == null) {
            throw new BeanException("No public field or get method for {}", fieldName);
        }
        return (T) prop.getValue(this.bean);
    }

    public boolean containsProp(String fieldName) {
        return BeanUtil.getBeanDesc(this.beanClass).getProp(fieldName) != null;
    }

    public <T> T safeGet(String fieldName) {
        try {
            return (T) get(fieldName);
        } catch (Exception e) {
            return null;
        }
    }

    public void set(String fieldName, Object value) throws BeanException {
        if (Map.class.isAssignableFrom(this.beanClass)) {
            ((Map) this.bean).put(fieldName, value);
            return;
        }
        PropDesc prop = BeanUtil.getBeanDesc(this.beanClass).getProp(fieldName);
        if (prop == null) {
            throw new BeanException("No public field or set method for {}", fieldName);
        }
        prop.setValue(this.bean, value);
    }

    public Object invoke(String methodName, Object... params) {
        return ReflectUtil.invoke(this.bean, methodName, params);
    }

    public <T> T getBean() {
        return (T) this.bean;
    }

    public <T> Class<T> getBeanClass() {
        return (Class<T>) this.beanClass;
    }

    public int hashCode() {
        int i = 1 * 31;
        Object obj = this.bean;
        int result = i + (obj == null ? 0 : obj.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DynaBean other = (DynaBean) obj;
        Object obj2 = this.bean;
        if (obj2 == null) {
            if (other.bean == null) {
                return true;
            }
            return false;
        }
        return obj2.equals(other.bean);
    }

    public String toString() {
        return this.bean.toString();
    }
}
