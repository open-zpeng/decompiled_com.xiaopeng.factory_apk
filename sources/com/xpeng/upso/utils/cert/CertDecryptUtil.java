package com.xpeng.upso.utils.cert;

import androidx.annotation.Keep;
import androidx.core.app.NotificationCompat;
import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.lib.apirouter.ClientConstants;
import com.xiaopeng.xmlconfig.Support;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
@Keep
/* loaded from: classes2.dex */
public class CertDecryptUtil {
    public static final String APP_SECRET_WEB = "E938C653DCAD7C1A43E9F9";
    private static final String APP_WEB = "web.xmart.com";
    private static final String IV_STRING = "5s9F1pGR@VJZ8F3I";
    private static final String PASSWORD = "chengzi";
    private static final String SALT = "oBPMlkqO4eBRBa@N*zIGdRokeikjKbIg";
    private static final String SUCCESS = "200";

    public static List<CertResp> parseE38Response(String response, String sign) throws Exception {
        if (StringUtils.isEmpty(response)) {
            throw new Exception("response should not be empty");
        }
        JSONObject respObj = JSONObject.parseObject(response);
        String code = respObj.getString("code");
        if (!SUCCESS.equalsIgnoreCase(code)) {
            throw new Exception("fail:" + respObj.toJSONString());
        }
        String data = respObj.getString("data");
        if (StringUtils.isEmpty(data)) {
            throw new Exception("response data is empty");
        }
        String key = initKey(sign);
        String certJson = decryptAes(data, key);
        return JSONArray.parseArray(certJson, CertResp.class);
    }

    public static List<CertResp> parse4GResponse(String response, String sign) throws Exception {
        if (StringUtils.isEmpty(response)) {
            throw new Exception("response should not be empty");
        }
        JSONObject respObj = JSONObject.parseObject(response);
        String code = respObj.getString("code");
        if (!SUCCESS.equalsIgnoreCase(code)) {
            throw new Exception("fail:" + respObj.getString(NotificationCompat.CATEGORY_MESSAGE));
        }
        String data = respObj.getString("data");
        if (StringUtils.isEmpty(data)) {
            throw new Exception("response data is empty");
        }
        String key = initKey(sign);
        String certJson = decryptAes(data, key);
        List<CertInfo> certInfos = JSONArray.parseArray(certJson, CertInfo.class);
        return toCertResps(certInfos);
    }

    private static List<CertResp> toCertResps(List<CertInfo> certInfos) {
        if (certInfos == null || certInfos.size() == 0) {
            return null;
        }
        List<CertResp> certResps = new ArrayList<>();
        for (CertInfo certInfo : certInfos) {
            StringReader certReader = new StringReader(certInfo.getCert());
            StringReader keyReader = new StringReader(certInfo.getKey());
            String pkcs12 = pemToPKCS12(keyReader, certReader, PASSWORD);
            CertResp certResp = new CertResp();
            certResp.setPkcs12(pkcs12);
            certResps.add(certResp);
        }
        return certResps;
    }

    private static String pemToPKCS12(final Reader keyReader, final Reader certReader, final String password) {
        PrivateKey key;
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            PEMParser keyPem = new PEMParser(keyReader);
            PEMKeyPair pemKeyPair = (PEMKeyPair) keyPem.readObject();
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
            key = keyPair.getPrivate();
            keyPem.close();
            keyReader.close();
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return null;
        }
        try {
            PEMParser certPem = new PEMParser(certReader);
            X509CertificateHolder certHolder = (X509CertificateHolder) certPem.readObject();
            Certificate X509Certificate = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
            certPem.close();
            certReader.close();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(null);
            ks.setKeyEntry(ClientConstants.ALIAS.P_ALIAS, key, password.toCharArray(), new Certificate[]{X509Certificate});
            ks.store(bos, password.toCharArray());
            bos.close();
            return Base64.encode(bos.toByteArray());
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return null;
        }
    }

    public static String hexMd5(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(value.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static String byteToHexString(byte[] bytes) {
        return String.valueOf(Hex.encodeHex(bytes));
    }

    private static String initKey(String sign) {
        return hexMd5(sign + "oBPMlkqO4eBRBa@N*zIGdRokeikjKbIg");
    }

    public static String decryptAes(String content, String key) throws Exception {
        byte[] byteContent = TeeUtil.decodeBASE64(content);
        byte[] enCodeFormat = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        byte[] initParam = IV_STRING.getBytes();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(byteContent);
        return new String(decryptedBytes);
    }

    public static String sign(Map<String, String> requestBodyMap) {
        String parameters = sortParameterAndValues(requestBodyMap);
        String timestamp = requestBodyMap.get("timestamp");
        MD5 create = MD5.create();
        return StringUtils.lowerCase(create.digestHex("web.xmart.com" + timestamp + parameters + APP_SECRET_WEB)).toLowerCase();
    }

    private static String sortParameterAndValues(Map<String, String> parameterMap) {
        if (parameterMap == null) {
            return "";
        }
        Set<String> keySet = parameterMap.keySet();
        List<String> keyList = new ArrayList<>();
        for (String key : keySet) {
            keyList.add(key);
        }
        Collections.sort(keyList);
        StringBuffer sb = new StringBuffer();
        for (String key2 : keyList) {
            if (!"appId".equals(key2) && !"app_id".equals(key2) && !"timestamp".equals(key2) && !"sign".equals(key2)) {
                sb.append(key2);
                String val = parameterMap.get(key2);
                sb.append(val);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        List<CertResp> certResps = parse4GResponse("{\n    \"code\": 200,\n    \"data\": \"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\"\n}", "1a1e598465717efb0b98764315e7f6c5");
        for (CertResp certResp : certResps) {
            String pkcs12 = certResp.getPkcs12();
            byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(pkcs12);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new ByteArrayInputStream(bytes), PASSWORD.toCharArray());
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String element = aliases.nextElement();
                X509Certificate certificate = (X509Certificate) keyStore.getCertificate(element);
                PrintStream printStream = System.out;
                printStream.println("版本号 " + certificate.getVersion());
                PrintStream printStream2 = System.out;
                printStream2.println("序列号 " + certificate.getSerialNumber().toString(16));
                PrintStream printStream3 = System.out;
                printStream3.println("全名 " + certificate.getSubjectDN());
                PrintStream printStream4 = System.out;
                printStream4.println("签发者全名" + certificate.getIssuerDN());
                PrintStream printStream5 = System.out;
                printStream5.println("有效期起始日 " + certificate.getNotBefore());
                PrintStream printStream6 = System.out;
                printStream6.println("有效期截至日 " + certificate.getNotAfter());
                PrintStream printStream7 = System.out;
                printStream7.println("签名算法 " + certificate.getSigAlgName());
                System.out.println("======================================");
                String uuid = UUID.randomUUID().toString();
                FileOutputStream fileOutputStream = new FileOutputStream("D:\\test\\certs\\" + uuid + ".p12");
                keyStore.store(fileOutputStream, PASSWORD.toCharArray());
            }
        }
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put(Constant.HARDWAREID, "2333333444444444444444");
        requestMap.put("timestamp", "20");
        requestMap.put(Constant.ACTION, Support.Properties.XPU);
        String sign1 = sign(requestMap);
        System.out.println(requestMap.get("timestamp"));
        System.out.println(sign1);
        requestMap.put("sign", sign1);
        System.out.println(JSONObject.toJSONString(requestMap));
    }
}
