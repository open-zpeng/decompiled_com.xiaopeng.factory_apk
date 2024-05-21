package com.jcraft.jsch.jgss;

import com.jcraft.jsch.GSSContext;
import com.jcraft.jsch.JSchException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.lang3.BooleanUtils;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;
/* loaded from: classes.dex */
public class GSSContextKrb5 implements GSSContext {
    private static final String pUseSubjectCredsOnly = "javax.security.auth.useSubjectCredsOnly";
    private static String useSubjectCredsOnly = getSystemProperty(pUseSubjectCredsOnly);
    private org.ietf.jgss.GSSContext context = null;

    @Override // com.jcraft.jsch.GSSContext
    public void create(String user, String host) throws JSchException {
        try {
            Oid krb5 = new Oid("1.2.840.113554.1.2.2");
            Oid principalName = new Oid("1.2.840.113554.1.2.2.1");
            GSSManager mgr = GSSManager.getInstance();
            String cname = host;
            try {
                cname = InetAddress.getByName(cname).getCanonicalHostName();
            } catch (UnknownHostException e) {
            }
            GSSName _host = mgr.createName("host/" + cname, principalName);
            this.context = mgr.createContext(_host, krb5, (GSSCredential) null, 0);
            this.context.requestMutualAuth(true);
            this.context.requestConf(true);
            this.context.requestInteg(true);
            this.context.requestCredDeleg(true);
            this.context.requestAnonymity(false);
        } catch (GSSException ex) {
            throw new JSchException(ex.toString());
        }
    }

    @Override // com.jcraft.jsch.GSSContext
    public boolean isEstablished() {
        return this.context.isEstablished();
    }

    @Override // com.jcraft.jsch.GSSContext
    public byte[] init(byte[] token, int s, int l) throws JSchException {
        try {
            try {
                if (useSubjectCredsOnly == null) {
                    setSystemProperty(pUseSubjectCredsOnly, BooleanUtils.FALSE);
                }
                return this.context.initSecContext(token, 0, l);
            } catch (GSSException ex) {
                throw new JSchException(ex.toString());
            } catch (SecurityException ex2) {
                throw new JSchException(ex2.toString());
            }
        } finally {
            if (useSubjectCredsOnly == null) {
                setSystemProperty(pUseSubjectCredsOnly, "true");
            }
        }
    }

    @Override // com.jcraft.jsch.GSSContext
    public byte[] getMIC(byte[] message, int s, int l) {
        try {
            MessageProp prop = new MessageProp(0, true);
            return this.context.getMIC(message, s, l, prop);
        } catch (GSSException e) {
            return null;
        }
    }

    @Override // com.jcraft.jsch.GSSContext
    public void dispose() {
        try {
            this.context.dispose();
        } catch (GSSException e) {
        }
    }

    private static String getSystemProperty(String key) {
        try {
            return System.getProperty(key);
        } catch (Exception e) {
            return null;
        }
    }

    private static void setSystemProperty(String key, String value) {
        try {
            System.setProperty(key, value);
        } catch (Exception e) {
        }
    }
}
