package com.xpeng.upso.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/* loaded from: classes2.dex */
public class Utils {
    public static void printByteArr(byte[] arr) {
        System.out.print("[");
        int i = 0;
        while (i < arr.length) {
            System.out.printf(i == 0 ? "%d" : ",%d", Integer.valueOf(arr[i] & 255));
            i++;
        }
        System.out.println("]");
    }

    public static byte[] toBytes(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object toObject(byte[] data) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
