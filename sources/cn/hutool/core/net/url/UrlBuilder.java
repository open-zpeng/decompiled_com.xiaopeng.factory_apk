package cn.hutool.core.net.url;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public final class UrlBuilder implements Serializable {
    private static final String DEFAULT_SCHEME = "http";
    private static final long serialVersionUID = 1;
    private Charset charset;
    private String fragment;
    private String host;
    private UrlPath path;
    private int port;
    private UrlQuery query;
    private String scheme;

    public static UrlBuilder of(URI uri, Charset charset) {
        return of(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getRawQuery(), uri.getFragment(), charset);
    }

    public static UrlBuilder ofHttpWithoutEncode(String httpUrl) {
        return ofHttp(httpUrl, null);
    }

    public static UrlBuilder ofHttp(String httpUrl) {
        return ofHttp(httpUrl, CharsetUtil.CHARSET_UTF_8);
    }

    public static UrlBuilder ofHttp(String httpUrl, Charset charset) {
        Assert.notBlank(httpUrl, "Http url must be not blank!", new Object[0]);
        int sepIndex = httpUrl.indexOf("://");
        if (sepIndex < 0) {
            httpUrl = "http://" + httpUrl.trim();
        }
        return of(httpUrl, charset);
    }

    public static UrlBuilder of(String url) {
        return of(url, CharsetUtil.CHARSET_UTF_8);
    }

    public static UrlBuilder of(String url, Charset charset) {
        Assert.notBlank(url, "Url must be not blank!", new Object[0]);
        return of(URLUtil.url(url.trim()), charset);
    }

    public static UrlBuilder of(URL url, Charset charset) {
        return of(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef(), charset);
    }

    public static UrlBuilder of(String scheme, String host, int port, String path, String query, String fragment, Charset charset) {
        return of(scheme, host, port, UrlPath.of(path, charset), UrlQuery.of(query, charset, false), fragment, charset);
    }

    public static UrlBuilder of(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment, Charset charset) {
        return new UrlBuilder(scheme, host, port, path, query, fragment, charset);
    }

    public static UrlBuilder create() {
        return new UrlBuilder();
    }

    public UrlBuilder() {
        this.port = -1;
        this.charset = CharsetUtil.CHARSET_UTF_8;
    }

    public UrlBuilder(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment, Charset charset) {
        this.port = -1;
        this.charset = charset;
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        setFragment(fragment);
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getSchemeWithDefault() {
        return StrUtil.emptyToDefault(this.scheme, DEFAULT_SCHEME);
    }

    public UrlBuilder setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getHost() {
        return this.host;
    }

    public UrlBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public UrlBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public String getAuthority() {
        if (this.port < 0) {
            return this.host;
        }
        return this.host + ":" + this.port;
    }

    public UrlPath getPath() {
        return this.path;
    }

    public String getPathStr() {
        UrlPath urlPath = this.path;
        return urlPath == null ? "/" : urlPath.build(this.charset);
    }

    public UrlBuilder setPath(UrlPath path) {
        this.path = path;
        return this;
    }

    public UrlBuilder addPath(String segment) {
        if (StrUtil.isBlank(segment)) {
            return this;
        }
        if (this.path == null) {
            this.path = new UrlPath();
        }
        this.path.add(segment);
        return this;
    }

    public UrlBuilder appendPath(CharSequence segment) {
        if (StrUtil.isEmpty(segment)) {
            return this;
        }
        if (this.path == null) {
            this.path = new UrlPath();
        }
        this.path.add(segment);
        return this;
    }

    public UrlQuery getQuery() {
        return this.query;
    }

    public String getQueryStr() {
        UrlQuery urlQuery = this.query;
        if (urlQuery == null) {
            return null;
        }
        return urlQuery.build(this.charset);
    }

    public UrlBuilder setQuery(UrlQuery query) {
        this.query = query;
        return this;
    }

    public UrlBuilder addQuery(String key, String value) {
        if (StrUtil.isEmpty(key)) {
            return this;
        }
        if (this.query == null) {
            this.query = new UrlQuery();
        }
        this.query.add(key, value);
        return this;
    }

    public String getFragment() {
        return this.fragment;
    }

    public String getFragmentEncoded() {
        return URLUtil.encodeFragment(this.fragment, this.charset);
    }

    public UrlBuilder setFragment(String fragment) {
        if (StrUtil.isEmpty(fragment)) {
            this.fragment = null;
        }
        this.fragment = StrUtil.removePrefix(fragment, "#");
        return this;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public UrlBuilder setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public String build() {
        return toURL().toString();
    }

    public URL toURL() {
        return toURL(null);
    }

    public URL toURL(URLStreamHandler handler) {
        StringBuilder fileBuilder = new StringBuilder();
        fileBuilder.append(StrUtil.blankToDefault(getPathStr(), "/"));
        String query = getQueryStr();
        if (StrUtil.isNotBlank(query)) {
            fileBuilder.append('?');
            fileBuilder.append(query);
        }
        if (StrUtil.isNotBlank(this.fragment)) {
            fileBuilder.append('#');
            fileBuilder.append(getFragmentEncoded());
        }
        try {
            return new URL(getSchemeWithDefault(), this.host, this.port, fileBuilder.toString(), handler);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public URI toURI() {
        try {
            return new URI(getSchemeWithDefault(), getAuthority(), getPathStr(), getQueryStr(), getFragmentEncoded());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public String toString() {
        return build();
    }
}
