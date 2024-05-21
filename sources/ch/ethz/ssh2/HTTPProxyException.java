package ch.ethz.ssh2;

import java.io.IOException;
/* loaded from: classes.dex */
public class HTTPProxyException extends IOException {
    private static final long serialVersionUID = 2241537397104426186L;
    public final int httpErrorCode;
    public final String httpResponse;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public HTTPProxyException(java.lang.String r3, int r4) {
        /*
            r2 = this;
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            java.lang.String r1 = "HTTP Proxy Error ("
            r0.<init>(r1)
            r0.append(r4)
            java.lang.String r1 = " "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r1 = ")"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r2.<init>(r0)
            r2.httpResponse = r3
            r2.httpErrorCode = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.ethz.ssh2.HTTPProxyException.<init>(java.lang.String, int):void");
    }
}
