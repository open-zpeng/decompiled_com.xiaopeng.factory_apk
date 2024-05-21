package cn.hutool.core.io.file;

import cn.hutool.core.util.CharsetUtil;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class FileAppender implements Serializable {
    private static final long serialVersionUID = 1;
    private final int capacity;
    private final boolean isNewLineMode;
    private final List<String> list;
    private final FileWriter writer;

    public FileAppender(File destFile, int capacity, boolean isNewLineMode) {
        this(destFile, CharsetUtil.CHARSET_UTF_8, capacity, isNewLineMode);
    }

    public FileAppender(File destFile, Charset charset, int capacity, boolean isNewLineMode) {
        this.list = new ArrayList(100);
        this.capacity = capacity;
        this.isNewLineMode = isNewLineMode;
        this.writer = FileWriter.create(destFile, charset);
    }

    public FileAppender append(String line) {
        if (this.list.size() >= this.capacity) {
            flush();
        }
        this.list.add(line);
        return this;
    }

    public FileAppender flush() {
        PrintWriter pw = this.writer.getPrintWriter(true);
        try {
            for (String str : this.list) {
                pw.print(str);
                if (this.isNewLineMode) {
                    pw.println();
                }
            }
            if (pw != null) {
                pw.close();
            }
            this.list.clear();
            return this;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (pw != null) {
                    try {
                        pw.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }
}
