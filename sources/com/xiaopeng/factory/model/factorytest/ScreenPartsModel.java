package com.xiaopeng.factory.model.factorytest;

import android.os.SystemProperties;
import com.xiaopeng.commonfunc.model.car.McuModel;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class ScreenPartsModel {
    public static final int DISPLAY_COLOR_BLACK = 2;
    public static final int DISPLAY_COLOR_SILVERY = 0;
    public static final int DISPLAY_LEVEL_HIGH = 1;
    public static final String DISPLAY_LEVEL_HIGH_COLOR_BLACK = "8610001DB100A1";
    public static final String DISPLAY_LEVEL_HIGH_COLOR_SILVERY = "8610001DB100A3";
    public static final int DISPLAY_LEVEL_MIDDLE = 2;
    public static final String DISPLAY_LEVEL_MIDDLE_COLOR_BLACK = "8610001DA10003";
    public static final String DISPLAY_LEVEL_MIDDLE_COLOR_SILVERY = "8610001DA10001";
    public static final String DISPLAY_TYPE_CHANGXIN = "ChangXin";
    public static final String DISPLAY_TYPE_XINLI = "XinLi";
    public static final String SYSTEM_PROPS_DISPLAY_TYPE = "sys.xiaopeng.display_type";
    private static final String TAG = "ScreenPartsModel";
    private int mDisplayLevel = 1;
    private int mDisplayColor = 0;
    private final McuModel mMcuModel = new McuModel(TAG);

    public ScreenPartsModel() {
        initDisplayLevel();
    }

    public int getDisplayLevel() {
        return this.mDisplayLevel;
    }

    public void setDisplayLevel(int level) {
        this.mDisplayLevel = level;
    }

    public int getDisplayColor() {
        return this.mDisplayColor;
    }

    public void setDisplayColor(int color) {
        this.mDisplayColor = color;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void setDisplaySpec(String config) {
        char c;
        LogUtils.v(TAG, "setDisplaySpec");
        switch (config.hashCode()) {
            case 395905889:
                if (config.equals(DISPLAY_LEVEL_MIDDLE_COLOR_SILVERY)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 395905891:
                if (config.equals(DISPLAY_LEVEL_MIDDLE_COLOR_BLACK)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 424535567:
                if (config.equals(DISPLAY_LEVEL_HIGH_COLOR_BLACK)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 424535569:
                if (config.equals(DISPLAY_LEVEL_HIGH_COLOR_SILVERY)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            setDisplayColor(0);
            setDisplayLevel(1);
        } else if (c == 1) {
            setDisplayColor(0);
            setDisplayLevel(2);
        } else if (c == 2) {
            setDisplayColor(2);
            setDisplayLevel(1);
        } else if (c == 3) {
            setDisplayColor(2);
            setDisplayLevel(2);
        }
    }

    public String getPartsNumber() {
        String data = this.mMcuModel.getFactoryDisplayTypeMsgToMcu();
        LogUtils.v(TAG, "getPartsNumber data = " + data);
        return data;
    }

    public void setPartsNumber() {
        LogUtils.v(TAG, "setPartsNumber");
        this.mMcuModel.sendFactoryDisplayTypeMsgToMcu(this.mDisplayColor + this.mDisplayLevel);
    }

    private void initDisplayLevel() {
        LogUtils.v(TAG, "initDisplayLevel");
        String info = SystemProperties.get(SYSTEM_PROPS_DISPLAY_TYPE);
        if (DISPLAY_TYPE_XINLI.equals(info)) {
            setDisplayLevel(1);
        } else {
            setDisplayLevel(2);
        }
    }

    public void onDestroy() {
        this.mMcuModel.onDestroy();
    }
}
