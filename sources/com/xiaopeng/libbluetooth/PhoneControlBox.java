package com.xiaopeng.libbluetooth;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import cn.hutool.core.text.CharSequenceUtil;
import com.lzy.okgo.model.Progress;
import com.xiaopeng.xpbluetoothservice.IXPBluetoothService;
import com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService;
import com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback;
/* loaded from: classes2.dex */
public class PhoneControlBox extends AbsControlBox {
    private static final String TAG = "PhoneControlBox";
    private AbsPhoneControlCallback mCallback;
    private Context mContext;
    private IBtPhoneControlService mService;
    private IPhoneServiceCallback mServiceCallback = new IPhoneServiceCallback.Stub() { // from class: com.xiaopeng.libbluetooth.PhoneControlBox.1
        @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
        public void onMessageCallback(int action, Bundle bundle) {
            if (PhoneControlBox.this.mCallback == null || bundle == null) {
                return;
            }
            try {
                if (action == 1) {
                    String number = bundle.getString("CallerNumber");
                    String name = bundle.getString("CallerName");
                    int value = bundle.getInt("Value");
                    PhoneControlBox.this.mCallback.onCallStatus(value, number, name);
                } else if (action == 2) {
                    int missedCallCount = bundle.getInt("Value");
                    PhoneControlBox.this.mCallback.onMissCall(missedCallCount);
                } else if (action == 3) {
                    String text = bundle.getString("text");
                    String date = bundle.getString(Progress.DATE);
                    String number2 = bundle.getString("number");
                    PhoneControlBox.this.mCallback.onIncomingSms(number2, date, text);
                } else if (action == 4) {
                    int keyType = bundle.getInt("indKey");
                    int typeValue = bundle.getInt("indValue");
                    PhoneControlBox.this.mCallback.onHFPIndicator(keyType, typeValue);
                }
            } catch (Exception e) {
                PhoneControlBox.this.printError(e);
            }
        }

        @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
        public void onPhoneCallback(int status, int dataType, int cur, int total, int error, String name, String number, String time) {
            if (PhoneControlBox.this.mCallback == null) {
                return;
            }
            try {
                PhoneControlBox.this.mCallback.onPhoneBook(status, dataType, cur, total, error, name, number, time);
            } catch (Exception e) {
                PhoneControlBox.this.printError(e);
            }
        }

        @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
        public void onPBAPCallback(int status, int dataType, int cur, int total, int error, String name, String number, String time) {
            if (PhoneControlBox.this.mCallback == null) {
                return;
            }
            try {
                PhoneControlBox.this.mCallback.onPBAPCallback(status, dataType, cur, total, error, name, number, time);
            } catch (Exception e) {
                PhoneControlBox.this.printError(e);
            }
        }

        @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
        public void onSmsCallback(int status, int error, int cur, int total, String number, String date, String content) {
            if (PhoneControlBox.this.mCallback == null) {
                return;
            }
            try {
                PhoneControlBox.this.mCallback.onSmsCallback(status, error, cur, total, number, date, content);
            } catch (Exception e) {
                PhoneControlBox.this.printError(e);
            }
        }

        @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
        public void onCallRecordCallback(int type, String name) {
            if (PhoneControlBox.this.mCallback == null) {
                return;
            }
            try {
                PhoneControlBox.this.mCallback.onRecordCallback(type, name);
            } catch (Exception e) {
                PhoneControlBox.this.printError(e);
            }
        }
    };

    public PhoneControlBox(Context context, AbsPhoneControlCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void initService(IXPBluetoothService btService) {
        try {
            this.mService = btService.getBtPhoneControlService();
            this.mService.registerCallback(this.mServiceCallback);
            if (this.mCallback != null) {
                this.mCallback.onBindSuccess();
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void clearService() {
        this.mService = null;
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void onWorkDone() {
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void release() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService != null) {
            try {
                iBtPhoneControlService.unregisterCallback(this.mServiceCallback);
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    public boolean isCurrentDownloading() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.isCurrentDownloading(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean answerCall() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.answerCall(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean hangUpCall() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.hangUpCall(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean dialNumber(String number) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.dialNumber(BluetoothBoxes.getID(), number);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean dialDTMF(String number) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.dialDTMF(BluetoothBoxes.getID(), number);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean handleThreeWayCall(int type) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.handleThreeWayCall(BluetoothBoxes.getID(), type);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean reDial() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.reDial(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean queryCall(String[] number1, String[] name1, int[] callStatus1, String[] number2, String[] name2, int[] callStatus2) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.queryCall(BluetoothBoxes.getID(), number1, name1, callStatus1, number2, name2, callStatus2);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean transferAudio(boolean flag) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.transferAudio(BluetoothBoxes.getID(), flag);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean isSupportTransferAudio() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.isSupportTransferAudio(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public int getMissCallCount() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return 0;
        }
        try {
            int counts = iBtPhoneControlService.getMissCallCount(BluetoothBoxes.getID());
            return counts;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public boolean getContacts(boolean refresh, int memtype, int nMax_ME, int nMax_SM, int nMax_RC, int nMax_MC, int nMax_DC) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            iBtPhoneControlService.getContacts(BluetoothBoxes.getID(), refresh, memtype, nMax_ME, nMax_SM, nMax_RC, nMax_MC, nMax_DC);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean unRegisterContactsCallback() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            iBtPhoneControlService.unRegisterContactsCallback(BluetoothBoxes.getID());
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean setDownloadPBAttr(int attribute) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.setDownloadPBAttr(BluetoothBoxes.getID(), attribute);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public Bitmap readBitmap(String imgName, int width, int height) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return null;
        }
        try {
            Bitmap bitmap = iBtPhoneControlService.readBitmap(BluetoothBoxes.getID(), imgName, width, height);
            return bitmap;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public boolean deletePhote(String imgName) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.deletePhote(BluetoothBoxes.getID(), imgName);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getContactsByPBAP(int memtype, int nMax_ME, int nMax_SM, int nMax_RC, int nMax_MC, int nMax_DC) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            iBtPhoneControlService.getContactsByPBAP(BluetoothBoxes.getID(), memtype, nMax_ME, nMax_SM, nMax_RC, nMax_MC, nMax_DC);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean unRegisterContactsByPBAP() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            iBtPhoneControlService.unRegisterContactsByPBAP(BluetoothBoxes.getID());
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getMessage(boolean refresh, int nMaxGet) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            iBtPhoneControlService.getMessage(BluetoothBoxes.getID(), refresh, nMaxGet);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean unRegisterSMSCallback() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            iBtPhoneControlService.unRegisterSMSCallback(BluetoothBoxes.getID());
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean sendMessag(String number, String text) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.sendMessage(BluetoothBoxes.getID(), number, text);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean setMicVolume(int volume) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.setMicVolume(BluetoothBoxes.getID(), volume);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public int getMicVolume() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return 0;
        }
        try {
            int count = iBtPhoneControlService.getMicVolume(BluetoothBoxes.getID());
            return count;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public boolean setSpeakerVolume(int volume) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtPhoneControlService.setSpeakerVolume(BluetoothBoxes.getID(), volume);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public int getSpeakerVolume() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return 0;
        }
        try {
            int count = iBtPhoneControlService.getSpeakerVolume(BluetoothBoxes.getID());
            return count;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public int getMicMaxVolume() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return 0;
        }
        try {
            int count = iBtPhoneControlService.getMicMaxVolume(BluetoothBoxes.getID());
            return count;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public int getSpeakerMaxVolume() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService == null) {
            return 0;
        }
        try {
            int count = iBtPhoneControlService.getSpeakerMaxVolume(BluetoothBoxes.getID());
            return count;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public void changeDownLoadFlag(boolean flag) {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService != null) {
            try {
                iBtPhoneControlService.changeDownLoadFlag(BluetoothBoxes.getID(), flag);
                return;
            } catch (Exception e) {
                printError(e);
                return;
            }
        }
        Log.e(TAG, "changeDownloadFlag mService == null");
    }

    public void setForDownLoad() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService != null) {
            try {
                iBtPhoneControlService.setForDownLoad(BluetoothBoxes.getID());
                return;
            } catch (Exception e) {
                printError(e);
                return;
            }
        }
        Log.e(TAG, "changeDownloadFlag mService == null");
    }

    public boolean isForDownLoad() {
        IBtPhoneControlService iBtPhoneControlService = this.mService;
        if (iBtPhoneControlService != null) {
            try {
                return iBtPhoneControlService.isForDownLoad(BluetoothBoxes.getID());
            } catch (Exception e) {
                printError(e);
                return false;
            }
        }
        Log.e(TAG, "changeDownloadFlag mService == null");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printError(Exception e) {
        Log.e(TAG, e != null ? e.getMessage() : CharSequenceUtil.NULL);
    }
}
