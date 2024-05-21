package ch.ethz.ssh2;
/* loaded from: classes.dex */
public interface InteractiveCallback {
    String[] replyToChallenge(String str, String str2, int i, String[] strArr, boolean[] zArr) throws Exception;
}
