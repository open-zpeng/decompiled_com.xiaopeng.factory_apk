package io.sentry.transport;

import com.lzy.okgo.model.HttpHeaders;
import io.sentry.ILogger;
import io.sentry.RequestDetails;
import io.sentry.SentryEnvelope;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class HttpConnection {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @NotNull
    private final SentryOptions options;
    @Nullable
    private final Proxy proxy;
    @NotNull
    private final RateLimiter rateLimiter;
    @NotNull
    private final RequestDetails requestDetails;

    public HttpConnection(@NotNull SentryOptions options, @NotNull RequestDetails requestDetails, @NotNull RateLimiter rateLimiter) {
        this(options, requestDetails, AuthenticatorWrapper.getInstance(), rateLimiter);
    }

    HttpConnection(@NotNull SentryOptions options, @NotNull RequestDetails requestDetails, @NotNull AuthenticatorWrapper authenticatorWrapper, @NotNull RateLimiter rateLimiter) {
        this.requestDetails = requestDetails;
        this.options = options;
        this.rateLimiter = rateLimiter;
        this.proxy = resolveProxy(options.getProxy());
        if (this.proxy != null && options.getProxy() != null) {
            String proxyUser = options.getProxy().getUser();
            String proxyPassword = options.getProxy().getPass();
            if (proxyUser != null && proxyPassword != null) {
                authenticatorWrapper.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));
            }
        }
    }

    @Nullable
    private Proxy resolveProxy(@Nullable SentryOptions.Proxy optionsProxy) {
        if (optionsProxy == null) {
            return null;
        }
        String port = optionsProxy.getPort();
        String host = optionsProxy.getHost();
        if (port == null || host == null) {
            return null;
        }
        try {
            InetSocketAddress proxyAddr = new InetSocketAddress(host, Integer.parseInt(port));
            Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
            return proxy;
        } catch (NumberFormatException e) {
            ILogger logger = this.options.getLogger();
            SentryLevel sentryLevel = SentryLevel.ERROR;
            logger.log(sentryLevel, e, "Failed to parse Sentry Proxy port: " + optionsProxy.getPort() + ". Proxy is ignored", new Object[0]);
            return null;
        }
    }

    @NotNull
    HttpURLConnection open() throws IOException {
        URLConnection openConnection;
        if (this.proxy == null) {
            openConnection = this.requestDetails.getUrl().openConnection();
        } else {
            openConnection = this.requestDetails.getUrl().openConnection(this.proxy);
        }
        return (HttpURLConnection) openConnection;
    }

    @NotNull
    private HttpURLConnection createConnection() throws IOException {
        HttpURLConnection connection = open();
        for (Map.Entry<String, String> header : this.requestDetails.getHeaders().entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Encoding", "gzip");
        connection.setRequestProperty("Content-Type", "application/x-sentry-envelope");
        connection.setRequestProperty(HttpHeaders.HEAD_KEY_ACCEPT, "application/json");
        connection.setRequestProperty(HttpHeaders.HEAD_KEY_CONNECTION, HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE);
        connection.setConnectTimeout(this.options.getConnectionTimeoutMillis());
        connection.setReadTimeout(this.options.getReadTimeoutMillis());
        HostnameVerifier hostnameVerifier = this.options.getHostnameVerifier();
        if ((connection instanceof HttpsURLConnection) && hostnameVerifier != null) {
            ((HttpsURLConnection) connection).setHostnameVerifier(hostnameVerifier);
        }
        SSLSocketFactory sslSocketFactory = this.options.getSslSocketFactory();
        if ((connection instanceof HttpsURLConnection) && sslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
        }
        connection.connect();
        return connection;
    }

    @NotNull
    public TransportResult send(@NotNull SentryEnvelope envelope) throws IOException {
        TransportResult result;
        OutputStream outputStream;
        GZIPOutputStream gzip;
        HttpURLConnection connection = createConnection();
        try {
            outputStream = connection.getOutputStream();
            gzip = new GZIPOutputStream(outputStream);
        } finally {
            try {
                return result;
            } finally {
            }
        }
        try {
            this.options.getSerializer().serialize(envelope, gzip);
            gzip.close();
            if (outputStream != null) {
                outputStream.close();
            }
            return result;
        } catch (Throwable th) {
            try {
                gzip.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @NotNull
    private TransportResult readAndLog(@NotNull HttpURLConnection connection) {
        try {
            try {
                int responseCode = connection.getResponseCode();
                updateRetryAfterLimits(connection, responseCode);
                if (isSuccessfulResponseCode(responseCode)) {
                    this.options.getLogger().log(SentryLevel.DEBUG, "Envelope sent successfully.", new Object[0]);
                    return TransportResult.success();
                }
                this.options.getLogger().log(SentryLevel.ERROR, "Request failed, API returned %s", Integer.valueOf(responseCode));
                if (this.options.isDebug()) {
                    String errorMessage = getErrorMessageFromStream(connection);
                    this.options.getLogger().log(SentryLevel.ERROR, errorMessage, new Object[0]);
                }
                return TransportResult.error(responseCode);
            } catch (IOException e) {
                this.options.getLogger().log(SentryLevel.ERROR, e, "Error reading and logging the response stream", new Object[0]);
                closeAndDisconnect(connection);
                return TransportResult.error();
            }
        } finally {
            closeAndDisconnect(connection);
        }
    }

    public void updateRetryAfterLimits(@NotNull HttpURLConnection connection, int responseCode) {
        String retryAfterHeader = connection.getHeaderField("Retry-After");
        String sentryRateLimitHeader = connection.getHeaderField("X-Sentry-Rate-Limits");
        this.rateLimiter.updateRetryAfterLimits(sentryRateLimitHeader, retryAfterHeader, responseCode);
    }

    private void closeAndDisconnect(@NotNull HttpURLConnection connection) {
        try {
            connection.getInputStream().close();
        } catch (IOException e) {
        } catch (Throwable th) {
            connection.disconnect();
            throw th;
        }
        connection.disconnect();
    }

    @NotNull
    private String getErrorMessageFromStream(@NotNull HttpURLConnection connection) {
        try {
            InputStream errorStream = connection.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, UTF_8));
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (!first) {
                    sb.append("\n");
                }
                sb.append(line);
                first = false;
            }
            String sb2 = sb.toString();
            reader.close();
            if (errorStream != null) {
                errorStream.close();
            }
            return sb2;
        } catch (IOException e) {
            return "Failed to obtain error message while analyzing send failure.";
        }
    }

    private boolean isSuccessfulResponseCode(int responseCode) {
        return responseCode == 200;
    }

    @TestOnly
    @Nullable
    Proxy getProxy() {
        return this.proxy;
    }
}
