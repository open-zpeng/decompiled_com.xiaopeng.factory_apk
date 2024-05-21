package cn.hutool.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
/* loaded from: classes.dex */
public interface OptArrayTypeGetter {
    BigDecimal[] getBigDecimals(String str, BigDecimal[] bigDecimalArr);

    BigInteger[] getBigIntegers(String str, BigInteger[] bigIntegerArr);

    Boolean[] getBools(String str, Boolean[] boolArr);

    Byte[] getBytes(String str, Byte[] bArr);

    Character[] getChars(String str, Character[] chArr);

    Double[] getDoubles(String str, Double[] dArr);

    Integer[] getInts(String str, Integer[] numArr);

    Long[] getLongs(String str, Long[] lArr);

    Object[] getObjs(String str, Object[] objArr);

    Short[] getShorts(String str, Short[] shArr);

    String[] getStrs(String str, String[] strArr);
}
