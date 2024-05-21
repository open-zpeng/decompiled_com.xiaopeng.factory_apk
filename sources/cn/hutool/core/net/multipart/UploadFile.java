package cn.hutool.core.net.multipart;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
/* loaded from: classes.dex */
public class UploadFile {
    private static final String TMP_FILE_PREFIX = "hutool-";
    private static final String TMP_FILE_SUFFIX = ".upload.tmp";
    private byte[] data;
    private final UploadFileHeader header;
    private final UploadSetting setting;
    private long size = -1;
    private File tempFile;

    public UploadFile(UploadFileHeader header, UploadSetting setting) {
        this.header = header;
        this.setting = setting;
    }

    public void delete() {
        File file = this.tempFile;
        if (file != null) {
            file.delete();
        }
        if (this.data != null) {
            this.data = null;
        }
    }

    public File write(String destPath) throws IOException {
        if (this.data != null || this.tempFile != null) {
            return write(FileUtil.file(destPath));
        }
        return null;
    }

    public File write(File destination) throws IOException {
        assertValid();
        if (destination.isDirectory()) {
            destination = new File(destination, this.header.getFileName());
        }
        byte[] bArr = this.data;
        if (bArr != null) {
            FileUtil.writeBytes(bArr, destination);
            this.data = null;
        } else {
            File file = this.tempFile;
            if (file == null) {
                throw new NullPointerException("Temp file is null !");
            }
            if (!file.exists()) {
                throw new NoSuchFileException("Temp file: [" + this.tempFile.getAbsolutePath() + "] not exist!");
            }
            FileUtil.move(this.tempFile, destination, true);
        }
        return destination;
    }

    public byte[] getFileContent() throws IOException {
        assertValid();
        byte[] bArr = this.data;
        if (bArr != null) {
            return bArr;
        }
        File file = this.tempFile;
        if (file != null) {
            return FileUtil.readBytes(file);
        }
        return null;
    }

    public InputStream getFileInputStream() throws IOException {
        assertValid();
        byte[] bArr = this.data;
        if (bArr != null) {
            return IoUtil.toBuffered(IoUtil.toStream(bArr));
        }
        File file = this.tempFile;
        if (file != null) {
            return IoUtil.toBuffered(IoUtil.toStream(file));
        }
        return null;
    }

    public UploadFileHeader getHeader() {
        return this.header;
    }

    public String getFileName() {
        UploadFileHeader uploadFileHeader = this.header;
        if (uploadFileHeader == null) {
            return null;
        }
        return uploadFileHeader.getFileName();
    }

    public long size() {
        return this.size;
    }

    public boolean isUploaded() {
        return this.size > 0;
    }

    public boolean isInMemory() {
        return this.data != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean processStream(MultipartRequestInputStream input) throws IOException {
        if (!isAllowedExtension()) {
            this.size = input.skipToBoundary();
            return false;
        }
        this.size = 0L;
        int memoryThreshold = this.setting.memoryThreshold;
        if (memoryThreshold > 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(memoryThreshold);
            long written = input.copy(baos, memoryThreshold);
            this.data = baos.toByteArray();
            if (written <= memoryThreshold) {
                this.size = this.data.length;
                return true;
            }
        }
        this.tempFile = FileUtil.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX, FileUtil.touch(this.setting.tmpUploadPath), false);
        BufferedOutputStream out = FileUtil.getOutputStream(this.tempFile);
        byte[] bArr = this.data;
        if (bArr != null) {
            this.size = bArr.length;
            out.write(bArr);
            this.data = null;
        }
        long maxFileSize = this.setting.maxFileSize;
        try {
            if (maxFileSize == -1) {
                this.size += input.copy(out);
                return true;
            }
            this.size += input.copy(out, (maxFileSize - this.size) + 1);
            if (this.size > maxFileSize) {
                this.tempFile.delete();
                this.tempFile = null;
                input.skipToBoundary();
                return false;
            }
            return true;
        } finally {
            IoUtil.close((Closeable) out);
        }
    }

    private boolean isAllowedExtension() {
        String[] strArr;
        String[] exts = this.setting.fileExts;
        boolean isAllow = this.setting.isAllowFileExts;
        if (exts == null || exts.length == 0) {
            return isAllow;
        }
        String fileNameExt = FileUtil.extName(getFileName());
        for (String fileExtension : this.setting.fileExts) {
            if (fileNameExt.equalsIgnoreCase(fileExtension)) {
                return isAllow;
            }
        }
        return !isAllow;
    }

    private void assertValid() throws IOException {
        if (!isUploaded()) {
            throw new IOException(StrUtil.format("File [{}] upload fail", getFileName()));
        }
    }
}
