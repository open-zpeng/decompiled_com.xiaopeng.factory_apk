package cn.hutool.core.date.format;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;
/* loaded from: classes.dex */
public abstract class AbstractDateBasic implements DateBasic, Serializable {
    private static final long serialVersionUID = 6333136319870641818L;
    protected final Locale locale;
    protected final String pattern;
    protected final TimeZone timeZone;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDateBasic(String pattern, TimeZone timeZone, Locale locale) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale;
    }

    @Override // cn.hutool.core.date.format.DateBasic
    public String getPattern() {
        return this.pattern;
    }

    @Override // cn.hutool.core.date.format.DateBasic
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    @Override // cn.hutool.core.date.format.DateBasic
    public Locale getLocale() {
        return this.locale;
    }

    public boolean equals(Object obj) {
        if (obj instanceof FastDatePrinter) {
            AbstractDateBasic other = (AbstractDateBasic) obj;
            return this.pattern.equals(other.pattern) && this.timeZone.equals(other.timeZone) && this.locale.equals(other.locale);
        }
        return false;
    }

    public int hashCode() {
        return this.pattern.hashCode() + ((this.timeZone.hashCode() + (this.locale.hashCode() * 13)) * 13);
    }

    public String toString() {
        return "FastDatePrinter[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
    }
}
