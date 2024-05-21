package com.xpeng.upso.aesserver;

import android.content.Context;
import cn.hutool.core.net.SSLProtocols;
import com.xpeng.upso.proxy.ProxyService;
import com.xpeng.upso.utils.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
/* loaded from: classes2.dex */
public class AesHttpHelper {
    private static final String KEY_STORE_CLIENT_PATH = "test_client.bks";
    private static final String KEY_STORE_TRUST_PATH = "ca-chained.pem";
    private static final String KEY_STORE_TYPE_BKS = "BKS";
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";
    private static final String ROOT_CERT_KEY_PASSWORD = "chengzi";
    private static final String TAG = "Upso-AesHttpHelper";

    public static OkHttpClient getTrustedOkHttpClient() {
        try {
            X509TrustManager trustManager = new X509TrustManager() { // from class: com.xpeng.upso.aesserver.AesHttpHelper.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            SSLContext sslContext = SSLContext.getInstance(SSLProtocols.SSL);
            sslContext.init(null, new X509TrustManager[]{trustManager}, new SecureRandom());
            sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(getTLS2SocketFactory(ProxyService.getAppContext()), getX509TrustManager());
            builder.hostnameVerifier(new HostnameVerifier() { // from class: com.xpeng.upso.aesserver.AesHttpHelper.2
                @Override // javax.net.ssl.HostnameVerifier
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.connectTimeout(30000L, TimeUnit.MILLISECONDS).readTimeout(30000L, TimeUnit.MILLISECONDS).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getOkHttpCLient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(10L, TimeUnit.SECONDS).build();
        return httpClient;
    }

    private static X509TrustManager getX509TrustManager() {
        try {
            CompositeX509TrustManager trustManager = new CompositeX509TrustManager();
            if (!trustManager.isHasValidCert()) {
                LogUtils.e(TAG, "getX509TrustManager trustManager has no valid cert");
            }
            return trustManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static synchronized KeyManager[] getKeyManagers(Context context) {
        synchronized (AesHttpHelper.class) {
            try {
                try {
                    try {
                        try {
                            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
                            InputStream ksIn = ProxyService.getAppContext().getAssets().open(KEY_STORE_CLIENT_PATH);
                            try {
                                if (ksIn != null) {
                                    try {
                                        try {
                                            keyStore.load(ksIn, ROOT_CERT_KEY_PASSWORD.toCharArray());
                                            try {
                                                ksIn.close();
                                            } catch (IOException e) {
                                                e = e;
                                                e.printStackTrace();
                                                KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
                                                kmf.init(keyStore, ROOT_CERT_KEY_PASSWORD.toCharArray());
                                                return kmf.getKeyManagers();
                                            }
                                        } catch (IOException e2) {
                                            e2.printStackTrace();
                                            try {
                                                ksIn.close();
                                            } catch (IOException e3) {
                                                e = e3;
                                                e.printStackTrace();
                                                KeyManagerFactory kmf2 = KeyManagerFactory.getInstance("X509");
                                                kmf2.init(keyStore, ROOT_CERT_KEY_PASSWORD.toCharArray());
                                                return kmf2.getKeyManagers();
                                            }
                                        }
                                    } catch (NoSuchAlgorithmException e4) {
                                        e4.printStackTrace();
                                        try {
                                            ksIn.close();
                                        } catch (IOException e5) {
                                            e = e5;
                                            e.printStackTrace();
                                            KeyManagerFactory kmf22 = KeyManagerFactory.getInstance("X509");
                                            kmf22.init(keyStore, ROOT_CERT_KEY_PASSWORD.toCharArray());
                                            return kmf22.getKeyManagers();
                                        }
                                    } catch (CertificateException e6) {
                                        e6.printStackTrace();
                                        try {
                                            ksIn.close();
                                        } catch (IOException e7) {
                                            e = e7;
                                            e.printStackTrace();
                                            KeyManagerFactory kmf222 = KeyManagerFactory.getInstance("X509");
                                            kmf222.init(keyStore, ROOT_CERT_KEY_PASSWORD.toCharArray());
                                            return kmf222.getKeyManagers();
                                        }
                                    }
                                }
                                KeyManagerFactory kmf2222 = KeyManagerFactory.getInstance("X509");
                                kmf2222.init(keyStore, ROOT_CERT_KEY_PASSWORD.toCharArray());
                                return kmf2222.getKeyManagers();
                            } catch (Throwable th) {
                                try {
                                    ksIn.close();
                                } catch (IOException e8) {
                                    e8.printStackTrace();
                                }
                                throw th;
                            }
                        } catch (NoSuchAlgorithmException e9) {
                            e9.printStackTrace();
                            return null;
                        }
                    } catch (KeyStoreException e10) {
                        e10.printStackTrace();
                        return null;
                    }
                } catch (UnrecoverableKeyException e11) {
                    e11.printStackTrace();
                    return null;
                }
            } catch (IOException e12) {
                e12.printStackTrace();
                return null;
            }
        }
    }

    private static SSLSocketFactory getTLS2SocketFactory(Context context) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            CompositeX509TrustManager trustManager = new CompositeX509TrustManager();
            if (!trustManager.isHasValidCert()) {
                LogUtils.e(TAG, "getTLS2SocketFactory trustManager has no valid cert");
            }
            TrustManager[] ts = {trustManager};
            sslContext.init(getKeyManagers(context), ts, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class CompositeX509TrustManager implements X509TrustManager {
        private boolean hasValidCert;
        private final List<X509TrustManager> trustManagers = new ArrayList();
        private final List<X509Certificate> certificates = new ArrayList();

        /* JADX WARN: Removed duplicated region for block: B:42:0x007d A[Catch: Exception -> 0x00bb, TryCatch #4 {Exception -> 0x00bb, blocks: (B:3:0x0014, B:7:0x003f, B:18:0x0050, B:15:0x004b, B:22:0x0058, B:28:0x0062, B:39:0x0073, B:40:0x0077, B:42:0x007d, B:44:0x008b, B:46:0x0097, B:48:0x00a6, B:50:0x00a9, B:52:0x00af, B:6:0x0037, B:14:0x0048, B:21:0x0055, B:27:0x005f), top: B:60:0x0014, inners: #7 }] */
        /* JADX WARN: Removed duplicated region for block: B:48:0x00a6 A[Catch: Exception -> 0x00bb, TryCatch #4 {Exception -> 0x00bb, blocks: (B:3:0x0014, B:7:0x003f, B:18:0x0050, B:15:0x004b, B:22:0x0058, B:28:0x0062, B:39:0x0073, B:40:0x0077, B:42:0x007d, B:44:0x008b, B:46:0x0097, B:48:0x00a6, B:50:0x00a9, B:52:0x00af, B:6:0x0037, B:14:0x0048, B:21:0x0055, B:27:0x005f), top: B:60:0x0014, inners: #7 }] */
        /* JADX WARN: Removed duplicated region for block: B:76:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public CompositeX509TrustManager() {
            /*
                r12 = this;
                r12.<init>()
                r0 = 0
                r12.hasValidCert = r0
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                r12.trustManagers = r1
                java.util.ArrayList r1 = new java.util.ArrayList
                r1.<init>()
                r12.certificates = r1
                java.lang.String r1 = java.security.KeyStore.getDefaultType()     // Catch: java.lang.Exception -> Lbb
                java.security.KeyStore r1 = java.security.KeyStore.getInstance(r1)     // Catch: java.lang.Exception -> Lbb
                android.content.Context r2 = com.xpeng.upso.proxy.ProxyService.getAppContext()     // Catch: java.lang.Exception -> Lbb
                android.content.res.AssetManager r2 = r2.getAssets()     // Catch: java.lang.Exception -> Lbb
                java.lang.String r3 = "ca-chained.pem"
                java.io.InputStream r2 = r2.open(r3)     // Catch: java.lang.Exception -> Lbb
                java.lang.String r3 = "X.509"
                java.security.cert.CertificateFactory r3 = java.security.cert.CertificateFactory.getInstance(r3)     // Catch: java.lang.Exception -> Lbb
                java.security.cert.Certificate r4 = r3.generateCertificate(r2)     // Catch: java.lang.Exception -> Lbb
                if (r2 == 0) goto L73
                r5 = 0
                r1.load(r5, r5)     // Catch: java.lang.Throwable -> L45 java.security.cert.CertificateException -> L47 java.security.NoSuchAlgorithmException -> L54 java.io.IOException -> L5e
                java.lang.String r5 = "ca"
                r1.setCertificateEntry(r5, r4)     // Catch: java.lang.Throwable -> L45 java.security.cert.CertificateException -> L47 java.security.NoSuchAlgorithmException -> L54 java.io.IOException -> L5e
                r2.close()     // Catch: java.io.IOException -> L43 java.lang.Exception -> Lbb
                goto L65
            L43:
                r5 = move-exception
                goto L50
            L45:
                r0 = move-exception
                goto L69
            L47:
                r5 = move-exception
                r5.printStackTrace()     // Catch: java.lang.Throwable -> L45
                r2.close()     // Catch: java.io.IOException -> L4f java.lang.Exception -> Lbb
                goto L65
            L4f:
                r5 = move-exception
            L50:
                r5.printStackTrace()     // Catch: java.lang.Exception -> Lbb
                goto L68
            L54:
                r5 = move-exception
                r5.printStackTrace()     // Catch: java.lang.Throwable -> L45
                r2.close()     // Catch: java.io.IOException -> L5c java.lang.Exception -> Lbb
                goto L65
            L5c:
                r5 = move-exception
                goto L50
            L5e:
                r5 = move-exception
                r5.printStackTrace()     // Catch: java.lang.Throwable -> L45
                r2.close()     // Catch: java.io.IOException -> L66 java.lang.Exception -> Lbb
            L65:
                goto L73
            L66:
                r5 = move-exception
                goto L50
            L68:
                goto L73
            L69:
                r2.close()     // Catch: java.io.IOException -> L6d java.lang.Exception -> Lbb
                goto L71
            L6d:
                r5 = move-exception
                r5.printStackTrace()     // Catch: java.lang.Exception -> Lbb
            L71:
                throw r0     // Catch: java.lang.Exception -> Lbb
            L73:
                java.util.Enumeration r5 = r1.aliases()     // Catch: java.lang.Exception -> Lbb
            L77:
                boolean r6 = r5.hasMoreElements()     // Catch: java.lang.Exception -> Lbb
                if (r6 == 0) goto L97
                java.lang.Object r6 = r5.nextElement()     // Catch: java.lang.Exception -> Lbb
                java.lang.String r6 = (java.lang.String) r6     // Catch: java.lang.Exception -> Lbb
                java.security.cert.Certificate r6 = r1.getCertificate(r6)     // Catch: java.lang.Exception -> Lbb
                boolean r7 = r6 instanceof java.security.cert.X509Certificate     // Catch: java.lang.Exception -> Lbb
                if (r7 == 0) goto L96
                java.util.List<java.security.cert.X509Certificate> r7 = r12.certificates     // Catch: java.lang.Exception -> Lbb
                r8 = r6
                java.security.cert.X509Certificate r8 = (java.security.cert.X509Certificate) r8     // Catch: java.lang.Exception -> Lbb
                r7.add(r8)     // Catch: java.lang.Exception -> Lbb
                r7 = 1
                r12.hasValidCert = r7     // Catch: java.lang.Exception -> Lbb
            L96:
                goto L77
            L97:
                java.lang.String r6 = "X509"
                javax.net.ssl.TrustManagerFactory r6 = javax.net.ssl.TrustManagerFactory.getInstance(r6)     // Catch: java.lang.Exception -> Lbb
                r6.init(r1)     // Catch: java.lang.Exception -> Lbb
                javax.net.ssl.TrustManager[] r7 = r6.getTrustManagers()     // Catch: java.lang.Exception -> Lbb
                if (r7 == 0) goto Lba
                int r8 = r7.length     // Catch: java.lang.Exception -> Lbb
            La7:
                if (r0 >= r8) goto Lba
                r9 = r7[r0]     // Catch: java.lang.Exception -> Lbb
                boolean r10 = r9 instanceof javax.net.ssl.X509TrustManager     // Catch: java.lang.Exception -> Lbb
                if (r10 == 0) goto Lb7
                java.util.List<javax.net.ssl.X509TrustManager> r10 = r12.trustManagers     // Catch: java.lang.Exception -> Lbb
                r11 = r9
                javax.net.ssl.X509TrustManager r11 = (javax.net.ssl.X509TrustManager) r11     // Catch: java.lang.Exception -> Lbb
                r10.add(r11)     // Catch: java.lang.Exception -> Lbb
            Lb7:
                int r0 = r0 + 1
                goto La7
            Lba:
                goto Lbf
            Lbb:
                r0 = move-exception
                r0.printStackTrace()
            Lbf:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.aesserver.AesHttpHelper.CompositeX509TrustManager.<init>():void");
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509TrustManager trustManager : this.trustManagers) {
                try {
                    trustManager.checkClientTrusted(chain, authType);
                    LogUtils.d(AesHttpHelper.TAG, "checkClientTrusted true");
                    return;
                } catch (CertificateException e) {
                }
            }
            throw new CertificateException("None of the TrustManagers trust this client certificate chain");
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509TrustManager trustManager : this.trustManagers) {
                try {
                    trustManager.checkServerTrusted(chain, authType);
                    LogUtils.d(AesHttpHelper.TAG, "checkServerTrusted true");
                    return;
                } catch (CertificateException e) {
                }
            }
            throw new CertificateException("None of the TrustManagers trust this server certificate chain");
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return (X509Certificate[]) this.certificates.toArray(new X509Certificate[0]);
        }

        public boolean isHasValidCert() {
            return this.hasValidCert;
        }
    }
}
