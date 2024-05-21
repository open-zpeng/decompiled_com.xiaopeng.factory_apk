package cn.hutool.core.compiler;

import cn.hutool.core.io.resource.FileObjectResource;
import cn.hutool.core.lang.ResourceClassLoader;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ObjectUtil;
import java.util.HashMap;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
/* loaded from: classes.dex */
class JavaClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private final Map<String, FileObjectResource> classFileObjectMap;
    private final ClassLoader parent;

    /* JADX INFO: Access modifiers changed from: protected */
    public JavaClassFileManager(ClassLoader parent, JavaFileManager fileManager) {
        super(fileManager);
        this.classFileObjectMap = new HashMap();
        this.parent = (ClassLoader) ObjectUtil.defaultIfNull(parent, ClassLoaderUtil.getClassLoader());
    }

    public ClassLoader getClassLoader(JavaFileManager.Location location) {
        return new ResourceClassLoader(this.parent, this.classFileObjectMap);
    }

    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        JavaClassFileObject javaClassFileObject = new JavaClassFileObject(className);
        this.classFileObjectMap.put(className, new FileObjectResource(javaClassFileObject));
        return javaClassFileObject;
    }
}
