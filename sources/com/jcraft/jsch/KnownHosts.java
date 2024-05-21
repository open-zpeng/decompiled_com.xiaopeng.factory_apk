package com.jcraft.jsch;

import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
/* loaded from: classes.dex */
public class KnownHosts implements HostKeyRepository {
    private static final String _known_hosts = "known_hosts";
    private MAC hmacsha1;
    private JSch jsch;
    private String known_hosts = null;
    private Vector pool;
    private static final byte[] space = {WlanPresenter.WL_RMMOD_INSMOD};
    private static final byte[] cr = Util.str2byte("\n");

    /* JADX INFO: Access modifiers changed from: package-private */
    public KnownHosts(JSch jsch) {
        this.jsch = null;
        this.pool = null;
        this.hmacsha1 = null;
        this.jsch = jsch;
        this.hmacsha1 = getHMACSHA1();
        this.pool = new Vector();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setKnownHosts(String filename) throws JSchException {
        try {
            this.known_hosts = filename;
            FileInputStream fis = new FileInputStream(Util.checkTilde(filename));
            setKnownHosts(fis);
        } catch (FileNotFoundException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x010f, code lost:
        if (r1 >= r12) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0111, code lost:
        r7 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0113, code lost:
        r1 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0115, code lost:
        if (r1 == 32) goto L223;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0117, code lost:
        if (r1 != 9) goto L122;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x011a, code lost:
        r0.append((char) r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x011e, code lost:
        r1 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0122, code lost:
        r1 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0123, code lost:
        r5 = r0.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x012e, code lost:
        if (com.jcraft.jsch.HostKey.name2type(r5) == 6) goto L222;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0130, code lost:
        r5 = com.jcraft.jsch.HostKey.name2type(r5);
        r19 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0138, code lost:
        r1 = r12;
        r19 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x013b, code lost:
        if (r1 < r12) goto L131;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x013d, code lost:
        addInvalidLine(com.jcraft.jsch.Util.byte2str(r13, 0, r12));
        r4 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0148, code lost:
        if (r1 >= r12) goto L218;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x014a, code lost:
        r4 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x014c, code lost:
        if (r4 == 32) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x014e, code lost:
        if (r4 != 9) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0150, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0153, code lost:
        r0.setLength(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0156, code lost:
        if (r1 >= r12) goto L214;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0158, code lost:
        r4 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x015a, code lost:
        r1 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x015e, code lost:
        if (r1 != 13) goto L146;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0163, code lost:
        if (r1 != 10) goto L148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0166, code lost:
        if (r1 == 32) goto L206;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0168, code lost:
        if (r1 != 9) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x016b, code lost:
        r0.append((char) r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x016f, code lost:
        r1 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0171, code lost:
        r1 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0173, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0177, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x017b, code lost:
        r4 = r0.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0185, code lost:
        if (r4.length() != 0) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0187, code lost:
        addInvalidLine(com.jcraft.jsch.Util.byte2str(r13, 0, r12));
        r4 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0194, code lost:
        if (r1 >= r12) goto L202;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0196, code lost:
        r2 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0198, code lost:
        if (r2 == 32) goto L201;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x019a, code lost:
        if (r2 != 9) goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x019c, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x01a0, code lost:
        if (r1 >= r12) goto L198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x01a2, code lost:
        r0.setLength(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x01a5, code lost:
        if (r1 >= r12) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x01a7, code lost:
        r3 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x01a9, code lost:
        r1 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x01ad, code lost:
        if (r1 != 13) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x01b4, code lost:
        if (r1 != 10) goto L176;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x01b6, code lost:
        r1 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x01b8, code lost:
        r0.append((char) r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x01bc, code lost:
        r1 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x01be, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x01c2, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x01c6, code lost:
        r3 = r0.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x01ca, code lost:
        r17 = r1;
        r16 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x01d0, code lost:
        r17 = r1;
        r16 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x01d6, code lost:
        r23.pool.addElement(new com.jcraft.jsch.KnownHosts.HashedHostKey(r23, r14, r15, r19, com.jcraft.jsch.Util.fromBase64(com.jcraft.jsch.Util.str2byte(r4), 0, r4.length()), r16));
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x01f8, code lost:
        r4 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0200, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0204, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0208, code lost:
        addInvalidLine(com.jcraft.jsch.Util.byte2str(r13, 0, r12));
        r4 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0251, code lost:
        r24.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x0255, code lost:
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x0256, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0262, code lost:
        throw new com.jcraft.jsch.JSchException(r0.toString(), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0070, code lost:
        if (r1 < r12) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0072, code lost:
        addInvalidLine(com.jcraft.jsch.Util.byte2str(r13, 0, r12));
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x007d, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0080, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0083, code lost:
        r0.setLength(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0086, code lost:
        if (r1 >= r12) goto L250;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0088, code lost:
        r7 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x008a, code lost:
        r1 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x008c, code lost:
        if (r1 == 32) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x008e, code lost:
        if (r1 != 9) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0091, code lost:
        r0.append((char) r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0095, code lost:
        r1 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0097, code lost:
        r1 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0099, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x009d, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00a1, code lost:
        r7 = r0.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00a5, code lost:
        if (r1 >= r12) goto L243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00ab, code lost:
        if (r7.length() != 0) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00af, code lost:
        if (r1 >= r12) goto L242;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b1, code lost:
        r14 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00b3, code lost:
        if (r14 == 32) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00b5, code lost:
        if (r14 != 9) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00b7, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00ba, code lost:
        r14 = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00c2, code lost:
        if (r7.charAt(0) != '@') goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00c4, code lost:
        r0.setLength(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00c8, code lost:
        if (r1 >= r12) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x00ca, code lost:
        r14 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00cc, code lost:
        r1 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00ce, code lost:
        if (r1 == 32) goto L230;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00d0, code lost:
        if (r1 != 9) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x00d3, code lost:
        r0.append((char) r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x00d7, code lost:
        r1 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x00d9, code lost:
        r1 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x00db, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x00df, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x00e3, code lost:
        r14 = r0.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x00e8, code lost:
        if (r1 >= r12) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x00ee, code lost:
        if (r14.length() != 0) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x00f1, code lost:
        if (r1 >= r12) goto L228;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x00f3, code lost:
        r14 = r13[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x00f5, code lost:
        if (r14 == 32) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x00f7, code lost:
        if (r14 != 9) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x00f9, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x00fc, code lost:
        r14 = r7;
        r15 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x00ff, code lost:
        addInvalidLine(com.jcraft.jsch.Util.byte2str(r13, 0, r12));
        r4 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x010a, code lost:
        r15 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x010b, code lost:
        r0.setLength(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0028, code lost:
        if (r12 != 0) goto L19;
     */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0234 A[Catch: all -> 0x0250, TryCatch #9 {all -> 0x0250, blocks: (B:16:0x003e, B:17:0x0045, B:189:0x0230, B:191:0x0234, B:193:0x0238, B:194:0x0241, B:195:0x0242, B:196:0x024b, B:197:0x024c, B:198:0x024f, B:29:0x005a, B:35:0x0065, B:45:0x0083, B:59:0x00a1, B:61:0x00a7, B:65:0x00b1, B:68:0x00b7, B:69:0x00ba, B:71:0x00c4, B:85:0x00e3, B:87:0x00ea, B:91:0x00f3, B:94:0x00f9, B:98:0x010b, B:109:0x0123, B:111:0x0130, B:114:0x013d, B:116:0x014a, B:119:0x0150, B:120:0x0153, B:139:0x017b, B:141:0x0187, B:143:0x0196, B:146:0x019c, B:149:0x01a2, B:164:0x01c6, B:96:0x00ff, B:174:0x0208, B:22:0x004c, B:175:0x0213, B:176:0x021d), top: B:209:0x0014 }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x024c A[Catch: all -> 0x0250, TryCatch #9 {all -> 0x0250, blocks: (B:16:0x003e, B:17:0x0045, B:189:0x0230, B:191:0x0234, B:193:0x0238, B:194:0x0241, B:195:0x0242, B:196:0x024b, B:197:0x024c, B:198:0x024f, B:29:0x005a, B:35:0x0065, B:45:0x0083, B:59:0x00a1, B:61:0x00a7, B:65:0x00b1, B:68:0x00b7, B:69:0x00ba, B:71:0x00c4, B:85:0x00e3, B:87:0x00ea, B:91:0x00f3, B:94:0x00f9, B:98:0x010b, B:109:0x0123, B:111:0x0130, B:114:0x013d, B:116:0x014a, B:119:0x0150, B:120:0x0153, B:139:0x017b, B:141:0x0187, B:143:0x0196, B:146:0x019c, B:149:0x01a2, B:164:0x01c6, B:96:0x00ff, B:174:0x0208, B:22:0x004c, B:175:0x0213, B:176:0x021d), top: B:209:0x0014 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setKnownHosts(java.io.InputStream r24) throws com.jcraft.jsch.JSchException {
        /*
            Method dump skipped, instructions count: 611
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.KnownHosts.setKnownHosts(java.io.InputStream):void");
    }

    private void addInvalidLine(String line) throws JSchException {
        HostKey hk = new HostKey(line, 6, null);
        this.pool.addElement(hk);
    }

    String getKnownHostsFile() {
        return this.known_hosts;
    }

    @Override // com.jcraft.jsch.HostKeyRepository
    public String getKnownHostsRepositoryID() {
        return this.known_hosts;
    }

    @Override // com.jcraft.jsch.HostKeyRepository
    public int check(String host, byte[] key) {
        if (host == null) {
            return 1;
        }
        try {
            HostKey hk = new HostKey(host, 0, key);
            synchronized (this.pool) {
                int result = 1;
                for (int i = 0; i < this.pool.size(); i++) {
                    HostKey _hk = (HostKey) this.pool.elementAt(i);
                    if (_hk.isMatched(host) && _hk.type == hk.type) {
                        if (Util.array_equals(_hk.key, key)) {
                            return 0;
                        }
                        result = 2;
                    }
                }
                if (result == 1 && host.startsWith("[") && host.indexOf("]:") > 1) {
                    return check(host.substring(1, host.indexOf("]:")), key);
                }
                return result;
            }
        } catch (JSchException e) {
            return 1;
        }
    }

    @Override // com.jcraft.jsch.HostKeyRepository
    public void add(HostKey hostkey, UserInfo userinfo) {
        int i = hostkey.type;
        String host = hostkey.getHost();
        byte[] bArr = hostkey.key;
        synchronized (this.pool) {
            for (int i2 = 0; i2 < this.pool.size(); i2++) {
                HostKey hk = (HostKey) this.pool.elementAt(i2);
                if (hk.isMatched(host)) {
                    int i3 = hk.type;
                }
            }
        }
        this.pool.addElement(hostkey);
        String bar = getKnownHostsRepositoryID();
        if (bar != null) {
            boolean foo = true;
            File goo = new File(Util.checkTilde(bar));
            if (!goo.exists()) {
                foo = false;
                if (userinfo != null) {
                    foo = userinfo.promptYesNo(bar + " does not exist.\nAre you sure you want to create it?");
                    File goo2 = goo.getParentFile();
                    if (foo && goo2 != null && !goo2.exists()) {
                        foo = userinfo.promptYesNo("The parent directory " + goo2 + " does not exist.\nAre you sure you want to create it?");
                        if (foo) {
                            if (goo2.mkdirs()) {
                                userinfo.showMessage(goo2 + " has been succesfully created.\nPlease check its access permission.");
                            } else {
                                userinfo.showMessage(goo2 + " has not been created.");
                                foo = false;
                            }
                        }
                    }
                    if (goo2 == null) {
                        foo = false;
                    }
                }
            }
            if (foo) {
                try {
                    sync(bar);
                } catch (Exception e) {
                    PrintStream printStream = System.err;
                    printStream.println("sync known_hosts: " + e);
                }
            }
        }
    }

    @Override // com.jcraft.jsch.HostKeyRepository
    public HostKey[] getHostKey() {
        return getHostKey(null, null);
    }

    @Override // com.jcraft.jsch.HostKeyRepository
    public HostKey[] getHostKey(String host, String type) {
        HostKey[] foo;
        synchronized (this.pool) {
            ArrayList v = new ArrayList();
            for (int i = 0; i < this.pool.size(); i++) {
                HostKey hk = (HostKey) this.pool.elementAt(i);
                if (hk.type != 6 && (host == null || (hk.isMatched(host) && (type == null || hk.getType().equals(type))))) {
                    v.add(hk);
                }
            }
            int i2 = v.size();
            foo = new HostKey[i2];
            for (int i3 = 0; i3 < v.size(); i3++) {
                foo[i3] = (HostKey) v.get(i3);
            }
            if (host != null && host.startsWith("[") && host.indexOf("]:") > 1) {
                HostKey[] tmp = getHostKey(host.substring(1, host.indexOf("]:")), type);
                if (tmp.length > 0) {
                    HostKey[] bar = new HostKey[foo.length + tmp.length];
                    System.arraycopy(foo, 0, bar, 0, foo.length);
                    System.arraycopy(tmp, 0, bar, foo.length, tmp.length);
                    foo = bar;
                }
            }
        }
        return foo;
    }

    @Override // com.jcraft.jsch.HostKeyRepository
    public void remove(String host, String type) {
        remove(host, type, null);
    }

    @Override // com.jcraft.jsch.HostKeyRepository
    public void remove(String host, String type, byte[] key) {
        boolean sync = false;
        synchronized (this.pool) {
            for (int i = 0; i < this.pool.size(); i++) {
                HostKey hk = (HostKey) this.pool.elementAt(i);
                if (host == null || (hk.isMatched(host) && (type == null || (hk.getType().equals(type) && (key == null || Util.array_equals(key, hk.key)))))) {
                    String hosts = hk.getHost();
                    if (!hosts.equals(host) && (!(hk instanceof HashedHostKey) || !((HashedHostKey) hk).isHashed())) {
                        hk.host = deleteSubString(hosts, host);
                        sync = true;
                    }
                    this.pool.removeElement(hk);
                    sync = true;
                }
            }
        }
        if (sync) {
            try {
                sync();
            } catch (Exception e) {
            }
        }
    }

    protected void sync() throws IOException {
        String str = this.known_hosts;
        if (str != null) {
            sync(str);
        }
    }

    protected synchronized void sync(String foo) throws IOException {
        if (foo == null) {
            return;
        }
        FileOutputStream fos = new FileOutputStream(Util.checkTilde(foo));
        dump(fos);
        fos.close();
    }

    void dump(OutputStream out) throws IOException {
        try {
            synchronized (this.pool) {
                for (int i = 0; i < this.pool.size(); i++) {
                    HostKey hk = (HostKey) this.pool.elementAt(i);
                    String marker = hk.getMarker();
                    String host = hk.getHost();
                    String type = hk.getType();
                    String comment = hk.getComment();
                    if (type.equals(Constant.UNKNOWN_STRING)) {
                        out.write(Util.str2byte(host));
                        out.write(cr);
                    } else {
                        if (marker.length() != 0) {
                            out.write(Util.str2byte(marker));
                            out.write(space);
                        }
                        out.write(Util.str2byte(host));
                        out.write(space);
                        out.write(Util.str2byte(type));
                        out.write(space);
                        out.write(Util.str2byte(hk.getKey()));
                        if (comment != null) {
                            out.write(space);
                            out.write(Util.str2byte(comment));
                        }
                        out.write(cr);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private String deleteSubString(String hosts, String host) {
        int j;
        int i = 0;
        int hostlen = host.length();
        int hostslen = hosts.length();
        while (i < hostslen && (j = hosts.indexOf(44, i)) != -1) {
            if (!host.equals(hosts.substring(i, j))) {
                i = j + 1;
            } else {
                return hosts.substring(0, i) + hosts.substring(j + 1);
            }
        }
        if (hosts.endsWith(host) && hostslen - i == hostlen) {
            return hosts.substring(0, hostlen == hostslen ? 0 : (hostslen - hostlen) - 1);
        }
        return hosts;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MAC getHMACSHA1() {
        if (this.hmacsha1 == null) {
            try {
                JSch jSch = this.jsch;
                Class c = Class.forName(JSch.getConfig("hmac-sha1"));
                this.hmacsha1 = (MAC) c.newInstance();
            } catch (Exception e) {
                PrintStream printStream = System.err;
                printStream.println("hmacsha1: " + e);
            }
        }
        return this.hmacsha1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HostKey createHashedHostKey(String host, byte[] key) throws JSchException {
        HashedHostKey hhk = new HashedHostKey(this, host, key);
        hhk.hash();
        return hhk;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class HashedHostKey extends HostKey {
        private static final String HASH_DELIM = "|";
        private static final String HASH_MAGIC = "|1|";
        byte[] hash;
        private boolean hashed;
        byte[] salt;

        HashedHostKey(KnownHosts knownHosts, String host, byte[] key) throws JSchException {
            this(knownHosts, host, 0, key);
        }

        HashedHostKey(KnownHosts knownHosts, String host, int type, byte[] key) throws JSchException {
            this("", host, type, key, null);
        }

        HashedHostKey(String marker, String host, int type, byte[] key, String comment) throws JSchException {
            super(marker, host, type, key, comment);
            this.hashed = false;
            this.salt = null;
            this.hash = null;
            if (this.host.startsWith(HASH_MAGIC) && this.host.substring(HASH_MAGIC.length()).indexOf("|") > 0) {
                String data = this.host.substring(HASH_MAGIC.length());
                String _salt = data.substring(0, data.indexOf("|"));
                String _hash = data.substring(data.indexOf("|") + 1);
                this.salt = Util.fromBase64(Util.str2byte(_salt), 0, _salt.length());
                this.hash = Util.fromBase64(Util.str2byte(_hash), 0, _hash.length());
                if (this.salt.length != 20 || this.hash.length != 20) {
                    this.salt = null;
                    this.hash = null;
                    return;
                }
                this.hashed = true;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.jcraft.jsch.HostKey
        public boolean isMatched(String _host) {
            boolean array_equals;
            if (this.hashed) {
                MAC macsha1 = KnownHosts.this.getHMACSHA1();
                try {
                    synchronized (macsha1) {
                        macsha1.init(this.salt);
                        byte[] foo = Util.str2byte(_host);
                        macsha1.update(foo, 0, foo.length);
                        byte[] bar = new byte[macsha1.getBlockSize()];
                        macsha1.doFinal(bar, 0);
                        array_equals = Util.array_equals(this.hash, bar);
                    }
                    return array_equals;
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            }
            return super.isMatched(_host);
        }

        boolean isHashed() {
            return this.hashed;
        }

        void hash() {
            if (!this.hashed) {
                MAC macsha1 = KnownHosts.this.getHMACSHA1();
                if (this.salt == null) {
                    Random random = Session.random;
                    synchronized (random) {
                        this.salt = new byte[macsha1.getBlockSize()];
                        random.fill(this.salt, 0, this.salt.length);
                    }
                }
                try {
                    synchronized (macsha1) {
                        macsha1.init(this.salt);
                        byte[] foo = Util.str2byte(this.host);
                        macsha1.update(foo, 0, foo.length);
                        this.hash = new byte[macsha1.getBlockSize()];
                        macsha1.doFinal(this.hash, 0);
                    }
                } catch (Exception e) {
                }
                StringBuilder sb = new StringBuilder();
                sb.append(HASH_MAGIC);
                byte[] bArr = this.salt;
                sb.append(Util.byte2str(Util.toBase64(bArr, 0, bArr.length)));
                sb.append("|");
                byte[] bArr2 = this.hash;
                sb.append(Util.byte2str(Util.toBase64(bArr2, 0, bArr2.length)));
                this.host = sb.toString();
                this.hashed = true;
            }
        }
    }
}
