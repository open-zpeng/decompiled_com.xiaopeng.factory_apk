package com.xpeng.upso.utils;

import java.io.File;
/* loaded from: classes2.dex */
public class FileUtils {
    public static boolean isFileExists(File file) {
        if (file.exists()) {
            System.out.println("file exists");
            return true;
        }
        System.out.println("file not exists, create it ...");
        return false;
    }
}
