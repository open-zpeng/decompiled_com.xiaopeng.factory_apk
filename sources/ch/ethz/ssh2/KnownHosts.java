package ch.ethz.ssh2;

import ch.ethz.ssh2.crypto.Base64;
import ch.ethz.ssh2.crypto.digest.Digest;
import ch.ethz.ssh2.crypto.digest.HMAC;
import ch.ethz.ssh2.crypto.digest.MD5;
import ch.ethz.ssh2.crypto.digest.SHA1;
import ch.ethz.ssh2.signature.DSAPublicKey;
import ch.ethz.ssh2.signature.DSASHA1Verify;
import ch.ethz.ssh2.signature.RSAPublicKey;
import ch.ethz.ssh2.signature.RSASHA1Verify;
import cn.hutool.core.text.CharPool;
import com.xiaopeng.commonfunc.Constant;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
/* loaded from: classes.dex */
public class KnownHosts {
    public static final int HOSTKEY_HAS_CHANGED = 2;
    public static final int HOSTKEY_IS_NEW = 1;
    public static final int HOSTKEY_IS_OK = 0;
    private LinkedList publicKeys = new LinkedList();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class KnownHostsEntry {
        Object key;
        String[] patterns;

        KnownHostsEntry(String[] patterns, Object key) {
            this.patterns = patterns;
            this.key = key;
        }
    }

    public KnownHosts() {
    }

    public KnownHosts(char[] knownHostsData) throws IOException {
        initialize(knownHostsData);
    }

    public KnownHosts(File knownHosts) throws IOException {
        initialize(knownHosts);
    }

    public void addHostkey(String[] hostnames, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
        if (hostnames == null) {
            throw new IllegalArgumentException("hostnames may not be null");
        }
        if ("ssh-rsa".equals(serverHostKeyAlgorithm)) {
            RSAPublicKey rpk = RSASHA1Verify.decodeSSHRSAPublicKey(serverHostKey);
            synchronized (this.publicKeys) {
                this.publicKeys.add(new KnownHostsEntry(hostnames, rpk));
            }
        } else if ("ssh-dss".equals(serverHostKeyAlgorithm)) {
            DSAPublicKey dpk = DSASHA1Verify.decodeSSHDSAPublicKey(serverHostKey);
            synchronized (this.publicKeys) {
                this.publicKeys.add(new KnownHostsEntry(hostnames, dpk));
            }
        } else {
            StringBuffer stringBuffer = new StringBuffer("Unknwon host key type (");
            stringBuffer.append(serverHostKeyAlgorithm);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
    }

    public void addHostkeys(char[] knownHostsData) throws IOException {
        initialize(knownHostsData);
    }

    public void addHostkeys(File knownHosts) throws IOException {
        initialize(knownHosts);
    }

    public static final String createHashedHostname(String hostname) {
        SHA1 sha1 = new SHA1();
        byte[] salt = new byte[sha1.getDigestLength()];
        new SecureRandom().nextBytes(salt);
        byte[] hash = hmacSha1Hash(salt, hostname);
        String base64_salt = new String(Base64.encode(salt));
        String base64_hash = new String(Base64.encode(hash));
        StringBuffer stringBuffer = new StringBuffer("|1|");
        stringBuffer.append(base64_salt);
        stringBuffer.append(Constant.VERTIAL_BAR_STRING);
        stringBuffer.append(base64_hash);
        return new String(stringBuffer.toString());
    }

    private static final byte[] hmacSha1Hash(byte[] salt, String hostname) {
        SHA1 sha1 = new SHA1();
        if (salt.length != sha1.getDigestLength()) {
            StringBuffer stringBuffer = new StringBuffer("Salt has wrong length (");
            stringBuffer.append(salt.length);
            stringBuffer.append(")");
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        HMAC hmac = new HMAC(sha1, salt, salt.length);
        hmac.update(hostname.getBytes());
        byte[] dig = new byte[hmac.getDigestLength()];
        hmac.digest(dig);
        return dig;
    }

    private final boolean checkHashed(String entry, String hostname) {
        int delim_idx;
        if (entry.startsWith("|1|") && (delim_idx = entry.indexOf(124, 3)) != -1) {
            String salt_base64 = entry.substring(3, delim_idx);
            String hash_base64 = entry.substring(delim_idx + 1);
            try {
                byte[] salt = Base64.decode(salt_base64.toCharArray());
                byte[] hash = Base64.decode(hash_base64.toCharArray());
                SHA1 sha1 = new SHA1();
                if (salt.length != sha1.getDigestLength()) {
                    return false;
                }
                byte[] dig = hmacSha1Hash(salt, hostname);
                for (int i = 0; i < dig.length; i++) {
                    if (dig[i] != hash[i]) {
                        return false;
                    }
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    private int checkKey(String remoteHostname, Object remoteKey) {
        int result = 1;
        synchronized (this.publicKeys) {
            try {
                try {
                    Iterator i = this.publicKeys.iterator();
                    while (i.hasNext()) {
                        KnownHostsEntry ke = (KnownHostsEntry) i.next();
                        if (hostnameMatches(ke.patterns, remoteHostname)) {
                            boolean res = matchKeys(ke.key, remoteKey);
                            if (res) {
                                return 0;
                            }
                            result = 2;
                        }
                    }
                    return result;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    private Vector getAllKeys(String hostname) {
        Vector keys = new Vector();
        synchronized (this.publicKeys) {
            try {
                try {
                    Iterator i = this.publicKeys.iterator();
                    while (i.hasNext()) {
                        KnownHostsEntry ke = (KnownHostsEntry) i.next();
                        if (hostnameMatches(ke.patterns, hostname)) {
                            keys.addElement(ke.key);
                        }
                    }
                    return keys;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public String[] getPreferredServerHostkeyAlgorithmOrder(String hostname) {
        String[] algos = recommendHostkeyAlgorithms(hostname);
        if (algos != null) {
            return algos;
        }
        try {
            InetAddress[] ipAdresses = InetAddress.getAllByName(hostname);
            for (InetAddress inetAddress : ipAdresses) {
                String[] algos2 = recommendHostkeyAlgorithms(inetAddress.getHostAddress());
                if (algos2 != null) {
                    return algos2;
                }
            }
            return null;
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private final boolean hostnameMatches(String[] hostpatterns, String hostname) {
        String pattern;
        boolean negate;
        boolean isMatch = false;
        String hostname2 = hostname.toLowerCase();
        for (int k = 0; k < hostpatterns.length; k++) {
            if (hostpatterns[k] != null) {
                if (hostpatterns[k].length() > 0 && hostpatterns[k].charAt(0) == '!') {
                    pattern = hostpatterns[k].substring(1);
                    negate = true;
                } else {
                    pattern = hostpatterns[k];
                    negate = false;
                }
                if (!isMatch || negate) {
                    if (pattern.charAt(0) == '|') {
                        if (!checkHashed(pattern, hostname2)) {
                            continue;
                        } else if (negate) {
                            return false;
                        } else {
                            isMatch = true;
                        }
                    } else {
                        String pattern2 = pattern.toLowerCase();
                        if (pattern2.indexOf(63) != -1 || pattern2.indexOf(42) != -1) {
                            if (!pseudoRegex(pattern2.toCharArray(), 0, hostname2.toCharArray(), 0)) {
                                continue;
                            } else if (negate) {
                                return false;
                            } else {
                                isMatch = true;
                            }
                        } else if (pattern2.compareTo(hostname2) != 0) {
                            continue;
                        } else if (negate) {
                            return false;
                        } else {
                            isMatch = true;
                        }
                    }
                }
            }
        }
        return isMatch;
    }

    private void initialize(char[] knownHostsData) throws IOException {
        BufferedReader br = new BufferedReader(new CharArrayReader(knownHostsData));
        while (true) {
            String line = br.readLine();
            if (line != null) {
                String line2 = line.trim();
                if (!line2.startsWith("#")) {
                    String[] arr = line2.split(" ");
                    if (arr.length >= 3 && (arr[1].compareTo("ssh-rsa") == 0 || arr[1].compareTo("ssh-dss") == 0)) {
                        String[] hostnames = arr[0].split(",");
                        byte[] msg = Base64.decode(arr[2].toCharArray());
                        addHostkey(hostnames, arr[1], msg);
                    }
                }
            } else {
                return;
            }
        }
    }

    private void initialize(File knownHosts) throws IOException {
        char[] buff = new char[512];
        CharArrayWriter cw = new CharArrayWriter();
        knownHosts.createNewFile();
        FileReader fr = new FileReader(knownHosts);
        while (true) {
            int len = fr.read(buff);
            if (len >= 0) {
                cw.write(buff, 0, len);
            } else {
                fr.close();
                initialize(cw.toCharArray());
                return;
            }
        }
    }

    private final boolean matchKeys(Object key1, Object key2) {
        if ((key1 instanceof RSAPublicKey) && (key2 instanceof RSAPublicKey)) {
            RSAPublicKey savedRSAKey = (RSAPublicKey) key1;
            RSAPublicKey remoteRSAKey = (RSAPublicKey) key2;
            return savedRSAKey.getE().equals(remoteRSAKey.getE()) && savedRSAKey.getN().equals(remoteRSAKey.getN());
        } else if ((key1 instanceof DSAPublicKey) && (key2 instanceof DSAPublicKey)) {
            DSAPublicKey savedDSAKey = (DSAPublicKey) key1;
            DSAPublicKey remoteDSAKey = (DSAPublicKey) key2;
            return savedDSAKey.getG().equals(remoteDSAKey.getG()) && savedDSAKey.getP().equals(remoteDSAKey.getP()) && savedDSAKey.getQ().equals(remoteDSAKey.getQ()) && savedDSAKey.getY().equals(remoteDSAKey.getY());
        } else {
            return false;
        }
    }

    private final boolean pseudoRegex(char[] pattern, int i, char[] match, int j) {
        while (pattern.length != i) {
            if (pattern[i] == '*') {
                int i2 = i + 1;
                int i3 = pattern.length;
                if (i3 == i2) {
                    return true;
                }
                if (pattern[i2] != '*' && pattern[i2] != '?') {
                    do {
                        if (pattern[i2] == match[j] && pseudoRegex(pattern, i2 + 1, match, j + 1)) {
                            return true;
                        }
                        j++;
                    } while (match.length != j);
                    return false;
                }
                while (!pseudoRegex(pattern, i2, match, j)) {
                    j++;
                    if (match.length == j) {
                        return false;
                    }
                }
                return true;
            }
            int i4 = match.length;
            if (i4 == j) {
                return false;
            }
            if (pattern[i] != '?' && pattern[i] != match[j]) {
                return false;
            }
            i++;
            j++;
        }
        return match.length == j;
    }

    private String[] recommendHostkeyAlgorithms(String hostname) {
        String thisAlgo;
        String preferredAlgo = null;
        Vector keys = getAllKeys(hostname);
        for (int i = 0; i < keys.size(); i++) {
            if (keys.elementAt(i) instanceof RSAPublicKey) {
                thisAlgo = "ssh-rsa";
            } else if (!(keys.elementAt(i) instanceof DSAPublicKey)) {
                continue;
            } else {
                thisAlgo = "ssh-dss";
            }
            if (0 != 0 && preferredAlgo.compareTo(thisAlgo) != 0) {
                return null;
            }
        }
        if (0 == 0) {
            return null;
        }
        if (preferredAlgo.equals("ssh-rsa")) {
            return new String[]{"ssh-rsa", "ssh-dss"};
        }
        return new String[]{"ssh-dss", "ssh-rsa"};
    }

    public int verifyHostkey(String hostname, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
        Object remoteKey;
        if ("ssh-rsa".equals(serverHostKeyAlgorithm)) {
            remoteKey = RSASHA1Verify.decodeSSHRSAPublicKey(serverHostKey);
        } else if ("ssh-dss".equals(serverHostKeyAlgorithm)) {
            remoteKey = DSASHA1Verify.decodeSSHDSAPublicKey(serverHostKey);
        } else {
            StringBuffer stringBuffer = new StringBuffer("Unknown hostkey type ");
            stringBuffer.append(serverHostKeyAlgorithm);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        int result = checkKey(hostname, remoteKey);
        if (result == 0) {
            return result;
        }
        try {
            InetAddress[] ipAdresses = InetAddress.getAllByName(hostname);
            for (InetAddress inetAddress : ipAdresses) {
                int newresult = checkKey(inetAddress.getHostAddress(), remoteKey);
                if (newresult == 0) {
                    return newresult;
                }
                if (newresult == 2) {
                    result = 2;
                }
            }
            return result;
        } catch (UnknownHostException e) {
            return result;
        }
    }

    public static final void addHostkeyToFile(File knownHosts, String[] hostnames, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
        if (hostnames == null || hostnames.length == 0) {
            throw new IllegalArgumentException("Need at least one hostname specification");
        }
        if (serverHostKeyAlgorithm == null || serverHostKey == null) {
            throw new IllegalArgumentException();
        }
        CharArrayWriter writer = new CharArrayWriter();
        for (int i = 0; i < hostnames.length; i++) {
            if (i != 0) {
                writer.write(44);
            }
            writer.write(hostnames[i]);
        }
        writer.write(32);
        writer.write(serverHostKeyAlgorithm);
        writer.write(32);
        writer.write(Base64.encode(serverHostKey));
        writer.write("\n");
        char[] entry = writer.toCharArray();
        RandomAccessFile raf = new RandomAccessFile(knownHosts, "rw");
        long len = raf.length();
        if (len > 0) {
            raf.seek(len - 1);
            int last = raf.read();
            if (last != 10) {
                raf.write(10);
            }
        }
        raf.write(new String(entry).getBytes());
        raf.close();
    }

    private static final byte[] rawFingerPrint(String type, String keyType, byte[] hostkey) {
        Digest dig;
        if ("md5".equals(type)) {
            dig = new MD5();
        } else if ("sha1".equals(type)) {
            dig = new SHA1();
        } else {
            StringBuffer stringBuffer = new StringBuffer("Unknown hash type ");
            stringBuffer.append(type);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        if (!"ssh-rsa".equals(keyType) && !"ssh-dss".equals(keyType)) {
            StringBuffer stringBuffer2 = new StringBuffer("Unknown key type ");
            stringBuffer2.append(keyType);
            throw new IllegalArgumentException(stringBuffer2.toString());
        } else if (hostkey == null) {
            throw new IllegalArgumentException("hostkey is null");
        } else {
            dig.update(hostkey);
            byte[] res = new byte[dig.getDigestLength()];
            dig.digest(res);
            return res;
        }
    }

    private static final String rawToHexFingerprint(byte[] fingerprint) {
        char[] alpha = "0123456789abcdef".toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fingerprint.length; i++) {
            if (i != 0) {
                sb.append(':');
            }
            int b = fingerprint[i] & 255;
            sb.append(alpha[b >> 4]);
            sb.append(alpha[b & 15]);
        }
        return sb.toString();
    }

    private static final String rawToBubblebabbleFingerprint(byte[] raw) {
        char[] v = "aeiouy".toCharArray();
        char[] c = "bcdfghklmnprstvzx".toCharArray();
        StringBuffer sb = new StringBuffer();
        int seed = 1;
        int rounds = (raw.length / 2) + 1;
        sb.append('x');
        for (int i = 0; i < rounds; i++) {
            if (i + 1 < rounds || raw.length % 2 != 0) {
                sb.append(v[(((raw[i * 2] >> 6) & 3) + seed) % 6]);
                sb.append(c[(raw[i * 2] >> 2) & 15]);
                sb.append(v[((raw[i * 2] & 3) + (seed / 6)) % 6]);
                if (i + 1 < rounds) {
                    sb.append(c[(raw[(i * 2) + 1] >> 4) & 15]);
                    sb.append(CharPool.DASHED);
                    sb.append(c[raw[(i * 2) + 1] & 15]);
                    seed = ((seed * 5) + (((raw[i * 2] & 255) * 7) + (raw[(i * 2) + 1] & 255))) % 36;
                }
            } else {
                sb.append(v[seed % 6]);
                sb.append('x');
                sb.append(v[seed / 6]);
            }
        }
        sb.append('x');
        return sb.toString();
    }

    public static final String createHexFingerprint(String keytype, byte[] publickey) {
        byte[] raw = rawFingerPrint("md5", keytype, publickey);
        return rawToHexFingerprint(raw);
    }

    public static final String createBubblebabbleFingerprint(String keytype, byte[] publickey) {
        byte[] raw = rawFingerPrint("sha1", keytype, publickey);
        return rawToBubblebabbleFingerprint(raw);
    }
}
