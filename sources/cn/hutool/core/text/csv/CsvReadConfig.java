package cn.hutool.core.text.csv;

import java.io.Serializable;
/* loaded from: classes.dex */
public class CsvReadConfig extends CsvConfig<CsvReadConfig> implements Serializable {
    private static final long serialVersionUID = 5396453565371560052L;
    protected long beginLineNo;
    protected boolean containsHeader;
    protected boolean errorOnDifferentFieldCount;
    protected boolean trimField;
    protected boolean skipEmptyRows = true;
    protected long endLineNo = 9223372036854775806L;

    public static CsvReadConfig defaultConfig() {
        return new CsvReadConfig();
    }

    public CsvReadConfig setContainsHeader(boolean containsHeader) {
        this.containsHeader = containsHeader;
        return this;
    }

    public CsvReadConfig setSkipEmptyRows(boolean skipEmptyRows) {
        this.skipEmptyRows = skipEmptyRows;
        return this;
    }

    public CsvReadConfig setErrorOnDifferentFieldCount(boolean errorOnDifferentFieldCount) {
        this.errorOnDifferentFieldCount = errorOnDifferentFieldCount;
        return this;
    }

    public CsvReadConfig setBeginLineNo(long beginLineNo) {
        this.beginLineNo = beginLineNo;
        return this;
    }

    public CsvReadConfig setEndLineNo(long endLineNo) {
        this.endLineNo = endLineNo;
        return this;
    }

    public CsvReadConfig setTrimField(boolean trimField) {
        this.trimField = trimField;
        return this;
    }
}
