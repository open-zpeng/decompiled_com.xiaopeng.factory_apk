package cn.hutool.core.text.escape;

import cn.hutool.core.text.replacer.LookupReplacer;
import cn.hutool.core.text.replacer.ReplacerChain;
import cn.hutool.core.text.replacer.StrReplacer;
import com.xiaopeng.commonfunc.Constant;
/* loaded from: classes.dex */
public class XmlEscape extends ReplacerChain {
    protected static final String[][] BASIC_ESCAPE = {new String[]{Constant.DOUBLE_QUOTA, "&quot;"}, new String[]{"&", "&amp;"}, new String[]{"<", "&lt;"}, new String[]{">", "&gt;"}};
    private static final long serialVersionUID = 1;

    public XmlEscape() {
        super(new StrReplacer[0]);
        addChain((StrReplacer) new LookupReplacer(BASIC_ESCAPE));
    }
}
