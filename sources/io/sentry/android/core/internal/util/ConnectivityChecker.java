package io.sentry.android.core.internal.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.android.core.IBuildInfoProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class ConnectivityChecker {

    /* loaded from: classes2.dex */
    public enum Status {
        CONNECTED,
        NOT_CONNECTED,
        NO_PERMISSION,
        UNKNOWN
    }

    private ConnectivityChecker() {
    }

    @NotNull
    public static Status getConnectionStatus(@NotNull Context context, @NotNull ILogger logger) {
        ConnectivityManager connectivityManager = getConnectivityManager(context, logger);
        if (connectivityManager == null) {
            return Status.UNKNOWN;
        }
        return getConnectionStatus(context, connectivityManager, logger);
    }

    @NotNull
    private static Status getConnectionStatus(@NotNull Context context, @NotNull ConnectivityManager connectivityManager, @NotNull ILogger logger) {
        if (!Permissions.hasPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
            logger.log(SentryLevel.INFO, "No permission (ACCESS_NETWORK_STATE) to check network status.", new Object[0]);
            return Status.NO_PERMISSION;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected() ? Status.CONNECTED : Status.NOT_CONNECTED;
        }
        logger.log(SentryLevel.INFO, "NetworkInfo is null, there's no active network.", new Object[0]);
        return Status.NOT_CONNECTED;
    }

    @SuppressLint({"ObsoleteSdkInt", "MissingPermission", "NewApi"})
    @Nullable
    public static String getConnectionType(@NotNull Context context, @NotNull ILogger logger, @NotNull IBuildInfoProvider buildInfoProvider) {
        ConnectivityManager connectivityManager = getConnectivityManager(context, logger);
        if (connectivityManager == null) {
            return null;
        }
        if (!Permissions.hasPermission(context, "android.permission.ACCESS_NETWORK_STATE")) {
            logger.log(SentryLevel.INFO, "No permission (ACCESS_NETWORK_STATE) to check network status.", new Object[0]);
            return null;
        }
        boolean ethernet = false;
        boolean wifi = false;
        boolean cellular = false;
        if (buildInfoProvider.getSdkInfoVersion() >= 23) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                logger.log(SentryLevel.INFO, "Network is null and cannot check network status", new Object[0]);
                return null;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null) {
                logger.log(SentryLevel.INFO, "NetworkCapabilities is null and cannot check network type", new Object[0]);
                return null;
            }
            if (networkCapabilities.hasTransport(3)) {
                ethernet = true;
            }
            if (networkCapabilities.hasTransport(1)) {
                wifi = true;
            }
            if (networkCapabilities.hasTransport(0)) {
                cellular = true;
            }
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                logger.log(SentryLevel.INFO, "NetworkInfo is null, there's no active network.", new Object[0]);
                return null;
            }
            int type = activeNetworkInfo.getType();
            if (type == 0) {
                cellular = true;
            } else if (type == 1) {
                wifi = true;
            } else if (type == 9) {
                ethernet = true;
            }
        }
        if (ethernet) {
            return "ethernet";
        }
        if (wifi) {
            return "wifi";
        }
        if (!cellular) {
            return null;
        }
        return "cellular";
    }

    @Nullable
    private static ConnectivityManager getConnectivityManager(@NotNull Context context, @NotNull ILogger logger) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            logger.log(SentryLevel.INFO, "ConnectivityManager is null and cannot check network status", new Object[0]);
        }
        return connectivityManager;
    }
}
