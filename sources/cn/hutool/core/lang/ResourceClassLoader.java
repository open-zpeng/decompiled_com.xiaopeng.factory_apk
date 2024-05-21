package cn.hutool.core.lang;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ObjectUtil;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class ResourceClassLoader<T extends Resource> extends SecureClassLoader {
    private final Map<String, T> resourceMap;

    public ResourceClassLoader(ClassLoader parentClassLoader, Map<String, T> resourceMap) {
        super((ClassLoader) ObjectUtil.defaultIfNull(parentClassLoader, ClassLoaderUtil.getClassLoader()));
        this.resourceMap = (Map) ObjectUtil.defaultIfNull(resourceMap, new HashMap());
    }

    public ResourceClassLoader<T> addResource(T resource) {
        this.resourceMap.put(resource.getName(), resource);
        return this;
    }

    @Override // java.lang.ClassLoader
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Resource resource = this.resourceMap.get(name);
        if (resource != null) {
            byte[] bytes = resource.readBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }
}
