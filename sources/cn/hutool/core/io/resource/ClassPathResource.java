package cn.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import java.net.URL;
/* loaded from: classes.dex */
public class ClassPathResource extends UrlResource {
    private static final long serialVersionUID = 1;
    private final ClassLoader classLoader;
    private final Class<?> clazz;
    private final String path;

    public ClassPathResource(String path) {
        this(path, null, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this(path, classLoader, null);
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this(path, null, clazz);
    }

    public ClassPathResource(String pathBaseClassLoader, ClassLoader classLoader, Class<?> clazz) {
        super((URL) null);
        Assert.notNull(pathBaseClassLoader, "Path must not be null", new Object[0]);
        String path = normalizePath(pathBaseClassLoader);
        this.path = path;
        this.name = StrUtil.isBlank(path) ? null : FileUtil.getName(path);
        this.classLoader = (ClassLoader) ObjectUtil.defaultIfNull(classLoader, ClassUtil.getClassLoader());
        this.clazz = clazz;
        initUrl();
    }

    public final String getPath() {
        return this.path;
    }

    public final String getAbsolutePath() {
        if (FileUtil.isAbsolutePath(this.path)) {
            return this.path;
        }
        return FileUtil.normalize(URLUtil.getDecodedPath(this.url));
    }

    public final ClassLoader getClassLoader() {
        return this.classLoader;
    }

    private void initUrl() {
        Class<?> cls = this.clazz;
        if (cls != null) {
            this.url = cls.getResource(this.path);
        } else {
            ClassLoader classLoader = this.classLoader;
            if (classLoader != null) {
                this.url = classLoader.getResource(this.path);
            } else {
                this.url = ClassLoader.getSystemResource(this.path);
            }
        }
        if (this.url == null) {
            throw new NoResourceException("Resource of path [{}] not exist!", this.path);
        }
    }

    @Override // cn.hutool.core.io.resource.UrlResource
    public String toString() {
        if (this.path == null) {
            return super.toString();
        }
        return URLUtil.CLASSPATH_URL_PREFIX + this.path;
    }

    private String normalizePath(String path) {
        String path2 = StrUtil.removePrefix(FileUtil.normalize(path), "/");
        Assert.isFalse(FileUtil.isAbsolutePath(path2), "Path [{}] must be a relative path !", path2);
        return path2;
    }
}
