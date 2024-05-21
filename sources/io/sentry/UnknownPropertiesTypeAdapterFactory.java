package io.sentry;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class UnknownPropertiesTypeAdapterFactory implements TypeAdapterFactory {
    private static final TypeAdapterFactory instance = new UnknownPropertiesTypeAdapterFactory();

    private UnknownPropertiesTypeAdapterFactory() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TypeAdapterFactory get() {
        return instance;
    }

    @Override // com.google.gson.TypeAdapterFactory
    @Nullable
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (!IUnknownPropertiesConsumer.class.isAssignableFrom(typeToken.getRawType())) {
            return null;
        }
        Class<? super T> rawType = typeToken.getRawType();
        TypeAdapter<IUnknownPropertiesConsumer> delegateTypeAdapter = gson.getDelegateAdapter(this, typeToken);
        Excluder excluder = gson.excluder();
        FieldNamingStrategy fieldNamingStrategy = gson.fieldNamingStrategy();
        return UnknownPropertiesTypeAdapter.create(rawType, delegateTypeAdapter, excluder, fieldNamingStrategy);
    }

    /* loaded from: classes2.dex */
    private static final class UnknownPropertiesTypeAdapter<T extends IUnknownPropertiesConsumer> extends TypeAdapter<T> {
        private final Collection<String> propertyNames;
        private final TypeAdapter<T> typeAdapter;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.gson.TypeAdapter
        public /* bridge */ /* synthetic */ void write(JsonWriter jsonWriter, Object obj) throws IOException {
            write(jsonWriter, (JsonWriter) ((IUnknownPropertiesConsumer) obj));
        }

        private UnknownPropertiesTypeAdapter(TypeAdapter<T> typeAdapter, Collection<String> propertyNames) {
            this.typeAdapter = typeAdapter;
            this.propertyNames = propertyNames;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static <T extends IUnknownPropertiesConsumer> TypeAdapter<T> create(Class<? super T> clazz, TypeAdapter<T> typeAdapter, Excluder excluder, FieldNamingStrategy fieldNamingStrategy) {
            Collection<String> propertyNames = getPropertyNames(clazz, excluder, fieldNamingStrategy);
            return new UnknownPropertiesTypeAdapter(typeAdapter, propertyNames);
        }

        private static Collection<String> getPropertyNames(Class<?> clazz, Excluder excluder, FieldNamingStrategy fieldNamingStrategy) {
            Field[] declaredFields;
            Collection<String> propertyNames = new ArrayList<>();
            for (Class<?> i = clazz; i.getSuperclass() != null && i != Object.class; i = i.getSuperclass()) {
                for (Field declaredField : i.getDeclaredFields()) {
                    if (!excluder.excludeField(declaredField, false)) {
                        String propertyName = fieldNamingStrategy.translateName(declaredField);
                        propertyNames.add(propertyName);
                    }
                }
            }
            return propertyNames;
        }

        public void write(JsonWriter out, T value) throws IOException {
            this.typeAdapter.write(out, value);
        }

        @Override // com.google.gson.TypeAdapter
        @Nullable
        public T read(JsonReader in) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(in);
            if (jsonElement == null || jsonElement.isJsonNull()) {
                return null;
            }
            JsonObject jsonObjectToParse = jsonElement.getAsJsonObject();
            Map<String, Object> unknownProperties = new HashMap<>();
            for (Map.Entry<String, JsonElement> e : jsonObjectToParse.entrySet()) {
                String propertyName = e.getKey();
                if (!this.propertyNames.contains(propertyName)) {
                    unknownProperties.put(propertyName, e.getValue());
                }
            }
            T object = this.typeAdapter.fromJsonTree(jsonObjectToParse);
            if (!unknownProperties.isEmpty()) {
                object.acceptUnknownProperties(unknownProperties);
            }
            return object;
        }
    }
}
