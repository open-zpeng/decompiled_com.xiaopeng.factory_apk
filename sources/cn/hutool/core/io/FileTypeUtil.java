package cn.hutool.core.io;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import io.sentry.SentryBaseEvent;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
/* loaded from: classes.dex */
public class FileTypeUtil {
    private static final Map<String, String> FILE_TYPE_MAP = new ConcurrentSkipListMap(new Comparator() { // from class: cn.hutool.core.io.-$$Lambda$FileTypeUtil$RkGIePb36UpPbK2FNXlkr6kAc_0
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return FileTypeUtil.lambda$static$0((String) obj, (String) obj2);
        }
    });

    static {
        FILE_TYPE_MAP.put("ffd8ff", ImgUtil.IMAGE_TYPE_JPG);
        FILE_TYPE_MAP.put("89504e47", ImgUtil.IMAGE_TYPE_PNG);
        FILE_TYPE_MAP.put("4749463837", ImgUtil.IMAGE_TYPE_GIF);
        FILE_TYPE_MAP.put("4749463839", ImgUtil.IMAGE_TYPE_GIF);
        FILE_TYPE_MAP.put("49492a00227105008037", "tif");
        FILE_TYPE_MAP.put("424d228c010000000000", ImgUtil.IMAGE_TYPE_BMP);
        FILE_TYPE_MAP.put("424d8240090000000000", ImgUtil.IMAGE_TYPE_BMP);
        FILE_TYPE_MAP.put("424d8e1b030000000000", ImgUtil.IMAGE_TYPE_BMP);
        FILE_TYPE_MAP.put("41433130313500000000", "dwg");
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf");
        FILE_TYPE_MAP.put("38425053000100000000", ImgUtil.IMAGE_TYPE_PSD);
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml");
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb");
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");
        FILE_TYPE_MAP.put("255044462d312e", "pdf");
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb");
        FILE_TYPE_MAP.put("464c5601050000000900", "flv");
        FILE_TYPE_MAP.put("0000001C66747970", "mp4");
        FILE_TYPE_MAP.put("00000020667479706", "mp4");
        FILE_TYPE_MAP.put("00000018667479706D70", "mp4");
        FILE_TYPE_MAP.put("49443303000000002176", "mp3");
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg");
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv");
        FILE_TYPE_MAP.put("52494646e27807005741", "wav");
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");
        FILE_TYPE_MAP.put("4d546864000000060001", "mid");
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");
        FILE_TYPE_MAP.put("504B03040a0000000000", URLUtil.URL_PROTOCOL_JAR);
        FILE_TYPE_MAP.put("504B0304140008000800", URLUtil.URL_PROTOCOL_JAR);
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10", "xls");
        FILE_TYPE_MAP.put("504B0304", URLUtil.URL_PROTOCOL_ZIP);
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");
        FILE_TYPE_MAP.put("7061636b616765207765", SentryBaseEvent.DEFAULT_PLATFORM);
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat");
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");
        FILE_TYPE_MAP.put("49545346030000006000", "chm");
        FILE_TYPE_MAP.put("04000000010000001300", "mxp");
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
        FILE_TYPE_MAP.put("6D6F6F76", "mov");
        FILE_TYPE_MAP.put("FF575043", "wpd");
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx");
        FILE_TYPE_MAP.put("2142444E", "pst");
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf");
        FILE_TYPE_MAP.put("E3828596", "pwl");
        FILE_TYPE_MAP.put("2E7261FD", "ram");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$static$0(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 == len2) {
            return s1.compareTo(s2);
        }
        return len2 - len1;
    }

    public static String putFileType(String fileStreamHexHead, String extName) {
        return FILE_TYPE_MAP.put(fileStreamHexHead, extName);
    }

    public static String removeFileType(String fileStreamHexHead) {
        return FILE_TYPE_MAP.remove(fileStreamHexHead);
    }

    public static String getType(String fileStreamHexHead) {
        for (Map.Entry<String, String> fileTypeEntry : FILE_TYPE_MAP.entrySet()) {
            if (StrUtil.startWithIgnoreCase(fileStreamHexHead, fileTypeEntry.getKey())) {
                return fileTypeEntry.getValue();
            }
        }
        return null;
    }

    public static String getType(InputStream in) throws IORuntimeException {
        return getType(IoUtil.readHex28Upper(in));
    }

    public static String getType(InputStream in, String filename) {
        String typeName = getType(in);
        if (typeName == null) {
            return FileUtil.extName(filename);
        }
        if ("xls".equals(typeName)) {
            String extName = FileUtil.extName(filename);
            if ("doc".equalsIgnoreCase(extName)) {
                return "doc";
            }
            if ("msi".equalsIgnoreCase(extName)) {
                return "msi";
            }
            return typeName;
        } else if (!URLUtil.URL_PROTOCOL_ZIP.equals(typeName)) {
            if (URLUtil.URL_PROTOCOL_JAR.equals(typeName)) {
                String extName2 = FileUtil.extName(filename);
                if ("xlsx".equalsIgnoreCase(extName2)) {
                    return "xlsx";
                }
                if ("docx".equalsIgnoreCase(extName2)) {
                    return "docx";
                }
                return typeName;
            }
            return typeName;
        } else {
            String extName3 = FileUtil.extName(filename);
            if ("docx".equalsIgnoreCase(extName3)) {
                return "docx";
            }
            if ("xlsx".equalsIgnoreCase(extName3)) {
                return "xlsx";
            }
            if ("pptx".equalsIgnoreCase(extName3)) {
                return "pptx";
            }
            if (URLUtil.URL_PROTOCOL_JAR.equalsIgnoreCase(extName3)) {
                return URLUtil.URL_PROTOCOL_JAR;
            }
            if ("war".equalsIgnoreCase(extName3)) {
                return "war";
            }
            if ("ofd".equalsIgnoreCase(extName3)) {
                return "ofd";
            }
            return typeName;
        }
    }

    public static String getType(File file) throws IORuntimeException {
        FileInputStream in = null;
        try {
            in = IoUtil.toStream(file);
            return getType(in, file.getName());
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public static String getTypeByPath(String path) throws IORuntimeException {
        return getType(FileUtil.file(path));
    }
}
