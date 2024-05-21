package cn.hutool.core.io.file;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.visitor.CopyVisitor;
import cn.hutool.core.io.file.visitor.DelVisitor;
import cn.hutool.core.io.file.visitor.MoveVisitor;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
/* loaded from: classes.dex */
public class PathUtil {
    public static boolean isDirEmpty(Path dirPath) {
        try {
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(dirPath);
            boolean z = !dirStream.iterator().hasNext();
            dirStream.close();
            return z;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static List<File> loopFiles(Path path, int maxDepth, final FileFilter fileFilter) {
        final List<File> fileList = new ArrayList<>();
        if (path == null || !Files.exists(path, new LinkOption[0])) {
            return fileList;
        }
        if (!isDirectory(path)) {
            File file = path.toFile();
            if (fileFilter == null || fileFilter.accept(file)) {
                fileList.add(file);
            }
            return fileList;
        }
        walkFiles(path, maxDepth, new SimpleFileVisitor<Path>() { // from class: cn.hutool.core.io.file.PathUtil.1
            @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
            public FileVisitResult visitFile(Path path2, BasicFileAttributes attrs) {
                File file2 = path2.toFile();
                FileFilter fileFilter2 = fileFilter;
                if (fileFilter2 == null || fileFilter2.accept(file2)) {
                    fileList.add(file2);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return fileList;
    }

    public static void walkFiles(Path start, FileVisitor<? super Path> visitor) {
        walkFiles(start, -1, visitor);
    }

    public static void walkFiles(Path start, int maxDepth, FileVisitor<? super Path> visitor) {
        if (maxDepth < 0) {
            maxDepth = Integer.MAX_VALUE;
        }
        try {
            Files.walkFileTree(start, EnumSet.noneOf(FileVisitOption.class), maxDepth, visitor);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static boolean del(Path path) throws IORuntimeException {
        if (Files.notExists(path, new LinkOption[0])) {
            return true;
        }
        try {
            if (isDirectory(path)) {
                Files.walkFileTree(path, DelVisitor.INSTANCE);
            } else {
                delFile(path);
            }
            return true;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static Path copyFile(Path src, Path dest, StandardCopyOption... options) throws IORuntimeException {
        return copyFile(src, dest, (CopyOption[]) options);
    }

    public static Path copyFile(Path src, Path target, CopyOption... options) throws IORuntimeException {
        Assert.notNull(src, "Source File is null !", new Object[0]);
        Assert.notNull(target, "Destination File or directiory is null !", new Object[0]);
        Path targetPath = isDirectory(target) ? target.resolve(src.getFileName()) : target;
        mkParentDirs(targetPath);
        try {
            return Files.copy(src, targetPath, options);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static Path copy(Path src, Path target, CopyOption... options) throws IORuntimeException {
        Assert.notNull(src, "Src path must be not null !", new Object[0]);
        Assert.notNull(target, "Target path must be not null !", new Object[0]);
        if (isDirectory(src)) {
            return copyContent(src, target.resolve(src.getFileName()), options);
        }
        return copyFile(src, target, options);
    }

    public static Path copyContent(Path src, Path target, CopyOption... options) throws IORuntimeException {
        Assert.notNull(src, "Src path must be not null !", new Object[0]);
        Assert.notNull(target, "Target path must be not null !", new Object[0]);
        try {
            Files.walkFileTree(src, new CopyVisitor(src, target, options));
            return target;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static boolean isDirectory(Path path) {
        return isDirectory(path, false);
    }

    public static boolean isDirectory(Path path, boolean isFollowLinks) {
        if (path == null) {
            return false;
        }
        LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        return Files.isDirectory(path, options);
    }

    public static Path getPathEle(Path path, int index) {
        return subPath(path, index, index == -1 ? path.getNameCount() : index + 1);
    }

    public static Path getLastPathEle(Path path) {
        return getPathEle(path, path.getNameCount() - 1);
    }

    public static Path subPath(Path path, int fromIndex, int toIndex) {
        if (path == null) {
            return null;
        }
        int len = path.getNameCount();
        if (fromIndex < 0) {
            fromIndex += len;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }
        if (toIndex < 0) {
            toIndex += len;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }
        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }
        if (fromIndex == toIndex) {
            return null;
        }
        return path.subpath(fromIndex, toIndex);
    }

    public static BasicFileAttributes getAttributes(Path path, boolean isFollowLinks) throws IORuntimeException {
        if (path == null) {
            return null;
        }
        LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        try {
            return Files.readAttributes(path, BasicFileAttributes.class, options);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static BufferedInputStream getInputStream(Path path) throws IORuntimeException {
        try {
            InputStream in = Files.newInputStream(path, new OpenOption[0]);
            return IoUtil.toBuffered(in);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static BufferedReader getUtf8Reader(Path path) throws IORuntimeException {
        return getReader(path, CharsetUtil.CHARSET_UTF_8);
    }

    public static BufferedReader getReader(Path path, Charset charset) throws IORuntimeException {
        return IoUtil.getReader(getInputStream(path), charset);
    }

    public static byte[] readBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static BufferedOutputStream getOutputStream(Path path) throws IORuntimeException {
        try {
            OutputStream in = Files.newOutputStream(path, new OpenOption[0]);
            return IoUtil.toBuffered(in);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static Path rename(Path path, String newName, boolean isOverride) {
        return move(path, path.resolveSibling(newName), isOverride);
    }

    public static Path move(Path src, Path target, boolean isOverride) {
        Assert.notNull(src, "Src path must be not null !", new Object[0]);
        Assert.notNull(target, "Target path must be not null !", new Object[0]);
        if (isDirectory(target)) {
            target = target.resolve(src.getFileName());
        }
        return moveContent(src, target, isOverride);
    }

    public static Path moveContent(Path src, Path target, boolean isOverride) {
        Assert.notNull(src, "Src path must be not null !", new Object[0]);
        Assert.notNull(target, "Target path must be not null !", new Object[0]);
        CopyOption[] options = isOverride ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[0];
        mkParentDirs(target);
        try {
            return Files.move(src, target, options);
        } catch (IOException e) {
            try {
                Files.walkFileTree(src, new MoveVisitor(src, target, options));
                del(src);
                return target;
            } catch (IOException e2) {
                throw new IORuntimeException(e2);
            }
        }
    }

    public static boolean equals(Path file1, Path file2) throws IORuntimeException {
        try {
            return Files.isSameFile(file1, file2);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static boolean isFile(Path path, boolean isFollowLinks) {
        if (path == null) {
            return false;
        }
        LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        return Files.isRegularFile(path, options);
    }

    public static boolean isSymlink(Path path) {
        return Files.isSymbolicLink(path);
    }

    public static boolean exists(Path path, boolean isFollowLinks) {
        LinkOption[] options = isFollowLinks ? new LinkOption[0] : new LinkOption[]{LinkOption.NOFOLLOW_LINKS};
        return Files.exists(path, options);
    }

    public static boolean isSub(Path parent, Path sub) {
        return toAbsNormal(sub).startsWith(toAbsNormal(parent));
    }

    public static Path toAbsNormal(Path path) {
        Assert.notNull(path);
        return path.toAbsolutePath().normalize();
    }

    public static String getMimeType(Path file) {
        try {
            return Files.probeContentType(file);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static Path mkdir(Path dir) {
        if (dir != null && !exists(dir, false)) {
            try {
                Files.createDirectories(dir, new FileAttribute[0]);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
        return dir;
    }

    public static Path mkParentDirs(Path path) {
        return mkdir(path.getParent());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void delFile(Path path) throws IOException {
        try {
            Files.delete(path);
        } catch (AccessDeniedException e) {
            if (!path.toFile().delete()) {
                throw e;
            }
        }
    }
}
