package io.sentry.android.ndk;
/* loaded from: classes2.dex */
final class NativeScope implements INativeScope {
    public static native void nativeAddBreadcrumb(String str, String str2, String str3, String str4, String str5, String str6);

    public static native void nativeRemoveExtra(String str);

    public static native void nativeRemoveTag(String str);

    public static native void nativeRemoveUser();

    public static native void nativeSetExtra(String str, String str2);

    public static native void nativeSetTag(String str, String str2);

    public static native void nativeSetUser(String str, String str2, String str3, String str4);

    @Override // io.sentry.android.ndk.INativeScope
    public void setTag(String key, String value) {
        nativeSetTag(key, value);
    }

    @Override // io.sentry.android.ndk.INativeScope
    public void removeTag(String key) {
        nativeRemoveTag(key);
    }

    @Override // io.sentry.android.ndk.INativeScope
    public void setExtra(String key, String value) {
        nativeSetExtra(key, value);
    }

    @Override // io.sentry.android.ndk.INativeScope
    public void removeExtra(String key) {
        nativeRemoveExtra(key);
    }

    @Override // io.sentry.android.ndk.INativeScope
    public void setUser(String id, String email, String ipAddress, String username) {
        nativeSetUser(id, email, ipAddress, username);
    }

    @Override // io.sentry.android.ndk.INativeScope
    public void removeUser() {
        nativeRemoveUser();
    }

    @Override // io.sentry.android.ndk.INativeScope
    public void addBreadcrumb(String level, String message, String category, String type, String timestamp, String data) {
        nativeAddBreadcrumb(level, message, category, type, timestamp, data);
    }
}
