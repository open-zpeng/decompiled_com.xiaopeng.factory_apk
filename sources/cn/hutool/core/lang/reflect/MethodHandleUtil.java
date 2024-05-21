package cn.hutool.core.lang.reflect;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
/* loaded from: classes.dex */
public class MethodHandleUtil {
    public static MethodHandles.Lookup lookup(Class<?> callerClass) {
        return LookupFactory.lookup(callerClass);
    }

    public static MethodHandle findMethod(Class<?> callerClass, String name, MethodType type) {
        if (StrUtil.isBlank(name)) {
            return findConstructor(callerClass, type);
        }
        MethodHandle handle = null;
        MethodHandles.Lookup lookup = lookup(callerClass);
        try {
            handle = lookup.findVirtual(callerClass, name, type);
        } catch (IllegalAccessException | NoSuchMethodException e) {
        }
        if (handle == null) {
            try {
                handle = lookup.findStatic(callerClass, name, type);
            } catch (IllegalAccessException | NoSuchMethodException e2) {
            }
        }
        if (handle == null) {
            try {
                MethodHandle handle2 = lookup.findSpecial(callerClass, name, type, callerClass);
                return handle2;
            } catch (IllegalAccessException e3) {
                throw new UtilException(e3);
            } catch (NoSuchMethodException e4) {
                return handle;
            }
        }
        return handle;
    }

    public static MethodHandle findConstructor(Class<?> callerClass, Class<?>... args) {
        return findConstructor(callerClass, MethodType.methodType(Void.TYPE, args));
    }

    public static MethodHandle findConstructor(Class<?> callerClass, MethodType type) {
        MethodHandles.Lookup lookup = lookup(callerClass);
        try {
            return lookup.findConstructor(callerClass, type);
        } catch (IllegalAccessException e) {
            throw new UtilException(e);
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    public static <T> T invokeSpecial(Object obj, String methodName, Object... args) {
        Assert.notNull(obj, "Object to get method must be not null!", new Object[0]);
        Assert.notBlank(methodName, "Method name must be not blank!", new Object[0]);
        Method method = ReflectUtil.getMethodOfObj(obj, methodName, args);
        if (method == null) {
            throw new UtilException("No such method: [{}] from [{}]", methodName, obj.getClass());
        }
        return (T) invokeSpecial(obj, method, args);
    }

    public static <T> T invoke(Object obj, Method method, Object... args) {
        return (T) invoke(false, obj, method, args);
    }

    public static <T> T invokeSpecial(Object obj, Method method, Object... args) {
        return (T) invoke(true, obj, method, args);
    }

    public static <T> T invoke(boolean isSpecial, Object obj, Method method, Object... args) {
        Assert.notNull(method, "Method must be not null!", new Object[0]);
        Class<?> declaringClass = method.getDeclaringClass();
        MethodHandles.Lookup lookup = lookup(declaringClass);
        try {
            MethodHandle handle = isSpecial ? lookup.unreflectSpecial(method, declaringClass) : lookup.unreflect(method);
            if (obj != null) {
                handle = handle.bindTo(obj);
            }
            return (T) handle.invokeWithArguments(args);
        } catch (Throwable e) {
            throw new UtilException(e);
        }
    }
}
