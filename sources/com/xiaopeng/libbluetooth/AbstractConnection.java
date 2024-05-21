package com.xiaopeng.libbluetooth;

import com.xiaopeng.xpbluetoothservice.IXPBluetoothService;
import java.util.Iterator;
import java.util.LinkedList;
/* loaded from: classes2.dex */
public abstract class AbstractConnection {
    private LinkedList<Runnable> mToDoList = new LinkedList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void connectService(IXPBluetoothService iXPBluetoothService);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void onDisconnected();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void onUnregister();

    void post(Runnable item) {
        this.mToDoList.add(item);
    }

    void remove(Runnable item) {
        this.mToDoList.remove(item);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onConnected(IXPBluetoothService bluetoothService) {
        connectService(bluetoothService);
        work();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void work() {
        Iterator<Runnable> it = this.mToDoList.iterator();
        while (it.hasNext()) {
            it.next().run();
            it.remove();
        }
    }
}
