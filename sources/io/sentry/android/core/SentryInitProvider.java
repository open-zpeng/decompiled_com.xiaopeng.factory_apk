package io.sentry.android.core;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryInitProvider extends ContentProvider {
    @Override // android.content.ContentProvider
    public boolean onCreate() {
        AndroidLogger logger = new AndroidLogger();
        Context context = getContext();
        if (context == null) {
            logger.log(SentryLevel.FATAL, "App. Context from ContentProvider is null", new Object[0]);
            return false;
        } else if (ManifestMetadataReader.isAutoInit(context, logger)) {
            SentryAndroid.init(context, logger);
            return true;
        } else {
            return true;
        }
    }

    @Override // android.content.ContentProvider
    public void shutdown() {
        Sentry.close();
    }

    @Override // android.content.ContentProvider
    public void attachInfo(@NotNull Context context, @NotNull ProviderInfo info) {
        if (SentryInitProvider.class.getName().equals(info.authority)) {
            throw new IllegalStateException("An applicationId is required to fulfill the manifest placeholder.");
        }
        super.attachInfo(context, info);
    }

    @Override // android.content.ContentProvider
    @Nullable
    public Cursor query(@NotNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public String getType(@NotNull Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    @Nullable
    public Uri insert(@NotNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(@NotNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(@NotNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
