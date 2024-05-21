package io.sentry.android.core.internal.util;

import android.content.Context;
import android.content.pm.PackageManager;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.android.core.IBuildInfoProvider;
import io.sentry.util.Objects;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class RootChecker {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @NotNull
    private final IBuildInfoProvider buildInfoProvider;
    @NotNull
    private final Context context;
    @NotNull
    private final ILogger logger;
    @NotNull
    private final String[] rootFiles;
    @NotNull
    private final String[] rootPackages;
    @NotNull
    private final Runtime runtime;

    public RootChecker(@NotNull Context context, @NotNull IBuildInfoProvider buildInfoProvider, @NotNull ILogger logger) {
        this(context, buildInfoProvider, logger, new String[]{"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su", "/su/bin", "/system/xbin/daemonsu"}, new String[]{"com.devadvance.rootcloak", "com.devadvance.rootcloakplus", "com.koushikdutta.superuser", "com.thirdparty.superuser", "eu.chainfire.supersu", "com.noshufou.android.su"}, Runtime.getRuntime());
    }

    RootChecker(@NotNull Context context, @NotNull IBuildInfoProvider buildInfoProvider, @NotNull ILogger logger, @NotNull String[] rootFiles, @NotNull String[] rootPackages, @NotNull Runtime runtime) {
        this.context = (Context) Objects.requireNonNull(context, "The application context is required.");
        this.buildInfoProvider = (IBuildInfoProvider) Objects.requireNonNull(buildInfoProvider, "The BuildInfoProvider is required.");
        this.logger = (ILogger) Objects.requireNonNull(logger, "The Logger is required.");
        this.rootFiles = (String[]) Objects.requireNonNull(rootFiles, "The root Files are required.");
        this.rootPackages = (String[]) Objects.requireNonNull(rootPackages, "The root packages are required.");
        this.runtime = (Runtime) Objects.requireNonNull(runtime, "The Runtime is required.");
    }

    public boolean isDeviceRooted() {
        return checkTestKeys() || checkRootFiles() || checkSUExist() || checkRootPackages();
    }

    private boolean checkTestKeys() {
        String buildTags = this.buildInfoProvider.getBuildTags();
        return buildTags != null && buildTags.contains("test-keys");
    }

    private boolean checkRootFiles() {
        String[] strArr;
        for (String path : this.rootFiles) {
            try {
            } catch (RuntimeException e) {
                this.logger.log(SentryLevel.ERROR, e, "Error when trying to check if root file %s exists.", path);
            }
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSUExist() {
        Process process = null;
        String[] su = {"/system/xbin/which", "su"};
        try {
            try {
                Process process2 = this.runtime.exec(su);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process2.getInputStream(), UTF_8));
                try {
                    boolean z = reader.readLine() != null;
                    reader.close();
                    process2.destroy();
                    return z;
                } catch (Throwable th) {
                    try {
                        reader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e) {
                this.logger.log(SentryLevel.DEBUG, "SU isn't found on this Device.", new Object[0]);
                if (0 != 0) {
                    process.destroy();
                }
                return false;
            }
        }
    }

    private boolean checkRootPackages() {
        String[] strArr;
        PackageManager pm = this.context.getPackageManager();
        if (pm != null) {
            for (String pkg : this.rootPackages) {
                try {
                    pm.getPackageInfo(pkg, 0);
                    return true;
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
        }
        return false;
    }
}
