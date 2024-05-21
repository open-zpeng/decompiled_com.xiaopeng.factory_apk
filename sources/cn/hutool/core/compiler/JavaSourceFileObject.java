package cn.hutool.core.compiler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.URLUtil;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class JavaSourceFileObject extends SimpleJavaFileObject {
    private InputStream inputStream;

    /* JADX INFO: Access modifiers changed from: protected */
    public JavaSourceFileObject(URI uri) {
        super(uri, JavaFileObject.Kind.SOURCE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JavaSourceFileObject(String className, String code, Charset charset) {
        this(className, IoUtil.toStream(code, charset));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JavaSourceFileObject(String name, InputStream inputStream) {
        this(URLUtil.getStringURI(name.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension));
        this.inputStream = inputStream;
    }

    public InputStream openInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = toUri().toURL().openStream();
        }
        return new BufferedInputStream(this.inputStream);
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        InputStream in = openInputStream();
        try {
            String readUtf8 = IoUtil.readUtf8(in);
            if (in != null) {
                in.close();
            }
            return readUtf8;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }
}
