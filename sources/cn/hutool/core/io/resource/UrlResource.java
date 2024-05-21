package cn.hutool.core.io.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.URLUtil;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
/* loaded from: classes.dex */
public class UrlResource implements Resource, Serializable {
    private static final long serialVersionUID = 1;
    protected String name;
    protected URL url;

    public UrlResource(URL url) {
        this(url, null);
    }

    public UrlResource(URL url, String name) {
        this.url = url;
        this.name = (String) ObjectUtil.defaultIfNull(name, url != null ? FileUtil.getName(url.getPath()) : null);
    }

    @Deprecated
    public UrlResource(File file) {
        this.url = URLUtil.getURL(file);
    }

    @Override // cn.hutool.core.io.resource.Resource
    public String getName() {
        return this.name;
    }

    @Override // cn.hutool.core.io.resource.Resource
    public URL getUrl() {
        return this.url;
    }

    @Override // cn.hutool.core.io.resource.Resource
    public InputStream getStream() throws NoResourceException {
        URL url = this.url;
        if (url == null) {
            throw new NoResourceException("Resource URL is null!");
        }
        return URLUtil.getStream(url);
    }

    public File getFile() {
        return FileUtil.file(this.url);
    }

    public String toString() {
        URL url = this.url;
        return url == null ? CharSequenceUtil.NULL : url.toString();
    }
}
