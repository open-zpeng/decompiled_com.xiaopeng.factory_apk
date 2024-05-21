package cn.hutool.core.convert;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.impl.ArrayConverter;
import cn.hutool.core.convert.impl.AtomicBooleanConverter;
import cn.hutool.core.convert.impl.AtomicIntegerArrayConverter;
import cn.hutool.core.convert.impl.AtomicLongArrayConverter;
import cn.hutool.core.convert.impl.AtomicReferenceConverter;
import cn.hutool.core.convert.impl.BeanConverter;
import cn.hutool.core.convert.impl.BooleanConverter;
import cn.hutool.core.convert.impl.CalendarConverter;
import cn.hutool.core.convert.impl.CharacterConverter;
import cn.hutool.core.convert.impl.CharsetConverter;
import cn.hutool.core.convert.impl.ClassConverter;
import cn.hutool.core.convert.impl.CollectionConverter;
import cn.hutool.core.convert.impl.CurrencyConverter;
import cn.hutool.core.convert.impl.DateConverter;
import cn.hutool.core.convert.impl.DurationConverter;
import cn.hutool.core.convert.impl.EnumConverter;
import cn.hutool.core.convert.impl.LocaleConverter;
import cn.hutool.core.convert.impl.MapConverter;
import cn.hutool.core.convert.impl.NumberConverter;
import cn.hutool.core.convert.impl.OptionalConverter;
import cn.hutool.core.convert.impl.PathConverter;
import cn.hutool.core.convert.impl.PeriodConverter;
import cn.hutool.core.convert.impl.PrimitiveConverter;
import cn.hutool.core.convert.impl.ReferenceConverter;
import cn.hutool.core.convert.impl.StackTraceElementConverter;
import cn.hutool.core.convert.impl.StringConverter;
import cn.hutool.core.convert.impl.TemporalAccessorConverter;
import cn.hutool.core.convert.impl.TimeZoneConverter;
import cn.hutool.core.convert.impl.URIConverter;
import cn.hutool.core.convert.impl.URLConverter;
import cn.hutool.core.convert.impl.UUIDConverter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import cn.hutool.core.util.TypeUtil;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ConverterRegistry implements Serializable {
    private static final long serialVersionUID = 1;
    private volatile Map<Type, Converter<?>> customConverterMap;
    private Map<Type, Converter<?>> defaultConverterMap;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SingletonHolder {
        private static final ConverterRegistry INSTANCE = new ConverterRegistry();

        private SingletonHolder() {
        }
    }

    public static ConverterRegistry getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ConverterRegistry() {
        defaultConverter();
        putCustomBySpi();
    }

    private void putCustomBySpi() {
        ServiceLoaderUtil.load(Converter.class).forEach(new Consumer() { // from class: cn.hutool.core.convert.-$$Lambda$ConverterRegistry$Tr6i25ijoqIqbEkhSA1azNTG6JQ
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ConverterRegistry.this.lambda$putCustomBySpi$0$ConverterRegistry((Converter) obj);
            }
        });
    }

    public /* synthetic */ void lambda$putCustomBySpi$0$ConverterRegistry(Converter converter) {
        try {
            Type type = TypeUtil.getTypeArgument(ClassUtil.getClass(converter));
            if (type != null) {
                putCustom(type, converter);
            }
        } catch (Exception e) {
        }
    }

    public ConverterRegistry putCustom(Type type, Class<? extends Converter<?>> converterClass) {
        return putCustom(type, (Converter) ReflectUtil.newInstance(converterClass, new Object[0]));
    }

    public ConverterRegistry putCustom(Type type, Converter<?> converter) {
        if (this.customConverterMap == null) {
            synchronized (this) {
                if (this.customConverterMap == null) {
                    this.customConverterMap = new ConcurrentHashMap();
                }
            }
        }
        this.customConverterMap.put(type, converter);
        return this;
    }

    public <T> Converter<T> getConverter(Type type, boolean isCustomFirst) {
        if (isCustomFirst) {
            Converter<T> converter = getCustomConverter(type);
            if (converter == null) {
                return getDefaultConverter(type);
            }
            return converter;
        }
        Converter<T> converter2 = getDefaultConverter(type);
        if (converter2 == null) {
            return getCustomConverter(type);
        }
        return converter2;
    }

    public <T> Converter<T> getDefaultConverter(Type type) {
        Map<Type, Converter<?>> map = this.defaultConverterMap;
        if (map == null) {
            return null;
        }
        return (Converter<T>) map.get(type);
    }

    public <T> Converter<T> getCustomConverter(Type type) {
        if (this.customConverterMap == null) {
            return null;
        }
        return (Converter<T>) this.customConverterMap.get(type);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T convert(Type type, Object value, T defaultValue, boolean isCustomFirst) throws ConvertException {
        if (TypeUtil.isUnknown(type) && defaultValue == null) {
            return value;
        }
        if (ObjectUtil.isNull(value)) {
            return defaultValue;
        }
        if (TypeUtil.isUnknown(type)) {
            type = defaultValue.getClass();
        }
        if (type instanceof TypeReference) {
            type = ((TypeReference) type).getType();
        }
        Converter<T> converter = getConverter(type, isCustomFirst);
        if (converter != null) {
            return converter.convert(value, defaultValue);
        }
        Class<?> cls = TypeUtil.getClass(type);
        if (cls == null) {
            if (defaultValue != null) {
                cls = defaultValue.getClass();
            } else {
                return value;
            }
        }
        T result = (T) convertSpecial(type, cls, value, defaultValue);
        if (result != null) {
            return result;
        }
        if (BeanUtil.isBean(cls)) {
            return new BeanConverter(type).convert(value, defaultValue);
        }
        throw new ConvertException("Can not Converter from [{}] to [{}]", value.getClass().getName(), type.getTypeName());
    }

    public <T> T convert(Type type, Object value, T defaultValue) throws ConvertException {
        return (T) convert(type, value, defaultValue, true);
    }

    public <T> T convert(Type type, Object value) throws ConvertException {
        return (T) convert(type, value, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T convertSpecial(Type type, Class<T> rowType, Object value, T defaultValue) {
        if (rowType == null) {
            return null;
        }
        if (Collection.class.isAssignableFrom(rowType)) {
            CollectionConverter collectionConverter = new CollectionConverter(type);
            return (T) collectionConverter.convert(value, (Collection) defaultValue);
        } else if (Map.class.isAssignableFrom(rowType)) {
            MapConverter mapConverter = new MapConverter(type);
            return (T) mapConverter.convert(value, (Map) defaultValue);
        } else if (rowType.isInstance(value)) {
            return value;
        } else {
            if (rowType.isEnum()) {
                return (T) new EnumConverter(rowType).convert(value, defaultValue);
            }
            if (!rowType.isArray()) {
                return null;
            }
            ArrayConverter arrayConverter = new ArrayConverter(rowType);
            return (T) arrayConverter.convert(value, defaultValue);
        }
    }

    private ConverterRegistry defaultConverter() {
        this.defaultConverterMap = new ConcurrentHashMap();
        this.defaultConverterMap.put(Integer.TYPE, new PrimitiveConverter(Integer.TYPE));
        this.defaultConverterMap.put(Long.TYPE, new PrimitiveConverter(Long.TYPE));
        this.defaultConverterMap.put(Byte.TYPE, new PrimitiveConverter(Byte.TYPE));
        this.defaultConverterMap.put(Short.TYPE, new PrimitiveConverter(Short.TYPE));
        this.defaultConverterMap.put(Float.TYPE, new PrimitiveConverter(Float.TYPE));
        this.defaultConverterMap.put(Double.TYPE, new PrimitiveConverter(Double.TYPE));
        this.defaultConverterMap.put(Character.TYPE, new PrimitiveConverter(Character.TYPE));
        this.defaultConverterMap.put(Boolean.TYPE, new PrimitiveConverter(Boolean.TYPE));
        this.defaultConverterMap.put(Number.class, new NumberConverter());
        this.defaultConverterMap.put(Integer.class, new NumberConverter(Integer.class));
        this.defaultConverterMap.put(AtomicInteger.class, new NumberConverter(AtomicInteger.class));
        this.defaultConverterMap.put(Long.class, new NumberConverter(Long.class));
        this.defaultConverterMap.put(AtomicLong.class, new NumberConverter(AtomicLong.class));
        this.defaultConverterMap.put(Byte.class, new NumberConverter(Byte.class));
        this.defaultConverterMap.put(Short.class, new NumberConverter(Short.class));
        this.defaultConverterMap.put(Float.class, new NumberConverter(Float.class));
        this.defaultConverterMap.put(Double.class, new NumberConverter(Double.class));
        this.defaultConverterMap.put(Character.class, new CharacterConverter());
        this.defaultConverterMap.put(Boolean.class, new BooleanConverter());
        this.defaultConverterMap.put(AtomicBoolean.class, new AtomicBooleanConverter());
        this.defaultConverterMap.put(BigDecimal.class, new NumberConverter(BigDecimal.class));
        this.defaultConverterMap.put(BigInteger.class, new NumberConverter(BigInteger.class));
        this.defaultConverterMap.put(CharSequence.class, new StringConverter());
        this.defaultConverterMap.put(String.class, new StringConverter());
        this.defaultConverterMap.put(URI.class, new URIConverter());
        this.defaultConverterMap.put(URL.class, new URLConverter());
        this.defaultConverterMap.put(Calendar.class, new CalendarConverter());
        this.defaultConverterMap.put(Date.class, new DateConverter(Date.class));
        this.defaultConverterMap.put(DateTime.class, new DateConverter(DateTime.class));
        this.defaultConverterMap.put(java.sql.Date.class, new DateConverter(java.sql.Date.class));
        this.defaultConverterMap.put(Time.class, new DateConverter(Time.class));
        this.defaultConverterMap.put(Timestamp.class, new DateConverter(Timestamp.class));
        this.defaultConverterMap.put(TemporalAccessor.class, new TemporalAccessorConverter(Instant.class));
        this.defaultConverterMap.put(Instant.class, new TemporalAccessorConverter(Instant.class));
        this.defaultConverterMap.put(LocalDateTime.class, new TemporalAccessorConverter(LocalDateTime.class));
        this.defaultConverterMap.put(LocalDate.class, new TemporalAccessorConverter(LocalDate.class));
        this.defaultConverterMap.put(LocalTime.class, new TemporalAccessorConverter(LocalTime.class));
        this.defaultConverterMap.put(ZonedDateTime.class, new TemporalAccessorConverter(ZonedDateTime.class));
        this.defaultConverterMap.put(OffsetDateTime.class, new TemporalAccessorConverter(OffsetDateTime.class));
        this.defaultConverterMap.put(OffsetTime.class, new TemporalAccessorConverter(OffsetTime.class));
        this.defaultConverterMap.put(Period.class, new PeriodConverter());
        this.defaultConverterMap.put(Duration.class, new DurationConverter());
        this.defaultConverterMap.put(WeakReference.class, new ReferenceConverter(WeakReference.class));
        this.defaultConverterMap.put(SoftReference.class, new ReferenceConverter(SoftReference.class));
        this.defaultConverterMap.put(AtomicReference.class, new AtomicReferenceConverter());
        this.defaultConverterMap.put(AtomicIntegerArray.class, new AtomicIntegerArrayConverter());
        this.defaultConverterMap.put(AtomicLongArray.class, new AtomicLongArrayConverter());
        this.defaultConverterMap.put(Class.class, new ClassConverter());
        this.defaultConverterMap.put(TimeZone.class, new TimeZoneConverter());
        this.defaultConverterMap.put(Locale.class, new LocaleConverter());
        this.defaultConverterMap.put(Charset.class, new CharsetConverter());
        this.defaultConverterMap.put(Path.class, new PathConverter());
        this.defaultConverterMap.put(Currency.class, new CurrencyConverter());
        this.defaultConverterMap.put(UUID.class, new UUIDConverter());
        this.defaultConverterMap.put(StackTraceElement.class, new StackTraceElementConverter());
        this.defaultConverterMap.put(Optional.class, new OptionalConverter());
        return this;
    }
}
