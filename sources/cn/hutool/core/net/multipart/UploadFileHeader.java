package cn.hutool.core.net.multipart;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
/* loaded from: classes.dex */
public class UploadFileHeader {
    String contentDisposition;
    String contentType;
    String fileName;
    String formFieldName;
    String formFileName;
    boolean isFile;
    String mimeSubtype;
    String mimeType;
    String path;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UploadFileHeader(String dataHeader) {
        processHeaderString(dataHeader);
    }

    public boolean isFile() {
        return this.isFile;
    }

    public String getFormFieldName() {
        return this.formFieldName;
    }

    public String getFormFileName() {
        return this.formFileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getMimeSubtype() {
        return this.mimeSubtype;
    }

    public String getContentDisposition() {
        return this.contentDisposition;
    }

    private String getDataFieldValue(String dataHeader, String fieldName) {
        String token = StrUtil.format("{}=\"", fieldName);
        int pos = dataHeader.indexOf(token);
        if (pos <= 0) {
            return null;
        }
        int start = token.length() + pos;
        int end = dataHeader.indexOf(34, start);
        if (start <= 0 || end <= 0) {
            return null;
        }
        String value = dataHeader.substring(start, end);
        return value;
    }

    private String getContentType(String dataHeader) {
        int start = dataHeader.indexOf("Content-Type:");
        if (start == -1) {
            return "";
        }
        return dataHeader.substring(start + "Content-Type:".length());
    }

    private String getContentDisposition(String dataHeader) {
        int start = dataHeader.indexOf(58) + 1;
        int end = dataHeader.indexOf(59);
        return dataHeader.substring(start, end);
    }

    private String getMimeType(String ContentType) {
        int pos = ContentType.indexOf(47);
        if (pos == -1) {
            return ContentType;
        }
        return ContentType.substring(1, pos);
    }

    private String getMimeSubtype(String ContentType) {
        int start = ContentType.indexOf(47);
        if (start == -1) {
            return ContentType;
        }
        return ContentType.substring(start + 1);
    }

    private void processHeaderString(String dataHeader) {
        this.isFile = dataHeader.indexOf("filename") > 0;
        this.formFieldName = getDataFieldValue(dataHeader, "name");
        if (this.isFile) {
            this.formFileName = getDataFieldValue(dataHeader, "filename");
            String str = this.formFileName;
            if (str == null) {
                return;
            }
            if (str.length() == 0) {
                this.path = "";
                this.fileName = "";
            }
            int ls = FileUtil.lastIndexOfSeparator(this.formFileName);
            if (ls == -1) {
                this.path = "";
                this.fileName = this.formFileName;
            } else {
                this.path = this.formFileName.substring(0, ls);
                this.fileName = this.formFileName.substring(ls);
            }
            if (this.fileName.length() > 0) {
                this.contentType = getContentType(dataHeader);
                this.mimeType = getMimeType(this.contentType);
                this.mimeSubtype = getMimeSubtype(this.contentType);
                this.contentDisposition = getContentDisposition(dataHeader);
            }
        }
    }
}
