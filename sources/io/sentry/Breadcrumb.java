package io.sentry;

import androidx.core.app.NotificationCompat;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.utils.ProcessUtil;
import io.sentry.util.CollectionUtils;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class Breadcrumb implements IUnknownPropertiesConsumer {
    @Nullable
    private String category;
    @NotNull
    private Map<String, Object> data;
    @Nullable
    private SentryLevel level;
    @Nullable
    private String message;
    @NotNull
    private final Date timestamp;
    @Nullable
    private String type;
    @Nullable
    private Map<String, Object> unknown;

    public Breadcrumb(@NotNull Date timestamp) {
        this.data = new ConcurrentHashMap();
        this.timestamp = timestamp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Breadcrumb(@NotNull Breadcrumb breadcrumb) {
        this.data = new ConcurrentHashMap();
        this.timestamp = breadcrumb.timestamp;
        this.message = breadcrumb.message;
        this.type = breadcrumb.type;
        this.category = breadcrumb.category;
        Map<String, Object> dataClone = CollectionUtils.newConcurrentHashMap(breadcrumb.data);
        if (dataClone != null) {
            this.data = dataClone;
        }
        this.unknown = CollectionUtils.newConcurrentHashMap(breadcrumb.unknown);
        this.level = breadcrumb.level;
    }

    @NotNull
    public static Breadcrumb http(@NotNull String url, @NotNull String method) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType("http");
        breadcrumb.setCategory("http");
        breadcrumb.setData(Progress.URL, url);
        breadcrumb.setData("method", method.toUpperCase(Locale.ROOT));
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb http(@NotNull String url, @NotNull String method, @Nullable Integer code) {
        Breadcrumb breadcrumb = http(url, method);
        if (code != null) {
            breadcrumb.setData("status_code", code);
        }
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb navigation(@NotNull String from, @NotNull String to) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setCategory(NotificationCompat.CATEGORY_NAVIGATION);
        breadcrumb.setType(NotificationCompat.CATEGORY_NAVIGATION);
        breadcrumb.setData("from", from);
        breadcrumb.setData("to", to);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb transaction(@NotNull String message) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType("default");
        breadcrumb.setCategory("sentry.transaction");
        breadcrumb.setMessage(message);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb debug(@NotNull String message) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType(com.xiaopeng.lib.framework.ipcmodule.BuildConfig.BUILD_TYPE);
        breadcrumb.setMessage(message);
        breadcrumb.setLevel(SentryLevel.DEBUG);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb error(@NotNull String message) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType(ProcessUtil.RESULT_ERROR);
        breadcrumb.setMessage(message);
        breadcrumb.setLevel(SentryLevel.ERROR);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb info(@NotNull String message) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType("info");
        breadcrumb.setMessage(message);
        breadcrumb.setLevel(SentryLevel.INFO);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb query(@NotNull String message) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType("query");
        breadcrumb.setMessage(message);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb ui(@NotNull String category, @NotNull String message) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType("default");
        breadcrumb.setCategory("ui." + category);
        breadcrumb.setMessage(message);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb user(@NotNull String category, @NotNull String message) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType(Constant.BUILD_TYPE_USER);
        breadcrumb.setCategory(category);
        breadcrumb.setMessage(message);
        return breadcrumb;
    }

    @NotNull
    public static Breadcrumb userInteraction(@NotNull String subCategory, @Nullable String viewId, @Nullable String viewClass) {
        return userInteraction(subCategory, viewId, viewClass, Collections.emptyMap());
    }

    @NotNull
    public static Breadcrumb userInteraction(@NotNull String subCategory, @Nullable String viewId, @Nullable String viewClass, @NotNull Map<String, Object> additionalData) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType(Constant.BUILD_TYPE_USER);
        breadcrumb.setCategory("ui." + subCategory);
        if (viewId != null) {
            breadcrumb.setData("view.id", viewId);
        }
        if (viewClass != null) {
            breadcrumb.setData("view.class", viewClass);
        }
        for (Map.Entry<String, Object> entry : additionalData.entrySet()) {
            breadcrumb.getData().put(entry.getKey(), entry.getValue());
        }
        breadcrumb.setLevel(SentryLevel.INFO);
        return breadcrumb;
    }

    public Breadcrumb() {
        this(DateUtils.getCurrentDateTime());
    }

    public Breadcrumb(@Nullable String message) {
        this();
        this.message = message;
    }

    @NotNull
    public Date getTimestamp() {
        return (Date) this.timestamp.clone();
    }

    @Nullable
    public String getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public String getType() {
        return this.type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @ApiStatus.Internal
    @NotNull
    public Map<String, Object> getData() {
        return this.data;
    }

    @Nullable
    public Object getData(@NotNull String key) {
        return this.data.get(key);
    }

    public void setData(@NotNull String key, @NotNull Object value) {
        this.data.put(key, value);
    }

    public void removeData(@NotNull String key) {
        this.data.remove(key);
    }

    @Nullable
    public String getCategory() {
        return this.category;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    @Nullable
    public SentryLevel getLevel() {
        return this.level;
    }

    public void setLevel(@Nullable SentryLevel level) {
        this.level = level;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = new ConcurrentHashMap(unknown);
    }

    @TestOnly
    @Nullable
    Map<String, Object> getUnknown() {
        return this.unknown;
    }
}
