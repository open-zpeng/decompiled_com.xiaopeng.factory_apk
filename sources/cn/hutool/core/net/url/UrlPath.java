package cn.hutool.core.net.url;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes.dex */
public class UrlPath {
    private List<String> segments;
    private boolean withEngTag;

    public static UrlPath of(String pathStr, Charset charset) {
        UrlPath urlPath = new UrlPath();
        urlPath.parse(pathStr, charset);
        return urlPath;
    }

    public UrlPath setWithEndTag(boolean withEngTag) {
        this.withEngTag = withEngTag;
        return this;
    }

    public List<String> getSegments() {
        return this.segments;
    }

    public String getSegment(int index) {
        List<String> list = this.segments;
        if (list == null || index >= list.size()) {
            return null;
        }
        return this.segments.get(index);
    }

    public UrlPath add(CharSequence segment) {
        addInternal(fixPath(segment), false);
        return this;
    }

    public UrlPath addBefore(CharSequence segment) {
        addInternal(fixPath(segment), true);
        return this;
    }

    public UrlPath parse(String path, Charset charset) {
        if (StrUtil.isNotEmpty(path)) {
            if (StrUtil.endWith((CharSequence) path, '/')) {
                this.withEngTag = true;
            }
            List<String> split = StrUtil.split((CharSequence) fixPath(path), '/');
            for (String seg : split) {
                addInternal(URLDecoder.decodeForPath(seg, charset), false);
            }
        }
        return this;
    }

    public String build(Charset charset) {
        if (CollUtil.isEmpty((Collection<?>) this.segments)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String segment : this.segments) {
            builder.append('/');
            builder.append(URLUtil.encodePathSegment(segment, charset));
        }
        if (this.withEngTag || StrUtil.isEmpty(builder)) {
            builder.append('/');
        }
        return builder.toString();
    }

    public String toString() {
        return build(null);
    }

    private void addInternal(CharSequence segment, boolean before) {
        if (this.segments == null) {
            this.segments = new LinkedList();
        }
        String seg = StrUtil.str(segment);
        if (before) {
            this.segments.add(0, seg);
        } else {
            this.segments.add(seg);
        }
    }

    private static String fixPath(CharSequence path) {
        Assert.notNull(path, "Path segment must be not null!", new Object[0]);
        if ("/".contentEquals(path)) {
            return "";
        }
        String segmentStr = StrUtil.trim(path);
        return StrUtil.trim(StrUtil.removeSuffix(StrUtil.removePrefix(segmentStr, "/"), "/"));
    }
}
