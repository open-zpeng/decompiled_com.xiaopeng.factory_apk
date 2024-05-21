package cn.hutool.core.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
/* loaded from: classes.dex */
public interface ListTypeGetter {
    List<BigDecimal> getBigDecimalList(String str);

    List<BigInteger> getBigIntegerList(String str);

    List<Boolean> getBoolList(String str);

    List<Byte> getByteList(String str);

    List<Character> getCharList(String str);

    List<Double> getDoubleList(String str);

    List<Integer> getIntList(String str);

    List<Long> getLongList(String str);

    List<Object> getObjList(String str);

    List<Short> getShortList(String str);

    List<String> getStrList(String str);
}
