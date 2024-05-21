package com.jcraft.jsch;

import java.util.Vector;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LocalIdentityRepository implements IdentityRepository {
    private static final String name = "Local Identity Repository";
    private Vector identities = new Vector();
    private JSch jsch;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalIdentityRepository(JSch jsch) {
        this.jsch = jsch;
    }

    @Override // com.jcraft.jsch.IdentityRepository
    public String getName() {
        return name;
    }

    @Override // com.jcraft.jsch.IdentityRepository
    public int getStatus() {
        return 2;
    }

    @Override // com.jcraft.jsch.IdentityRepository
    public synchronized Vector getIdentities() {
        Vector v;
        removeDupulicates();
        v = new Vector();
        for (int i = 0; i < this.identities.size(); i++) {
            v.addElement(this.identities.elementAt(i));
        }
        return v;
    }

    public synchronized void add(Identity identity) {
        if (!this.identities.contains(identity)) {
            byte[] blob1 = identity.getPublicKeyBlob();
            if (blob1 == null) {
                this.identities.addElement(identity);
                return;
            }
            for (int i = 0; i < this.identities.size(); i++) {
                byte[] blob2 = ((Identity) this.identities.elementAt(i)).getPublicKeyBlob();
                if (blob2 != null && Util.array_equals(blob1, blob2)) {
                    if (identity.isEncrypted() || !((Identity) this.identities.elementAt(i)).isEncrypted()) {
                        return;
                    }
                    remove(blob2);
                }
            }
            this.identities.addElement(identity);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:14:0x0015
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    @Override // com.jcraft.jsch.IdentityRepository
    public synchronized boolean add(byte[] r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.lang.String r0 = "from remote:"
            r1 = 0
            com.jcraft.jsch.JSch r2 = r3.jsch     // Catch: com.jcraft.jsch.JSchException -> L10 java.lang.Throwable -> L12
            com.jcraft.jsch.IdentityFile r0 = com.jcraft.jsch.IdentityFile.newInstance(r0, r4, r1, r2)     // Catch: com.jcraft.jsch.JSchException -> L10 java.lang.Throwable -> L12
            r3.add(r0)     // Catch: com.jcraft.jsch.JSchException -> L10 java.lang.Throwable -> L12
            r1 = 1
            monitor-exit(r3)
            return r1
        L10:
            r0 = move-exception
            goto L16
        L12:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        L15:
            r0 = move-exception
        L16:
            r1 = 0
            monitor-exit(r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.LocalIdentityRepository.add(byte[]):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void remove(Identity identity) {
        if (this.identities.contains(identity)) {
            this.identities.removeElement(identity);
            identity.clear();
        } else {
            remove(identity.getPublicKeyBlob());
        }
    }

    @Override // com.jcraft.jsch.IdentityRepository
    public synchronized boolean remove(byte[] blob) {
        if (blob == null) {
            return false;
        }
        for (int i = 0; i < this.identities.size(); i++) {
            Identity _identity = (Identity) this.identities.elementAt(i);
            byte[] _blob = _identity.getPublicKeyBlob();
            if (_blob != null && Util.array_equals(blob, _blob)) {
                this.identities.removeElement(_identity);
                _identity.clear();
                return true;
            }
        }
        return false;
    }

    @Override // com.jcraft.jsch.IdentityRepository
    public synchronized void removeAll() {
        for (int i = 0; i < this.identities.size(); i++) {
            Identity identity = (Identity) this.identities.elementAt(i);
            identity.clear();
        }
        this.identities.removeAllElements();
    }

    private void removeDupulicates() {
        Vector v = new Vector();
        int len = this.identities.size();
        if (len == 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            Identity foo = (Identity) this.identities.elementAt(i);
            byte[] foo_blob = foo.getPublicKeyBlob();
            if (foo_blob != null) {
                int j = i + 1;
                while (true) {
                    if (j >= len) {
                        break;
                    }
                    Identity bar = (Identity) this.identities.elementAt(j);
                    byte[] bar_blob = bar.getPublicKeyBlob();
                    if (bar_blob == null || !Util.array_equals(foo_blob, bar_blob) || foo.isEncrypted() != bar.isEncrypted()) {
                        j++;
                    } else {
                        v.addElement(foo_blob);
                        break;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < v.size(); i2++) {
            remove((byte[]) v.elementAt(i2));
        }
    }
}
