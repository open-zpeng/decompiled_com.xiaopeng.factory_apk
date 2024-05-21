package cn.hutool.core.lang;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.getter.BasicTypeGetter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public class Dict extends LinkedHashMap<String, Object> implements BasicTypeGetter<String> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final long serialVersionUID = 6135423866861206530L;
    private boolean caseInsensitive;

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public /* bridge */ /* synthetic */ Enum getEnum(Class cls, String str) {
        return getEnum2((Class<Enum>) cls, str);
    }

    public static Dict create() {
        return new Dict();
    }

    public static <T> Dict parse(T bean) {
        return create().parseBean(bean);
    }

    @SafeVarargs
    public static Dict of(Pair<String, Object>... pairs) {
        Dict dict = create();
        for (Pair<String, Object> pair : pairs) {
            dict.put(pair.getKey(), pair.getValue());
        }
        return dict;
    }

    public static Dict of(Object... keysAndValues) {
        Dict dict = create();
        String key = null;
        for (int i = 0; i < keysAndValues.length; i++) {
            if (i % 2 == 0) {
                key = Convert.toStr(keysAndValues[i]);
            } else {
                dict.put(key, keysAndValues[i]);
            }
        }
        return dict;
    }

    public Dict() {
        this(false);
    }

    public Dict(boolean caseInsensitive) {
        this(16, caseInsensitive);
    }

    public Dict(int initialCapacity) {
        this(initialCapacity, false);
    }

    public Dict(int initialCapacity, boolean caseInsensitive) {
        this(initialCapacity, 0.75f, caseInsensitive);
    }

    public Dict(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, false);
    }

    public Dict(int initialCapacity, float loadFactor, boolean caseInsensitive) {
        super(initialCapacity, loadFactor);
        this.caseInsensitive = caseInsensitive;
    }

    public Dict(Map<String, Object> m) {
        super(m == null ? new HashMap<>() : m);
    }

    public <T> T toBean(T bean) {
        return (T) toBean(bean, false);
    }

    public <T> T toBeanIgnoreCase(T bean) {
        BeanUtil.fillBeanWithMapIgnoreCase(this, bean, false);
        return bean;
    }

    public <T> T toBean(T bean, boolean isToCamelCase) {
        BeanUtil.fillBeanWithMap((Map<?, ?>) this, (Object) bean, isToCamelCase, false);
        return bean;
    }

    public <T> T toBeanWithCamelCase(T bean) {
        BeanUtil.fillBeanWithMap((Map<?, ?>) this, (Object) bean, true, false);
        return bean;
    }

    public <T> T toBean(Class<T> clazz) {
        return (T) BeanUtil.toBean(this, clazz);
    }

    public <T> T toBeanIgnoreCase(Class<T> clazz) {
        return (T) BeanUtil.toBeanIgnoreCase(this, clazz, false);
    }

    public <T> Dict parseBean(T bean) {
        Assert.notNull(bean, "Bean class must be not null", new Object[0]);
        putAll(BeanUtil.beanToMap(bean));
        return this;
    }

    public <T> Dict parseBean(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
        Assert.notNull(bean, "Bean class must be not null", new Object[0]);
        putAll(BeanUtil.beanToMap(bean, isToUnderlineCase, ignoreNullValue));
        return this;
    }

    public <T extends Dict> void removeEqual(T dict, String... withoutNames) {
        Object value;
        HashSet<String> withoutSet = CollUtil.newHashSet(withoutNames);
        for (Map.Entry<String, Object> entry : dict.entrySet()) {
            if (!withoutSet.contains(entry.getKey()) && (value = get(entry.getKey())) != null && value.equals(entry.getValue())) {
                remove(entry.getKey());
            }
        }
    }

    public Dict filter(String... keys) {
        Dict result = new Dict(keys.length, 1.0f);
        for (String key : keys) {
            if (containsKey(key)) {
                result.put(key, get(key));
            }
        }
        return result;
    }

    public Dict set(String attr, Object value) {
        put(attr, value);
        return this;
    }

    public Dict setIgnoreNull(String attr, Object value) {
        if (attr != null && value != null) {
            set(attr, value);
        }
        return this;
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Object getObj(String key) {
        return super.get(key);
    }

    public <T> T getBean(String attr) {
        return (T) get(attr, null);
    }

    public <T> T get(String attr, T defaultValue) {
        T t = (T) get(attr);
        return t != null ? t : defaultValue;
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public String getStr(String attr) {
        return Convert.toStr(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Integer getInt(String attr) {
        return Convert.toInt(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Long getLong(String attr) {
        return Convert.toLong(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Float getFloat(String attr) {
        return Convert.toFloat(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Short getShort(String attr) {
        return Convert.toShort(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Character getChar(String attr) {
        return Convert.toChar(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Double getDouble(String attr) {
        return Convert.toDouble(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Byte getByte(String attr) {
        return Convert.toByte(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Boolean getBool(String attr) {
        return Convert.toBool(get(attr), null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public BigDecimal getBigDecimal(String attr) {
        return Convert.toBigDecimal(get(attr));
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public BigInteger getBigInteger(String attr) {
        return Convert.toBigInteger(get(attr));
    }

    /* renamed from: getEnum  reason: avoid collision after fix types in other method */
    public <E extends Enum<E>> E getEnum2(Class<E> clazz, String key) {
        return (E) Convert.toEnum(clazz, get(key));
    }

    public byte[] getBytes(String attr) {
        return (byte[]) get(attr, null);
    }

    @Override // cn.hutool.core.getter.BasicTypeGetter
    public Date getDate(String attr) {
        return (Date) get(attr, null);
    }

    public Time getTime(String attr) {
        return (Time) get(attr, null);
    }

    public Timestamp getTimestamp(String attr) {
        return (Timestamp) get(attr, null);
    }

    public Number getNumber(String attr) {
        return (Number) get(attr, null);
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        return super.get(customKey((String) key));
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        return super.put((Dict) customKey(key), (String) value);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends String, ?> m) {
        m.forEach(new BiConsumer() { // from class: cn.hutool.core.lang.-$$Lambda$gigxxd4y1FyBmqze3tlJaJZQkjU
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                Dict.this.put((String) obj, obj2);
            }
        });
    }

    @Override // java.util.HashMap, java.util.AbstractMap
    public Dict clone() {
        return (Dict) super.clone();
    }

    private String customKey(String key) {
        if (this.caseInsensitive && key != null) {
            return key.toLowerCase();
        }
        return key;
    }
}
