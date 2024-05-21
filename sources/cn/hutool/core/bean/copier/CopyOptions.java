package cn.hutool.core.bean.copier;

import cn.hutool.core.lang.Editor;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiPredicate;
/* loaded from: classes.dex */
public class CopyOptions implements Serializable {
    private static final long serialVersionUID = 1;
    protected Class<?> editable;
    protected Map<String, String> fieldMapping;
    protected Editor<String> fieldNameEditor;
    protected boolean ignoreCase;
    protected boolean ignoreError;
    protected boolean ignoreNullValue;
    protected String[] ignoreProperties;
    protected BiPredicate<Field, Object> propertiesFilter;
    private Map<String, String> reversedFieldMapping;
    private boolean transientSupport;

    public static CopyOptions create() {
        return new CopyOptions();
    }

    public static CopyOptions create(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
        return new CopyOptions(editable, ignoreNullValue, ignoreProperties);
    }

    public CopyOptions() {
        this.transientSupport = true;
    }

    public CopyOptions(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
        this.transientSupport = true;
        this.propertiesFilter = new BiPredicate() { // from class: cn.hutool.core.bean.copier.-$$Lambda$CopyOptions$TDulYvlqMOxSENt6XQjAaBGMZtg
            @Override // java.util.function.BiPredicate
            public final boolean test(Object obj, Object obj2) {
                return CopyOptions.lambda$new$0((Field) obj, obj2);
            }
        };
        this.editable = editable;
        this.ignoreNullValue = ignoreNullValue;
        this.ignoreProperties = ignoreProperties;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$new$0(Field f, Object v) {
        return true;
    }

    public CopyOptions setEditable(Class<?> editable) {
        this.editable = editable;
        return this;
    }

    public CopyOptions setIgnoreNullValue(boolean ignoreNullVall) {
        this.ignoreNullValue = ignoreNullVall;
        return this;
    }

    public CopyOptions ignoreNullValue() {
        return setIgnoreNullValue(true);
    }

    public CopyOptions setPropertiesFilter(BiPredicate<Field, Object> propertiesFilter) {
        this.propertiesFilter = propertiesFilter;
        return this;
    }

    public CopyOptions setIgnoreProperties(String... ignoreProperties) {
        this.ignoreProperties = ignoreProperties;
        return this;
    }

    public CopyOptions setIgnoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
        return this;
    }

    public CopyOptions ignoreError() {
        return setIgnoreError(true);
    }

    public CopyOptions setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        return this;
    }

    public CopyOptions ignoreCase() {
        return setIgnoreCase(true);
    }

    public CopyOptions setFieldMapping(Map<String, String> fieldMapping) {
        this.fieldMapping = fieldMapping;
        return this;
    }

    public CopyOptions setFieldNameEditor(Editor<String> fieldNameEditor) {
        this.fieldNameEditor = fieldNameEditor;
        return this;
    }

    public boolean isTransientSupport() {
        return this.transientSupport;
    }

    public CopyOptions setTransientSupport(boolean transientSupport) {
        this.transientSupport = transientSupport;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getMappedFieldName(String fieldName, boolean reversed) {
        Map<String, String> mapping = reversed ? getReversedMapping() : this.fieldMapping;
        if (MapUtil.isEmpty(mapping)) {
            return fieldName;
        }
        return (String) ObjectUtil.defaultIfNull(mapping.get(fieldName), fieldName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String editFieldName(String fieldName) {
        Editor<String> editor = this.fieldNameEditor;
        return editor != null ? editor.edit(fieldName) : fieldName;
    }

    private Map<String, String> getReversedMapping() {
        Map<String, String> map = this.fieldMapping;
        if (map == null) {
            return null;
        }
        if (this.reversedFieldMapping == null) {
            this.reversedFieldMapping = MapUtil.reverse(map);
        }
        return this.reversedFieldMapping;
    }
}
