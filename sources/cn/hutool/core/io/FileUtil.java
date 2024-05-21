package cn.hutool.core.io;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileCopier;
import cn.hutool.core.io.file.FileMode;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.io.file.LineSeparator;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.io.file.Tailer;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.core.util.ZipUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
/* loaded from: classes.dex */
public class FileUtil extends PathUtil {
    public static final String CLASS_EXT = ".class";
    public static final String JAR_FILE_EXT = ".jar";
    public static final String JAR_PATH_EXT = ".jar!";
    public static final String PATH_FILE_PRE = "file:";
    public static final String FILE_SEPARATOR = File.separator;
    public static final String PATH_SEPARATOR = File.pathSeparator;

    public static boolean isWindows() {
        return '\\' == File.separatorChar;
    }

    public static File[] ls(String path) {
        if (path == null) {
            return null;
        }
        File file = file(path);
        if (file.isDirectory()) {
            return file.listFiles();
        }
        throw new IORuntimeException(StrUtil.format("Path [{}] is not directory!", path));
    }

    public static boolean isEmpty(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.isFile() && file.length() <= 0;
        }
        String[] subFiles = file.list();
        return ArrayUtil.isEmpty((Object[]) subFiles);
    }

    public static boolean isNotEmpty(File file) {
        return !isEmpty(file);
    }

    public static boolean isDirEmpty(File dir) {
        return isDirEmpty(dir.toPath());
    }

    public static List<File> loopFiles(String path, FileFilter fileFilter) {
        return loopFiles(file(path), fileFilter);
    }

    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        return loopFiles(file, -1, fileFilter);
    }

    public static void walkFiles(File file, Consumer<File> consumer) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty((Object[]) subFiles)) {
                for (File tmp : subFiles) {
                    walkFiles(tmp, consumer);
                }
                return;
            }
            return;
        }
        consumer.accept(file);
    }

    public static List<File> loopFiles(File file, int maxDepth, FileFilter fileFilter) {
        return loopFiles(file.toPath(), maxDepth, fileFilter);
    }

    public static List<File> loopFiles(String path) {
        return loopFiles(file(path));
    }

    public static List<File> loopFiles(File file) {
        return loopFiles(file, (FileFilter) null);
    }

    public static List<String> listFileNames(String path) throws IORuntimeException {
        if (path == null) {
            return new ArrayList(0);
        }
        int index = path.lastIndexOf(JAR_PATH_EXT);
        if (index < 0) {
            List<String> paths = new ArrayList<>();
            File[] files = ls(path);
            for (File file : files) {
                if (file.isFile()) {
                    paths.add(file.getName());
                }
            }
            return paths;
        }
        String path2 = getAbsolutePath(path);
        int index2 = index + ".jar".length();
        JarFile jarFile = null;
        try {
            try {
                jarFile = new JarFile(path2.substring(0, index2));
                return ZipUtil.listFileNames(jarFile, StrUtil.removePrefix(path2.substring(index2 + 1), "/"));
            } catch (IOException e) {
                throw new IORuntimeException(StrUtil.format("Can not read file path of [{}]", path2), e);
            }
        } finally {
            IoUtil.close((Closeable) jarFile);
        }
    }

    public static File newFile(String path) {
        return new File(path);
    }

    public static File file(String path) {
        if (path == null) {
            return null;
        }
        return new File(getAbsolutePath(path));
    }

    public static File file(String parent, String path) {
        return file(new File(parent), path);
    }

    public static File file(File parent, String path) {
        if (StrUtil.isBlank(path)) {
            throw new NullPointerException("File path is blank!");
        }
        return checkSlip(parent, buildFile(parent, path));
    }

    public static File file(File directory, String... names) {
        Assert.notNull(directory, "directory must not be null", new Object[0]);
        if (ArrayUtil.isEmpty((Object[]) names)) {
            return directory;
        }
        File file = directory;
        for (String name : names) {
            if (name != null) {
                file = file(file, name);
            }
        }
        return file;
    }

    public static File file(String... names) {
        if (ArrayUtil.isEmpty((Object[]) names)) {
            return null;
        }
        File file = null;
        for (String name : names) {
            if (file == null) {
                file = file(name);
            } else {
                file = file(file, name);
            }
        }
        return file;
    }

    public static File file(URI uri) {
        if (uri == null) {
            throw new NullPointerException("File uri is null!");
        }
        return new File(uri);
    }

    public static File file(URL url) {
        return new File(URLUtil.toURI(url));
    }

    public static String getTmpDirPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getTmpDir() {
        return file(getTmpDirPath());
    }

    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }

    public static File getUserHomeDir() {
        return file(getUserHomePath());
    }

    public static boolean exist(String path) {
        return path != null && file(path).exists();
    }

    public static boolean exist(File file) {
        return file != null && file.exists();
    }

    public static boolean exist(String directory, String regexp) {
        String[] fileList;
        File file = new File(directory);
        if (file.exists() && (fileList = file.list()) != null) {
            for (String fileName : fileList) {
                if (fileName.matches(regexp)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static Date lastModifiedTime(File file) {
        if (!exist(file)) {
            return null;
        }
        return new Date(file.lastModified());
    }

    public static Date lastModifiedTime(String path) {
        return lastModifiedTime(new File(path));
    }

    public static long size(File file) {
        if (file == null || !file.exists() || isSymlink(file)) {
            return 0L;
        }
        if (file.isDirectory()) {
            long size = 0;
            File[] subFiles = file.listFiles();
            if (ArrayUtil.isEmpty((Object[]) subFiles)) {
                return 0L;
            }
            for (File subFile : subFiles) {
                size += size(subFile);
            }
            return size;
        }
        return file.length();
    }

    public static boolean newerThan(File file, File reference) {
        if (reference == null || !reference.exists()) {
            return true;
        }
        return newerThan(file, reference.lastModified());
    }

    public static boolean newerThan(File file, long timeMillis) {
        return file != null && file.exists() && file.lastModified() > timeMillis;
    }

    public static File touch(String fullFilePath) throws IORuntimeException {
        if (fullFilePath == null) {
            return null;
        }
        return touch(file(fullFilePath));
    }

    public static File touch(File file) throws IORuntimeException {
        if (file == null) {
            return null;
        }
        if (!file.exists()) {
            mkParentDirs(file);
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new IORuntimeException(e);
            }
        }
        return file;
    }

    public static File touch(File parent, String path) throws IORuntimeException {
        return touch(file(parent, path));
    }

    public static File touch(String parent, String path) throws IORuntimeException {
        return touch(file(parent, path));
    }

    public static File mkParentDirs(File file) {
        if (file == null) {
            return null;
        }
        return mkdir(file.getParentFile());
    }

    public static File mkParentDirs(String path) {
        if (path == null) {
            return null;
        }
        return mkParentDirs(file(path));
    }

    public static boolean del(String fullFileOrDirPath) throws IORuntimeException {
        return del(file(fullFileOrDirPath));
    }

    public static boolean del(File file) throws IORuntimeException {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            boolean isOk = clean(file);
            if (!isOk) {
                return false;
            }
        }
        Path path = file.toPath();
        try {
            delFile(path);
        } catch (DirectoryNotEmptyException e) {
            del(path);
        } catch (IOException e2) {
            throw new IORuntimeException(e2);
        }
        return true;
    }

    public static boolean clean(String dirPath) throws IORuntimeException {
        return clean(file(dirPath));
    }

    public static boolean clean(File directory) throws IORuntimeException {
        File[] files;
        if (directory != null && directory.exists() && directory.isDirectory() && (files = directory.listFiles()) != null) {
            for (File childFile : files) {
                if (!del(childFile)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean cleanEmpty(File directory) throws IORuntimeException {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return true;
        }
        File[] files = directory.listFiles();
        if (ArrayUtil.isEmpty((Object[]) files)) {
            return directory.delete();
        }
        for (File childFile : files) {
            cleanEmpty(childFile);
        }
        return true;
    }

    public static File mkdir(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        File dir = file(dirPath);
        return mkdir(dir);
    }

    public static File mkdir(File dir) {
        if (dir == null) {
            return null;
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File createTempFile(File dir) throws IORuntimeException {
        return createTempFile("hutool", null, dir, true);
    }

    public static File createTempFile(File dir, boolean isReCreat) throws IORuntimeException {
        return createTempFile("hutool", null, dir, isReCreat);
    }

    public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IORuntimeException {
        int exceptionsCount = 0;
        do {
            try {
                File file = File.createTempFile(prefix, suffix, mkdir(dir)).getCanonicalFile();
                if (isReCreat) {
                    file.delete();
                    file.createNewFile();
                }
                return file;
            } catch (IOException ioex) {
                exceptionsCount++;
                if (exceptionsCount >= 50) {
                    throw new IORuntimeException(ioex);
                }
            }
        } while (exceptionsCount >= 50);
        throw new IORuntimeException(ioex);
    }

    public static File copyFile(String src, String dest, StandardCopyOption... options) throws IORuntimeException {
        Assert.notBlank(src, "Source File path is blank !", new Object[0]);
        Assert.notBlank(dest, "Destination File path is blank !", new Object[0]);
        return copyFile(Paths.get(src, new String[0]), Paths.get(dest, new String[0]), options).toFile();
    }

    public static File copyFile(File src, File dest, StandardCopyOption... options) throws IORuntimeException {
        Assert.notNull(src, "Source File is null !", new Object[0]);
        if (!src.exists()) {
            throw new IORuntimeException("File not exist: " + src);
        }
        Assert.notNull(dest, "Destination File or directiory is null !", new Object[0]);
        if (equals(src, dest)) {
            throw new IORuntimeException("Files '{}' and '{}' are equal", src, dest);
        }
        return copyFile(src.toPath(), dest.toPath(), options).toFile();
    }

    public static File copy(String srcPath, String destPath, boolean isOverride) throws IORuntimeException {
        return copy(file(srcPath), file(destPath), isOverride);
    }

    public static File copy(File src, File dest, boolean isOverride) throws IORuntimeException {
        return FileCopier.create(src, dest).setOverride(isOverride).copy();
    }

    public static File copyContent(File src, File dest, boolean isOverride) throws IORuntimeException {
        return FileCopier.create(src, dest).setCopyContentIfDir(true).setOverride(isOverride).copy();
    }

    public static File copyFilesFromDir(File src, File dest, boolean isOverride) throws IORuntimeException {
        return FileCopier.create(src, dest).setCopyContentIfDir(true).setOnlyCopyFile(true).setOverride(isOverride).copy();
    }

    public static void move(File src, File target, boolean isOverride) throws IORuntimeException {
        Assert.notNull(src, "Src file must be not null!", new Object[0]);
        Assert.notNull(target, "target file must be not null!", new Object[0]);
        move(src.toPath(), target.toPath(), isOverride);
    }

    public static void moveContent(File src, File target, boolean isOverride) throws IORuntimeException {
        Assert.notNull(src, "Src file must be not null!", new Object[0]);
        Assert.notNull(target, "target file must be not null!", new Object[0]);
        moveContent(src.toPath(), target.toPath(), isOverride);
    }

    public static File rename(File file, String newName, boolean isOverride) {
        return rename(file, newName, false, isOverride);
    }

    public static File rename(File file, String newName, boolean isRetainExt, boolean isOverride) {
        if (isRetainExt) {
            String extName = extName(file);
            if (StrUtil.isNotBlank(extName)) {
                newName = newName.concat(".").concat(extName);
            }
        }
        return rename(file.toPath(), newName, isOverride).toFile();
    }

    public static String getCanonicalPath(File file) {
        if (file == null) {
            return null;
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static String getAbsolutePath(String path, Class<?> baseClass) {
        String normalPath;
        if (path == null) {
            normalPath = "";
        } else {
            normalPath = normalize(path);
            if (isAbsolutePath(normalPath)) {
                return normalPath;
            }
        }
        URL url = ResourceUtil.getResource(normalPath, baseClass);
        if (url != null) {
            return normalize(URLUtil.getDecodedPath(url));
        }
        String classPath = ClassUtil.getClassPath();
        if (classPath == null) {
            return path;
        }
        return normalize(classPath.concat((String) Objects.requireNonNull(path)));
    }

    public static String getAbsolutePath(String path) {
        return getAbsolutePath(path, null);
    }

    public static String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    public static boolean isAbsolutePath(String path) {
        if (StrUtil.isEmpty(path)) {
            return false;
        }
        return '/' == path.charAt(0) || path.matches("^[a-zA-Z]:([/\\\\].*)?");
    }

    public static boolean isDirectory(String path) {
        return path != null && file(path).isDirectory();
    }

    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }

    public static boolean isFile(String path) {
        return path != null && file(path).isFile();
    }

    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    public static boolean equals(File file1, File file2) throws IORuntimeException {
        Assert.notNull(file1);
        Assert.notNull(file2);
        if (file1.exists() && file2.exists()) {
            return equals(file1.toPath(), file2.toPath());
        }
        return (file1.exists() || file2.exists() || !pathEquals(file1, file2)) ? false : true;
    }

    public static boolean contentEquals(File file1, File file2) throws IORuntimeException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }
        if (!file1Exists) {
            return true;
        }
        if (file1.isDirectory() || file2.isDirectory()) {
            throw new IORuntimeException("Can't compare directories, only files");
        }
        if (file1.length() != file2.length()) {
            return false;
        }
        if (equals(file1, file2)) {
            return true;
        }
        InputStream input1 = null;
        InputStream input2 = null;
        try {
            input1 = getInputStream(file1);
            input2 = getInputStream(file2);
            return IoUtil.contentEquals(input1, input2);
        } finally {
            IoUtil.close((Closeable) input1);
            IoUtil.close((Closeable) input2);
        }
    }

    public static boolean contentEqualsIgnoreEOL(File file1, File file2, Charset charset) throws IORuntimeException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }
        if (!file1Exists) {
            return true;
        }
        if (file1.isDirectory() || file2.isDirectory()) {
            throw new IORuntimeException("Can't compare directories, only files");
        }
        if (equals(file1, file2)) {
            return true;
        }
        Reader input1 = null;
        Reader input2 = null;
        try {
            input1 = getReader(file1, charset);
            input2 = getReader(file2, charset);
            return IoUtil.contentEqualsIgnoreEOL(input1, input2);
        } finally {
            IoUtil.close((Closeable) input1);
            IoUtil.close((Closeable) input2);
        }
    }

    public static boolean pathEquals(File file1, File file2) {
        if (isWindows()) {
            try {
                return StrUtil.equalsIgnoreCase(file1.getCanonicalPath(), file2.getCanonicalPath());
            } catch (Exception e) {
                return StrUtil.equalsIgnoreCase(file1.getAbsolutePath(), file2.getAbsolutePath());
            }
        }
        try {
            return StrUtil.equals(file1.getCanonicalPath(), file2.getCanonicalPath());
        } catch (Exception e2) {
            return StrUtil.equals(file1.getAbsolutePath(), file2.getAbsolutePath());
        }
    }

    public static int lastIndexOfSeparator(String filePath) {
        char c;
        if (StrUtil.isNotEmpty(filePath)) {
            int i = filePath.length();
            do {
                i--;
                if (i >= 0) {
                    c = filePath.charAt(i);
                }
            } while (!CharUtil.isFileSeparator(c));
            return i;
        }
        return -1;
    }

    @Deprecated
    public static boolean isModifed(File file, long lastModifyTime) {
        return isModified(file, lastModifyTime);
    }

    public static boolean isModified(File file, long lastModifyTime) {
        return (file != null && file.exists() && file.lastModified() == lastModifyTime) ? false : true;
    }

    public static String normalize(String path) {
        if (path == null) {
            return null;
        }
        String pathToUse = StrUtil.removePrefixIgnoreCase(StrUtil.removePrefixIgnoreCase(path, URLUtil.CLASSPATH_URL_PREFIX), "file:");
        if (StrUtil.startWith((CharSequence) pathToUse, '~')) {
            pathToUse = getUserHomePath() + pathToUse.substring(1);
        }
        String pathToUse2 = StrUtil.trimStart(pathToUse.replaceAll("[/\\\\]+", "/"));
        if (path.startsWith("\\\\")) {
            pathToUse2 = StrPool.BACKSLASH + pathToUse2;
        }
        String prefix = "";
        int prefixIndex = pathToUse2.indexOf(":");
        if (prefixIndex > -1) {
            prefix = pathToUse2.substring(0, prefixIndex + 1);
            if (StrUtil.startWith((CharSequence) prefix, '/')) {
                prefix = prefix.substring(1);
            }
            if (!prefix.contains("/")) {
                pathToUse2 = pathToUse2.substring(prefixIndex + 1);
            } else {
                prefix = "";
            }
        }
        if (pathToUse2.startsWith("/")) {
            prefix = prefix + "/";
            pathToUse2 = pathToUse2.substring(1);
        }
        List<String> pathList = StrUtil.split((CharSequence) pathToUse2, '/');
        List<String> pathElements = new LinkedList<>();
        int tops = 0;
        for (int i = pathList.size() - 1; i >= 0; i--) {
            String element = pathList.get(i);
            if (!".".equals(element)) {
                if (StrPool.DOUBLE_DOT.equals(element)) {
                    tops++;
                } else if (tops > 0) {
                    tops--;
                } else {
                    pathElements.add(0, element);
                }
            }
        }
        if (tops > 0 && StrUtil.isEmpty(prefix)) {
            while (true) {
                int tops2 = tops - 1;
                if (tops <= 0) {
                    break;
                }
                pathElements.add(0, StrPool.DOUBLE_DOT);
                tops = tops2;
            }
        }
        return prefix + CollUtil.join(pathElements, "/");
    }

    public static String subPath(String rootDir, File file) {
        try {
            return subPath(rootDir, file.getCanonicalPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static String subPath(String dirPath, String filePath) {
        if (StrUtil.isNotEmpty(dirPath) && StrUtil.isNotEmpty(filePath)) {
            String result = StrUtil.removePrefixIgnoreCase(normalize(filePath), StrUtil.removeSuffix(normalize(dirPath), "/"));
            return StrUtil.removePrefix(result, "/");
        }
        return filePath;
    }

    public static String getName(File file) {
        return FileNameUtil.getName(file);
    }

    public static String getName(String filePath) {
        return FileNameUtil.getName(filePath);
    }

    public static String getSuffix(File file) {
        return FileNameUtil.getSuffix(file);
    }

    public static String getSuffix(String fileName) {
        return FileNameUtil.getSuffix(fileName);
    }

    public static String getPrefix(File file) {
        return FileNameUtil.getPrefix(file);
    }

    public static String getPrefix(String fileName) {
        return FileNameUtil.getPrefix(fileName);
    }

    public static String mainName(File file) {
        return FileNameUtil.mainName(file);
    }

    public static String mainName(String fileName) {
        return FileNameUtil.mainName(fileName);
    }

    public static String extName(File file) {
        return FileNameUtil.extName(file);
    }

    public static String extName(String fileName) {
        return FileNameUtil.extName(fileName);
    }

    public static boolean pathEndsWith(File file, String suffix) {
        return file.getPath().toLowerCase().endsWith(suffix);
    }

    public static String getType(File file) throws IORuntimeException {
        return FileTypeUtil.getType(file);
    }

    public static BufferedInputStream getInputStream(File file) throws IORuntimeException {
        return IoUtil.toBuffered(IoUtil.toStream(file));
    }

    public static BufferedInputStream getInputStream(String path) throws IORuntimeException {
        return getInputStream(file(path));
    }

    public static BOMInputStream getBOMInputStream(File file) throws IORuntimeException {
        try {
            return new BOMInputStream(new FileInputStream(file));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static BufferedReader getBOMReader(File file) {
        return IoUtil.getReader(getBOMInputStream(file));
    }

    public static BufferedReader getUtf8Reader(File file) throws IORuntimeException {
        return getReader(file, CharsetUtil.CHARSET_UTF_8);
    }

    public static BufferedReader getUtf8Reader(String path) throws IORuntimeException {
        return getReader(path, CharsetUtil.CHARSET_UTF_8);
    }

    @Deprecated
    public static BufferedReader getReader(File file, String charsetName) throws IORuntimeException {
        return IoUtil.getReader(getInputStream(file), CharsetUtil.charset(charsetName));
    }

    public static BufferedReader getReader(File file, Charset charset) throws IORuntimeException {
        return IoUtil.getReader(getInputStream(file), charset);
    }

    @Deprecated
    public static BufferedReader getReader(String path, String charsetName) throws IORuntimeException {
        return getReader(path, CharsetUtil.charset(charsetName));
    }

    public static BufferedReader getReader(String path, Charset charset) throws IORuntimeException {
        return getReader(file(path), charset);
    }

    public static byte[] readBytes(File file) throws IORuntimeException {
        return FileReader.create(file).readBytes();
    }

    public static byte[] readBytes(String filePath) throws IORuntimeException {
        return readBytes(file(filePath));
    }

    public static String readUtf8String(File file) throws IORuntimeException {
        return readString(file, CharsetUtil.CHARSET_UTF_8);
    }

    public static String readUtf8String(String path) throws IORuntimeException {
        return readString(path, CharsetUtil.CHARSET_UTF_8);
    }

    @Deprecated
    public static String readString(File file, String charsetName) throws IORuntimeException {
        return readString(file, CharsetUtil.charset(charsetName));
    }

    public static String readString(File file, Charset charset) throws IORuntimeException {
        return FileReader.create(file, charset).readString();
    }

    @Deprecated
    public static String readString(String path, String charsetName) throws IORuntimeException {
        return readString(path, CharsetUtil.charset(charsetName));
    }

    public static String readString(String path, Charset charset) throws IORuntimeException {
        return readString(file(path), charset);
    }

    @Deprecated
    public static String readString(URL url, String charsetName) throws IORuntimeException {
        return readString(url, CharsetUtil.charset(charsetName));
    }

    public static String readString(URL url, Charset charset) throws IORuntimeException {
        if (url == null) {
            throw new NullPointerException("Empty url provided!");
        }
        InputStream in = null;
        try {
            try {
                in = url.openStream();
                return IoUtil.read(in, charset);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public static <T extends Collection<String>> T readUtf8Lines(String path, T collection) throws IORuntimeException {
        return (T) readLines(path, CharsetUtil.CHARSET_UTF_8, collection);
    }

    public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IORuntimeException {
        return (T) readLines(file(path), charset, collection);
    }

    public static <T extends Collection<String>> T readLines(String path, Charset charset, T collection) throws IORuntimeException {
        return (T) readLines(file(path), charset, collection);
    }

    public static <T extends Collection<String>> T readUtf8Lines(File file, T collection) throws IORuntimeException {
        return (T) readLines(file, CharsetUtil.CHARSET_UTF_8, collection);
    }

    public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IORuntimeException {
        return (T) FileReader.create(file, CharsetUtil.charset(charset)).readLines((FileReader) collection);
    }

    public static <T extends Collection<String>> T readLines(File file, Charset charset, T collection) throws IORuntimeException {
        return (T) FileReader.create(file, charset).readLines((FileReader) collection);
    }

    public static <T extends Collection<String>> T readUtf8Lines(URL url, T collection) throws IORuntimeException {
        return (T) readLines(url, CharsetUtil.CHARSET_UTF_8, collection);
    }

    @Deprecated
    public static <T extends Collection<String>> T readLines(URL url, String charsetName, T collection) throws IORuntimeException {
        return (T) readLines(url, CharsetUtil.charset(charsetName), collection);
    }

    public static <T extends Collection<String>> T readLines(URL url, Charset charset, T collection) throws IORuntimeException {
        InputStream in = null;
        try {
            try {
                in = url.openStream();
                return (T) IoUtil.readLines(in, charset, collection);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public static List<String> readUtf8Lines(URL url) throws IORuntimeException {
        return readLines(url, CharsetUtil.CHARSET_UTF_8);
    }

    @Deprecated
    public static List<String> readLines(URL url, String charsetName) throws IORuntimeException {
        return readLines(url, CharsetUtil.charset(charsetName));
    }

    public static List<String> readLines(URL url, Charset charset) throws IORuntimeException {
        return (List) readLines(url, charset, new ArrayList());
    }

    public static List<String> readUtf8Lines(String path) throws IORuntimeException {
        return readLines(path, CharsetUtil.CHARSET_UTF_8);
    }

    public static List<String> readLines(String path, String charset) throws IORuntimeException {
        return (List) readLines(path, charset, new ArrayList());
    }

    public static List<String> readLines(String path, Charset charset) throws IORuntimeException {
        return (List) readLines(path, charset, new ArrayList());
    }

    public static List<String> readUtf8Lines(File file) throws IORuntimeException {
        return readLines(file, CharsetUtil.CHARSET_UTF_8);
    }

    public static List<String> readLines(File file, String charset) throws IORuntimeException {
        return (List) readLines(file, charset, new ArrayList());
    }

    public static List<String> readLines(File file, Charset charset) throws IORuntimeException {
        return (List) readLines(file, charset, new ArrayList());
    }

    public static void readUtf8Lines(File file, LineHandler lineHandler) throws IORuntimeException {
        readLines(file, CharsetUtil.CHARSET_UTF_8, lineHandler);
    }

    public static void readLines(File file, Charset charset, LineHandler lineHandler) throws IORuntimeException {
        FileReader.create(file, charset).readLines(lineHandler);
    }

    public static void readLines(RandomAccessFile file, Charset charset, LineHandler lineHandler) {
        while (true) {
            try {
                String line = file.readLine();
                if (line != null) {
                    lineHandler.handle(CharsetUtil.convert(line, CharsetUtil.CHARSET_ISO_8859_1, charset));
                } else {
                    return;
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
    }

    public static void readLine(RandomAccessFile file, Charset charset, LineHandler lineHandler) {
        String line = readLine(file, charset);
        if (line != null) {
            lineHandler.handle(line);
        }
    }

    public static String readLine(RandomAccessFile file, Charset charset) {
        try {
            String line = file.readLine();
            if (line != null) {
                return CharsetUtil.convert(line, CharsetUtil.CHARSET_ISO_8859_1, charset);
            }
            return null;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static <T> T loadUtf8(String path, FileReader.ReaderHandler<T> readerHandler) throws IORuntimeException {
        return (T) load(path, CharsetUtil.CHARSET_UTF_8, readerHandler);
    }

    public static <T> T load(String path, String charset, FileReader.ReaderHandler<T> readerHandler) throws IORuntimeException {
        return (T) FileReader.create(file(path), CharsetUtil.charset(charset)).read(readerHandler);
    }

    public static <T> T load(String path, Charset charset, FileReader.ReaderHandler<T> readerHandler) throws IORuntimeException {
        return (T) FileReader.create(file(path), charset).read(readerHandler);
    }

    public static <T> T loadUtf8(File file, FileReader.ReaderHandler<T> readerHandler) throws IORuntimeException {
        return (T) load(file, CharsetUtil.CHARSET_UTF_8, readerHandler);
    }

    public static <T> T load(File file, Charset charset, FileReader.ReaderHandler<T> readerHandler) throws IORuntimeException {
        return (T) FileReader.create(file, charset).read(readerHandler);
    }

    public static BufferedOutputStream getOutputStream(File file) throws IORuntimeException {
        try {
            OutputStream out = new FileOutputStream(touch(file));
            return IoUtil.toBuffered(out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static BufferedOutputStream getOutputStream(String path) throws IORuntimeException {
        return getOutputStream(touch(path));
    }

    @Deprecated
    public static BufferedWriter getWriter(String path, String charsetName, boolean isAppend) throws IORuntimeException {
        return getWriter(path, Charset.forName(charsetName), isAppend);
    }

    public static BufferedWriter getWriter(String path, Charset charset, boolean isAppend) throws IORuntimeException {
        return getWriter(touch(path), charset, isAppend);
    }

    @Deprecated
    public static BufferedWriter getWriter(File file, String charsetName, boolean isAppend) throws IORuntimeException {
        return getWriter(file, Charset.forName(charsetName), isAppend);
    }

    public static BufferedWriter getWriter(File file, Charset charset, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, charset).getWriter(isAppend);
    }

    public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IORuntimeException {
        return new PrintWriter(getWriter(path, charset, isAppend));
    }

    public static PrintWriter getPrintWriter(String path, Charset charset, boolean isAppend) throws IORuntimeException {
        return new PrintWriter(getWriter(path, charset, isAppend));
    }

    public static PrintWriter getPrintWriter(File file, String charset, boolean isAppend) throws IORuntimeException {
        return new PrintWriter(getWriter(file, charset, isAppend));
    }

    public static PrintWriter getPrintWriter(File file, Charset charset, boolean isAppend) throws IORuntimeException {
        return new PrintWriter(getWriter(file, charset, isAppend));
    }

    public static String getLineSeparator() {
        return System.lineSeparator();
    }

    public static File writeUtf8String(String content, String path) throws IORuntimeException {
        return writeString(content, path, CharsetUtil.CHARSET_UTF_8);
    }

    public static File writeUtf8String(String content, File file) throws IORuntimeException {
        return writeString(content, file, CharsetUtil.CHARSET_UTF_8);
    }

    public static File writeString(String content, String path, String charset) throws IORuntimeException {
        return writeString(content, touch(path), charset);
    }

    public static File writeString(String content, String path, Charset charset) throws IORuntimeException {
        return writeString(content, touch(path), charset);
    }

    public static File writeString(String content, File file, String charset) throws IORuntimeException {
        return FileWriter.create(file, CharsetUtil.charset(charset)).write(content);
    }

    public static File writeString(String content, File file, Charset charset) throws IORuntimeException {
        return FileWriter.create(file, charset).write(content);
    }

    public static File appendUtf8String(String content, String path) throws IORuntimeException {
        return appendString(content, path, CharsetUtil.CHARSET_UTF_8);
    }

    public static File appendString(String content, String path, String charset) throws IORuntimeException {
        return appendString(content, touch(path), charset);
    }

    public static File appendString(String content, String path, Charset charset) throws IORuntimeException {
        return appendString(content, touch(path), charset);
    }

    public static File appendUtf8String(String content, File file) throws IORuntimeException {
        return appendString(content, file, CharsetUtil.CHARSET_UTF_8);
    }

    public static File appendString(String content, File file, String charset) throws IORuntimeException {
        return FileWriter.create(file, CharsetUtil.charset(charset)).append(content);
    }

    public static File appendString(String content, File file, Charset charset) throws IORuntimeException {
        return FileWriter.create(file, charset).append(content);
    }

    public static <T> File writeUtf8Lines(Collection<T> list, String path) throws IORuntimeException {
        return writeLines(list, path, CharsetUtil.CHARSET_UTF_8);
    }

    public static <T> File writeUtf8Lines(Collection<T> list, File file) throws IORuntimeException {
        return writeLines(list, file, CharsetUtil.CHARSET_UTF_8);
    }

    public static <T> File writeLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        return writeLines((Collection) list, path, charset, false);
    }

    public static <T> File writeLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
        return writeLines((Collection) list, path, charset, false);
    }

    public static <T> File writeLines(Collection<T> list, File file, String charset) throws IORuntimeException {
        return writeLines((Collection) list, file, charset, false);
    }

    public static <T> File writeLines(Collection<T> list, File file, Charset charset) throws IORuntimeException {
        return writeLines((Collection) list, file, charset, false);
    }

    public static <T> File appendUtf8Lines(Collection<T> list, File file) throws IORuntimeException {
        return appendLines(list, file, CharsetUtil.CHARSET_UTF_8);
    }

    public static <T> File appendUtf8Lines(Collection<T> list, String path) throws IORuntimeException {
        return appendLines(list, path, CharsetUtil.CHARSET_UTF_8);
    }

    public static <T> File appendLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        return writeLines((Collection) list, path, charset, true);
    }

    public static <T> File appendLines(Collection<T> list, File file, String charset) throws IORuntimeException {
        return writeLines((Collection) list, file, charset, true);
    }

    public static <T> File appendLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
        return writeLines((Collection) list, path, charset, true);
    }

    public static <T> File appendLines(Collection<T> list, File file, Charset charset) throws IORuntimeException {
        return writeLines((Collection) list, file, charset, true);
    }

    public static <T> File writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IORuntimeException {
        return writeLines(list, file(path), charset, isAppend);
    }

    public static <T> File writeLines(Collection<T> list, String path, Charset charset, boolean isAppend) throws IORuntimeException {
        return writeLines(list, file(path), charset, isAppend);
    }

    public static <T> File writeLines(Collection<T> list, File file, String charset, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, CharsetUtil.charset(charset)).writeLines(list, isAppend);
    }

    public static <T> File writeLines(Collection<T> list, File file, Charset charset, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, charset).writeLines(list, isAppend);
    }

    public static File writeUtf8Map(Map<?, ?> map, File file, String kvSeparator, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, CharsetUtil.CHARSET_UTF_8).writeMap(map, kvSeparator, isAppend);
    }

    public static File writeMap(Map<?, ?> map, File file, Charset charset, String kvSeparator, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(file, charset).writeMap(map, kvSeparator, isAppend);
    }

    public static File writeBytes(byte[] data, String path) throws IORuntimeException {
        return writeBytes(data, touch(path));
    }

    public static File writeBytes(byte[] data, File dest) throws IORuntimeException {
        return writeBytes(data, dest, 0, data.length, false);
    }

    public static File writeBytes(byte[] data, File dest, int off, int len, boolean isAppend) throws IORuntimeException {
        return FileWriter.create(dest).write(data, off, len, isAppend);
    }

    public static File writeFromStream(InputStream in, File dest) throws IORuntimeException {
        return writeFromStream(in, dest, true);
    }

    public static File writeFromStream(InputStream in, File dest, boolean isCloseIn) throws IORuntimeException {
        return FileWriter.create(dest).writeFromStream(in, isCloseIn);
    }

    public static File writeFromStream(InputStream in, String fullFilePath) throws IORuntimeException {
        return writeFromStream(in, touch(fullFilePath));
    }

    public static long writeToStream(File file, OutputStream out) throws IORuntimeException {
        return FileReader.create(file).writeToStream(out);
    }

    public static long writeToStream(String fullFilePath, OutputStream out) throws IORuntimeException {
        return writeToStream(touch(fullFilePath), out);
    }

    public static String readableFileSize(File file) {
        return readableFileSize(file.length());
    }

    public static String readableFileSize(long size) {
        return DataSizeUtil.format(size);
    }

    public static File convertCharset(File file, Charset srcCharset, Charset destCharset) {
        return CharsetUtil.convert(file, srcCharset, destCharset);
    }

    public static File convertLineSeparator(File file, Charset charset, LineSeparator lineSeparator) {
        List<String> lines = readLines(file, charset);
        return FileWriter.create(file, charset).writeLines(lines, lineSeparator, false);
    }

    public static String cleanInvalid(String fileName) {
        return FileNameUtil.cleanInvalid(fileName);
    }

    public static boolean containsInvalid(String fileName) {
        return FileNameUtil.containsInvalid(fileName);
    }

    public static long checksumCRC32(File file) throws IORuntimeException {
        return checksum(file, new CRC32()).getValue();
    }

    public static Checksum checksum(File file, Checksum checksum) throws IORuntimeException {
        Assert.notNull(file, "File is null !", new Object[0]);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Checksums can't be computed on directories");
        }
        try {
            return IoUtil.checksum(new FileInputStream(file), checksum);
        } catch (FileNotFoundException e) {
            throw new IORuntimeException(e);
        }
    }

    public static File getWebRoot() {
        String classPath = ClassUtil.getClassPath();
        if (StrUtil.isNotBlank(classPath)) {
            return getParent(file(classPath), 2);
        }
        return null;
    }

    public static String getParent(String filePath, int level) {
        File parent = getParent(file(filePath), level);
        if (parent == null) {
            return null;
        }
        try {
            return parent.getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static File getParent(File file, int level) {
        if (level < 1 || file == null) {
            return file;
        }
        try {
            File parentFile = file.getCanonicalFile().getParentFile();
            if (1 == level) {
                return parentFile;
            }
            return getParent(parentFile, level - 1);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static File checkSlip(File parentFile, File file) throws IllegalArgumentException {
        if (parentFile != null && file != null) {
            try {
                String parentCanonicalPath = parentFile.getCanonicalPath();
                String canonicalPath = file.getCanonicalPath();
                if (!canonicalPath.startsWith(parentCanonicalPath)) {
                    throw new IllegalArgumentException("New file is outside of the parent dir: " + file.getName());
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
        return file;
    }

    public static String getMimeType(String filePath) {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
        if (contentType == null) {
            if (filePath.endsWith(".css")) {
                contentType = "text/css";
            } else if (filePath.endsWith(".js")) {
                contentType = "application/x-javascript";
            }
        }
        if (contentType == null) {
            return getMimeType(Paths.get(filePath, new String[0]));
        }
        return contentType;
    }

    public static boolean isSymlink(File file) {
        return isSymlink(file.toPath());
    }

    public static boolean isSub(File parent, File sub) {
        Assert.notNull(parent);
        Assert.notNull(sub);
        return isSub(parent.toPath(), sub.toPath());
    }

    public static RandomAccessFile createRandomAccessFile(Path path, FileMode mode) {
        return createRandomAccessFile(path.toFile(), mode);
    }

    public static RandomAccessFile createRandomAccessFile(File file, FileMode mode) {
        try {
            return new RandomAccessFile(file, mode.name());
        } catch (FileNotFoundException e) {
            throw new IORuntimeException(e);
        }
    }

    public static void tail(File file, LineHandler handler) {
        tail(file, CharsetUtil.CHARSET_UTF_8, handler);
    }

    public static void tail(File file, Charset charset, LineHandler handler) {
        new Tailer(file, charset, handler).start();
    }

    public static void tail(File file, Charset charset) {
        tail(file, charset, Tailer.CONSOLE_HANDLER);
    }

    private static File buildFile(File outFile, String fileName) {
        String fileName2 = fileName.replace('\\', '/');
        if (!isWindows() && fileName2.lastIndexOf(47, fileName2.length() - 2) > 0) {
            List<String> pathParts = StrUtil.split((CharSequence) fileName2, '/', false, true);
            int lastPartIndex = pathParts.size() - 1;
            for (int i = 0; i < lastPartIndex; i++) {
                outFile = new File(outFile, pathParts.get(i));
            }
            outFile.mkdirs();
            String fileName3 = pathParts.get(lastPartIndex);
            fileName2 = fileName3;
        }
        return new File(outFile, fileName2);
    }
}
