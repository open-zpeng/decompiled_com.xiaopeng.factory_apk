package cn.hutool.core.io.file;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class FileNameUtil {
    public static final String EXT_CLASS = ".class";
    public static final String EXT_JAR = ".jar";
    public static final String EXT_JAVA = ".java";
    private static final Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");
    public static final char UNIX_SEPARATOR = '/';
    public static final char WINDOWS_SEPARATOR = '\\';

    public static String getName(File file) {
        if (file != null) {
            return file.getName();
        }
        return null;
    }

    public static String getName(String filePath) {
        if (filePath == null) {
            return null;
        }
        int len = filePath.length();
        if (len == 0) {
            return filePath;
        }
        if (CharUtil.isFileSeparator(filePath.charAt(len - 1))) {
            len--;
        }
        int begin = 0;
        int i = len - 1;
        while (true) {
            if (i <= -1) {
                break;
            }
            char c = filePath.charAt(i);
            if (!CharUtil.isFileSeparator(c)) {
                i--;
            } else {
                begin = i + 1;
                break;
            }
        }
        return filePath.substring(begin, len);
    }

    public static String getSuffix(File file) {
        return extName(file);
    }

    public static String getSuffix(String fileName) {
        return extName(fileName);
    }

    public static String getPrefix(File file) {
        return mainName(file);
    }

    public static String getPrefix(String fileName) {
        return mainName(fileName);
    }

    public static String mainName(File file) {
        if (file.isDirectory()) {
            return file.getName();
        }
        return mainName(file.getName());
    }

    public static String mainName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int len = fileName.length();
        if (len == 0) {
            return fileName;
        }
        if (CharUtil.isFileSeparator(fileName.charAt(len - 1))) {
            len--;
        }
        int begin = 0;
        int end = len;
        int i = len - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            char c = fileName.charAt(i);
            if (len == end && '.' == c) {
                end = i;
            }
            if (!CharUtil.isFileSeparator(c)) {
                i--;
            } else {
                begin = i + 1;
                break;
            }
        }
        return fileName.substring(begin, end);
    }

    public static String extName(File file) {
        if (file == null || file.isDirectory()) {
            return null;
        }
        return extName(file.getName());
    }

    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        String ext = fileName.substring(index + 1);
        return StrUtil.containsAny(ext, '/', '\\') ? "" : ext;
    }

    public static String cleanInvalid(String fileName) {
        return StrUtil.isBlank(fileName) ? fileName : ReUtil.delAll(FILE_NAME_INVALID_PATTERN_WIN, fileName);
    }

    public static boolean containsInvalid(String fileName) {
        return !StrUtil.isBlank(fileName) && ReUtil.contains(FILE_NAME_INVALID_PATTERN_WIN, fileName);
    }

    public static boolean isType(String fileName, String... extNames) {
        return StrUtil.equalsAnyIgnoreCase(extName(fileName), extNames);
    }
}
