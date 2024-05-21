package cn.hutool.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
/* loaded from: classes.dex */
public interface BasicTypeGetter<K> {
    BigDecimal getBigDecimal(K k);

    BigInteger getBigInteger(K k);

    Boolean getBool(K k);

    Byte getByte(K k);

    Character getChar(K k);

    Date getDate(K k);

    Double getDouble(K k);

    <E extends Enum<E>> E getEnum(Class<E> cls, K k);

    Float getFloat(K k);

    Integer getInt(K k);

    Long getLong(K k);

    Object getObj(K k);

    Short getShort(K k);

    String getStr(K k);
}
