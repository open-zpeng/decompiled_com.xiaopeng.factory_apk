package com.xiaopeng.libbluetooth;

import com.xiaopeng.xpbluetoothservice.IXPBluetoothService;
/* loaded from: classes2.dex */
public abstract class AbsControlBox {
    protected AbstractConnection mConnection = new AbstractConnection() { // from class: com.xiaopeng.libbluetooth.AbsControlBox.1
        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.xiaopeng.libbluetooth.AbstractConnection
        public void onDisconnected() {
            AbsControlBox.this.clearService();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.xiaopeng.libbluetooth.AbstractConnection
        public void connectService(IXPBluetoothService btService) {
            AbsControlBox.this.initService(btService);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.xiaopeng.libbluetooth.AbstractConnection
        public void work() {
            super.work();
            AbsControlBox.this.onWorkDone();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.xiaopeng.libbluetooth.AbstractConnection
        public void onUnregister() {
            AbsControlBox.this.release();
        }
    };

    protected abstract void clearService();

    protected abstract void initService(IXPBluetoothService iXPBluetoothService);

    protected abstract void onWorkDone();

    protected abstract void release();
}
