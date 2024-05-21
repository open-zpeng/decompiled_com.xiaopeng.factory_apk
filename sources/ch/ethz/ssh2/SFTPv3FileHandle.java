package ch.ethz.ssh2;
/* loaded from: classes.dex */
public class SFTPv3FileHandle {
    final SFTPv3Client client;
    final byte[] fileHandle;
    boolean isClosed = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SFTPv3FileHandle(SFTPv3Client client, byte[] h) {
        this.client = client;
        this.fileHandle = h;
    }

    public SFTPv3Client getClient() {
        return this.client;
    }

    public boolean isClosed() {
        return this.isClosed;
    }
}
