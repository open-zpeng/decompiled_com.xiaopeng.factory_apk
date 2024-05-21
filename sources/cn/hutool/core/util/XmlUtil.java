package cn.hutool.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.BiMap;
import cn.hutool.core.map.MapUtil;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
/* loaded from: classes.dex */
public class XmlUtil {
    public static final String AMP = "&amp;";
    public static final String APOS = "&apos;";
    public static final String COMMENT_REGEX = "(?s)<!--.+?-->";
    public static final String GT = "&gt;";
    public static final int INDENT_DEFAULT = 2;
    public static final String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";
    public static final String LT = "&lt;";
    public static final String NBSP = "&nbsp;";
    public static final String QUOTE = "&quot;";
    private static SAXParserFactory factory;
    private static String defaultDocumentBuilderFactory = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
    private static boolean namespaceAware = true;

    public static synchronized void disableDefaultDocumentBuilderFactory() {
        synchronized (XmlUtil.class) {
            defaultDocumentBuilderFactory = null;
        }
    }

    public static synchronized void setNamespaceAware(boolean isNamespaceAware) {
        synchronized (XmlUtil.class) {
            namespaceAware = isNamespaceAware;
        }
    }

    public static Document readXML(File file) {
        Assert.notNull(file, "Xml file is null !", new Object[0]);
        if (!file.exists()) {
            throw new UtilException("File [{}] not a exist!", file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new UtilException("[{}] not a file!", file.getAbsolutePath());
        }
        try {
            file = file.getCanonicalFile();
        } catch (IOException e) {
        }
        BufferedInputStream in = null;
        try {
            in = FileUtil.getInputStream(file);
            return readXML(in);
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public static Document readXML(String pathOrContent) {
        if (StrUtil.startWith((CharSequence) pathOrContent, '<')) {
            return parseXml(pathOrContent);
        }
        return readXML(FileUtil.file(pathOrContent));
    }

    public static Document readXML(InputStream inputStream) throws UtilException {
        return readXML(new InputSource(inputStream));
    }

    public static Document readXML(Reader reader) throws UtilException {
        return readXML(new InputSource(reader));
    }

    public static Document readXML(InputSource source) {
        DocumentBuilder builder = createDocumentBuilder();
        try {
            return builder.parse(source);
        } catch (Exception e) {
            throw new UtilException(e, "Parse XML from stream error!", new Object[0]);
        }
    }

    public static void readBySax(File file, ContentHandler contentHandler) {
        InputStream in = null;
        try {
            in = FileUtil.getInputStream(file);
            readBySax(new InputSource(in), contentHandler);
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public static void readBySax(Reader reader, ContentHandler contentHandler) {
        try {
            readBySax(new InputSource(reader), contentHandler);
        } finally {
            IoUtil.close((Closeable) reader);
        }
    }

    public static void readBySax(InputStream source, ContentHandler contentHandler) {
        try {
            readBySax(new InputSource(source), contentHandler);
        } finally {
            IoUtil.close((Closeable) source);
        }
    }

    public static void readBySax(InputSource source, ContentHandler contentHandler) {
        if (factory == null) {
            factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(namespaceAware);
        }
        try {
            SAXParser parse = factory.newSAXParser();
            if (contentHandler instanceof DefaultHandler) {
                parse.parse(source, (DefaultHandler) contentHandler);
                return;
            }
            XMLReader reader = parse.getXMLReader();
            reader.setContentHandler(contentHandler);
            reader.parse(source);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (ParserConfigurationException | SAXException e2) {
            throw new UtilException(e2);
        }
    }

    public static Document parseXml(String xmlStr) {
        if (StrUtil.isBlank(xmlStr)) {
            throw new IllegalArgumentException("XML content string is empty !");
        }
        return readXML(StrUtil.getReader(cleanInvalid(xmlStr)));
    }

    public static <T> T readObjectFromXml(File source) {
        return (T) readObjectFromXml(new InputSource(FileUtil.getInputStream(source)));
    }

    public static <T> T readObjectFromXml(String xmlStr) {
        return (T) readObjectFromXml(new InputSource(StrUtil.getReader(xmlStr)));
    }

    public static <T> T readObjectFromXml(InputSource source) {
        XMLDecoder xmldec = null;
        try {
            xmldec = new XMLDecoder(source);
            return (T) xmldec.readObject();
        } finally {
            IoUtil.close((AutoCloseable) xmldec);
        }
    }

    public static String toStr(Node doc) {
        return toStr(doc, false);
    }

    public static String toStr(Document doc) {
        return toStr((Node) doc);
    }

    public static String toStr(Node doc, boolean isPretty) {
        return toStr(doc, "UTF-8", isPretty);
    }

    public static String toStr(Document doc, boolean isPretty) {
        return toStr((Node) doc, isPretty);
    }

    public static String toStr(Node doc, String charset, boolean isPretty) {
        return toStr(doc, charset, isPretty, false);
    }

    public static String toStr(Document doc, String charset, boolean isPretty) {
        return toStr((Node) doc, charset, isPretty);
    }

    public static String toStr(Node doc, String charset, boolean isPretty, boolean omitXmlDeclaration) {
        StringWriter writer = StrUtil.getWriter();
        try {
            write(doc, writer, charset, isPretty ? 2 : 0, omitXmlDeclaration);
            return writer.toString();
        } catch (Exception e) {
            throw new UtilException(e, "Trans xml document to string error!", new Object[0]);
        }
    }

    public static String format(Document doc) {
        return toStr(doc, true);
    }

    public static String format(String xmlStr) {
        return format(parseXml(xmlStr));
    }

    public static void toFile(Document doc, String absolutePath) {
        toFile(doc, absolutePath, null);
    }

    public static void toFile(Document doc, String path, String charset) {
        if (StrUtil.isBlank(charset)) {
            charset = doc.getXmlEncoding();
        }
        if (StrUtil.isBlank(charset)) {
            charset = "UTF-8";
        }
        BufferedWriter writer = null;
        try {
            writer = FileUtil.getWriter(path, charset, false);
            write(doc, writer, charset, 2);
        } finally {
            IoUtil.close((Closeable) writer);
        }
    }

    public static void write(Node node, Writer writer, String charset, int indent) {
        transform(new DOMSource(node), new StreamResult(writer), charset, indent);
    }

    public static void write(Node node, Writer writer, String charset, int indent, boolean omitXmlDeclaration) {
        transform(new DOMSource(node), new StreamResult(writer), charset, indent, omitXmlDeclaration);
    }

    public static void write(Node node, OutputStream out, String charset, int indent) {
        transform(new DOMSource(node), new StreamResult(out), charset, indent);
    }

    public static void write(Node node, OutputStream out, String charset, int indent, boolean omitXmlDeclaration) {
        transform(new DOMSource(node), new StreamResult(out), charset, indent, omitXmlDeclaration);
    }

    public static void transform(Source source, Result result, String charset, int indent) {
        transform(source, result, charset, indent, false);
    }

    public static void transform(Source source, Result result, String charset, int indent, boolean omitXmlDeclaration) {
        TransformerFactory factory2 = TransformerFactory.newInstance();
        try {
            Transformer xformer = factory2.newTransformer();
            if (indent > 0) {
                xformer.setOutputProperty("indent", BooleanUtils.YES);
                xformer.setOutputProperty("doctype-public", BooleanUtils.YES);
                xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
            }
            if (StrUtil.isNotBlank(charset)) {
                xformer.setOutputProperty("encoding", charset);
            }
            if (omitXmlDeclaration) {
                xformer.setOutputProperty("omit-xml-declaration", BooleanUtils.YES);
            }
            xformer.transform(source, result);
        } catch (Exception e) {
            throw new UtilException(e, "Trans xml document to string error!", new Object[0]);
        }
    }

    public static Document createXml() {
        return createDocumentBuilder().newDocument();
    }

    public static DocumentBuilder createDocumentBuilder() {
        try {
            DocumentBuilder builder = createDocumentBuilderFactory().newDocumentBuilder();
            return builder;
        } catch (Exception e) {
            throw new UtilException(e, "Create xml document error!", new Object[0]);
        }
    }

    public static DocumentBuilderFactory createDocumentBuilderFactory() {
        DocumentBuilderFactory factory2;
        if (StrUtil.isNotEmpty(defaultDocumentBuilderFactory)) {
            factory2 = DocumentBuilderFactory.newInstance(defaultDocumentBuilderFactory, null);
        } else {
            factory2 = DocumentBuilderFactory.newInstance();
        }
        factory2.setNamespaceAware(namespaceAware);
        return disableXXE(factory2);
    }

    public static Document createXml(String rootElementName) {
        return createXml(rootElementName, null);
    }

    public static Document createXml(String rootElementName, String namespace) {
        Document doc = createXml();
        doc.appendChild(namespace == null ? doc.createElement(rootElementName) : doc.createElementNS(namespace, rootElementName));
        return doc;
    }

    public static Element getRootElement(Document doc) {
        if (doc == null) {
            return null;
        }
        return doc.getDocumentElement();
    }

    public static Document getOwnerDocument(Node node) {
        return node instanceof Document ? (Document) node : node.getOwnerDocument();
    }

    public static String cleanInvalid(String xmlContent) {
        if (xmlContent == null) {
            return null;
        }
        return xmlContent.replaceAll(INVALID_REGEX, "");
    }

    public static String cleanComment(String xmlContent) {
        if (xmlContent == null) {
            return null;
        }
        return xmlContent.replaceAll(COMMENT_REGEX, "");
    }

    public static List<Element> getElements(Element element, String tagName) {
        NodeList nodeList = StrUtil.isBlank(tagName) ? element.getChildNodes() : element.getElementsByTagName(tagName);
        return transElements(element, nodeList);
    }

    public static Element getElement(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList == null || nodeList.getLength() < 1) {
            return null;
        }
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Element childEle = (Element) nodeList.item(i);
            if (childEle == null || childEle.getParentNode() == element) {
                return childEle;
            }
        }
        return null;
    }

    public static String elementText(Element element, String tagName) {
        Element child = getElement(element, tagName);
        if (child == null) {
            return null;
        }
        return child.getTextContent();
    }

    public static String elementText(Element element, String tagName, String defaultValue) {
        Element child = getElement(element, tagName);
        return child == null ? defaultValue : child.getTextContent();
    }

    public static List<Element> transElements(NodeList nodeList) {
        return transElements(null, nodeList);
    }

    public static List<Element> transElements(Element parentEle, NodeList nodeList) {
        int length = nodeList.getLength();
        ArrayList<Element> elements = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Node node = nodeList.item(i);
            if (1 == node.getNodeType()) {
                Element element = (Element) nodeList.item(i);
                if (parentEle == null || element.getParentNode() == parentEle) {
                    elements.add(element);
                }
            }
        }
        return elements;
    }

    public static void writeObjectAsXml(File dest, Object bean) {
        XMLEncoder xmlenc = null;
        try {
            xmlenc = new XMLEncoder(FileUtil.getOutputStream(dest));
            xmlenc.writeObject(bean);
        } finally {
            IoUtil.close((AutoCloseable) xmlenc);
        }
    }

    public static XPath createXPath() {
        return XPathFactory.newInstance().newXPath();
    }

    public static Element getElementByXPath(String expression, Object source) {
        return (Element) getNodeByXPath(expression, source);
    }

    public static NodeList getNodeListByXPath(String expression, Object source) {
        return (NodeList) getByXPath(expression, source, XPathConstants.NODESET);
    }

    public static Node getNodeByXPath(String expression, Object source) {
        return (Node) getByXPath(expression, source, XPathConstants.NODE);
    }

    public static Object getByXPath(String expression, Object source, QName returnType) {
        NamespaceContext nsContext = null;
        if (source instanceof Node) {
            nsContext = new UniversalNamespaceCache((Node) source, false);
        }
        return getByXPath(expression, source, returnType, nsContext);
    }

    public static Object getByXPath(String expression, Object source, QName returnType, NamespaceContext nsContext) {
        XPath xPath = createXPath();
        if (nsContext != null) {
            xPath.setNamespaceContext(nsContext);
        }
        try {
            if (source instanceof InputSource) {
                return xPath.evaluate(expression, (InputSource) source, returnType);
            }
            return xPath.evaluate(expression, source, returnType);
        } catch (XPathExpressionException e) {
            throw new UtilException(e);
        }
    }

    public static String escape(String string) {
        return EscapeUtil.escapeHtml4(string);
    }

    public static String unescape(String string) {
        return EscapeUtil.unescapeHtml4(string);
    }

    public static Map<String, Object> xmlToMap(String xmlStr) {
        return xmlToMap(xmlStr, new HashMap());
    }

    public static <T> T xmlToBean(Node node, Class<T> bean) {
        Map<String, Object> map = xmlToMap(node);
        if (map != null && map.size() == 1) {
            String simpleName = bean.getSimpleName();
            if (map.containsKey(simpleName)) {
                return (T) BeanUtil.toBean(map.get(simpleName), bean);
            }
        }
        return (T) BeanUtil.toBean(map, bean);
    }

    public static Map<String, Object> xmlToMap(Node node) {
        return xmlToMap(node, new HashMap());
    }

    public static Map<String, Object> xmlToMap(String xmlStr, Map<String, Object> result) {
        Document doc = parseXml(xmlStr);
        Element root = getRootElement(doc);
        root.normalize();
        return xmlToMap(root, result);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v6, types: [java.util.Map] */
    public static Map<String, Object> xmlToMap(Node node, Map<String, Object> result) {
        String textContent;
        if (result == null) {
            result = new HashMap();
        }
        NodeList nodeList = node.getChildNodes();
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Node childNode = nodeList.item(i);
            if (isElement(childNode)) {
                Element childEle = (Element) childNode;
                Object value = result.get(childEle.getNodeName());
                if (childEle.hasChildNodes()) {
                    ?? xmlToMap = xmlToMap(childEle);
                    if (MapUtil.isNotEmpty(xmlToMap)) {
                        textContent = xmlToMap;
                    } else {
                        textContent = childEle.getTextContent();
                    }
                } else {
                    textContent = childEle.getTextContent();
                }
                if (textContent != null) {
                    if (value != null) {
                        if (value instanceof List) {
                            ((List) value).add(textContent);
                        } else {
                            result.put(childEle.getNodeName(), CollUtil.newArrayList(value, textContent));
                        }
                    } else {
                        result.put(childEle.getNodeName(), textContent);
                    }
                }
            }
        }
        return result;
    }

    public static String mapToXmlStr(Map<?, ?> data) {
        return toStr(mapToXml(data, "xml"));
    }

    public static String mapToXmlStr(Map<?, ?> data, boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, "xml"), "UTF-8", false, omitXmlDeclaration);
    }

    public static String mapToXmlStr(Map<?, ?> data, String rootName) {
        return toStr(mapToXml(data, rootName));
    }

    public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace) {
        return toStr(mapToXml(data, rootName, namespace));
    }

    public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, rootName, namespace), "UTF-8", false, omitXmlDeclaration);
    }

    public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, boolean isPretty, boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, rootName, namespace), "UTF-8", isPretty, omitXmlDeclaration);
    }

    public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, String charset, boolean isPretty, boolean omitXmlDeclaration) {
        return toStr(mapToXml(data, rootName, namespace), charset, isPretty, omitXmlDeclaration);
    }

    public static Document mapToXml(Map<?, ?> data, String rootName) {
        return mapToXml(data, rootName, null);
    }

    public static Document mapToXml(Map<?, ?> data, String rootName, String namespace) {
        Document doc = createXml();
        Element root = appendChild(doc, rootName, namespace);
        appendMap(doc, root, data);
        return doc;
    }

    public static Document beanToXml(Object bean) {
        return beanToXml(bean, null);
    }

    public static Document beanToXml(Object bean, String namespace) {
        return beanToXml(bean, namespace, false);
    }

    public static Document beanToXml(Object bean, String namespace, boolean ignoreNull) {
        if (bean == null) {
            return null;
        }
        return mapToXml(BeanUtil.beanToMap(bean, false, ignoreNull), bean.getClass().getSimpleName(), namespace);
    }

    public static boolean isElement(Node node) {
        return node != null && 1 == node.getNodeType();
    }

    public static Element appendChild(Node node, String tagName) {
        return appendChild(node, tagName, null);
    }

    public static Element appendChild(Node node, String tagName, String namespace) {
        Document doc = getOwnerDocument(node);
        Element child = namespace == null ? doc.createElement(tagName) : doc.createElementNS(namespace, tagName);
        node.appendChild(child);
        return child;
    }

    public static Node appendText(Node node, CharSequence text) {
        return appendText(getOwnerDocument(node), node, text);
    }

    public static void append(Node node, Object data) {
        append(getOwnerDocument(node), node, data);
    }

    private static void append(Document doc, Node node, Object data) {
        if (data instanceof Map) {
            appendMap(doc, node, (Map) data);
        } else if (data instanceof Iterator) {
            appendIterator(doc, node, (Iterator) data);
        } else if (data instanceof Iterable) {
            appendIterator(doc, node, ((Iterable) data).iterator());
        } else {
            appendText(doc, node, data.toString());
        }
    }

    private static void appendMap(final Document doc, final Node node, Map data) {
        data.forEach(new BiConsumer() { // from class: cn.hutool.core.util.-$$Lambda$XmlUtil$jJyHmL82FyPUX99N7QTadEZmpms
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                XmlUtil.lambda$appendMap$0(node, doc, obj, obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$appendMap$0(Node node, Document doc, Object key, Object value) {
        if (key != null) {
            Element child = appendChild(node, key.toString());
            if (value != null) {
                append(doc, child, value);
            }
        }
    }

    private static void appendIterator(Document doc, Node node, Iterator data) {
        Node parentNode = node.getParentNode();
        boolean isFirst = true;
        while (data.hasNext()) {
            Object eleData = data.next();
            if (isFirst) {
                append(doc, node, eleData);
                isFirst = false;
            } else {
                Node cloneNode = node.cloneNode(false);
                parentNode.appendChild(cloneNode);
                append(doc, cloneNode, eleData);
            }
        }
    }

    private static Node appendText(Document doc, Node node, CharSequence text) {
        return node.appendChild(doc.createTextNode(StrUtil.str(text)));
    }

    private static DocumentBuilderFactory disableXXE(DocumentBuilderFactory dbf) {
        try {
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);
        } catch (ParserConfigurationException e) {
        }
        return dbf;
    }

    /* loaded from: classes.dex */
    public static class UniversalNamespaceCache implements NamespaceContext {
        private static final String DEFAULT_NS = "DEFAULT";
        private final BiMap<String, String> prefixUri = new BiMap<>(new HashMap());

        public UniversalNamespaceCache(Node node, boolean toplevelOnly) {
            examineNode(node.getFirstChild(), toplevelOnly);
        }

        private void examineNode(Node node, boolean attributesOnly) {
            NodeList childNodes;
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attribute = attributes.item(i);
                    storeAttribute(attribute);
                }
            }
            if (!attributesOnly && (childNodes = node.getChildNodes()) != null) {
                for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                    Node item = childNodes.item(i2);
                    if (item.getNodeType() == 1) {
                        examineNode(item, false);
                    }
                }
            }
        }

        private void storeAttribute(Node attribute) {
            if (attribute != null && "http://www.w3.org/2000/xmlns/".equals(attribute.getNamespaceURI())) {
                if ("xmlns".equals(attribute.getNodeName())) {
                    this.prefixUri.put(DEFAULT_NS, attribute.getNodeValue());
                } else {
                    this.prefixUri.put(attribute.getLocalName(), attribute.getNodeValue());
                }
            }
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getNamespaceURI(String prefix) {
            if (prefix == null || "".equals(prefix)) {
                return this.prefixUri.get(DEFAULT_NS);
            }
            return this.prefixUri.get(prefix);
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String namespaceURI) {
            return this.prefixUri.getInverse().get(namespaceURI);
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator<String> getPrefixes(String namespaceURI) {
            return null;
        }
    }
}
