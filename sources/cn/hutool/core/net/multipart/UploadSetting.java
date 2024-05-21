package cn.hutool.core.net.multipart;
/* loaded from: classes.dex */
public class UploadSetting {
    protected String[] fileExts;
    protected String tmpUploadPath;
    protected long maxFileSize = -1;
    protected int memoryThreshold = 8192;
    protected boolean isAllowFileExts = true;

    public long getMaxFileSize() {
        return this.maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public int getMemoryThreshold() {
        return this.memoryThreshold;
    }

    public void setMemoryThreshold(int memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }

    public String getTmpUploadPath() {
        return this.tmpUploadPath;
    }

    public void setTmpUploadPath(String tmpUploadPath) {
        this.tmpUploadPath = tmpUploadPath;
    }

    public String[] getFileExts() {
        return this.fileExts;
    }

    public void setFileExts(String[] fileExts) {
        this.fileExts = fileExts;
    }

    public boolean isAllowFileExts() {
        return this.isAllowFileExts;
    }

    public void setAllowFileExts(boolean isAllowFileExts) {
        this.isAllowFileExts = isAllowFileExts;
    }
}
