package cn.hutool.core.io.resource;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
/* loaded from: classes.dex */
public class InputStreamResource implements Resource, Serializable {
    private static final long serialVersionUID = 1;
    private final InputStream in;
    private final String name;

    public InputStreamResource(InputStream in) {
        this(in, null);
    }

    public InputStreamResource(InputStream in, String name) {
        this.in = in;
        this.name = name;
    }

    @Override // cn.hutool.core.io.resource.Resource
    public String getName() {
        return this.name;
    }

    @Override // cn.hutool.core.io.resource.Resource
    public URL getUrl() {
        return null;
    }

    @Override // cn.hutool.core.io.resource.Resource
    public InputStream getStream() {
        return this.in;
    }
}
