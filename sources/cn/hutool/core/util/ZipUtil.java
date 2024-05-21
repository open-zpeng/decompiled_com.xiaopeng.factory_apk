package cn.hutool.core.util;

import cn.hutool.core.compress.Deflate;
import cn.hutool.core.compress.Gzip;
import cn.hutool.core.compress.ZipReader;
import cn.hutool.core.compress.ZipWriter;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.Resource;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
/* loaded from: classes.dex */
public class ZipUtil {
    private static final int DEFAULT_BYTE_ARRAY_LENGTH = 32;
    private static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();

    public static ZipFile toZipFile(File file, Charset charset) {
        try {
            return new ZipFile(file, (Charset) ObjectUtil.defaultIfNull(charset, CharsetUtil.CHARSET_UTF_8));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static InputStream getStream(ZipFile zipFile, ZipEntry zipEntry) {
        try {
            return zipFile.getInputStream(zipEntry);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static File zip(String srcPath) throws UtilException {
        return zip(srcPath, DEFAULT_CHARSET);
    }

    public static File zip(String srcPath, Charset charset) throws UtilException {
        return zip(FileUtil.file(srcPath), charset);
    }

    public static File zip(File srcFile) throws UtilException {
        return zip(srcFile, DEFAULT_CHARSET);
    }

    public static File zip(File srcFile, Charset charset) throws UtilException {
        File parentFile = srcFile.getParentFile();
        File zipFile = FileUtil.file(parentFile, FileUtil.mainName(srcFile) + ".zip");
        zip(zipFile, charset, false, srcFile);
        return zipFile;
    }

    public static File zip(String srcPath, String zipPath) throws UtilException {
        return zip(srcPath, zipPath, false);
    }

    public static File zip(String srcPath, String zipPath, boolean withSrcDir) throws UtilException {
        return zip(srcPath, zipPath, DEFAULT_CHARSET, withSrcDir);
    }

    public static File zip(String srcPath, String zipPath, Charset charset, boolean withSrcDir) throws UtilException {
        File srcFile = FileUtil.file(srcPath);
        File zipFile = FileUtil.file(zipPath);
        zip(zipFile, charset, withSrcDir, srcFile);
        return zipFile;
    }

    public static File zip(File zipFile, boolean withSrcDir, File... srcFiles) throws UtilException {
        return zip(zipFile, DEFAULT_CHARSET, withSrcDir, srcFiles);
    }

    public static File zip(File zipFile, Charset charset, boolean withSrcDir, File... srcFiles) throws UtilException {
        return zip(zipFile, charset, withSrcDir, (FileFilter) null, srcFiles);
    }

    public static File zip(File zipFile, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
        validateFiles(zipFile, srcFiles);
        ZipWriter.of(zipFile, charset).add(withSrcDir, filter, srcFiles).close();
        return zipFile;
    }

    public static void zip(OutputStream out, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
        ZipWriter.of(out, charset).add(withSrcDir, filter, srcFiles).close();
    }

    @Deprecated
    public static void zip(ZipOutputStream zipOutputStream, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
        ZipWriter zipWriter = new ZipWriter(zipOutputStream);
        try {
            zipWriter.add(withSrcDir, filter, srcFiles);
            zipWriter.close();
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    zipWriter.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static File zip(File zipFile, String path, String data) throws UtilException {
        return zip(zipFile, path, data, DEFAULT_CHARSET);
    }

    public static File zip(File zipFile, String path, String data, Charset charset) throws UtilException {
        return zip(zipFile, path, IoUtil.toStream(data, charset), charset);
    }

    public static File zip(File zipFile, String path, InputStream in) throws UtilException {
        return zip(zipFile, path, in, DEFAULT_CHARSET);
    }

    public static File zip(File zipFile, String path, InputStream in, Charset charset) throws UtilException {
        return zip(zipFile, new String[]{path}, new InputStream[]{in}, charset);
    }

    public static File zip(File zipFile, String[] paths, InputStream[] ins) throws UtilException {
        return zip(zipFile, paths, ins, DEFAULT_CHARSET);
    }

    public static File zip(File zipFile, String[] paths, InputStream[] ins, Charset charset) throws UtilException {
        if (ArrayUtil.isEmpty((Object[]) paths) || ArrayUtil.isEmpty((Object[]) ins)) {
            throw new IllegalArgumentException("Paths or ins is empty !");
        }
        if (paths.length != ins.length) {
            throw new IllegalArgumentException("Paths length is not equals to ins length !");
        }
        ZipWriter zipWriter = ZipWriter.of(zipFile, charset);
        for (int i = 0; i < paths.length; i++) {
            try {
                zipWriter.add(paths[i], ins[i]);
            } finally {
            }
        }
        if (zipWriter != null) {
            zipWriter.close();
        }
        return zipFile;
    }

    public static void zip(OutputStream out, String[] paths, InputStream[] ins) {
        if (ArrayUtil.isEmpty((Object[]) paths) || ArrayUtil.isEmpty((Object[]) ins)) {
            throw new IllegalArgumentException("Paths or ins is empty !");
        }
        if (paths.length != ins.length) {
            throw new IllegalArgumentException("Paths length is not equals to ins length !");
        }
        ZipWriter zipWriter = ZipWriter.of(out, DEFAULT_CHARSET);
        for (int i = 0; i < paths.length; i++) {
            try {
                zipWriter.add(paths[i], ins[i]);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    if (zipWriter != null) {
                        try {
                            zipWriter.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    }
                    throw th2;
                }
            }
        }
        if (zipWriter != null) {
            zipWriter.close();
        }
    }

    public static void zip(ZipOutputStream zipOutputStream, String[] paths, InputStream[] ins) throws IORuntimeException {
        if (ArrayUtil.isEmpty((Object[]) paths) || ArrayUtil.isEmpty((Object[]) ins)) {
            throw new IllegalArgumentException("Paths or ins is empty !");
        }
        if (paths.length != ins.length) {
            throw new IllegalArgumentException("Paths length is not equals to ins length !");
        }
        ZipWriter zipWriter = new ZipWriter(zipOutputStream);
        for (int i = 0; i < paths.length; i++) {
            try {
                zipWriter.add(paths[i], ins[i]);
            } finally {
            }
        }
        zipWriter.close();
    }

    public static File zip(File zipFile, Charset charset, Resource... resources) throws UtilException {
        ZipWriter.of(zipFile, charset).add(resources).close();
        return zipFile;
    }

    public static File unzip(String zipFilePath) throws UtilException {
        return unzip(zipFilePath, DEFAULT_CHARSET);
    }

    public static File unzip(String zipFilePath, Charset charset) throws UtilException {
        return unzip(FileUtil.file(zipFilePath), charset);
    }

    public static File unzip(File zipFile) throws UtilException {
        return unzip(zipFile, DEFAULT_CHARSET);
    }

    public static File unzip(File zipFile, Charset charset) throws UtilException {
        File destDir = FileUtil.file(zipFile.getParentFile(), FileUtil.mainName(zipFile));
        return unzip(zipFile, destDir, charset);
    }

    public static File unzip(String zipFilePath, String outFileDir) throws UtilException {
        return unzip(zipFilePath, outFileDir, DEFAULT_CHARSET);
    }

    public static File unzip(String zipFilePath, String outFileDir, Charset charset) throws UtilException {
        return unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir), charset);
    }

    public static File unzip(File zipFile, File outFile) throws UtilException {
        return unzip(zipFile, outFile, DEFAULT_CHARSET);
    }

    public static File unzip(File zipFile, File outFile, Charset charset) {
        return unzip(toZipFile(zipFile, charset), outFile);
    }

    public static File unzip(ZipFile zipFile, File outFile) throws IORuntimeException {
        if (outFile.exists() && outFile.isFile()) {
            throw new IllegalArgumentException(StrUtil.format("Target path [{}] exist!", outFile.getAbsolutePath()));
        }
        ZipReader reader = new ZipReader(zipFile);
        try {
            reader.readTo(outFile);
            reader.close();
            return outFile;
        } finally {
        }
    }

    public static InputStream get(File zipFile, Charset charset, String path) {
        return get(toZipFile(zipFile, charset), path);
    }

    public static InputStream get(ZipFile zipFile, String path) {
        ZipEntry entry = zipFile.getEntry(path);
        if (entry != null) {
            return getStream(zipFile, entry);
        }
        return null;
    }

    public static void read(ZipFile zipFile, Consumer<ZipEntry> consumer) {
        ZipReader reader = new ZipReader(zipFile);
        try {
            reader.read(consumer);
            reader.close();
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    reader.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static File unzip(InputStream in, File outFile, Charset charset) throws UtilException {
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        return unzip(new ZipInputStream(in, charset), outFile);
    }

    public static File unzip(ZipInputStream zipStream, File outFile) throws UtilException {
        ZipReader reader = new ZipReader(zipStream);
        try {
            reader.readTo(outFile);
            reader.close();
            return outFile;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    reader.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static void read(ZipInputStream zipStream, Consumer<ZipEntry> consumer) {
        ZipReader reader = new ZipReader(zipStream);
        try {
            reader.read(consumer);
            reader.close();
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    reader.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    public static byte[] unzipFileBytes(String zipFilePath, String name) {
        return unzipFileBytes(zipFilePath, DEFAULT_CHARSET, name);
    }

    public static byte[] unzipFileBytes(String zipFilePath, Charset charset, String name) {
        return unzipFileBytes(FileUtil.file(zipFilePath), charset, name);
    }

    public static byte[] unzipFileBytes(File zipFile, String name) {
        return unzipFileBytes(zipFile, DEFAULT_CHARSET, name);
    }

    public static byte[] unzipFileBytes(File zipFile, Charset charset, String name) {
        ZipReader reader = ZipReader.of(zipFile, charset);
        try {
            byte[] readBytes = IoUtil.readBytes(reader.get(name));
            reader.close();
            return readBytes;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    public static byte[] gzip(String content, String charset) throws UtilException {
        return gzip(StrUtil.bytes(content, charset));
    }

    public static byte[] gzip(byte[] buf) throws UtilException {
        return gzip(new ByteArrayInputStream(buf), buf.length);
    }

    public static byte[] gzip(File file) throws UtilException {
        BufferedInputStream in = null;
        try {
            in = FileUtil.getInputStream(file);
            return gzip(in, (int) file.length());
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public static byte[] gzip(InputStream in) throws UtilException {
        return gzip(in, 32);
    }

    public static byte[] gzip(InputStream in, int length) throws UtilException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        Gzip.of(in, bos).gzip().close();
        return bos.toByteArray();
    }

    public static String unGzip(byte[] buf, String charset) throws UtilException {
        return StrUtil.str(unGzip(buf), charset);
    }

    public static byte[] unGzip(byte[] buf) throws UtilException {
        return unGzip(new ByteArrayInputStream(buf), buf.length);
    }

    public static byte[] unGzip(InputStream in) throws UtilException {
        return unGzip(in, 32);
    }

    public static byte[] unGzip(InputStream in, int length) throws UtilException {
        FastByteArrayOutputStream bos = new FastByteArrayOutputStream(length);
        Gzip.of(in, bos).unGzip().close();
        return bos.toByteArray();
    }

    public static byte[] zlib(String content, String charset, int level) {
        return zlib(StrUtil.bytes(content, charset), level);
    }

    public static byte[] zlib(File file, int level) {
        BufferedInputStream in = null;
        try {
            in = FileUtil.getInputStream(file);
            return zlib(in, level, (int) file.length());
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public static byte[] zlib(byte[] buf, int level) {
        return zlib(new ByteArrayInputStream(buf), level, buf.length);
    }

    public static byte[] zlib(InputStream in, int level) {
        return zlib(in, level, 32);
    }

    public static byte[] zlib(InputStream in, int level, int length) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        Deflate.of(in, out, false).deflater(level);
        return out.toByteArray();
    }

    public static String unZlib(byte[] buf, String charset) {
        return StrUtil.str(unZlib(buf), charset);
    }

    public static byte[] unZlib(byte[] buf) {
        return unZlib(new ByteArrayInputStream(buf), buf.length);
    }

    public static byte[] unZlib(InputStream in) {
        return unZlib(in, 32);
    }

    public static byte[] unZlib(InputStream in, int length) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        Deflate.of(in, out, false).inflater();
        return out.toByteArray();
    }

    public static List<String> listFileNames(ZipFile zipFile, String dir) {
        if (StrUtil.isNotBlank(dir)) {
            dir = StrUtil.addSuffixIfNot(dir, "/");
        }
        List<String> fileNames = new ArrayList<>();
        Iterator it = Collections.list(zipFile.entries()).iterator();
        while (it.hasNext()) {
            ZipEntry entry = (ZipEntry) it.next();
            String name = entry.getName();
            if (StrUtil.isEmpty(dir) || name.startsWith(dir)) {
                String nameSuffix = StrUtil.removePrefix(name, dir);
                if (StrUtil.isNotEmpty(nameSuffix) && !StrUtil.contains((CharSequence) nameSuffix, '/')) {
                    fileNames.add(nameSuffix);
                }
            }
        }
        return fileNames;
    }

    private static void validateFiles(File zipFile, File... srcFiles) throws UtilException {
        if (zipFile.isDirectory()) {
            throw new UtilException("Zip file [{}] must not be a directory !", zipFile.getAbsoluteFile());
        }
        for (File srcFile : srcFiles) {
            if (srcFile != null) {
                if (!srcFile.exists()) {
                    throw new UtilException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
                }
                if (srcFile.isDirectory() && FileUtil.isSub(srcFile, zipFile.getParentFile())) {
                    throw new UtilException("Zip file path [{}] must not be the child directory of [{}] !", zipFile.getPath(), srcFile.getPath());
                }
            }
        }
    }
}
