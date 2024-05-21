package cn.hutool.core.compiler;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.core.util.ZipUtil;
import io.sentry.SentryBaseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.tools.JavaFileObject;
/* loaded from: classes.dex */
public class JavaFileObjectUtil {
    public static List<JavaFileObject> getJavaFileObjects(File file) {
        List<JavaFileObject> result = new ArrayList<>();
        String fileName = file.getName();
        if (isJavaFile(fileName)) {
            result.add(new JavaSourceFileObject(file.toURI()));
        } else if (isJarOrZipFile(fileName)) {
            result.addAll(getJavaFileObjectByZipOrJarFile(file));
        }
        return result;
    }

    public static boolean isJarOrZipFile(String fileName) {
        return FileNameUtil.isType(fileName, URLUtil.URL_PROTOCOL_JAR, URLUtil.URL_PROTOCOL_ZIP);
    }

    public static boolean isJavaFile(String fileName) {
        return FileNameUtil.isType(fileName, SentryBaseEvent.DEFAULT_PLATFORM);
    }

    private static List<JavaFileObject> getJavaFileObjectByZipOrJarFile(File file) {
        final List<JavaFileObject> collection = new ArrayList<>();
        final ZipFile zipFile = ZipUtil.toZipFile(file, null);
        ZipUtil.read(zipFile, new Consumer() { // from class: cn.hutool.core.compiler.-$$Lambda$JavaFileObjectUtil$QvxqtZdz-JGopyKG-ddHgIFPaY0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                JavaFileObjectUtil.lambda$getJavaFileObjectByZipOrJarFile$0(collection, zipFile, (ZipEntry) obj);
            }
        });
        return collection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getJavaFileObjectByZipOrJarFile$0(List collection, ZipFile zipFile, ZipEntry zipEntry) {
        String name = zipEntry.getName();
        if (isJavaFile(name)) {
            collection.add(new JavaSourceFileObject(name, ZipUtil.getStream(zipFile, zipEntry)));
        }
    }
}
