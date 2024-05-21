package xp.hardware.dab.internal;

import android.content.Context;
import android.os.Environment;
import dalvik.system.PathClassLoader;
import java.io.File;
/* loaded from: classes2.dex */
public class Loader {
    private static final String RADIO_JAR_PATH;
    public static final String RADIO_SERVER_CLASS_NAME = "com.ts.service.RadioService";
    private static final String RADIO_SERVICE_JAR_PATH;
    private static Loader sRadioClassLoader;
    private static Loader sRadioServiceClassLoader;
    private ClassLoader mClassLoader;
    private static final String ROOT_DIR = Environment.getRootDirectory().getAbsolutePath() + File.separator;
    private static final String FRAMEWORK_DIR = ROOT_DIR + "framework" + File.separator;
    private static final String LIB_DIR = ROOT_DIR + "lib" + File.separator;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(FRAMEWORK_DIR);
        sb.append("xp_radio_base.jar");
        RADIO_JAR_PATH = sb.toString();
        RADIO_SERVICE_JAR_PATH = FRAMEWORK_DIR + "xp_radio_service.jar";
    }

    Loader(String jarPath, Context context) {
        this(jarPath, null, context);
    }

    Loader(String jarPath, String jniPath, Context context) {
        ClassLoader parentLoader;
        this.mClassLoader = null;
        if (context == null) {
            parentLoader = ClassLoader.getSystemClassLoader();
        } else {
            parentLoader = context.getClassLoader();
        }
        this.mClassLoader = new PathClassLoader(jarPath, jniPath, parentLoader);
    }

    public static Loader getRadioJar(Context context) {
        if (sRadioClassLoader == null) {
            sRadioClassLoader = new Loader(RADIO_JAR_PATH, null, context);
        }
        return sRadioClassLoader;
    }

    public static Loader getRadioServiceJar(Context context) {
        if (sRadioServiceClassLoader == null) {
            sRadioServiceClassLoader = new Loader(RADIO_SERVICE_JAR_PATH, null, context);
        }
        return sRadioServiceClassLoader;
    }

    public Class loadClass(String className) {
        try {
            return this.mClassLoader.loadClass(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
