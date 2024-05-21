package cn.hutool.core.text.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import java.io.Closeable;
import java.io.File;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class CsvBaseReader implements Serializable {
    protected static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;
    private static final long serialVersionUID = 1;
    private final CsvReadConfig config;

    public CsvBaseReader() {
        this(null);
    }

    public CsvBaseReader(CsvReadConfig config) {
        this.config = (CsvReadConfig) ObjectUtil.defaultIfNull(config, CsvReadConfig.defaultConfig());
    }

    public void setFieldSeparator(char fieldSeparator) {
        this.config.setFieldSeparator(fieldSeparator);
    }

    public void setTextDelimiter(char textDelimiter) {
        this.config.setTextDelimiter(textDelimiter);
    }

    public void setContainsHeader(boolean containsHeader) {
        this.config.setContainsHeader(containsHeader);
    }

    public void setSkipEmptyRows(boolean skipEmptyRows) {
        this.config.setSkipEmptyRows(skipEmptyRows);
    }

    public void setErrorOnDifferentFieldCount(boolean errorOnDifferentFieldCount) {
        this.config.setErrorOnDifferentFieldCount(errorOnDifferentFieldCount);
    }

    public CsvData read(File file) throws IORuntimeException {
        return read(file, DEFAULT_CHARSET);
    }

    public CsvData readFromStr(String csvStr) {
        return read(new StringReader(csvStr));
    }

    public void readFromStr(String csvStr, CsvRowHandler rowHandler) {
        read(parse(new StringReader(csvStr)), rowHandler);
    }

    public CsvData read(File file, Charset charset) throws IORuntimeException {
        return read((Path) Objects.requireNonNull(file.toPath(), "file must not be null"), charset);
    }

    public CsvData read(Path path) throws IORuntimeException {
        return read(path, DEFAULT_CHARSET);
    }

    public CsvData read(Path path, Charset charset) throws IORuntimeException {
        Assert.notNull(path, "path must not be null", new Object[0]);
        return read(FileUtil.getReader(path, charset));
    }

    public CsvData read(Reader reader) throws IORuntimeException {
        CsvParser csvParser = parse(reader);
        final List<CsvRow> rows = new ArrayList<>();
        rows.getClass();
        read(csvParser, new CsvRowHandler() { // from class: cn.hutool.core.text.csv.-$$Lambda$xhuLeBOuzkbX_HtdKCMYXSbnEE8
            @Override // cn.hutool.core.text.csv.CsvRowHandler
            public final void handle(CsvRow csvRow) {
                rows.add(csvRow);
            }
        });
        List<String> header = this.config.containsHeader ? csvParser.getHeader() : null;
        return new CsvData(header, rows);
    }

    public List<Map<String, String>> readMapList(Reader reader) throws IORuntimeException {
        this.config.setContainsHeader(true);
        final List<Map<String, String>> result = new ArrayList<>();
        read(reader, new CsvRowHandler() { // from class: cn.hutool.core.text.csv.-$$Lambda$CsvBaseReader$0aTRjtX4S0VIflbiCJsT-swOfxE
            @Override // cn.hutool.core.text.csv.CsvRowHandler
            public final void handle(CsvRow csvRow) {
                result.add(csvRow.getFieldMap());
            }
        });
        return result;
    }

    public <T> List<T> read(Reader reader, final Class<T> clazz) {
        this.config.setContainsHeader(true);
        final List<T> result = new ArrayList<>();
        read(reader, new CsvRowHandler() { // from class: cn.hutool.core.text.csv.-$$Lambda$CsvBaseReader$gNgDc1utk0JFoJWyZYIjH3fiFXA
            @Override // cn.hutool.core.text.csv.CsvRowHandler
            public final void handle(CsvRow csvRow) {
                result.add(csvRow.toBean(clazz));
            }
        });
        return result;
    }

    public <T> List<T> read(String csvStr, final Class<T> clazz) {
        this.config.setContainsHeader(true);
        final List<T> result = new ArrayList<>();
        read(new StringReader(csvStr), new CsvRowHandler() { // from class: cn.hutool.core.text.csv.-$$Lambda$CsvBaseReader$Ro6jcvlfQWSUI8ux9HIWtjH6ckQ
            @Override // cn.hutool.core.text.csv.CsvRowHandler
            public final void handle(CsvRow csvRow) {
                result.add(csvRow.toBean(clazz));
            }
        });
        return result;
    }

    public void read(Reader reader, CsvRowHandler rowHandler) throws IORuntimeException {
        read(parse(reader), rowHandler);
    }

    private void read(CsvParser csvParser, CsvRowHandler rowHandler) throws IORuntimeException {
        while (true) {
            try {
                CsvRow csvRow = csvParser.nextRow();
                if (csvRow != null) {
                    rowHandler.handle(csvRow);
                } else {
                    return;
                }
            } finally {
                IoUtil.close((Closeable) csvParser);
            }
        }
    }

    private CsvParser parse(Reader reader) throws IORuntimeException {
        return new CsvParser(reader, this.config);
    }
}
