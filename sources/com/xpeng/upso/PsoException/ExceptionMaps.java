package com.xpeng.upso.PsoException;

import androidx.annotation.Keep;
import java.util.HashMap;
import java.util.Map;
@Keep
/* loaded from: classes2.dex */
public class ExceptionMaps {
    public static final String COMMON_PRESET = "common_preset";
    public static final String COMMON_VERIFY = "common_vreify";
    public static final String TBOX_TEST = "TboxTest";
    public static final String UNKNOWN_OPTION = "unknown_option";
    public static final String XPU_TEST = "XpuTest";
    static Map<String, Throwable> exceptionMaps = new HashMap();

    static {
        exceptionMaps.put("Preset_TboxTest", new TboxPresetTestException());
        exceptionMaps.put("Preset_XpuTest", new XpuPresetTestException());
        exceptionMaps.put("Verify_TboxTest", new TboxVerifyTestException());
        exceptionMaps.put("Verify_XpuTest", new XpuVerifyTestException());
        exceptionMaps.put("Preset_E38_TBOX", new TboxE38PresetException());
        exceptionMaps.put("Verify_E38_TBOX", new TboxE38VerifyException());
        exceptionMaps.put("Preset_E38V_TBOX", new TboxE38vPresetException());
        exceptionMaps.put("Verify_E38V_TBOX", new TboxE38vVerifyException());
        exceptionMaps.put("Preset_E28A_TBOX", new TboxE28aPresetException());
        exceptionMaps.put("Verify_E28A_TBOX", new TboxE28aVerifyException());
        exceptionMaps.put("Preset_E28AV_TBOX", new TboxE28avPresetException());
        exceptionMaps.put("Verify_E28AV_TBOX", new TboxE28avVerifyException());
        exceptionMaps.put("Preset_E38_XPU", new XpuE38PresetException());
        exceptionMaps.put("Verify_E38_XPU", new XpuE38VerifyException());
        exceptionMaps.put("Preset_E38V_XPU", new XpuE38vPresetException());
        exceptionMaps.put("Verify_E38V_XPU", new XpuE38vVerifyException());
        exceptionMaps.put("Preset_E28A_XPU", new XpuE28aPresetException());
        exceptionMaps.put("Verify_E28A_XPU", new XpuE28aVerifyException());
        exceptionMaps.put("Preset_E28AV_XPU", new XpuE28avPresetException());
        exceptionMaps.put("Verify_E28AV_XPU", new XpuE28avVerifyException());
        exceptionMaps.put("common_preset", new UnSupportedTypePresetException());
        exceptionMaps.put("common_vreify", new UnSupportedTypeVerifyException());
        exceptionMaps.put("unknown_option", new UnknownException());
    }

    public static Throwable getThrowable(int op, String key) {
        Throwable throwable;
        Throwable throwable2;
        try {
            if (op == 2) {
                Map<String, Throwable> map = exceptionMaps;
                throwable2 = map.get("Verify_" + key);
            } else {
                Map<String, Throwable> map2 = exceptionMaps;
                throwable2 = map2.get("Preset_" + key);
            }
            if (throwable2 != null) {
                return throwable2;
            }
        } catch (Exception e) {
        }
        try {
            if (op == 2) {
                throwable = exceptionMaps.get("common_vreify");
            } else {
                throwable = exceptionMaps.get("common_preset");
            }
            if (throwable != null) {
                return throwable;
            }
        } catch (Exception e2) {
        }
        return exceptionMaps.get("unknown_option");
    }

    public static Throwable getTestThrowable(int op, String key) {
        Throwable throwable;
        try {
            if (op == 2) {
                Map<String, Throwable> map = exceptionMaps;
                throwable = map.get("Verify_" + key);
            } else {
                Map<String, Throwable> map2 = exceptionMaps;
                throwable = map2.get("Preset_" + key);
            }
            if (throwable != null) {
                return throwable;
            }
        } catch (Exception e) {
        }
        return exceptionMaps.get("unknown_option");
    }
}
