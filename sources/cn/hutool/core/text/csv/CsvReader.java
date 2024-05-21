package cn.hutool.core.text.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import java.io.File;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
/* loaded from: classes.dex */
public class CsvReader extends CsvBaseReader {
    private static final long serialVersionUID = 1;
    private final Reader reader;

    public CsvReader() {
        this(null);
    }

    public CsvReader(CsvReadConfig config) {
        this((Reader) null, config);
    }

    public CsvReader(File file, CsvReadConfig config) {
        this(file, DEFAULT_CHARSET, config);
    }

    public CsvReader(Path path, CsvReadConfig config) {
        this(path, DEFAULT_CHARSET, config);
    }

    public CsvReader(File file, Charset charset, CsvReadConfig config) {
        this(FileUtil.getReader(file, charset), config);
    }

    public CsvReader(Path path, Charset charset, CsvReadConfig config) {
        this(FileUtil.getReader(path, charset), config);
    }

    public CsvReader(Reader reader, CsvReadConfig config) {
        super(config);
        this.reader = reader;
    }

    public CsvData read() throws IORuntimeException {
        return read(this.reader);
    }

    public void read(CsvRowHandler rowHandler) throws IORuntimeException {
        read(this.reader, rowHandler);
    }
}
