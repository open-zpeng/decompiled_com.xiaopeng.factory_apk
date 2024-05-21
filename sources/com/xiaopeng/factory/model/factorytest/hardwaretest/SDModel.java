package com.xiaopeng.factory.model.factorytest.hardwaretest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import com.xiaopeng.commonfunc.model.test.StorageTest;
import com.xiaopeng.factory.MyApplication;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class SDModel {
    private static final int MESSAGE_READ_WRITE_FAIL = 2;
    private static final int MESSAGE_READ_WRITE_SUCCESS = 1;
    private final WeakReference<Handler> mHandlerWeakReference;
    private String mStoragePath;
    private final Context mContext = MyApplication.getContext();
    private final StorageManager mStorageManager = StorageManager.from(this.mContext);

    public SDModel(Handler handler) {
        this.mHandlerWeakReference = new WeakReference<>(handler);
    }

    @SuppressLint({"NewApi"})
    public boolean isSDExist() {
        StorageTest storageTest = new StorageTest(this.mContext);
        String state = storageTest.getVolumeState("SD");
        return "mounted".equals(state);
    }

    public void startReadAndWriteSD() {
        if (TextUtils.isEmpty(this.mStoragePath)) {
            return;
        }
        new Thread(new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.-$$Lambda$SDModel$XwrhI5YjsFB2EfWMkQxIc2qF2Dg
            @Override // java.lang.Runnable
            public final void run() {
                SDModel.this.lambda$startReadAndWriteSD$0$SDModel();
            }
        }, "ReadWriteThread").start();
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x005c, code lost:
        if (r1 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x006a, code lost:
        if (r1 == null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x006c, code lost:
        r1.delete();
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x006f, code lost:
        r2.sendToTarget();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$startReadAndWriteSD$0$SDModel() {
        /*
            r10 = this;
            java.lang.String r0 = "test.txt"
            r1 = 0
            java.lang.ref.WeakReference<android.os.Handler> r2 = r10.mHandlerWeakReference
            java.lang.Object r2 = r2.get()
            if (r2 == 0) goto L7c
            java.lang.ref.WeakReference<android.os.Handler> r2 = r10.mHandlerWeakReference
            java.lang.Object r2 = r2.get()
            android.os.Handler r2 = (android.os.Handler) r2
            android.os.Message r2 = r2.obtainMessage()
            r3 = 2
            java.io.File r4 = new java.io.File     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            java.lang.String r5 = r10.mStoragePath     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            boolean r5 = r4.exists()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            if (r5 == 0) goto L5c
            boolean r5 = r4.isDirectory()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            if (r5 == 0) goto L5c
            java.io.File r5 = new java.io.File     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            java.lang.String r6 = r10.mStoragePath     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            r5.<init>(r6, r0)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            r1 = r5
            boolean r5 = r1.exists()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            if (r5 != 0) goto L3c
            r1.createNewFile()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
        L3c:
            java.lang.String[] r5 = r4.list()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            int r6 = r5.length     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            r7 = 0
        L42:
            if (r7 >= r6) goto L59
            r8 = r5[r7]     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            boolean r9 = r8.equals(r0)     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            if (r9 == 0) goto L50
            r0 = 1
            r2.what = r0     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            goto L59
        L50:
            r2.what = r3     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            java.lang.String r9 = ""
            r2.obj = r9     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
            int r7 = r7 + 1
            goto L42
        L59:
            r1.delete()     // Catch: java.lang.Throwable -> L5f java.lang.Exception -> L61
        L5c:
            if (r1 == 0) goto L6f
            goto L6c
        L5f:
            r0 = move-exception
            goto L73
        L61:
            r0 = move-exception
            r2.what = r3     // Catch: java.lang.Throwable -> L5f
            java.lang.String r3 = r0.toString()     // Catch: java.lang.Throwable -> L5f
            r2.obj = r3     // Catch: java.lang.Throwable -> L5f
            if (r1 == 0) goto L6f
        L6c:
            r1.delete()
        L6f:
            r2.sendToTarget()
            goto L7c
        L73:
            if (r1 == 0) goto L78
            r1.delete()
        L78:
            r2.sendToTarget()
            throw r0
        L7c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.factory.model.factorytest.hardwaretest.SDModel.lambda$startReadAndWriteSD$0$SDModel():void");
    }
}
