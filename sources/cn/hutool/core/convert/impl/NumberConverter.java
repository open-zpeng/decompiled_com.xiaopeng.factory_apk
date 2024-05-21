package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
/* loaded from: classes.dex */
public class NumberConverter extends AbstractConverter<Number> {
    private static final long serialVersionUID = 1;
    private final Class<? extends Number> targetType;

    public NumberConverter() {
        this.targetType = Number.class;
    }

    public NumberConverter(Class<? extends Number> clazz) {
        this.targetType = clazz == null ? Number.class : clazz;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // cn.hutool.core.convert.AbstractConverter
    public Number convertInternal(Object value) {
        return convert(value, this.targetType, new Function() { // from class: cn.hutool.core.convert.impl.-$$Lambda$fdMhdkFo1kVpswgf49400ExdHBE
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return NumberConverter.this.convertToStr(obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Number convert(Object value, Class<?> targetType, Function<Object, String> toStrFunc) {
        if (value instanceof Enum) {
            return convert(Integer.valueOf(((Enum) value).ordinal()), targetType, toStrFunc);
        }
        if (Byte.class == targetType) {
            if (value instanceof Number) {
                return Byte.valueOf(((Number) value).byteValue());
            }
            if (value instanceof Boolean) {
                return BooleanUtil.toByteObj(((Boolean) value).booleanValue());
            }
            String valueStr = toStrFunc.apply(value);
            try {
                if (StrUtil.isBlank(valueStr)) {
                    return null;
                }
                return Byte.valueOf(valueStr);
            } catch (NumberFormatException e) {
                return Byte.valueOf(NumberUtil.parseNumber(valueStr).byteValue());
            }
        } else if (Short.class == targetType) {
            if (value instanceof Number) {
                return Short.valueOf(((Number) value).shortValue());
            }
            if (value instanceof Boolean) {
                return BooleanUtil.toShortObj(((Boolean) value).booleanValue());
            }
            if (value instanceof byte[]) {
                return Short.valueOf(ByteUtil.bytesToShort((byte[]) value));
            }
            String valueStr2 = toStrFunc.apply(value);
            try {
                if (StrUtil.isBlank(valueStr2)) {
                    return null;
                }
                return Short.valueOf(valueStr2);
            } catch (NumberFormatException e2) {
                return Short.valueOf(NumberUtil.parseNumber(valueStr2).shortValue());
            }
        } else if (Integer.class == targetType) {
            if (value instanceof Number) {
                return Integer.valueOf(((Number) value).intValue());
            }
            if (value instanceof Boolean) {
                return BooleanUtil.toInteger(((Boolean) value).booleanValue());
            }
            if (value instanceof Date) {
                return Integer.valueOf((int) ((Date) value).getTime());
            }
            if (value instanceof Calendar) {
                return Integer.valueOf((int) ((Calendar) value).getTimeInMillis());
            }
            if (value instanceof TemporalAccessor) {
                return Integer.valueOf((int) DateUtil.toInstant((TemporalAccessor) value).toEpochMilli());
            }
            if (value instanceof byte[]) {
                return Integer.valueOf(ByteUtil.bytesToInt((byte[]) value));
            }
            String valueStr3 = toStrFunc.apply(value);
            if (StrUtil.isBlank(valueStr3)) {
                return null;
            }
            return Integer.valueOf(NumberUtil.parseInt(valueStr3));
        } else {
            if (AtomicInteger.class == targetType) {
                Number number = convert(value, Integer.class, toStrFunc);
                if (number != null) {
                    AtomicInteger intValue = new AtomicInteger();
                    intValue.set(number.intValue());
                    return intValue;
                }
            } else if (Long.class == targetType) {
                if (value instanceof Number) {
                    return Long.valueOf(((Number) value).longValue());
                }
                if (value instanceof Boolean) {
                    return BooleanUtil.toLongObj(((Boolean) value).booleanValue());
                }
                if (value instanceof Date) {
                    return Long.valueOf(((Date) value).getTime());
                }
                if (value instanceof Calendar) {
                    return Long.valueOf(((Calendar) value).getTimeInMillis());
                }
                if (value instanceof TemporalAccessor) {
                    return Long.valueOf(DateUtil.toInstant((TemporalAccessor) value).toEpochMilli());
                }
                if (value instanceof byte[]) {
                    return Long.valueOf(ByteUtil.bytesToLong((byte[]) value));
                }
                String valueStr4 = toStrFunc.apply(value);
                if (StrUtil.isBlank(valueStr4)) {
                    return null;
                }
                return Long.valueOf(NumberUtil.parseLong(valueStr4));
            } else if (AtomicLong.class == targetType) {
                Number number2 = convert(value, Long.class, toStrFunc);
                if (number2 != null) {
                    AtomicLong longValue = new AtomicLong();
                    longValue.set(number2.longValue());
                    return longValue;
                }
            } else if (LongAdder.class == targetType) {
                Number number3 = convert(value, Long.class, toStrFunc);
                if (number3 != null) {
                    LongAdder longValue2 = new LongAdder();
                    longValue2.add(number3.longValue());
                    return longValue2;
                }
            } else if (Float.class == targetType) {
                if (value instanceof Number) {
                    return Float.valueOf(((Number) value).floatValue());
                }
                if (value instanceof Boolean) {
                    return BooleanUtil.toFloatObj(((Boolean) value).booleanValue());
                }
                if (value instanceof byte[]) {
                    return Float.valueOf((float) ByteUtil.bytesToDouble((byte[]) value));
                }
                String valueStr5 = toStrFunc.apply(value);
                if (StrUtil.isBlank(valueStr5)) {
                    return null;
                }
                return Float.valueOf(NumberUtil.parseFloat(valueStr5));
            } else if (Double.class == targetType) {
                if (value instanceof Number) {
                    return Double.valueOf(((Number) value).doubleValue());
                }
                if (value instanceof Boolean) {
                    return BooleanUtil.toDoubleObj(((Boolean) value).booleanValue());
                }
                if (value instanceof byte[]) {
                    return Double.valueOf(ByteUtil.bytesToDouble((byte[]) value));
                }
                String valueStr6 = toStrFunc.apply(value);
                if (StrUtil.isBlank(valueStr6)) {
                    return null;
                }
                return Double.valueOf(NumberUtil.parseDouble(valueStr6));
            } else if (DoubleAdder.class == targetType) {
                Number number4 = convert(value, Long.class, toStrFunc);
                if (number4 != null) {
                    DoubleAdder doubleAdder = new DoubleAdder();
                    doubleAdder.add(number4.doubleValue());
                    return doubleAdder;
                }
            } else if (BigDecimal.class == targetType) {
                return toBigDecimal(value, toStrFunc);
            } else {
                if (BigInteger.class == targetType) {
                    return toBigInteger(value, toStrFunc);
                }
                if (Number.class == targetType) {
                    if (value instanceof Number) {
                        return (Number) value;
                    }
                    if (value instanceof Boolean) {
                        return BooleanUtil.toInteger(((Boolean) value).booleanValue());
                    }
                    String valueStr7 = toStrFunc.apply(value);
                    if (StrUtil.isBlank(valueStr7)) {
                        return null;
                    }
                    return NumberUtil.parseNumber(valueStr7);
                }
            }
            throw new UnsupportedOperationException(StrUtil.format("Unsupport Number type: {}", targetType.getName()));
        }
    }

    private static BigDecimal toBigDecimal(Object value, Function<Object, String> toStrFunc) {
        if (value instanceof Number) {
            return NumberUtil.toBigDecimal((Number) value);
        }
        if (value instanceof Boolean) {
            return new BigDecimal(((Boolean) value).booleanValue() ? 1 : 0);
        }
        if (value instanceof byte[]) {
            return NumberUtil.toBigDecimal(Double.valueOf(ByteUtil.bytesToDouble((byte[]) value)));
        }
        return NumberUtil.toBigDecimal(toStrFunc.apply(value));
    }

    private static BigInteger toBigInteger(Object value, Function<Object, String> toStrFunc) {
        if (value instanceof Long) {
            return BigInteger.valueOf(((Long) value).longValue());
        }
        if (value instanceof Boolean) {
            return BigInteger.valueOf(((Boolean) value).booleanValue() ? serialVersionUID : 0L);
        } else if (value instanceof byte[]) {
            return BigInteger.valueOf(ByteUtil.bytesToLong((byte[]) value));
        } else {
            return NumberUtil.toBigInteger(toStrFunc.apply(value));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public String convertToStr(Object value) {
        char c;
        String result = StrUtil.trim(super.convertToStr(value));
        if (StrUtil.isNotEmpty(result) && ((c = Character.toUpperCase(result.charAt(result.length() - 1))) == 'D' || c == 'L' || c == 'F')) {
            return StrUtil.subPre(result, -1);
        }
        return result;
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<Number> getTargetType() {
        return this.targetType;
    }
}
