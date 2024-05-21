package cn.hutool.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
/* loaded from: classes.dex */
public interface OptBasicTypeGetter<K> {
    BigDecimal getBigDecimal(K k, BigDecimal bigDecimal);

    BigInteger getBigInteger(K k, BigInteger bigInteger);

    Boolean getBool(K k, Boolean bool);

    Byte getByte(K k, Byte b);

    Character getChar(K k, Character ch2);

    Date getDate(K k, Date date);

    Double getDouble(K k, Double d);

    <E extends Enum<E>> E getEnum(Class<E> cls, K k, E e);

    Float getFloat(K k, Float f);

    Integer getInt(K k, Integer num);

    Long getLong(K k, Long l);

    Object getObj(K k, Object obj);

    Short getShort(K k, Short sh);

    String getStr(K k, String str);
}
