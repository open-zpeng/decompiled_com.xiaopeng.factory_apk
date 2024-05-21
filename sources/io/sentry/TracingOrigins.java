package io.sentry;

import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class TracingOrigins {
    /* JADX WARN: Removed duplicated region for block: B:8:0x0012  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean contain(@org.jetbrains.annotations.NotNull java.util.List<java.lang.String> r4, @org.jetbrains.annotations.NotNull java.lang.String r5) {
        /*
            boolean r0 = r4.isEmpty()
            r1 = 1
            if (r0 == 0) goto L8
            return r1
        L8:
            java.util.Iterator r0 = r4.iterator()
        Lc:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L27
            java.lang.Object r2 = r0.next()
            java.lang.String r2 = (java.lang.String) r2
            boolean r3 = r5.contains(r2)
            if (r3 != 0) goto L26
            boolean r3 = r5.matches(r2)
            if (r3 == 0) goto L25
            goto L26
        L25:
            goto Lc
        L26:
            return r1
        L27:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.sentry.TracingOrigins.contain(java.util.List, java.lang.String):boolean");
    }

    public static boolean contain(@NotNull List<String> origins, URI uri) {
        return contain(origins, uri.toString());
    }
}
