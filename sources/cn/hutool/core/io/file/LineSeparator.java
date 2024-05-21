package cn.hutool.core.io.file;
/* loaded from: classes.dex */
public enum LineSeparator {
    MAC("\r"),
    LINUX("\n"),
    WINDOWS("\r\n");
    
    private final String value;

    LineSeparator(String lineSeparator) {
        this.value = lineSeparator;
    }

    public String getValue() {
        return this.value;
    }
}
