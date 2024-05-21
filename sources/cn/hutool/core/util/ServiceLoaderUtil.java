package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
/* loaded from: classes.dex */
public class ServiceLoaderUtil {
    public static <T> T loadFirstAvailable(Class<T> clazz) {
        Iterator<T> iterator = load(clazz).iterator();
        while (iterator.hasNext()) {
            try {
                return iterator.next();
            } catch (ServiceConfigurationError e) {
            }
        }
        return null;
    }

    public static <T> T loadFirst(Class<T> clazz) {
        Iterator<T> iterator = load(clazz).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static <T> ServiceLoader<T> load(Class<T> clazz) {
        return load(clazz, null);
    }

    public static <T> ServiceLoader<T> load(Class<T> clazz, ClassLoader loader) {
        return ServiceLoader.load(clazz, (ClassLoader) ObjectUtil.defaultIfNull(loader, ClassLoaderUtil.getClassLoader()));
    }

    public static <T> List<T> loadList(Class<T> clazz) {
        return loadList(clazz, null);
    }

    public static <T> List<T> loadList(Class<T> clazz, ClassLoader loader) {
        return ListUtil.list(false, (Iterable) load(clazz, loader));
    }
}
