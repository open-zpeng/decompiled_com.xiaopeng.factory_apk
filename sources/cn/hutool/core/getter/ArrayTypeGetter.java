package cn.hutool.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
/* loaded from: classes.dex */
public interface ArrayTypeGetter {
    BigDecimal[] getBigDecimals(String str);

    BigInteger[] getBigIntegers(String str);

    Boolean[] getBools(String str);

    Byte[] getBytes(String str);

    Character[] getChars(String str);

    Double[] getDoubles(String str);

    Integer[] getInts(String str);

    Long[] getLongs(String str);

    String[] getObjs(String str);

    Short[] getShorts(String str);

    String[] getStrs(String str);
}
