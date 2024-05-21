package cn.hutool.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: classes.dex */
public class ModifierUtil {

    /* loaded from: classes.dex */
    public enum ModifierType {
        PUBLIC(1),
        PRIVATE(2),
        PROTECTED(4),
        STATIC(8),
        FINAL(16),
        SYNCHRONIZED(32),
        VOLATILE(64),
        TRANSIENT(128),
        NATIVE(256),
        ABSTRACT(1024),
        STRICT(2048);
        
        private final int value;

        ModifierType(int modifier) {
            this.value = modifier;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static boolean hasModifier(Class<?> clazz, ModifierType... modifierTypes) {
        return (clazz == null || ArrayUtil.isEmpty((Object[]) modifierTypes) || (clazz.getModifiers() & modifiersToInt(modifierTypes)) == 0) ? false : true;
    }

    public static boolean hasModifier(Constructor<?> constructor, ModifierType... modifierTypes) {
        return (constructor == null || ArrayUtil.isEmpty((Object[]) modifierTypes) || (constructor.getModifiers() & modifiersToInt(modifierTypes)) == 0) ? false : true;
    }

    public static boolean hasModifier(Method method, ModifierType... modifierTypes) {
        return (method == null || ArrayUtil.isEmpty((Object[]) modifierTypes) || (method.getModifiers() & modifiersToInt(modifierTypes)) == 0) ? false : true;
    }

    public static boolean hasModifier(Field field, ModifierType... modifierTypes) {
        return (field == null || ArrayUtil.isEmpty((Object[]) modifierTypes) || (field.getModifiers() & modifiersToInt(modifierTypes)) == 0) ? false : true;
    }

    public static boolean isPublic(Field field) {
        return hasModifier(field, ModifierType.PUBLIC);
    }

    public static boolean isPublic(Method method) {
        return hasModifier(method, ModifierType.PUBLIC);
    }

    public static boolean isPublic(Class<?> clazz) {
        return hasModifier(clazz, ModifierType.PUBLIC);
    }

    public static boolean isPublic(Constructor<?> constructor) {
        return hasModifier(constructor, ModifierType.PUBLIC);
    }

    public static boolean isStatic(Field field) {
        return hasModifier(field, ModifierType.STATIC);
    }

    public static boolean isStatic(Method method) {
        return hasModifier(method, ModifierType.STATIC);
    }

    public static boolean isStatic(Class<?> clazz) {
        return hasModifier(clazz, ModifierType.STATIC);
    }

    public static boolean isSynthetic(Field field) {
        return field.isSynthetic();
    }

    public static boolean isSynthetic(Method method) {
        return method.isSynthetic();
    }

    public static boolean isSynthetic(Class<?> clazz) {
        return clazz.isSynthetic();
    }

    private static int modifiersToInt(ModifierType... modifierTypes) {
        int modifier = modifierTypes[0].getValue();
        for (int i = 1; i < modifierTypes.length; i++) {
            modifier |= modifierTypes[i].getValue();
        }
        return modifier;
    }
}
