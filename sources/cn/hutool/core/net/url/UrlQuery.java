package cn.hutool.core.net.url;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.TableMap;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.xiaopeng.commonfunc.Constant;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public class UrlQuery {
    private final TableMap<CharSequence, CharSequence> query;

    public static UrlQuery of(Map<? extends CharSequence, ?> queryMap) {
        return new UrlQuery(queryMap);
    }

    public static UrlQuery of(String queryStr, Charset charset) {
        return of(queryStr, charset, true);
    }

    public static UrlQuery of(String queryStr, Charset charset, boolean autoRemovePath) {
        UrlQuery urlQuery = new UrlQuery();
        urlQuery.parse(queryStr, charset, autoRemovePath);
        return urlQuery;
    }

    public UrlQuery() {
        this(null);
    }

    public UrlQuery(Map<? extends CharSequence, ?> queryMap) {
        if (MapUtil.isNotEmpty(queryMap)) {
            this.query = new TableMap<>(queryMap.size());
            addAll(queryMap);
            return;
        }
        this.query = new TableMap<>(16);
    }

    public UrlQuery add(CharSequence key, Object value) {
        this.query.put(key, toStr(value));
        return this;
    }

    public UrlQuery addAll(Map<? extends CharSequence, ?> queryMap) {
        if (MapUtil.isNotEmpty(queryMap)) {
            queryMap.forEach(new BiConsumer() { // from class: cn.hutool.core.net.url.-$$Lambda$e0aPaCaoWeY95GjAvH8_7A30GJo
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    UrlQuery.this.add((CharSequence) obj, obj2);
                }
            });
        }
        return this;
    }

    public UrlQuery parse(String queryStr, Charset charset) {
        return parse(queryStr, charset, true);
    }

    public UrlQuery parse(String queryStr, Charset charset, boolean autoRemovePath) {
        int pathEndPos;
        if (StrUtil.isBlank(queryStr)) {
            return this;
        }
        if (autoRemovePath && (pathEndPos = queryStr.indexOf(63)) > -1) {
            queryStr = StrUtil.subSuf(queryStr, pathEndPos + 1);
            if (StrUtil.isBlank(queryStr)) {
                return this;
            }
        }
        int len = queryStr.length();
        String name = null;
        int pos = 0;
        int i = 0;
        while (i < len) {
            char c = queryStr.charAt(i);
            if (c != '&') {
                if (c == '=' && name == null) {
                    name = queryStr.substring(pos, i);
                    pos = i + 1;
                }
            } else {
                addParam(name, queryStr.substring(pos, i), charset);
                name = null;
                if (i + 4 < len && "amp;".equals(queryStr.substring(i + 1, i + 5))) {
                    i += 4;
                }
                pos = i + 1;
            }
            i++;
        }
        if (i - pos == len && (queryStr.startsWith("http") || queryStr.contains("/"))) {
            return this;
        }
        addParam(name, queryStr.substring(pos, i), charset);
        return this;
    }

    public Map<CharSequence, CharSequence> getQueryMap() {
        return MapUtil.unmodifiable(this.query);
    }

    public CharSequence get(CharSequence key) {
        if (MapUtil.isEmpty(this.query)) {
            return null;
        }
        return this.query.get(key);
    }

    public String build(Charset charset) {
        return build(charset, true);
    }

    public String build(Charset charset, boolean isEncode) {
        if (MapUtil.isEmpty(this.query)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        Iterator<Map.Entry<CharSequence, CharSequence>> it = this.query.iterator();
        while (it.hasNext()) {
            Map.Entry<CharSequence, CharSequence> entry = it.next();
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }
            CharSequence key = entry.getKey();
            if (key != null) {
                sb.append(toStr(key, charset, isEncode));
                CharSequence value = entry.getValue();
                if (value != null) {
                    sb.append(Constant.EQUALS_STRING);
                    sb.append(toStr(value, charset, isEncode));
                }
            }
        }
        return sb.toString();
    }

    public String toString() {
        return build(null);
    }

    private static String toStr(Object value) {
        if (value instanceof Iterable) {
            String result = CollUtil.join((Iterable) value, ",");
            return result;
        } else if (value instanceof Iterator) {
            String result2 = IterUtil.join((Iterator) value, ",");
            return result2;
        } else {
            String result3 = Convert.toStr(value);
            return result3;
        }
    }

    private void addParam(String key, String value, Charset charset) {
        if (key != null) {
            String actualKey = URLUtil.decode(key, charset);
            this.query.put(actualKey, StrUtil.nullToEmpty(URLUtil.decode(value, charset)));
        } else if (value != null) {
            this.query.put(URLUtil.decode(value, charset), null);
        }
    }

    private static String toStr(CharSequence str, Charset charset, boolean isEncode) {
        String result = StrUtil.str(str);
        if (isEncode) {
            return URLUtil.encodeAll(result, charset);
        }
        return result;
    }
}
