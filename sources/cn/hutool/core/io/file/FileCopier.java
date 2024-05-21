package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.copier.SrcToDestCopier;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class FileCopier extends SrcToDestCopier<File, FileCopier> {
    private static final long serialVersionUID = 1;
    private boolean isCopyAttributes;
    private boolean isCopyContentIfDir;
    private boolean isOnlyCopyFile;
    private boolean isOverride;

    public static FileCopier create(String srcPath, String destPath) {
        return new FileCopier(FileUtil.file(srcPath), FileUtil.file(destPath));
    }

    public static FileCopier create(File src, File dest) {
        return new FileCopier(src, dest);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FileCopier(File src, File dest) {
        this.src = src;
        this.dest = dest;
    }

    public boolean isOverride() {
        return this.isOverride;
    }

    public FileCopier setOverride(boolean isOverride) {
        this.isOverride = isOverride;
        return this;
    }

    public boolean isCopyAttributes() {
        return this.isCopyAttributes;
    }

    public FileCopier setCopyAttributes(boolean isCopyAttributes) {
        this.isCopyAttributes = isCopyAttributes;
        return this;
    }

    public boolean isCopyContentIfDir() {
        return this.isCopyContentIfDir;
    }

    public FileCopier setCopyContentIfDir(boolean isCopyContentIfDir) {
        this.isCopyContentIfDir = isCopyContentIfDir;
        return this;
    }

    public boolean isOnlyCopyFile() {
        return this.isOnlyCopyFile;
    }

    public FileCopier setOnlyCopyFile(boolean isOnlyCopyFile) {
        this.isOnlyCopyFile = isOnlyCopyFile;
        return this;
    }

    @Override // cn.hutool.core.lang.copier.Copier
    public File copy() throws IORuntimeException {
        File src = (File) this.src;
        File dest = (File) this.dest;
        Assert.notNull(src, "Source File is null !", new Object[0]);
        if (!src.exists()) {
            throw new IORuntimeException("File not exist: " + src);
        }
        Assert.notNull(dest, "Destination File or directiory is null !", new Object[0]);
        if (FileUtil.equals(src, dest)) {
            throw new IORuntimeException("Files '{}' and '{}' are equal", src, dest);
        }
        if (src.isDirectory()) {
            if (dest.exists() && !dest.isDirectory()) {
                throw new IORuntimeException("Src is a directory but dest is a file!");
            }
            if (FileUtil.isSub(src, dest)) {
                throw new IORuntimeException("Dest is a sub directory of src !");
            }
            File subTarget = this.isCopyContentIfDir ? dest : FileUtil.mkdir(FileUtil.file(dest, src.getName()));
            internalCopyDirContent(src, subTarget);
        } else {
            internalCopyFile(src, dest);
        }
        return dest;
    }

    private void internalCopyDirContent(File src, File dest) throws IORuntimeException {
        if (this.copyFilter != null && !this.copyFilter.accept(src)) {
            return;
        }
        if (!dest.exists()) {
            dest.mkdirs();
        } else if (!dest.isDirectory()) {
            throw new IORuntimeException(StrUtil.format("Src [{}] is a directory but dest [{}] is a file!", src.getPath(), dest.getPath()));
        }
        String[] files = src.list();
        if (ArrayUtil.isNotEmpty((Object[]) files)) {
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = this.isOnlyCopyFile ? dest : new File(dest, file);
                if (srcFile.isDirectory()) {
                    internalCopyDirContent(srcFile, destFile);
                } else {
                    internalCopyFile(srcFile, destFile);
                }
            }
        }
    }

    private void internalCopyFile(File src, File dest) throws IORuntimeException {
        if (this.copyFilter != null && !this.copyFilter.accept(src)) {
            return;
        }
        if (dest.exists()) {
            if (dest.isDirectory()) {
                dest = new File(dest, src.getName());
            }
            if (dest.exists() && !this.isOverride) {
                return;
            }
        } else {
            dest.getParentFile().mkdirs();
        }
        ArrayList<CopyOption> optionList = new ArrayList<>(2);
        if (this.isOverride) {
            optionList.add(StandardCopyOption.REPLACE_EXISTING);
        }
        if (this.isCopyAttributes) {
            optionList.add(StandardCopyOption.COPY_ATTRIBUTES);
        }
        try {
            Files.copy(src.toPath(), dest.toPath(), (CopyOption[]) optionList.toArray(new CopyOption[0]));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
