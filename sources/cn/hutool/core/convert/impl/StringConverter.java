package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.XmlUtil;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.TimeZone;
import org.w3c.dom.Node;
/* loaded from: classes.dex */
public class StringConverter extends AbstractConverter<String> {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public String convertInternal(Object value) {
        if (value instanceof TimeZone) {
            return ((TimeZone) value).getID();
        }
        if (value instanceof Node) {
            return XmlUtil.toStr((Node) value);
        }
        if (value instanceof Clob) {
            return clobToStr((Clob) value);
        }
        if (value instanceof Blob) {
            return blobToStr((Blob) value);
        }
        return convertToStr(value);
    }

    private static String clobToStr(Clob clob) {
        Reader reader = null;
        try {
            try {
                reader = clob.getCharacterStream();
                return IoUtil.read(reader);
            } catch (SQLException e) {
                throw new ConvertException(e);
            }
        } finally {
            IoUtil.close((Closeable) reader);
        }
    }

    private static String blobToStr(Blob blob) {
        InputStream in = null;
        try {
            try {
                in = blob.getBinaryStream();
                return IoUtil.read(in, CharsetUtil.CHARSET_UTF_8);
            } catch (SQLException e) {
                throw new ConvertException(e);
            }
        } finally {
            IoUtil.close((Closeable) in);
        }
    }
}
