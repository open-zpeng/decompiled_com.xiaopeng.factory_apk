package com.xiaopeng.libbluetooth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import cn.hutool.core.text.CharSequenceUtil;
import com.xiaopeng.xpbluetoothservice.IXPBluetoothService;
import com.xiaopeng.xpbluetoothservice.a2dpcontrol.IA2DPServiceCallback;
import com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class A2DPControlBox extends AbsControlBox {
    private static final String TAG = "A2DPControlBox";
    private AbsA2DPControlCallback mCallback;
    private Context mContext;
    private IBtA2DPControlService mService;
    private SparseArray<String> songMessages = new SparseArray<>();
    private IA2DPServiceCallback mServiceCallback = new IA2DPServiceCallback.Stub() { // from class: com.xiaopeng.libbluetooth.A2DPControlBox.1
        @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IA2DPServiceCallback
        public void onMessageCallback(int action, Bundle bundle) {
            if (bundle == null) {
                return;
            }
            try {
                if (A2DPControlBox.this.mCallback == null) {
                    return;
                }
                switch (action) {
                    case 10:
                        int cur = bundle.getInt("cur");
                        int total = bundle.getInt("total");
                        int code = bundle.getInt("code");
                        int complete = bundle.getInt("complete");
                        String title = bundle.getString("title");
                        String artist = bundle.getString("artist");
                        String itemType = bundle.getString("itemType");
                        A2DPControlBox.this.mCallback.onA2DPBrowsingCallback(cur, total, code, complete, title, artist, itemType);
                        break;
                    case 11:
                        boolean isSupportMetaData = bundle.getBoolean("MetaData");
                        boolean isSupportPlayStatus = bundle.getBoolean("PlayStatus");
                        A2DPControlBox.this.mCallback.onA2DPFeatureSupport(isSupportMetaData, isSupportPlayStatus);
                        break;
                    case 12:
                        int dataType = bundle.getInt("DataType");
                        int attributeId = bundle.getInt("Attribute_id");
                        String metaData = bundle.getString("MetaData");
                        A2DPControlBox.this.mCallback.onA2DPMetaData(dataType, attributeId, metaData);
                        break;
                    case 13:
                        String position = bundle.getString("Position");
                        A2DPControlBox.this.mCallback.onA2DPPlayBackPos(Integer.valueOf(position).intValue());
                        break;
                    case 14:
                        int playStatus = bundle.getInt("PlayStatus", -1);
                        A2DPControlBox.this.mCallback.onA2DPPlayStatus(playStatus);
                        break;
                    case 15:
                        int streamStatus = bundle.getInt("StreamStatus", -1);
                        A2DPControlBox.this.mCallback.onA2DPStreamStatus(streamStatus);
                        break;
                    case 16:
                        boolean isSupportCmd = bundle.getBoolean("Passthroughcmd");
                        boolean isSupportNavi = bundle.getBoolean("GroupNavi");
                        boolean isSupportCTPlayer = bundle.getBoolean("AdvancedCTPlayer");
                        boolean isSupportBrowsing = bundle.getBoolean("Browsing");
                        boolean isSupportSearch = bundle.getBoolean("Searching");
                        boolean addToNowPlaying = bundle.getBoolean("AddToNowPlaying");
                        boolean uidUnique = bundle.getBoolean("UIDUnique");
                        boolean onlyBrowsable = bundle.getBoolean("OnlyBrowsable");
                        boolean onlySearchable = bundle.getBoolean("OnlySearchable");
                        boolean nowPlaying = bundle.getBoolean("Nowplaying");
                        boolean uidPersistency = bundle.getBoolean("UIDPersistency");
                        A2DPControlBox.this.mCallback.onAVRCPFeatureSupport(isSupportCmd, isSupportNavi, isSupportCTPlayer, isSupportBrowsing, isSupportSearch, addToNowPlaying, uidUnique, onlyBrowsable, onlySearchable, nowPlaying, uidPersistency);
                        break;
                    case 17:
                        int eventId = bundle.getInt("EventID");
                        A2DPControlBox.this.mCallback.onAVRCPBrowsingEvent(eventId);
                        break;
                    case 18:
                        int attributeValue = bundle.getInt("Value");
                        int attributeId2 = bundle.getInt("AttributeID");
                        A2DPControlBox.this.mCallback.onAVRCPChangedEvent(attributeId2, attributeValue);
                        break;
                    case 19:
                        ArrayList<Integer> allowAttributes = bundle.getIntegerArrayList("Allowed");
                        int attributeId3 = bundle.getInt("AttributeID");
                        A2DPControlBox.this.mCallback.onAVRCPSupportedEvent(attributeId3, allowAttributes);
                        break;
                }
            } catch (Exception e) {
                A2DPControlBox.this.printError(e);
            }
        }
    };

    public A2DPControlBox(Context context, AbsA2DPControlCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void initService(IXPBluetoothService btService) {
        try {
            this.mService = btService.getBtA2DPControlService();
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
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService != null) {
            try {
                iBtA2DPControlService.unregisterCallback(this.mServiceCallback);
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    public boolean isCurrentA2DPPlaying() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.isCurrentA2DPPlaying(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean play() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.play(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean pause() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.pause(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean stop() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.stop(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean playNext() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.playNext(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean playPrevious() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.playPrevious(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean controlAVRCPEx(int code, int actFlag) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.controlAVRCPEx(BluetoothBoxes.getID(), code, actFlag);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean isA2DPSupportMetaData() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.isA2DPSupportMetaData(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public String getSongName() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return null;
        }
        try {
            String name = iBtA2DPControlService.getSongName(BluetoothBoxes.getID());
            return name;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public String getArtistName() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return null;
        }
        try {
            String data = iBtA2DPControlService.getArtistName(BluetoothBoxes.getID());
            return data;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public String getAlbumName() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return null;
        }
        try {
            String data = iBtA2DPControlService.getAlbumName(BluetoothBoxes.getID());
            return data;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public int getSongTimes() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return 0;
        }
        try {
            int data = iBtA2DPControlService.getSongTimes(BluetoothBoxes.getID());
            return data;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public boolean getMediaInfoEx(int mask) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            iBtA2DPControlService.getMediaInfoEx(BluetoothBoxes.getID(), mask);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getPlayStatus() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.getPlayStatus(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getPlayerSettings() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.getPlayerSettings(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean retrievePlayerSettings(int attribute) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.retrievePlayerSettings(BluetoothBoxes.getID(), attribute);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean setPlayerSettings(int attribute, int value) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.setPlayerSettings(BluetoothBoxes.getID(), attribute, value);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean retrievePlayerSupported(int attribute, int[] allowArray, int arraySize) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.retrievePlayerSupported(BluetoothBoxes.getID(), attribute, allowArray, arraySize);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean startBrowsingElement(String path, int mask, int maxGetCount) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            iBtA2DPControlService.startBrowsingElement(BluetoothBoxes.getID(), path, mask, maxGetCount);
            return false;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean startBrowsingElementEx(String path, int mask, int startPos, int maxGetCount) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            iBtA2DPControlService.startBrowsingElementEx(BluetoothBoxes.getID(), path, mask, startPos, maxGetCount);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean startBrowsingSearch(String path, String keyword, int mask, int maxGetCount) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            iBtA2DPControlService.startBrowsingSearch(BluetoothBoxes.getID(), path, keyword, mask, maxGetCount);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean startBrowsingSearchEx(String path, String keyword, int mask, int startPos, int maxGetCount) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            iBtA2DPControlService.startBrowsingSearchEx(BluetoothBoxes.getID(), path, keyword, mask, startPos, maxGetCount);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean startPlayingList(int mask, int maxGetCount) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            iBtA2DPControlService.startPlayingList(BluetoothBoxes.getID(), mask, maxGetCount);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getPlayingListEx(int mask, int startPos, int maxGetCount) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            iBtA2DPControlService.startPlayingListEx(BluetoothBoxes.getID(), mask, startPos, maxGetCount);
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean playBrowsingItem(int scope, String uuid) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.playBrowsingItem(BluetoothBoxes.getID(), scope, uuid);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean addToPlayingQueue(int scope, String uuid) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.addToPlayingQueue(BluetoothBoxes.getID(), scope, uuid);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean isBrowsingBusy() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.isBrowsingBusy(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getBrowsingSupportFeature() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.getBrowsingSupportFeature(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean setStreamMode(int mode) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.setStreamMode(BluetoothBoxes.getID(), mode);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public int getStreamMode() {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return 0;
        }
        try {
            int status = iBtA2DPControlService.getStreamMode(BluetoothBoxes.getID());
            return status;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public boolean setStreamVolume(float volume) {
        IBtA2DPControlService iBtA2DPControlService = this.mService;
        if (iBtA2DPControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtA2DPControlService.setStreamVolume(BluetoothBoxes.getID(), volume);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printError(Exception e) {
        String error = e.getMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(error);
        Log.e(TAG, sb.toString() != null ? error.toString() : CharSequenceUtil.NULL);
    }
}
