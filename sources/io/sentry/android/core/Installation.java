package io.sentry.android.core;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
final class Installation {
    @TestOnly
    static final String INSTALLATION = "INSTALLATION";
    @TestOnly
    @Nullable
    static String deviceId = null;
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private Installation() {
    }

    public static synchronized String id(@NotNull Context context) throws RuntimeException {
        synchronized (Installation.class) {
            if (deviceId == null) {
                File installation = new File(context.getFilesDir(), INSTALLATION);
                if (!installation.exists()) {
                    deviceId = writeInstallationFile(installation);
                    return deviceId;
                }
                deviceId = readInstallationFile(installation);
            }
            return deviceId;
        }
    }

    @TestOnly
    @NotNull
    static String readInstallationFile(@NotNull File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        try {
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            String str = new String(bytes, UTF_8);
            f.close();
            return str;
        } catch (Throwable th) {
            try {
                f.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @TestOnly
    @NotNull
    static String writeInstallationFile(@NotNull File installation) throws IOException {
        OutputStream out = new FileOutputStream(installation);
        try {
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes(UTF_8));
            out.flush();
            out.close();
            return id;
        } catch (Throwable th) {
            try {
                out.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
