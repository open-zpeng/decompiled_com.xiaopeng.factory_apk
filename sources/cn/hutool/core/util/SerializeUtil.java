package cn.hutool.core.util;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
/* loaded from: classes.dex */
public class SerializeUtil {
    public static <T> T clone(T obj) {
        if (!(obj instanceof Serializable)) {
            return null;
        }
        return (T) deserialize(serialize(obj));
    }

    public static <T> byte[] serialize(T obj) {
        if (!(obj instanceof Serializable)) {
            return null;
        }
        FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        IoUtil.writeObjects(byteOut, false, (Serializable) obj);
        return byteOut.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes) {
        return (T) IoUtil.readObj(new ByteArrayInputStream(bytes));
    }
}
