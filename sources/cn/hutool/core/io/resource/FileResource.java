package cn.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.URLUtil;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;
/* loaded from: classes.dex */
public class FileResource implements Resource, Serializable {
    private static final long serialVersionUID = 1;
    private final File file;

    public FileResource(Path path) {
        this(path.toFile());
    }

    public FileResource(File file) {
        this(file, file.getName());
    }

    public FileResource(File file, String fileName) {
        this.file = file;
    }

    public FileResource(String path) {
        this(FileUtil.file(path));
    }

    @Override // cn.hutool.core.io.resource.Resource
    public String getName() {
        return this.file.getName();
    }

    @Override // cn.hutool.core.io.resource.Resource
    public URL getUrl() {
        return URLUtil.getURL(this.file);
    }

    @Override // cn.hutool.core.io.resource.Resource
    public InputStream getStream() throws NoResourceException {
        return FileUtil.getInputStream(this.file);
    }

    public File getFile() {
        return this.file;
    }

    public String toString() {
        File file = this.file;
        return file == null ? CharSequenceUtil.NULL : file.toString();
    }
}
