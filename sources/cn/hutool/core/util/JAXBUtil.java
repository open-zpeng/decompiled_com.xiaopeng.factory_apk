package cn.hutool.core.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import java.io.Closeable;
import java.io.File;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
/* loaded from: classes.dex */
public class JAXBUtil {
    public static String beanToXml(Object bean) {
        return beanToXml(bean, CharsetUtil.CHARSET_UTF_8, true);
    }

    public static String beanToXml(Object bean, Charset charset, boolean format) {
        try {
            JAXBContext context = JAXBContext.newInstance(new Class[]{bean.getClass()});
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(format));
            marshaller.setProperty("jaxb.encoding", charset.name());
            StringWriter writer = new StringWriter();
            marshaller.marshal(bean, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new UtilException("convertToXml 错误：" + e.getMessage(), e);
        }
    }

    public static <T> T xmlToBean(String xml, Class<T> c) {
        return (T) xmlToBean(StrUtil.getReader(xml), c);
    }

    public static <T> T xmlToBean(File file, Charset charset, Class<T> c) {
        return (T) xmlToBean(FileUtil.getReader(file, charset), c);
    }

    public static <T> T xmlToBean(Reader reader, Class<T> c) {
        try {
            try {
                JAXBContext context = JAXBContext.newInstance(new Class[]{c});
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (T) unmarshaller.unmarshal(reader);
            } catch (Exception e) {
                throw new RuntimeException("convertToJava2 错误：" + e.getMessage(), e);
            }
        } finally {
            IoUtil.close((Closeable) reader);
        }
    }
}
