package com.xiaopeng.libbluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
/* loaded from: classes2.dex */
public class DemoActivity extends Activity {
    private static final boolean DEBUG = true;
    private static final String TAG = DemoActivity.class.getSimpleName();
    private BluetoothBoxes bluetoothBoxes;
    private GeneralControlBox controlBox;
    private PhoneControlBox phoneControlBox;
    String[] name = new String[16];
    int[] cod = new int[16];
    int[] count = new int[1];
    String[] address = new String[16];
    private AbsGeneralControlCallback controlCallback = new AbsGeneralControlCallback() { // from class: com.xiaopeng.libbluetooth.DemoActivity.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.xiaopeng.libbluetooth.AbsGeneralControlCallback
        public void onScanCallback(String address, String deviceName, int cod, int rssi, boolean complete) {
            String str = DemoActivity.TAG;
            Log.d(str, "address = " + address + ",deviceName = " + deviceName + ",cod = " + cod + ",rssi = " + rssi + ",complete = " + complete);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.xiaopeng.libbluetooth.AbsGeneralControlCallback
        public void onBindSuccess() {
            if (DemoActivity.this.controlBox != null) {
                DemoActivity.this.controlBox.startScanDeviceEx();
                DemoActivity.this.controlBox.getPairedList(DemoActivity.this.count, DemoActivity.this.name, DemoActivity.this.address, DemoActivity.this.cod);
                String str = DemoActivity.TAG;
                Log.d(str, "count = " + DemoActivity.this.count[0]);
                for (int i = 0; i < DemoActivity.this.count[0]; i++) {
                    String str2 = DemoActivity.TAG;
                    Log.d(str2, "address = " + DemoActivity.this.address[i] + ",name = " + DemoActivity.this.name[i]);
                }
            }
        }
    };
    private AbsPhoneControlCallback phoneControlCallback = new AbsPhoneControlCallback() { // from class: com.xiaopeng.libbluetooth.DemoActivity.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.xiaopeng.libbluetooth.AbsPhoneControlCallback
        public void onPhoneBook(int status, int dataType, int cur, int total, int error, String name, String number, String time) {
            String str = DemoActivity.TAG;
            Log.d(str, "status = " + status + ",dataType = " + dataType + ",cur = " + cur + ",total = " + total + ",error = " + error + ",name = " + name + ",number=" + number);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.xiaopeng.libbluetooth.AbsPhoneControlCallback
        public void onBindSuccess() {
            PhoneControlBox unused = DemoActivity.this.phoneControlBox;
        }
    };

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_layout);
        Log.d(TAG, "onCreate_before");
        this.bluetoothBoxes = BluetoothBoxes.getInstance();
        this.controlBox = this.bluetoothBoxes.getGeneralControlBox(this.controlCallback);
        this.bluetoothBoxes.registerCallback(this.controlBox);
        this.phoneControlBox = this.bluetoothBoxes.getPhoneControlBox(this.phoneControlCallback);
        this.bluetoothBoxes.registerCallback(this.phoneControlBox);
        Log.d(TAG, "onCreate_after");
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        BluetoothBoxes.getInstance().releaseGeneralControlBox(this.controlBox);
        BluetoothBoxes.getInstance().releasePhoneControlBox(this.phoneControlBox);
    }
}
