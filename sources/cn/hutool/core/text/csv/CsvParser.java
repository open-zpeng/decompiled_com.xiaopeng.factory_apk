package cn.hutool.core.text.csv;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class CsvParser implements Closeable, Serializable {
    private static final int DEFAULT_ROW_CAPACITY = 10;
    private static final long serialVersionUID = 1;
    private final CsvReadConfig config;
    private boolean finished;
    private CsvRow header;
    private boolean inQuotes;
    private long inQuotesLineCount;
    private int maxFieldCount;
    private final Reader reader;
    private final Buffer buf = new Buffer(32768);
    private int preChar = -1;
    private final StrBuilder currentField = new StrBuilder(512);
    private long lineNo = -1;
    private int firstLineFieldCount = -1;

    public CsvParser(Reader reader, CsvReadConfig config) {
        this.reader = (Reader) Objects.requireNonNull(reader, "reader must not be null");
        this.config = (CsvReadConfig) ObjectUtil.defaultIfNull(config, CsvReadConfig.defaultConfig());
    }

    public List<String> getHeader() {
        if (!this.config.containsHeader) {
            throw new IllegalStateException("No header available - header parsing is disabled");
        }
        if (this.lineNo < this.config.beginLineNo) {
            throw new IllegalStateException("No header available - call nextRow() first");
        }
        return this.header.fields;
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0099, code lost:
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public cn.hutool.core.text.csv.CsvRow nextRow() throws cn.hutool.core.io.IORuntimeException {
        /*
            r8 = this;
        L0:
            boolean r0 = r8.finished
            r1 = 0
            if (r0 != 0) goto L99
            java.util.List r0 = r8.readLine()
            int r2 = r0.size()
            r3 = 1
            if (r2 >= r3) goto L12
            goto L99
        L12:
            long r4 = r8.lineNo
            cn.hutool.core.text.csv.CsvReadConfig r6 = r8.config
            long r6 = r6.beginLineNo
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 >= 0) goto L1d
            goto L0
        L1d:
            long r4 = r8.lineNo
            cn.hutool.core.text.csv.CsvReadConfig r6 = r8.config
            long r6 = r6.endLineNo
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 <= 0) goto L29
            goto L99
        L29:
            cn.hutool.core.text.csv.CsvReadConfig r4 = r8.config
            boolean r4 = r4.skipEmptyRows
            r5 = 0
            if (r4 == 0) goto L3f
            if (r2 != r3) goto L3f
            java.lang.Object r4 = r0.get(r5)
            java.lang.String r4 = (java.lang.String) r4
            boolean r4 = r4.isEmpty()
            if (r4 == 0) goto L3f
            goto L0
        L3f:
            cn.hutool.core.text.csv.CsvReadConfig r4 = r8.config
            boolean r4 = r4.errorOnDifferentFieldCount
            if (r4 == 0) goto L75
            int r4 = r8.firstLineFieldCount
            if (r4 >= 0) goto L4c
            r8.firstLineFieldCount = r2
            goto L75
        L4c:
            if (r2 != r4) goto L4f
            goto L75
        L4f:
            cn.hutool.core.io.IORuntimeException r1 = new cn.hutool.core.io.IORuntimeException
            r4 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]
            long r6 = r8.lineNo
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            r4[r5] = r6
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)
            r4[r3] = r5
            r3 = 2
            int r5 = r8.firstLineFieldCount
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r3] = r5
            java.lang.String r3 = "Line %d has %d fields, but first line has %d fields"
            java.lang.String r3 = java.lang.String.format(r3, r4)
            r1.<init>(r3)
            throw r1
        L75:
            int r3 = r8.maxFieldCount
            if (r2 <= r3) goto L7b
            r8.maxFieldCount = r2
        L7b:
            cn.hutool.core.text.csv.CsvReadConfig r3 = r8.config
            boolean r3 = r3.containsHeader
            if (r3 == 0) goto L8a
            cn.hutool.core.text.csv.CsvRow r3 = r8.header
            if (r3 != 0) goto L8a
            r8.initHeader(r0)
            goto L0
        L8a:
            cn.hutool.core.text.csv.CsvRow r3 = new cn.hutool.core.text.csv.CsvRow
            long r4 = r8.lineNo
            cn.hutool.core.text.csv.CsvRow r6 = r8.header
            if (r6 != 0) goto L93
            goto L95
        L93:
            java.util.Map<java.lang.String, java.lang.Integer> r1 = r6.headerMap
        L95:
            r3.<init>(r4, r1, r0)
            return r3
        L99:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hutool.core.text.csv.CsvParser.nextRow():cn.hutool.core.text.csv.CsvRow");
    }

    private void initHeader(List<String> currentFields) {
        Map<String, Integer> localHeaderMap = new LinkedHashMap<>(currentFields.size());
        for (int i = 0; i < currentFields.size(); i++) {
            String field = currentFields.get(i);
            if (MapUtil.isNotEmpty(this.config.headerAlias)) {
                field = (String) ObjectUtil.defaultIfNull(this.config.headerAlias.get(field), field);
            }
            if (StrUtil.isNotEmpty(field) && !localHeaderMap.containsKey(field)) {
                localHeaderMap.put(field, Integer.valueOf(i));
            }
        }
        this.header = new CsvRow(this.lineNo, Collections.unmodifiableMap(localHeaderMap), Collections.unmodifiableList(currentFields));
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0046, code lost:
        if (r4 == r13.config.fieldSeparator) goto L23;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<java.lang.String> readLine() throws cn.hutool.core.io.IORuntimeException {
        /*
            Method dump skipped, instructions count: 237
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hutool.core.text.csv.CsvParser.readLine():java.util.List");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.reader.close();
    }

    private void addField(List<String> currentFields, String field) {
        char textDelimiter = this.config.textDelimiter;
        String field2 = StrUtil.replace(StrUtil.unWrap(StrUtil.trim(field, 1, new Predicate() { // from class: cn.hutool.core.text.csv.-$$Lambda$CsvParser$O4-VpdEERyEjSRnHvviafmSYsbU
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return CsvParser.lambda$addField$0((Character) obj);
            }
        }), textDelimiter), "" + textDelimiter + textDelimiter, textDelimiter + "");
        if (this.config.trimField) {
            field2 = StrUtil.trim(field2);
        }
        currentFields.add(field2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$addField$0(Character c) {
        return c.charValue() == '\n' || c.charValue() == '\r';
    }

    private boolean isLineEnd(char c) {
        return (c == '\r' || c == '\n') && this.preChar != 13;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Buffer implements Serializable {
        private static final long serialVersionUID = 1;
        final char[] buf;
        private int limit;
        private int mark;
        private int position;

        Buffer(int capacity) {
            this.buf = new char[capacity];
        }

        public final boolean hasRemaining() {
            return this.position < this.limit;
        }

        int read(Reader reader) {
            try {
                int length = reader.read(this.buf);
                this.mark = 0;
                this.position = 0;
                this.limit = length;
                return length;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }

        char get() {
            char[] cArr = this.buf;
            int i = this.position;
            this.position = i + 1;
            return cArr[i];
        }

        void mark() {
            this.mark = this.position;
        }

        void appendTo(StrBuilder builder, int length) {
            builder.append(this.buf, this.mark, length);
        }
    }
}
