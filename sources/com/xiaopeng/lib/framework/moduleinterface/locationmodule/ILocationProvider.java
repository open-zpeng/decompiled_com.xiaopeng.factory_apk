package com.xiaopeng.lib.framework.moduleinterface.locationmodule;

import android.content.Context;
import android.support.annotation.NonNull;
/* loaded from: classes2.dex */
public interface ILocationProvider {
    ILocation buildLocation();

    void publishLocation(@NonNull ILocation location) throws ILocationServiceException;

    boolean serviceStarted();

    void start(@NonNull Context context) throws ILocationServiceException;
}