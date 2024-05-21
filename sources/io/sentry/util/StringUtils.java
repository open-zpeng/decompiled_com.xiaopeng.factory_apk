package io.sentry.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class StringUtils {
    private StringUtils() {
    }

    @Nullable
    public static String getStringAfterDot(@Nullable String str) {
        if (str != null) {
            int lastDotIndex = str.lastIndexOf(".");
            if (lastDotIndex >= 0 && str.length() > lastDotIndex + 1) {
                return str.substring(lastDotIndex + 1);
            }
            return str;
        }
        return null;
    }

    @Nullable
    public static String capitalize(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1).toLowerCase(Locale.ROOT);
    }

    @Nullable
    public static String removeSurrounding(@Nullable String str, @Nullable String delimiter) {
        if (str != null && delimiter != null && str.startsWith(delimiter) && str.endsWith(delimiter)) {
            return str.substring(delimiter.length(), str.length() - delimiter.length());
        }
        return str;
    }

    @NotNull
    public static String byteCountToString(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (true) {
            if (bytes > -999950 && bytes < 999950) {
                return String.format(Locale.ROOT, "%.1f %cB", Double.valueOf(bytes / 1000.0d), Character.valueOf(ci.current()));
            }
            bytes /= 1000;
            ci.next();
        }
    }
}
