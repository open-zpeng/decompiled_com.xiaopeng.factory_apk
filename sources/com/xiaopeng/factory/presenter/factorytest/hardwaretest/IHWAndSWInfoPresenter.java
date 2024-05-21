package com.xiaopeng.factory.presenter.factorytest.hardwaretest;

import android.content.Context;
import android.graphics.Bitmap;
/* loaded from: classes2.dex */
public interface IHWAndSWInfoPresenter {
    void generateCarCodeBitmap(int i);

    void generateCodeBitmap(int i);

    void generateNumBitmap(int i);

    String getDeviceCode();

    String getHWInfo();

    void getPsuVersion();

    String getSWInfo();

    String getVehicleId();

    void init(Context context);

    void onDestroy();

    void requestCarCode();

    void setCarCodeBitmap(Bitmap bitmap);

    void setDeviceCodeBitmap(Bitmap bitmap);

    void setDeviceNumBitmap(Bitmap bitmap);

    void setPsuVersionTextView(String str);
}
