package cn.hutool.core.date.format;

import ch.ethz.ssh2.packets.Packets;
import ch.ethz.ssh2.sftp.Packet;
import cn.hutool.core.date.DateException;
import cn.hutool.core.text.CharPool;
import com.xiaopeng.libbluetooth.bean.AudioControl;
import com.xpeng.upso.proxy.ProxyStatusDescription;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes.dex */
public class FastDatePrinter extends AbstractDateBasic implements DatePrinter {
    private static final ConcurrentMap<TimeZoneDisplayKey, String> C_TIME_ZONE_DISPLAY_CACHE = new ConcurrentHashMap(7);
    private static final int MAX_DIGITS = 10;
    private static final long serialVersionUID = -6305750172255764887L;
    private transient int mMaxLengthEstimate;
    private transient Rule[] rules;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface NumberRule extends Rule {
        void appendTo(Appendable appendable, int i) throws IOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface Rule {
        void appendTo(Appendable appendable, Calendar calendar) throws IOException;

        int estimateLength();
    }

    public FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
        super(pattern, timeZone, locale);
        init();
    }

    private void init() {
        List<Rule> rulesList = parsePattern();
        this.rules = (Rule[]) rulesList.toArray(new Rule[0]);
        int len = 0;
        int i = this.rules.length;
        while (true) {
            i--;
            if (i >= 0) {
                len += this.rules[i].estimateLength();
            } else {
                this.mMaxLengthEstimate = len;
                return;
            }
        }
    }

    protected List<Rule> parsePattern() {
        String[] ERAs;
        String[] weekdays;
        boolean z;
        Rule rule;
        DateFormatSymbols symbols = new DateFormatSymbols(this.locale);
        List<Rule> rules = new ArrayList<>();
        String[] ERAs2 = symbols.getEras();
        String[] months = symbols.getMonths();
        String[] shortMonths = symbols.getShortMonths();
        String[] weekdays2 = symbols.getWeekdays();
        String[] shortWeekdays = symbols.getShortWeekdays();
        String[] AmPmStrings = symbols.getAmPmStrings();
        int length = this.pattern.length();
        int[] indexRef = new int[1];
        int i = 0;
        while (i < length) {
            indexRef[0] = i;
            String token = parseToken(this.pattern, indexRef);
            int i2 = indexRef[0];
            int tokenLen = token.length();
            if (tokenLen != 0) {
                char c = token.charAt(0);
                DateFormatSymbols symbols2 = symbols;
                if (c == 'y') {
                    ERAs = ERAs2;
                    weekdays = weekdays2;
                } else {
                    if (c == 'z') {
                        weekdays = weekdays2;
                        if (tokenLen >= 4) {
                            ERAs = ERAs2;
                            rule = new TimeZoneNameRule(this.timeZone, this.locale, 1);
                            z = true;
                        } else {
                            ERAs = ERAs2;
                            rule = new TimeZoneNameRule(this.timeZone, this.locale, 0);
                            z = true;
                        }
                    } else {
                        switch (c) {
                            case '\'':
                                weekdays = weekdays2;
                                String sub = token.substring(1);
                                if (sub.length() == 1) {
                                    rule = new CharacterLiteral(sub.charAt(0));
                                    ERAs = ERAs2;
                                    z = true;
                                    break;
                                } else {
                                    rule = new StringLiteral(sub);
                                    ERAs = ERAs2;
                                    z = true;
                                    break;
                                }
                            case AudioControl.CONTROL_FORWARD /* 75 */:
                                weekdays = weekdays2;
                                rule = selectNumberRule(10, tokenLen);
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case AudioControl.CONTROL_LIST /* 77 */:
                                weekdays = weekdays2;
                                if (tokenLen < 4) {
                                    if (tokenLen == 3) {
                                        rule = new TextField(2, shortMonths);
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    } else if (tokenLen == 2) {
                                        rule = TwoDigitMonthField.INSTANCE;
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    } else {
                                        rule = UnpaddedMonthField.INSTANCE;
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    }
                                } else {
                                    rule = new TextField(2, months);
                                    ERAs = ERAs2;
                                    z = true;
                                    break;
                                }
                            case 'S':
                                weekdays = weekdays2;
                                rule = selectNumberRule(14, tokenLen);
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case Packets.SSH_MSG_CHANNEL_CLOSE /* 97 */:
                                weekdays = weekdays2;
                                rule = new TextField(9, AmPmStrings);
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case 'd':
                                weekdays = weekdays2;
                                rule = selectNumberRule(5, tokenLen);
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case Packet.SSH_FXP_NAME /* 104 */:
                                weekdays = weekdays2;
                                rule = new TwelveHourField(selectNumberRule(10, tokenLen));
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case ProxyStatusDescription.PSD_ERR_VERIFY_FAIL /* 107 */:
                                weekdays = weekdays2;
                                rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case 'm':
                                weekdays = weekdays2;
                                rule = selectNumberRule(12, tokenLen);
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case AudioControl.CONTROL_F3 /* 115 */:
                                weekdays = weekdays2;
                                rule = selectNumberRule(13, tokenLen);
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case AudioControl.CONTROL_F5 /* 117 */:
                                weekdays = weekdays2;
                                rule = new DayInWeekField(selectNumberRule(7, tokenLen));
                                ERAs = ERAs2;
                                z = true;
                                break;
                            case 'w':
                                weekdays = weekdays2;
                                rule = selectNumberRule(3, tokenLen);
                                ERAs = ERAs2;
                                z = true;
                                break;
                            default:
                                switch (c) {
                                    case AudioControl.CONTROL_PLAY /* 68 */:
                                        weekdays = weekdays2;
                                        rule = selectNumberRule(6, tokenLen);
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    case 'E':
                                        weekdays = weekdays2;
                                        rule = new TextField(7, tokenLen < 4 ? shortWeekdays : weekdays);
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    case AudioControl.CONTROL_PAUSE /* 70 */:
                                        weekdays = weekdays2;
                                        rule = selectNumberRule(8, tokenLen);
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    case AudioControl.CONTROL_RECORD /* 71 */:
                                        weekdays = weekdays2;
                                        rule = new TextField(0, ERAs2);
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    case AudioControl.CONTROL_REWIND /* 72 */:
                                        weekdays = weekdays2;
                                        rule = selectNumberRule(11, tokenLen);
                                        ERAs = ERAs2;
                                        z = true;
                                        break;
                                    default:
                                        switch (c) {
                                            case 'W':
                                                weekdays = weekdays2;
                                                rule = selectNumberRule(4, tokenLen);
                                                ERAs = ERAs2;
                                                z = true;
                                                break;
                                            case 'X':
                                                weekdays = weekdays2;
                                                rule = Iso8601_Rule.getRule(tokenLen);
                                                ERAs = ERAs2;
                                                z = true;
                                                break;
                                            case 'Y':
                                                weekdays = weekdays2;
                                                ERAs = ERAs2;
                                                break;
                                            case 'Z':
                                                weekdays = weekdays2;
                                                if (tokenLen == 1) {
                                                    rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                                                    ERAs = ERAs2;
                                                    z = true;
                                                    break;
                                                } else if (tokenLen == 2) {
                                                    rule = Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES;
                                                    ERAs = ERAs2;
                                                    z = true;
                                                    break;
                                                } else {
                                                    rule = TimeZoneNumberRule.INSTANCE_COLON;
                                                    ERAs = ERAs2;
                                                    z = true;
                                                    break;
                                                }
                                            default:
                                                throw new IllegalArgumentException("Illegal pattern component: " + token);
                                        }
                                }
                        }
                    }
                    rules.add(rule);
                    i = i2 + 1;
                    symbols = symbols2;
                    ERAs2 = ERAs;
                    weekdays2 = weekdays;
                }
                if (tokenLen != 2) {
                    z = true;
                    rule = selectNumberRule(1, Math.max(tokenLen, 4));
                } else {
                    rule = TwoDigitYearField.INSTANCE;
                    z = true;
                }
                if (c == 'Y') {
                    rule = new WeekYear((NumberRule) rule);
                }
                rules.add(rule);
                i = i2 + 1;
                symbols = symbols2;
                ERAs2 = ERAs;
                weekdays2 = weekdays;
            } else {
                return rules;
            }
        }
        return rules;
    }

    protected String parseToken(String pattern, int[] indexRef) {
        boolean z;
        StringBuilder buf = new StringBuilder();
        int i = indexRef[0];
        int length = pattern.length();
        char c = pattern.charAt(i);
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            buf.append(c);
            while (i + 1 < length) {
                char peek = pattern.charAt(i + 1);
                if (peek != c) {
                    break;
                }
                buf.append(c);
                i++;
            }
        } else {
            buf.append(CharPool.SINGLE_QUOTE);
            boolean inLiteral = false;
            while (i < length) {
                char c2 = pattern.charAt(i);
                if (c2 == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        i++;
                        buf.append(c2);
                    } else {
                        if (inLiteral) {
                            z = false;
                        } else {
                            z = true;
                        }
                        inLiteral = z;
                    }
                } else if (!inLiteral && ((c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z'))) {
                    i--;
                    break;
                } else {
                    buf.append(c2);
                }
                i++;
            }
        }
        indexRef[0] = i;
        return buf.toString();
    }

    protected NumberRule selectNumberRule(int field, int padding) {
        if (padding != 1) {
            if (padding == 2) {
                return new TwoDigitNumberField(field);
            }
            return new PaddedNumberField(field, padding);
        }
        return new UnpaddedNumberField(field);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String format(Object obj) {
        if (obj instanceof Date) {
            return format((Date) obj);
        }
        if (obj instanceof Calendar) {
            return format((Calendar) obj);
        }
        if (obj instanceof Long) {
            return format(((Long) obj).longValue());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown class: ");
        sb.append(obj == null ? "<null>" : obj.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }

    @Override // cn.hutool.core.date.format.DatePrinter
    public String format(long millis) {
        Calendar c = Calendar.getInstance(this.timeZone, this.locale);
        c.setTimeInMillis(millis);
        return applyRulesToString(c);
    }

    @Override // cn.hutool.core.date.format.DatePrinter
    public String format(Date date) {
        Calendar c = Calendar.getInstance(this.timeZone, this.locale);
        c.setTime(date);
        return applyRulesToString(c);
    }

    @Override // cn.hutool.core.date.format.DatePrinter
    public String format(Calendar calendar) {
        return ((StringBuilder) format(calendar, (Calendar) new StringBuilder(this.mMaxLengthEstimate))).toString();
    }

    @Override // cn.hutool.core.date.format.DatePrinter
    public <B extends Appendable> B format(long millis, B buf) {
        Calendar c = Calendar.getInstance(this.timeZone, this.locale);
        c.setTimeInMillis(millis);
        return (B) applyRules(c, buf);
    }

    @Override // cn.hutool.core.date.format.DatePrinter
    public <B extends Appendable> B format(Date date, B buf) {
        Calendar c = Calendar.getInstance(this.timeZone, this.locale);
        c.setTime(date);
        return (B) applyRules(c, buf);
    }

    @Override // cn.hutool.core.date.format.DatePrinter
    public <B extends Appendable> B format(Calendar calendar, B buf) {
        if (!calendar.getTimeZone().equals(this.timeZone)) {
            calendar = (Calendar) calendar.clone();
            calendar.setTimeZone(this.timeZone);
        }
        return (B) applyRules(calendar, buf);
    }

    private String applyRulesToString(Calendar c) {
        return ((StringBuilder) applyRules(c, new StringBuilder(this.mMaxLengthEstimate))).toString();
    }

    private <B extends Appendable> B applyRules(Calendar calendar, B buf) {
        Rule[] ruleArr;
        try {
            for (Rule rule : this.rules) {
                rule.appendTo(buf, calendar);
            }
            return buf;
        } catch (IOException e) {
            throw new DateException(e);
        }
    }

    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void appendDigits(Appendable buffer, int value) throws IOException {
        buffer.append((char) ((value / 10) + 48));
        buffer.append((char) ((value % 10) + 48));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void appendFullDigits(Appendable buffer, int value, int minFieldWidth) throws IOException {
        if (value < 10000) {
            int nDigits = 4;
            if (value < 1000) {
                nDigits = 4 - 1;
                if (value < 100) {
                    nDigits--;
                    if (value < 10) {
                        nDigits--;
                    }
                }
            }
            for (int i = minFieldWidth - nDigits; i > 0; i--) {
                buffer.append('0');
            }
            if (nDigits != 1) {
                if (nDigits != 2) {
                    if (nDigits != 3) {
                        if (nDigits == 4) {
                            buffer.append((char) ((value / 1000) + 48));
                            value %= 1000;
                        } else {
                            return;
                        }
                    }
                    if (value >= 100) {
                        buffer.append((char) ((value / 100) + 48));
                        value %= 100;
                    } else {
                        buffer.append('0');
                    }
                }
                if (value >= 10) {
                    buffer.append((char) ((value / 10) + 48));
                    value %= 10;
                } else {
                    buffer.append('0');
                }
            }
            buffer.append((char) (value + 48));
            return;
        }
        char[] work = new char[10];
        int digit = 0;
        while (value != 0) {
            work[digit] = (char) ((value % 10) + 48);
            value /= 10;
            digit++;
        }
        while (digit < minFieldWidth) {
            buffer.append('0');
            minFieldWidth--;
        }
        while (true) {
            digit--;
            if (digit >= 0) {
                buffer.append(work[digit]);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CharacterLiteral implements Rule {
        private final char mValue;

        CharacterLiteral(char value) {
            this.mValue = value;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return 1;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(this.mValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StringLiteral implements Rule {
        private final String mValue;

        StringLiteral(String value) {
            this.mValue = value;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mValue.length();
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(this.mValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;

        TextField(int field, String[] values) {
            this.mField = field;
            this.mValues = values;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            int max = 0;
            int i = this.mValues.length;
            while (true) {
                i--;
                if (i >= 0) {
                    int len = this.mValues[i].length();
                    if (len > max) {
                        max = len;
                    }
                } else {
                    return max;
                }
            }
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            buffer.append(this.mValues[calendar.get(this.mField)]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UnpaddedNumberField implements NumberRule {
        private final int mField;

        UnpaddedNumberField(int field) {
            this.mField = field;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return 4;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public final void appendTo(Appendable buffer, int value) throws IOException {
            if (value < 10) {
                buffer.append((char) (value + 48));
            } else if (value < 100) {
                FastDatePrinter.appendDigits(buffer, value);
            } else {
                FastDatePrinter.appendFullDigits(buffer, value, 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        UnpaddedMonthField() {
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(2) + 1);
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public final void appendTo(Appendable buffer, int value) throws IOException {
            if (value >= 10) {
                FastDatePrinter.appendDigits(buffer, value);
            } else {
                buffer.append((char) (value + 48));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;

        PaddedNumberField(int field, int size) {
            if (size < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = field;
            this.mSize = size;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mSize;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public final void appendTo(Appendable buffer, int value) throws IOException {
            FastDatePrinter.appendFullDigits(buffer, value, this.mSize);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TwoDigitNumberField implements NumberRule {
        private final int mField;

        TwoDigitNumberField(int field) {
            this.mField = field;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public final void appendTo(Appendable buffer, int value) throws IOException {
            if (value < 100) {
                FastDatePrinter.appendDigits(buffer, value);
            } else {
                FastDatePrinter.appendFullDigits(buffer, value, 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(1) % 100);
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public final void appendTo(Appendable buffer, int value) throws IOException {
            FastDatePrinter.appendDigits(buffer, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            appendTo(buffer, calendar.get(2) + 1);
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public final void appendTo(Appendable buffer, int value) throws IOException {
            FastDatePrinter.appendDigits(buffer, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;

        TwelveHourField(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(10);
            if (value == 0) {
                value = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public void appendTo(Appendable buffer, int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;

        TwentyFourHourField(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(11);
            if (value == 0) {
                value = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public void appendTo(Appendable buffer, int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DayInWeekField implements NumberRule {
        private final NumberRule mRule;

        DayInWeekField(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int value = calendar.get(7);
            this.mRule.appendTo(buffer, value != 1 ? value - 1 : 7);
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public void appendTo(Appendable buffer, int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class WeekYear implements NumberRule {
        private final NumberRule mRule;

        WeekYear(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            this.mRule.appendTo(buffer, calendar.getWeekYear());
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.NumberRule
        public void appendTo(Appendable buffer, int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }

    static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = C_TIME_ZONE_DISPLAY_CACHE.get(key);
        if (value == null) {
            String value2 = tz.getDisplayName(daylight, style, locale);
            String prior = C_TIME_ZONE_DISPLAY_CACHE.putIfAbsent(key, value2);
            if (prior != null) {
                return prior;
            }
            return value2;
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TimeZoneNameRule implements Rule {
        private final String mDaylight;
        private final Locale mLocale;
        private final String mStandard;
        private final int mStyle;

        TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
            this.mLocale = locale;
            this.mStyle = style;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            TimeZone zone = calendar.getTimeZone();
            if (calendar.get(16) != 0) {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
            } else {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        final boolean mColon;

        TimeZoneNumberRule(boolean colon) {
            this.mColon = colon;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return 5;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int offset = calendar.get(15) + calendar.get(16);
            if (offset < 0) {
                buffer.append(CharPool.DASHED);
                offset = -offset;
            } else {
                buffer.append('+');
            }
            int hours = offset / 3600000;
            FastDatePrinter.appendDigits(buffer, hours);
            if (this.mColon) {
                buffer.append(':');
            }
            int minutes = (offset / 60000) - (hours * 60);
            FastDatePrinter.appendDigits(buffer, minutes);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Iso8601_Rule implements Rule {
        final int length;
        static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
        static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
        static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);

        static Iso8601_Rule getRule(int tokenLen) {
            if (tokenLen != 1) {
                if (tokenLen != 2) {
                    if (tokenLen == 3) {
                        return ISO8601_HOURS_COLON_MINUTES;
                    }
                    throw new IllegalArgumentException("invalid number of X");
                }
                return ISO8601_HOURS_MINUTES;
            }
            return ISO8601_HOURS;
        }

        Iso8601_Rule(int length) {
            this.length = length;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public int estimateLength() {
            return this.length;
        }

        @Override // cn.hutool.core.date.format.FastDatePrinter.Rule
        public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
            int offset = calendar.get(15) + calendar.get(16);
            if (offset == 0) {
                buffer.append("Z");
                return;
            }
            if (offset < 0) {
                buffer.append(CharPool.DASHED);
                offset = -offset;
            } else {
                buffer.append('+');
            }
            int hours = offset / 3600000;
            FastDatePrinter.appendDigits(buffer, hours);
            int i = this.length;
            if (i < 5) {
                return;
            }
            if (i == 6) {
                buffer.append(':');
            }
            int minutes = (offset / 60000) - (hours * 60);
            FastDatePrinter.appendDigits(buffer, minutes);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TimeZoneDisplayKey {
        private final Locale mLocale;
        private final int mStyle;
        private final TimeZone mTimeZone;

        TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
            this.mTimeZone = timeZone;
            if (daylight) {
                this.mStyle = Integer.MIN_VALUE | style;
            } else {
                this.mStyle = style;
            }
            this.mLocale = locale;
        }

        public int hashCode() {
            return (((this.mStyle * 31) + this.mLocale.hashCode()) * 31) + this.mTimeZone.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof TimeZoneDisplayKey) {
                TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
                return this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale);
            }
            return false;
        }
    }
}
