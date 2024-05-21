package com.jcraft.jsch;

import com.xiaopeng.lib.utils.FileUtils;
/* loaded from: classes.dex */
public class SftpStatVFS {
    int atime;
    private long bavail;
    private long bfree;
    private long blocks;
    private long bsize;
    private long favail;
    private long ffree;
    private long files;
    private long flag;
    private long frsize;
    private long fsid;
    int gid;
    int mtime;
    private long namemax;
    int permissions;
    long size;
    int uid;
    int flags = 0;
    String[] extended = null;

    private SftpStatVFS() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SftpStatVFS getStatVFS(Buffer buf) {
        SftpStatVFS statvfs = new SftpStatVFS();
        statvfs.bsize = buf.getLong();
        statvfs.frsize = buf.getLong();
        statvfs.blocks = buf.getLong();
        statvfs.bfree = buf.getLong();
        statvfs.bavail = buf.getLong();
        statvfs.files = buf.getLong();
        statvfs.ffree = buf.getLong();
        statvfs.favail = buf.getLong();
        statvfs.fsid = buf.getLong();
        int flag = (int) buf.getLong();
        statvfs.namemax = buf.getLong();
        statvfs.flag = (flag & 1) != 0 ? 1L : 0L;
        statvfs.flag |= (flag & 2) != 0 ? 2L : 0L;
        return statvfs;
    }

    public long getBlockSize() {
        return this.bsize;
    }

    public long getFragmentSize() {
        return this.frsize;
    }

    public long getBlocks() {
        return this.blocks;
    }

    public long getFreeBlocks() {
        return this.bfree;
    }

    public long getAvailBlocks() {
        return this.bavail;
    }

    public long getINodes() {
        return this.files;
    }

    public long getFreeINodes() {
        return this.ffree;
    }

    public long getAvailINodes() {
        return this.favail;
    }

    public long getFileSystemID() {
        return this.fsid;
    }

    public long getMountFlag() {
        return this.flag;
    }

    public long getMaximumFilenameLength() {
        return this.namemax;
    }

    public long getSize() {
        return (getFragmentSize() * getBlocks()) / FileUtils.SIZE_1KB;
    }

    public long getUsed() {
        return (getFragmentSize() * (getBlocks() - getFreeBlocks())) / FileUtils.SIZE_1KB;
    }

    public long getAvailForNonRoot() {
        return (getFragmentSize() * getAvailBlocks()) / FileUtils.SIZE_1KB;
    }

    public long getAvail() {
        return (getFragmentSize() * getFreeBlocks()) / FileUtils.SIZE_1KB;
    }

    public int getCapacity() {
        return (int) (((getBlocks() - getFreeBlocks()) * 100) / getBlocks());
    }
}
