package cn.hutool.core.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
/* loaded from: classes.dex */
public class ClassScanner implements Serializable {
    private static final long serialVersionUID = 1;
    private final Charset charset;
    private final Filter<Class<?>> classFilter;
    private ClassLoader classLoader;
    private final Set<Class<?>> classes;
    private boolean initialize;
    private final String packageDirName;
    private final String packageName;
    private final String packageNameWithDot;
    private final String packagePath;

    public static Set<Class<?>> scanAllPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
        return scanAllPackage(packageName, new Filter() { // from class: cn.hutool.core.lang.-$$Lambda$ClassScanner$kffg9DBP75EAxi_h6nZ1cQuNvSM
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                boolean isAnnotationPresent;
                isAnnotationPresent = ((Class) obj).isAnnotationPresent(annotationClass);
                return isAnnotationPresent;
            }
        });
    }

    public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
        return scanPackage(packageName, new Filter() { // from class: cn.hutool.core.lang.-$$Lambda$ClassScanner$RpraQZ9qG_OH85JfLp_M-i0lqGc
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                boolean isAnnotationPresent;
                isAnnotationPresent = ((Class) obj).isAnnotationPresent(annotationClass);
                return isAnnotationPresent;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$scanAllPackageBySuper$2(Class superClass, Class clazz) {
        return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
    }

    public static Set<Class<?>> scanAllPackageBySuper(String packageName, final Class<?> superClass) {
        return scanAllPackage(packageName, new Filter() { // from class: cn.hutool.core.lang.-$$Lambda$ClassScanner$W6phVXbwOpYA8VbG01KY93_2kjw
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return ClassScanner.lambda$scanAllPackageBySuper$2(superClass, (Class) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$scanPackageBySuper$3(Class superClass, Class clazz) {
        return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
    }

    public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
        return scanPackage(packageName, new Filter() { // from class: cn.hutool.core.lang.-$$Lambda$ClassScanner$MtCS9-nmaiF_aA15eCZE1q4ZqsA
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return ClassScanner.lambda$scanPackageBySuper$3(superClass, (Class) obj);
            }
        });
    }

    public static Set<Class<?>> scanAllPackage() {
        return scanAllPackage("", null);
    }

    public static Set<Class<?>> scanPackage() {
        return scanPackage("", null);
    }

    public static Set<Class<?>> scanPackage(String packageName) {
        return scanPackage(packageName, null);
    }

    public static Set<Class<?>> scanAllPackage(String packageName, Filter<Class<?>> classFilter) {
        return new ClassScanner(packageName, classFilter).scan(true);
    }

    public static Set<Class<?>> scanPackage(String packageName, Filter<Class<?>> classFilter) {
        return new ClassScanner(packageName, classFilter).scan();
    }

    public ClassScanner() {
        this(null);
    }

    public ClassScanner(String packageName) {
        this(packageName, null);
    }

    public ClassScanner(String packageName, Filter<Class<?>> classFilter) {
        this(packageName, classFilter, CharsetUtil.CHARSET_UTF_8);
    }

    public ClassScanner(String packageName, Filter<Class<?>> classFilter, Charset charset) {
        this.classes = new HashSet();
        String packageName2 = StrUtil.nullToEmpty(packageName);
        this.packageName = packageName2;
        this.packageNameWithDot = StrUtil.addSuffixIfNot(packageName2, ".");
        this.packageDirName = packageName2.replace('.', File.separatorChar);
        this.packagePath = packageName2.replace('.', '/');
        this.classFilter = classFilter;
        this.charset = charset;
    }

    public Set<Class<?>> scan() {
        return scan(false);
    }

    public Set<Class<?>> scan(boolean forceScanJavaClassPaths) {
        Iterator<URL> it = ResourceUtil.getResourceIter(this.packagePath).iterator();
        while (it.hasNext()) {
            URL url = it.next();
            String protocol = url.getProtocol();
            char c = 65535;
            int hashCode = protocol.hashCode();
            if (hashCode != 104987) {
                if (hashCode == 3143036 && protocol.equals(URLUtil.URL_PROTOCOL_FILE)) {
                    c = 0;
                }
            } else if (protocol.equals(URLUtil.URL_PROTOCOL_JAR)) {
                c = 1;
            }
            if (c == 0) {
                scanFile(new File(URLUtil.decode(url.getFile(), this.charset.name())), null);
            } else if (c == 1) {
                scanJar(URLUtil.getJarFile(url));
            }
        }
        if (forceScanJavaClassPaths || CollUtil.isEmpty((Collection<?>) this.classes)) {
            scanJavaClassPaths();
        }
        return Collections.unmodifiableSet(this.classes);
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private void scanJavaClassPaths() {
        String[] javaClassPaths = ClassUtil.getJavaClassPaths();
        for (String classPath : javaClassPaths) {
            scanFile(new File(URLUtil.decode(classPath, CharsetUtil.systemCharsetName())), null);
        }
    }

    private void scanFile(File file, String rootDir) {
        File[] files;
        if (file.isFile()) {
            String fileName = file.getAbsolutePath();
            if (fileName.endsWith(".class")) {
                String className = fileName.substring(rootDir.length(), fileName.length() - 6).replace(File.separatorChar, '.');
                addIfAccept(className);
            } else if (fileName.endsWith(".jar")) {
                try {
                    scanJar(new JarFile(file));
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            }
        } else if (file.isDirectory() && (files = file.listFiles()) != null) {
            for (File subFile : files) {
                scanFile(subFile, rootDir == null ? subPathBeforePackage(file) : rootDir);
            }
        }
    }

    private void scanJar(JarFile jar) {
        Iterator it = new EnumerationIter(jar.entries()).iterator();
        while (it.hasNext()) {
            JarEntry entry = (JarEntry) it.next();
            String name = StrUtil.removePrefix(entry.getName(), "/");
            if (StrUtil.isEmpty(this.packagePath) || name.startsWith(this.packagePath)) {
                if (name.endsWith(".class") && !entry.isDirectory()) {
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    addIfAccept(loadClass(className));
                }
            }
        }
    }

    private Class<?> loadClass(String className) {
        ClassLoader loader = this.classLoader;
        if (loader == null) {
            loader = ClassLoaderUtil.getClassLoader();
            this.classLoader = loader;
        }
        try {
            Class<?> clazz = Class.forName(className, this.initialize, loader);
            return clazz;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return null;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        } catch (UnsupportedClassVersionError e3) {
            return null;
        }
    }

    private void addIfAccept(String className) {
        if (StrUtil.isBlank(className)) {
            return;
        }
        int classLen = className.length();
        int packageLen = this.packageName.length();
        if (classLen == packageLen) {
            if (className.equals(this.packageName)) {
                addIfAccept(loadClass(className));
            }
        } else if (classLen > packageLen) {
            if (".".equals(this.packageNameWithDot) || className.startsWith(this.packageNameWithDot)) {
                addIfAccept(loadClass(className));
            }
        }
    }

    private void addIfAccept(Class<?> clazz) {
        if (clazz != null) {
            Filter<Class<?>> classFilter = this.classFilter;
            if (classFilter == null || classFilter.accept(clazz)) {
                this.classes.add(clazz);
            }
        }
    }

    private String subPathBeforePackage(File file) {
        String filePath = file.getAbsolutePath();
        if (StrUtil.isNotEmpty(this.packageDirName)) {
            filePath = StrUtil.subBefore((CharSequence) filePath, (CharSequence) this.packageDirName, true);
        }
        return StrUtil.addSuffixIfNot(filePath, File.separator);
    }
}
