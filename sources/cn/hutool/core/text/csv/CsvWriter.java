package cn.hutool.core.text.csv;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class CsvWriter implements Closeable, Flushable, Serializable {
    private static final long serialVersionUID = 1;
    private final CsvWriteConfig config;
    private boolean newline;
    private final Writer writer;

    public CsvWriter(String filePath) {
        this(FileUtil.file(filePath));
    }

    public CsvWriter(File file) {
        this(file, CharsetUtil.CHARSET_UTF_8);
    }

    public CsvWriter(String filePath, Charset charset) {
        this(FileUtil.file(filePath), charset);
    }

    public CsvWriter(File file, Charset charset) {
        this(file, charset, false);
    }

    public CsvWriter(String filePath, Charset charset, boolean isAppend) {
        this(FileUtil.file(filePath), charset, isAppend);
    }

    public CsvWriter(File file, Charset charset, boolean isAppend) {
        this(file, charset, isAppend, (CsvWriteConfig) null);
    }

    public CsvWriter(String filePath, Charset charset, boolean isAppend, CsvWriteConfig config) {
        this(FileUtil.file(filePath), charset, isAppend, config);
    }

    public CsvWriter(File file, Charset charset, boolean isAppend, CsvWriteConfig config) {
        this(FileUtil.getWriter(file, charset, isAppend), config);
    }

    public CsvWriter(Writer writer) {
        this(writer, (CsvWriteConfig) null);
    }

    public CsvWriter(Writer writer, CsvWriteConfig config) {
        this.newline = true;
        this.writer = writer instanceof BufferedWriter ? writer : new BufferedWriter(writer);
        this.config = (CsvWriteConfig) ObjectUtil.defaultIfNull(config, CsvWriteConfig.defaultConfig());
    }

    public CsvWriter setAlwaysDelimitText(boolean alwaysDelimitText) {
        this.config.setAlwaysDelimitText(alwaysDelimitText);
        return this;
    }

    public CsvWriter setLineDelimiter(char[] lineDelimiter) {
        this.config.setLineDelimiter(lineDelimiter);
        return this;
    }

    public CsvWriter write(String[]... lines) throws IORuntimeException {
        if (ArrayUtil.isNotEmpty((Object[]) lines)) {
            for (String[] values : lines) {
                appendLine(values);
            }
            flush();
        }
        return this;
    }

    public CsvWriter write(Iterable<?> lines) throws IORuntimeException {
        if (CollUtil.isNotEmpty(lines)) {
            for (Object values : lines) {
                appendLine(Convert.toStrArray(values));
            }
            flush();
        }
        return this;
    }

    public CsvWriter write(CsvData csvData) {
        if (csvData != null) {
            List<String> header = csvData.getHeader();
            if (CollUtil.isNotEmpty((Collection<?>) header)) {
                writeHeaderLine((String[]) header.toArray(new String[0]));
            }
            write(csvData.getRows());
            flush();
        }
        return this;
    }

    public CsvWriter writeBeans(Iterable<?> beans) {
        if (CollUtil.isNotEmpty(beans)) {
            boolean isFirst = true;
            for (Object bean : beans) {
                Map<String, Object> map = BeanUtil.beanToMap(bean);
                if (isFirst) {
                    writeHeaderLine((String[]) map.keySet().toArray(new String[0]));
                    isFirst = false;
                }
                writeLine(Convert.toStrArray(map.values()));
            }
            flush();
        }
        return this;
    }

    public CsvWriter writeHeaderLine(String... fields) throws IORuntimeException {
        Map<String, String> headerAlias = this.config.headerAlias;
        if (MapUtil.isNotEmpty(headerAlias)) {
            for (int i = 0; i < fields.length; i++) {
                String alias = headerAlias.get(fields[i]);
                if (alias != null) {
                    fields[i] = alias;
                }
            }
        }
        return writeLine(fields);
    }

    public CsvWriter writeLine(String... fields) throws IORuntimeException {
        if (ArrayUtil.isEmpty((Object[]) fields)) {
            return writeLine();
        }
        appendLine(fields);
        return this;
    }

    public CsvWriter writeLine() throws IORuntimeException {
        try {
            this.writer.write(this.config.lineDelimiter);
            this.newline = true;
            return this;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public CsvWriter writeComment(String comment) {
        try {
            this.writer.write(this.config.commentCharacter);
            this.writer.write(comment);
            this.writer.write(this.config.lineDelimiter);
            this.newline = true;
            return this;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        IoUtil.close((Closeable) this.writer);
    }

    @Override // java.io.Flushable
    public void flush() throws IORuntimeException {
        try {
            this.writer.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private void appendLine(String... fields) throws IORuntimeException {
        try {
            doAppendLine(fields);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private void doAppendLine(String... fields) throws IOException {
        if (fields != null) {
            for (String field : fields) {
                appendField(field);
            }
            this.writer.write(this.config.lineDelimiter);
            this.newline = true;
        }
    }

    private void appendField(String value) throws IOException {
        boolean alwaysDelimitText = this.config.alwaysDelimitText;
        char textDelimiter = this.config.textDelimiter;
        char fieldSeparator = this.config.fieldSeparator;
        if (!this.newline) {
            this.writer.write(fieldSeparator);
        } else {
            this.newline = false;
        }
        if (value == null) {
            if (alwaysDelimitText) {
                this.writer.write(new char[]{textDelimiter, textDelimiter});
                return;
            }
            return;
        }
        char[] valueChars = value.toCharArray();
        boolean containsTextDelimiter = false;
        int length = valueChars.length;
        boolean needsTextDelimiter = alwaysDelimitText;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            char c = valueChars[i];
            if (c == textDelimiter) {
                needsTextDelimiter = true;
                containsTextDelimiter = true;
                break;
            }
            if (c == fieldSeparator || c == '\n' || c == '\r') {
                needsTextDelimiter = true;
            }
            i++;
        }
        if (needsTextDelimiter) {
            this.writer.write(textDelimiter);
        }
        if (containsTextDelimiter) {
            for (char c2 : valueChars) {
                if (c2 == textDelimiter) {
                    this.writer.write(textDelimiter);
                }
                this.writer.write(c2);
            }
        } else {
            this.writer.write(valueChars);
        }
        if (needsTextDelimiter) {
            this.writer.write(textDelimiter);
        }
    }
}
