package ch.ethz.ssh2.log;
/* loaded from: classes.dex */
public class Logger {
    private static final boolean enabled = false;
    private static final int logLevel = 99;
    private String className;

    public static final Logger getLogger(Class x) {
        return new Logger(x);
    }

    public Logger(Class x) {
        this.className = x.getName();
    }

    public final boolean isEnabled() {
        return false;
    }

    public final void log(int level, String message) {
    }
}
