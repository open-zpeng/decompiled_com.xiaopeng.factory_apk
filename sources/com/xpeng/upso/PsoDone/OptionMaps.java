package com.xpeng.upso.PsoDone;

import androidx.annotation.Keep;
import java.util.HashMap;
import java.util.Map;
@Keep
/* loaded from: classes2.dex */
public class OptionMaps {
    public static final String COMMON_PRESET = "common_preset";
    public static final String COMMON_VERIFY = "common_vreify";
    public static final String TBOX_TEST = "TboxTest";
    public static final String UNKNOWN_OPTION = "unknown_option";
    public static final String XPU_TEST = "XpuTest";
    static Map<String, Throwable> optionsMaps = new HashMap();

    static {
        optionsMaps.put("Preset_TboxTest", new TboxPresetTestDone());
        optionsMaps.put("Preset_XpuTest", new XpuPresetTestDone());
        optionsMaps.put("Verify_TboxTest", new TboxVerifyTestDone());
        optionsMaps.put("Verify_XpuTest", new XpuVerifyTestDone());
        optionsMaps.put("Preset_E38_TBOX", new TboxE38PresetSucceed());
        optionsMaps.put("Verify_E38_TBOX", new TboxE38VerifySucceed());
        optionsMaps.put("Preset_E38V_TBOX", new TboxE38vPresetSucceed());
        optionsMaps.put("Verify_E38V_TBOX", new TboxE38vVerifySucceed());
        optionsMaps.put("Preset_E28A_TBOX", new TboxE28aPresetSucceed());
        optionsMaps.put("Verify_E28A_TBOX", new TboxE28aVerifySucceed());
        optionsMaps.put("Preset_E28AV_TBOX", new TboxE28avPresetSucceed());
        optionsMaps.put("Verify_E28AV_TBOX", new TboxE28avVerifySucceed());
        optionsMaps.put("Preset_E38_XPU", new XpuE38PresetSucceed());
        optionsMaps.put("Verify_E38_XPU", new XpuE38VerifySucceed());
        optionsMaps.put("Preset_E38V_XPU", new XpuE38vPresetSucceed());
        optionsMaps.put("Verify_E38V_XPU", new XpuE38vVerifySucceed());
        optionsMaps.put("Preset_E28A_XPU", new XpuE28aPresetSucceed());
        optionsMaps.put("Verify_E28A_XPU", new XpuE28aVerifySucceed());
        optionsMaps.put("Preset_E28AV_XPU", new XpuE28avPresetSucceed());
        optionsMaps.put("Verify_E28AV_XPU", new XpuE28avVerifySucceed());
        optionsMaps.put("common_preset", new CommonTypePresetSucceed());
        optionsMaps.put("common_vreify", new CommonTypeVerifySucceed());
        optionsMaps.put("unknown_option", new UnknownOption());
    }

    public static Throwable getOption(int op, String key) {
        Throwable throwable;
        Throwable throwable2;
        try {
            if (op == 2) {
                Map<String, Throwable> map = optionsMaps;
                throwable2 = map.get("Verify_" + key);
            } else {
                Map<String, Throwable> map2 = optionsMaps;
                throwable2 = map2.get("Preset_" + key);
            }
            if (throwable2 != null) {
                return throwable2;
            }
        } catch (Exception e) {
        }
        try {
            if (op == 2) {
                throwable = optionsMaps.get("common_vreify");
            } else {
                throwable = optionsMaps.get("common_preset");
            }
            if (throwable != null) {
                return throwable;
            }
        } catch (Exception e2) {
        }
        return optionsMaps.get("unknown_option");
    }

    public static Throwable geTestOption(int op, String key) {
        Throwable throwable;
        try {
            if (op == 2) {
                Map<String, Throwable> map = optionsMaps;
                throwable = map.get("Verify_" + key);
            } else {
                Map<String, Throwable> map2 = optionsMaps;
                throwable = map2.get("Preset_" + key);
            }
            if (throwable != null) {
                return throwable;
            }
        } catch (Exception e) {
        }
        return optionsMaps.get("unknown_option");
    }
}
