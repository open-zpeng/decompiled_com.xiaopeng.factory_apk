package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
/* loaded from: classes.dex */
public class ManifestUtil {
    private static final String[] MANIFEST_NAMES = {"Manifest.mf", "manifest.mf", "MANIFEST.MF"};

    public static Manifest getManifest(Class<?> cls) throws IORuntimeException {
        URL url = ResourceUtil.getResource(null, cls);
        try {
            URLConnection connection = url.openConnection();
            if (!(connection instanceof JarURLConnection)) {
                return null;
            }
            JarURLConnection conn = (JarURLConnection) connection;
            return getManifest(conn);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static Manifest getManifest(File classpathItem) throws IORuntimeException {
        Manifest manifest = null;
        if (classpathItem.isFile()) {
            try {
                JarFile jarFile = new JarFile(classpathItem);
                manifest = getManifest(jarFile);
                jarFile.close();
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } else {
            File metaDir = new File(classpathItem, "META-INF");
            File manifestFile = null;
            if (metaDir.isDirectory()) {
                String[] strArr = MANIFEST_NAMES;
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    String name = strArr[i];
                    File mFile = new File(metaDir, name);
                    if (!mFile.isFile()) {
                        i++;
                    } else {
                        manifestFile = mFile;
                        break;
                    }
                }
            }
            if (manifestFile != null) {
                try {
                    FileInputStream fis = new FileInputStream(manifestFile);
                    manifest = new Manifest(fis);
                    fis.close();
                } catch (IOException e2) {
                    throw new IORuntimeException(e2);
                }
            }
        }
        return manifest;
    }

    public static Manifest getManifest(JarURLConnection connection) throws IORuntimeException {
        try {
            JarFile jarFile = connection.getJarFile();
            return getManifest(jarFile);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static Manifest getManifest(JarFile jarFile) throws IORuntimeException {
        try {
            return jarFile.getManifest();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
