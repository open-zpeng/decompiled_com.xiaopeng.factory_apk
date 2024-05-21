package cn.hutool.core.text.replacer;

import cn.hutool.core.text.StrBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class LookupReplacer extends StrReplacer {
    private static final long serialVersionUID = 1;
    private final int maxLength;
    private final int minLength;
    private final Map<String, String> lookupMap = new HashMap();
    private final Set<Character> prefixSet = new HashSet();

    public LookupReplacer(String[]... lookup) {
        int maxLength = 0;
        int minLength = Integer.MAX_VALUE;
        for (String[] pair : lookup) {
            String key = pair[0];
            this.lookupMap.put(key, pair[1]);
            this.prefixSet.add(Character.valueOf(key.charAt(0)));
            int keySize = key.length();
            maxLength = keySize > maxLength ? keySize : maxLength;
            if (keySize < minLength) {
                minLength = keySize;
            }
        }
        this.maxLength = maxLength;
        this.minLength = minLength;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.text.replacer.StrReplacer
    public int replace(CharSequence str, int pos, StrBuilder out) {
        if (this.prefixSet.contains(Character.valueOf(str.charAt(pos)))) {
            int max = this.maxLength;
            if (this.maxLength + pos > str.length()) {
                max = str.length() - pos;
            }
            for (int i = max; i >= this.minLength; i--) {
                CharSequence subSeq = str.subSequence(pos, pos + i);
                String result = this.lookupMap.get(subSeq.toString());
                if (result != null) {
                    out.append((CharSequence) result);
                    return i;
                }
            }
            return 0;
        }
        return 0;
    }
}
