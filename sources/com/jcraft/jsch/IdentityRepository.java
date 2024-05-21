package com.jcraft.jsch;

import java.util.Vector;
/* loaded from: classes.dex */
public interface IdentityRepository {
    public static final int NOTRUNNING = 1;
    public static final int RUNNING = 2;
    public static final int UNAVAILABLE = 0;

    boolean add(byte[] bArr);

    Vector getIdentities();

    String getName();

    int getStatus();

    boolean remove(byte[] bArr);

    void removeAll();

    /* loaded from: classes.dex */
    public static class Wrapper implements IdentityRepository {
        private Vector cache;
        private IdentityRepository ir;
        private boolean keep_in_cache;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Wrapper(IdentityRepository ir) {
            this(ir, false);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Wrapper(IdentityRepository ir, boolean keep_in_cache) {
            this.cache = new Vector();
            this.keep_in_cache = false;
            this.ir = ir;
            this.keep_in_cache = keep_in_cache;
        }

        @Override // com.jcraft.jsch.IdentityRepository
        public String getName() {
            return this.ir.getName();
        }

        @Override // com.jcraft.jsch.IdentityRepository
        public int getStatus() {
            return this.ir.getStatus();
        }

        @Override // com.jcraft.jsch.IdentityRepository
        public boolean add(byte[] identity) {
            return this.ir.add(identity);
        }

        @Override // com.jcraft.jsch.IdentityRepository
        public boolean remove(byte[] blob) {
            return this.ir.remove(blob);
        }

        @Override // com.jcraft.jsch.IdentityRepository
        public void removeAll() {
            this.cache.removeAllElements();
            this.ir.removeAll();
        }

        @Override // com.jcraft.jsch.IdentityRepository
        public Vector getIdentities() {
            Vector result = new Vector();
            for (int i = 0; i < this.cache.size(); i++) {
                Identity identity = (Identity) this.cache.elementAt(i);
                result.add(identity);
            }
            Vector tmp = this.ir.getIdentities();
            for (int i2 = 0; i2 < tmp.size(); i2++) {
                result.add(tmp.elementAt(i2));
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void add(Identity identity) {
            if (!this.keep_in_cache && !identity.isEncrypted() && (identity instanceof IdentityFile)) {
                try {
                    this.ir.add(((IdentityFile) identity).getKeyPair().forSSHAgent());
                    return;
                } catch (JSchException e) {
                    return;
                }
            }
            this.cache.addElement(identity);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void check() {
            if (this.cache.size() > 0) {
                Object[] identities = this.cache.toArray();
                for (Object obj : identities) {
                    Identity identity = (Identity) obj;
                    this.cache.removeElement(identity);
                    add(identity);
                }
            }
        }
    }
}
