package cn.hutool.core.net;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class URLEncodeUtil {
    public static String encodeAll(String url) {
        return encodeAll(url, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encodeAll(String url, Charset charset) throws UtilException {
        return URLEncoder.ALL.encode(url, charset);
    }

    public static String encode(String url) throws UtilException {
        return encode(url, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encode(String url, Charset charset) {
        return URLEncoder.DEFAULT.encode(url, charset);
    }

    public static String encodeQuery(String url) throws UtilException {
        return encodeQuery(url, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encodeQuery(String url, Charset charset) {
        return URLEncoder.QUERY.encode(url, charset);
    }

    public static String encodePathSegment(String url) throws UtilException {
        return encodePathSegment(url, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encodePathSegment(String url, Charset charset) {
        if (StrUtil.isEmpty(url)) {
            return url;
        }
        if (charset == null) {
            charset = CharsetUtil.defaultCharset();
        }
        return URLEncoder.PATH_SEGMENT.encode(url, charset);
    }

    public static String encodeFragment(String url) throws UtilException {
        return encodeFragment(url, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encodeFragment(String url, Charset charset) {
        if (StrUtil.isEmpty(url)) {
            return url;
        }
        if (charset == null) {
            charset = CharsetUtil.defaultCharset();
        }
        return URLEncoder.FRAGMENT.encode(url, charset);
    }
}
