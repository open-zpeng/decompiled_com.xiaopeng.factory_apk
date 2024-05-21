package com.jcraft.jsch;

import com.jcraft.jsch.ConfigRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.lang3.BooleanUtils;
/* loaded from: classes.dex */
public class OpenSSHConfig implements ConfigRepository {
    private static final Hashtable keymap = new Hashtable();
    private final Hashtable config = new Hashtable();
    private final Vector hosts = new Vector();

    public static OpenSSHConfig parse(String conf) throws IOException {
        Reader r = new StringReader(conf);
        try {
            return new OpenSSHConfig(r);
        } finally {
            r.close();
        }
    }

    public static OpenSSHConfig parseFile(String file) throws IOException {
        Reader r = new FileReader(Util.checkTilde(file));
        try {
            return new OpenSSHConfig(r);
        } finally {
            r.close();
        }
    }

    OpenSSHConfig(Reader r) throws IOException {
        _parse(r);
    }

    private void _parse(Reader r) throws IOException {
        BufferedReader br = new BufferedReader(r);
        String host = "";
        Vector kv = new Vector();
        while (true) {
            String l = br.readLine();
            if (l != null) {
                String l2 = l.trim();
                if (l2.length() != 0 && !l2.startsWith("#")) {
                    String[] key_value = l2.split("[= \t]", 2);
                    for (int i = 0; i < key_value.length; i++) {
                        key_value[i] = key_value[i].trim();
                    }
                    int i2 = key_value.length;
                    if (i2 > 1) {
                        if (key_value[0].equals("Host")) {
                            this.config.put(host, kv);
                            this.hosts.addElement(host);
                            host = key_value[1];
                            kv = new Vector();
                        } else {
                            kv.addElement(key_value);
                        }
                    }
                }
            } else {
                this.config.put(host, kv);
                this.hosts.addElement(host);
                return;
            }
        }
    }

    @Override // com.jcraft.jsch.ConfigRepository
    public ConfigRepository.Config getConfig(String host) {
        return new MyConfig(host);
    }

    static {
        keymap.put("kex", "KexAlgorithms");
        keymap.put("server_host_key", "HostKeyAlgorithms");
        keymap.put("cipher.c2s", "Ciphers");
        keymap.put("cipher.s2c", "Ciphers");
        keymap.put("mac.c2s", "Macs");
        keymap.put("mac.s2c", "Macs");
        keymap.put("compression.s2c", "Compression");
        keymap.put("compression.c2s", "Compression");
        keymap.put("compression_level", "CompressionLevel");
        keymap.put("MaxAuthTries", "NumberOfPasswordPrompts");
    }

    /* loaded from: classes.dex */
    class MyConfig implements ConfigRepository.Config {
        private Vector _configs = new Vector();
        private String host;

        MyConfig(String host) {
            this.host = host;
            this._configs.addElement(OpenSSHConfig.this.config.get(""));
            byte[] _host = Util.str2byte(host);
            if (OpenSSHConfig.this.hosts.size() > 1) {
                for (int i = 1; i < OpenSSHConfig.this.hosts.size(); i++) {
                    String[] patterns = ((String) OpenSSHConfig.this.hosts.elementAt(i)).split("[ \t]");
                    for (String str : patterns) {
                        boolean negate = false;
                        String foo = str.trim();
                        if (foo.startsWith("!")) {
                            negate = true;
                            foo = foo.substring(1).trim();
                        }
                        if (Util.glob(Util.str2byte(foo), _host)) {
                            if (!negate) {
                                this._configs.addElement(OpenSSHConfig.this.config.get((String) OpenSSHConfig.this.hosts.elementAt(i)));
                            }
                        } else if (negate) {
                            this._configs.addElement(OpenSSHConfig.this.config.get((String) OpenSSHConfig.this.hosts.elementAt(i)));
                        }
                    }
                }
            }
        }

        private String find(String key) {
            if (OpenSSHConfig.keymap.get(key) != null) {
                key = (String) OpenSSHConfig.keymap.get(key);
            }
            String key2 = key.toUpperCase();
            String value = null;
            for (int i = 0; i < this._configs.size(); i++) {
                Vector v = (Vector) this._configs.elementAt(i);
                int j = 0;
                while (true) {
                    if (j >= v.size()) {
                        break;
                    }
                    String[] kv = (String[]) v.elementAt(j);
                    if (!kv[0].toUpperCase().equals(key2)) {
                        j++;
                    } else {
                        value = kv[1];
                        break;
                    }
                }
                if (value != null) {
                    break;
                }
            }
            return value;
        }

        private String[] multiFind(String key) {
            String foo;
            String key2 = key.toUpperCase();
            Vector value = new Vector();
            for (int i = 0; i < this._configs.size(); i++) {
                Vector v = (Vector) this._configs.elementAt(i);
                for (int j = 0; j < v.size(); j++) {
                    String[] kv = (String[]) v.elementAt(j);
                    if (kv[0].toUpperCase().equals(key2) && (foo = kv[1]) != null) {
                        value.remove(foo);
                        value.addElement(foo);
                    }
                }
            }
            int i2 = value.size();
            String[] result = new String[i2];
            value.toArray(result);
            return result;
        }

        @Override // com.jcraft.jsch.ConfigRepository.Config
        public String getHostname() {
            return find("Hostname");
        }

        @Override // com.jcraft.jsch.ConfigRepository.Config
        public String getUser() {
            return find("User");
        }

        @Override // com.jcraft.jsch.ConfigRepository.Config
        public int getPort() {
            String foo = find("Port");
            try {
                int port = Integer.parseInt(foo);
                return port;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override // com.jcraft.jsch.ConfigRepository.Config
        public String getValue(String key) {
            if (key.equals("compression.s2c") || key.equals("compression.c2s")) {
                String foo = find(key);
                if (foo == null || foo.equals(BooleanUtils.NO)) {
                    return "none,zlib@openssh.com,zlib";
                }
                return "zlib@openssh.com,zlib,none";
            }
            return find(key);
        }

        @Override // com.jcraft.jsch.ConfigRepository.Config
        public String[] getValues(String key) {
            return multiFind(key);
        }
    }
}
