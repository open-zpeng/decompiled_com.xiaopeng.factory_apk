package cn.hutool.core.lang;

import cn.hutool.core.lang.EnumItem;
import java.io.Serializable;
/* loaded from: classes.dex */
public interface EnumItem<E extends EnumItem<E>> extends Serializable {
    int intVal();

    String name();

    default String text() {
        return name();
    }

    default E[] items() {
        return (E[]) ((EnumItem[]) getClass().getEnumConstants());
    }

    default E fromInt(Integer intVal) {
        if (intVal == null) {
            return null;
        }
        E[] vs = items();
        for (E enumItem : vs) {
            if (enumItem.intVal() == intVal.intValue()) {
                return enumItem;
            }
        }
        return null;
    }

    default E fromStr(String strVal) {
        if (strVal == null) {
            return null;
        }
        E[] vs = items();
        for (E enumItem : vs) {
            if (strVal.equalsIgnoreCase(enumItem.name())) {
                return enumItem;
            }
        }
        return null;
    }
}
