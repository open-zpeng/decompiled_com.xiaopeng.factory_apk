package cn.hutool.core.lang;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class ConsoleTable {
    private static final char COLUMN_LINE = '|';
    private static final char CORNER = '+';
    private static final char LF = '\n';
    private static final char ROW_LINE = '-';
    private static final char SPACE = 12288;
    private List<Integer> columnCharNumber;
    private final List<List<String>> HEADER_LIST = new ArrayList();
    private final List<List<String>> BODY_LIST = new ArrayList();

    public static ConsoleTable create() {
        return new ConsoleTable();
    }

    public ConsoleTable addHeader(String... titles) {
        if (this.columnCharNumber == null) {
            this.columnCharNumber = new ArrayList(Collections.nCopies(titles.length, 0));
        }
        List<String> l = new ArrayList<>();
        fillColumns(l, titles);
        this.HEADER_LIST.add(l);
        return this;
    }

    public ConsoleTable addBody(String... values) {
        List<String> l = new ArrayList<>();
        this.BODY_LIST.add(l);
        fillColumns(l, values);
        return this;
    }

    private void fillColumns(List<String> l, String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            String col = Convert.toSBC(column);
            l.add(col);
            int width = col.length();
            if (width > this.columnCharNumber.get(i).intValue()) {
                this.columnCharNumber.set(i, Integer.valueOf(width));
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        fillBorder(sb);
        fillRow(sb, this.HEADER_LIST);
        fillBorder(sb);
        fillRow(sb, this.BODY_LIST);
        fillBorder(sb);
        return sb.toString();
    }

    private void fillRow(StringBuilder sb, List<List<String>> list) {
        for (List<String> r : list) {
            for (int i = 0; i < r.size(); i++) {
                if (i == 0) {
                    sb.append(COLUMN_LINE);
                }
                String header = r.get(i);
                sb.append(SPACE);
                sb.append(header);
                sb.append(SPACE);
                int l = header.length();
                int lw = this.columnCharNumber.get(i).intValue();
                if (lw > l) {
                    for (int j = 0; j < lw - l; j++) {
                        sb.append(SPACE);
                    }
                }
                sb.append(COLUMN_LINE);
            }
            sb.append('\n');
        }
    }

    private void fillBorder(StringBuilder sb) {
        sb.append(CORNER);
        for (Integer width : this.columnCharNumber) {
            sb.append(Convert.toSBC(StrUtil.fillAfter("", '-', width.intValue() + 2)));
            sb.append(CORNER);
        }
        sb.append('\n');
    }

    public void print() {
        Console.print(toString());
    }
}
