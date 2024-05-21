package cn.hutool.core.text.replacer;

import cn.hutool.core.lang.Replacer;
import cn.hutool.core.text.StrBuilder;
import java.io.Serializable;
/* loaded from: classes.dex */
public abstract class StrReplacer implements Replacer<CharSequence>, Serializable {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract int replace(CharSequence charSequence, int i, StrBuilder strBuilder);

    @Override // cn.hutool.core.lang.Replacer
    public CharSequence replace(CharSequence t) {
        int len = t.length();
        StrBuilder builder = StrBuilder.create(len);
        int pos = 0;
        while (pos < len) {
            int consumed = replace(t, pos, builder);
            if (consumed == 0) {
                builder.append(t.charAt(pos));
                pos++;
            }
            pos += consumed;
        }
        return builder;
    }
}
