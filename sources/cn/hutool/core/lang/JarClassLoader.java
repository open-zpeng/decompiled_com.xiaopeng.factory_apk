package cn.hutool.core.lang;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.URLUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
/* loaded from: classes.dex */
public class JarClassLoader extends URLClassLoader {
    public static JarClassLoader load(File dir) {
        JarClassLoader loader = new JarClassLoader();
        loader.addJar(dir);
        loader.addURL(dir);
        return loader;
    }

    public static JarClassLoader loadJar(File jarFile) {
        JarClassLoader loader = new JarClassLoader();
        loader.addJar(jarFile);
        return loader;
    }

    public static void loadJar(URLClassLoader loader, File jarFile) throws UtilException {
        try {
            Method method = ClassUtil.getDeclaredMethod(URLClassLoader.class, "addURL", URL.class);
            if (method != null) {
                method.setAccessible(true);
                List<File> jars = loopJar(jarFile);
                for (File jar : jars) {
                    ReflectUtil.invoke(loader, method, jar.toURI().toURL());
                }
            }
        } catch (IOException e) {
            throw new UtilException(e);
        }
    }

    public static URLClassLoader loadJarToSystemClassLoader(File jarFile) {
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        loadJar(urlClassLoader, jarFile);
        return urlClassLoader;
    }

    public JarClassLoader() {
        this(new URL[0]);
    }

    public JarClassLoader(URL[] urls) {
        super(urls, ClassUtil.getClassLoader());
    }

    public JarClassLoader addJar(File jarFileOrDir) {
        if (isJarFile(jarFileOrDir)) {
            return addURL(jarFileOrDir);
        }
        List<File> jars = loopJar(jarFileOrDir);
        for (File jar : jars) {
            addURL(jar);
        }
        return this;
    }

    @Override // java.net.URLClassLoader
    public void addURL(URL url) {
        super.addURL(url);
    }

    public JarClassLoader addURL(File dir) {
        super.addURL(URLUtil.getURL(dir));
        return this;
    }

    private static List<File> loopJar(File file) {
        return FileUtil.loopFiles(file, new FileFilter() { // from class: cn.hutool.core.lang.-$$Lambda$JarClassLoader$xni4in0e8XfwRFnf_3pnCYIi0NY
            @Override // java.io.FileFilter
            public final boolean accept(File file2) {
                boolean isJarFile;
                isJarFile = JarClassLoader.isJarFile(file2);
                return isJarFile;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isJarFile(File file) {
        if (!FileUtil.isFile(file)) {
            return false;
        }
        return file.getPath().toLowerCase().endsWith(".jar");
    }
}
