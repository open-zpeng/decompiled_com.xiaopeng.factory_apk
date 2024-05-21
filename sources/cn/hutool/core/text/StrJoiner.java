package cn.hutool.core.text;

import cn.hutool.core.collection.ArrayIter;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Function;
/* loaded from: classes.dex */
public class StrJoiner implements Appendable, Serializable {
    private static final long serialVersionUID = 1;
    private Appendable appendable;
    private CharSequence delimiter;
    private String emptyResult;
    private boolean hasContent;
    private NullMode nullMode;
    private CharSequence prefix;
    private CharSequence suffix;
    private boolean wrapElement;

    /* loaded from: classes.dex */
    public enum NullMode {
        IGNORE,
        TO_EMPTY,
        NULL_STRING
    }

    public static StrJoiner of(StrJoiner joiner) {
        StrJoiner joinerNew = new StrJoiner(joiner.delimiter, joiner.prefix, joiner.suffix);
        joinerNew.wrapElement = joiner.wrapElement;
        joinerNew.nullMode = joiner.nullMode;
        joinerNew.emptyResult = joiner.emptyResult;
        return joinerNew;
    }

    public static StrJoiner of(CharSequence delimiter) {
        return new StrJoiner(delimiter);
    }

    public static StrJoiner of(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return new StrJoiner(delimiter, prefix, suffix);
    }

    public StrJoiner(CharSequence delimiter) {
        this(null, delimiter);
    }

    public StrJoiner(Appendable appendable, CharSequence delimiter) {
        this(appendable, delimiter, null, null);
    }

    public StrJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        this(null, delimiter, prefix, suffix);
    }

    public StrJoiner(Appendable appendable, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        this.nullMode = NullMode.NULL_STRING;
        this.emptyResult = "";
        if (appendable != null) {
            this.appendable = appendable;
            checkHasContent(appendable);
        }
        this.delimiter = delimiter;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public StrJoiner setDelimiter(CharSequence delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public StrJoiner setPrefix(CharSequence prefix) {
        this.prefix = prefix;
        return this;
    }

    public StrJoiner setSuffix(CharSequence suffix) {
        this.suffix = suffix;
        return this;
    }

    public StrJoiner setWrapElement(boolean wrapElement) {
        this.wrapElement = wrapElement;
        return this;
    }

    public StrJoiner setNullMode(NullMode nullMode) {
        this.nullMode = nullMode;
        return this;
    }

    public StrJoiner setEmptyResult(String emptyResult) {
        this.emptyResult = emptyResult;
        return this;
    }

    public <T> StrJoiner append(Object obj) {
        if (obj == null) {
            append((CharSequence) null);
        } else if (ArrayUtil.isArray(obj)) {
            append((Iterator) new ArrayIter(obj));
        } else if (obj instanceof Iterator) {
            append((Iterator) ((Iterator) obj));
        } else if (obj instanceof Iterable) {
            append((Iterator) ((Iterable) obj).iterator());
        } else {
            append((CharSequence) String.valueOf(obj));
        }
        return this;
    }

    public <T> StrJoiner append(T[] array) {
        if (array == null) {
            return this;
        }
        return append((Iterator) new ArrayIter((Object[]) array));
    }

    public <T> StrJoiner append(Iterator<T> iterator) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                append(iterator.next());
            }
        }
        return this;
    }

    public <T> StrJoiner append(T[] array, Function<T, ? extends CharSequence> toStrFunc) {
        return append((Iterator) new ArrayIter((Object[]) array), (Function) toStrFunc);
    }

    public <T> StrJoiner append(Iterable<T> iterable, Function<T, ? extends CharSequence> toStrFunc) {
        return append(IterUtil.getIter(iterable), toStrFunc);
    }

    public <T> StrJoiner append(Iterator<T> iterator, Function<T, ? extends CharSequence> toStrFunc) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                append(toStrFunc.apply(iterator.next()));
            }
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: cn.hutool.core.text.StrJoiner$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$hutool$core$text$StrJoiner$NullMode = new int[NullMode.values().length];

        static {
            try {
                $SwitchMap$cn$hutool$core$text$StrJoiner$NullMode[NullMode.IGNORE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$hutool$core$text$StrJoiner$NullMode[NullMode.TO_EMPTY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$cn$hutool$core$text$StrJoiner$NullMode[NullMode.NULL_STRING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    @Override // java.lang.Appendable
    public StrJoiner append(CharSequence csq) {
        if (csq == null) {
            int i = AnonymousClass1.$SwitchMap$cn$hutool$core$text$StrJoiner$NullMode[this.nullMode.ordinal()];
            if (i == 1) {
                return this;
            }
            if (i == 2) {
                csq = "";
            } else if (i == 3) {
                csq = CharSequenceUtil.NULL;
            }
        }
        try {
            Appendable appendable = prepare();
            if (this.wrapElement && StrUtil.isNotEmpty(this.prefix)) {
                appendable.append(this.prefix);
            }
            appendable.append(csq);
            if (this.wrapElement && StrUtil.isNotEmpty(this.suffix)) {
                appendable.append(this.suffix);
            }
            return this;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override // java.lang.Appendable
    public StrJoiner append(CharSequence csq, int startInclude, int endExclude) {
        return append((CharSequence) StrUtil.sub(csq, startInclude, endExclude));
    }

    @Override // java.lang.Appendable
    public StrJoiner append(char c) {
        return append((CharSequence) String.valueOf(c));
    }

    public String toString() {
        if (this.appendable == null) {
            return this.emptyResult;
        }
        if (!this.wrapElement && StrUtil.isNotEmpty(this.suffix)) {
            try {
                this.appendable.append(this.suffix);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
        return this.appendable.toString();
    }

    private Appendable prepare() throws IOException {
        if (this.hasContent) {
            this.appendable.append(this.delimiter);
        } else {
            if (this.appendable == null) {
                this.appendable = new StringBuilder();
            }
            if (!this.wrapElement && StrUtil.isNotEmpty(this.prefix)) {
                this.appendable.append(this.prefix);
            }
            this.hasContent = true;
        }
        return this.appendable;
    }

    private void checkHasContent(Appendable appendable) {
        if (appendable instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) appendable;
            if (charSequence.length() > 0 && StrUtil.endWith(charSequence, this.delimiter)) {
                this.hasContent = true;
                return;
            }
            return;
        }
        String initStr = appendable.toString();
        if (StrUtil.isNotEmpty(initStr) && !StrUtil.endWith(initStr, this.delimiter)) {
            this.hasContent = true;
        }
    }
}
