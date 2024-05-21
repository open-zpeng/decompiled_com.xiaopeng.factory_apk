package cn.hutool.core.util;

import cn.hutool.core.convert.BasicType;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.JarClassLoader;
import cn.hutool.core.lang.SimpleCache;
import com.xiaopeng.commonfunc.Constant;
import java.io.File;
import java.lang.reflect.Array;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public class ClassLoaderUtil {
    private static final String ARRAY_SUFFIX = "[]";
    private static final char INNER_CLASS_SEPARATOR = '$';
    private static final String INTERNAL_ARRAY_PREFIX = "[";
    private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
    private static final char PACKAGE_SEPARATOR = '.';
    private static final Map<String, Class<?>> PRIMITIVE_TYPE_NAME_MAP = new ConcurrentHashMap(32);
    private static final SimpleCache<String, Class<?>> CLASS_CACHE = new SimpleCache<>();

    static {
        List<Class<?>> primitiveTypes = new ArrayList<>(32);
        primitiveTypes.addAll(BasicType.PRIMITIVE_WRAPPER_MAP.keySet());
        primitiveTypes.add(boolean[].class);
        primitiveTypes.add(byte[].class);
        primitiveTypes.add(char[].class);
        primitiveTypes.add(double[].class);
        primitiveTypes.add(float[].class);
        primitiveTypes.add(int[].class);
        primitiveTypes.add(long[].class);
        primitiveTypes.add(short[].class);
        primitiveTypes.add(Void.TYPE);
        for (Class<?> primitiveType : primitiveTypes) {
            PRIMITIVE_TYPE_NAME_MAP.put(primitiveType.getName(), primitiveType);
        }
    }

    public static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: cn.hutool.core.util.-$$Lambda$ClassLoaderUtil$1Y589Aeocie9Fshu_5eEBW5Z5PY
            @Override // java.security.PrivilegedAction
            public final Object run() {
                ClassLoader contextClassLoader;
                contextClassLoader = Thread.currentThread().getContextClassLoader();
                return contextClassLoader;
            }
        });
    }

    public static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: cn.hutool.core.util.-$$Lambda$WRmHljrQ8Jwi8Gq4TcOy2PZ8vhM
            @Override // java.security.PrivilegedAction
            public final Object run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            ClassLoader classLoader2 = ClassLoaderUtil.class.getClassLoader();
            if (classLoader2 == null) {
                return getSystemClassLoader();
            }
            return classLoader2;
        }
        return classLoader;
    }

    public static Class<?> loadClass(String name) throws UtilException {
        return loadClass(name, true);
    }

    public static Class<?> loadClass(String name, boolean isInitialized) throws UtilException {
        return loadClass(name, null, isInitialized);
    }

    public static Class<?> loadClass(String name, ClassLoader classLoader, boolean isInitialized) throws UtilException {
        Class<?> clazz;
        Assert.notNull(name, "Name must not be null", new Object[0]);
        Class<?> clazz2 = loadPrimitiveClass(name);
        if (clazz2 == null) {
            clazz2 = CLASS_CACHE.get(name);
        }
        if (clazz2 != null) {
            return clazz2;
        }
        if (name.endsWith(ARRAY_SUFFIX)) {
            String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
            Class<?> elementClass = loadClass(elementClassName, classLoader, isInitialized);
            clazz = Array.newInstance(elementClass, 0).getClass();
        } else if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(Constant.SEMICOLON_STRING)) {
            String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
            Class<?> elementClass2 = loadClass(elementName, classLoader, isInitialized);
            clazz = Array.newInstance(elementClass2, 0).getClass();
        } else if (name.startsWith("[")) {
            String elementName2 = name.substring("[".length());
            Class<?> elementClass3 = loadClass(elementName2, classLoader, isInitialized);
            clazz = Array.newInstance(elementClass3, 0).getClass();
        } else {
            if (classLoader == null) {
                classLoader = getClassLoader();
            }
            try {
                clazz = Class.forName(name, isInitialized, classLoader);
            } catch (ClassNotFoundException ex) {
                Class<?> clazz3 = tryLoadInnerClass(name, classLoader, isInitialized);
                if (clazz3 == null) {
                    throw new UtilException(ex);
                }
                clazz = clazz3;
            }
        }
        return CLASS_CACHE.put(name, clazz);
    }

    public static Class<?> loadPrimitiveClass(String name) {
        if (!StrUtil.isNotBlank(name)) {
            return null;
        }
        String name2 = name.trim();
        if (name2.length() > 8) {
            return null;
        }
        Class<?> result = PRIMITIVE_TYPE_NAME_MAP.get(name2);
        return result;
    }

    public static JarClassLoader getJarClassLoader(File jarOrDir) {
        return JarClassLoader.load(jarOrDir);
    }

    public static Class<?> loadClass(File jarOrDir, String name) {
        try {
            return getJarClassLoader(jarOrDir).loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e);
        }
    }

    public static boolean isPresent(String className) {
        return isPresent(className, null);
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            loadClass(className, classLoader, false);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static Class<?> tryLoadInnerClass(String name, ClassLoader classLoader, boolean isInitialized) {
        int lastDotIndex = name.lastIndexOf(46);
        if (lastDotIndex > 0) {
            String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
            try {
                return Class.forName(innerClassName, isInitialized, classLoader);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }
}
