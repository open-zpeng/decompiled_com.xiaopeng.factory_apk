package xp.hardware.dab;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import xp.hardware.dab.IDabListener;
import xp.hardware.dab.IRadioServiceBase;
import xp.hardware.dab.common.DabConstants;
/* loaded from: classes2.dex */
public class DabServiceMgr {
    private static final String TAG = "DabServiceMgr";
    private static final String VERSION = "DAB_VERSION:FW BASE V6.09_01";
    private static IRadioServiceBase manager;
    private Context mContext;
    private final Looper mTargetLooper;
    private final List<DabListener> mDabListeners = new ArrayList();
    private DabListenerImpl mDabListenerImpl = null;

    public DabServiceMgr(Context context) {
        UtilLog.i(TAG, VERSION);
        this.mContext = context;
        this.mTargetLooper = context.getMainLooper();
        getService();
    }

    private IRadioServiceBase getService() {
        if (manager == null) {
            IBinder binder = ServiceManager.getService(DabConstants.RADIO_SERVICE_NAME);
            manager = IRadioServiceBase.Stub.asInterface(binder);
        }
        return manager;
    }

    public boolean switchSourceTo(int source) {
        if (getService() != null) {
            try {
                boolean result = getService().switchSourceTo(source);
                UtilLog.i(TAG, "switchSourceTo = " + source + " result = " + result);
                return result;
            } catch (RemoteException e) {
                UtilLog.e(TAG, "switchSourceTo remote exception " + e.toString());
                return false;
            }
        }
        return false;
    }

    public int getLastSource() {
        if (getService() != null) {
            try {
                int lastSource = getService().getLastSource();
                UtilLog.i(TAG, "getLastSource  = " + lastSource);
                return lastSource;
            } catch (RemoteException e) {
                UtilLog.e(TAG, "getLastSource Exception : " + e.toString());
                return -1;
            }
        }
        return -1;
    }

    public boolean tuneToAmFm(int freqId) {
        UtilLog.i(TAG, "tuneToAmFm:" + freqId);
        if (getService() != null) {
            try {
                return getService().tuneToAmFm(freqId);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "tuneToAmFm Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public DabInfo getCurrentDabInfo() {
        if (getService() != null) {
            try {
                DabInfo temp = getService().getCurrentDabInfo();
                UtilLog.i(TAG, "getCurrentDabInfo = " + temp.toString());
                return temp;
            } catch (RemoteException e) {
                UtilLog.e(TAG, "getCurrentDabInfo Exception : " + e.toString());
                return null;
            }
        }
        return null;
    }

    public boolean stepUp() {
        UtilLog.i(TAG, "stepUp");
        if (getService() != null) {
            try {
                return getService().stepUp();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "stepUp Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean stepDown() {
        UtilLog.i(TAG, "stepDown");
        if (getService() != null) {
            try {
                return getService().stepDown();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "stepDown Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean seekUp() {
        UtilLog.i(TAG, "seekUp");
        if (getService() != null) {
            try {
                return getService().seekUp();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "seekUp Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean seekDown() {
        UtilLog.i(TAG, "seekDown");
        if (getService() != null) {
            try {
                return getService().seekDown();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "seekDown Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean componentSeekUp() {
        UtilLog.i(TAG, "componentSeekUp");
        if (getService() != null) {
            try {
                return getService().componentSeekUp();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "componentSeekUp Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean componentSeekDown() {
        UtilLog.i(TAG, "componentSeekDown");
        if (getService() != null) {
            try {
                return getService().componentSeekDown();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "componentSeekDown Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean stopComponentSeek() {
        UtilLog.i(TAG, "stopComponentSeek");
        if (getService() != null) {
            try {
                return getService().stopComponentSeek();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "stopComponentSeek Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean scan() {
        UtilLog.i(TAG, "scan");
        if (getService() != null) {
            try {
                return getService().scan();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "scan Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean updateStationListByType(int type) {
        UtilLog.i(TAG, "updateStationListByType : " + type);
        if (getService() != null) {
            try {
                return getService().updateStationListByType(type);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "updateStationListByType Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public boolean updateStationList() {
        UtilLog.i(TAG, "updateStationList");
        if (getService() != null) {
            try {
                return getService().updateStationList();
            } catch (RemoteException e) {
                UtilLog.e(TAG, "updateStationList Exception : " + e.toString());
                return false;
            }
        }
        return false;
    }

    public void rejectAnnouncement(int announcementType) {
        UtilLog.d(TAG, "rejectAnnouncement : " + announcementType);
        if (getService() != null) {
            try {
                getService().rejectAnnouncement(announcementType);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "rejectAnnouncement Exception : " + e.toString());
            }
        }
    }

    public void allowAnnouncement(int announcementType) {
        UtilLog.d(TAG, "allowAnnouncement : " + announcementType);
        if (getService() != null) {
            try {
                getService().allowAnnouncement(announcementType);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "allowAnnouncement Exception : " + e.toString());
            }
        }
    }

    public void cancelAnnouncement(int announcementType) {
        UtilLog.d(TAG, "cancelAnnouncement : " + announcementType);
        if (getService() != null) {
            try {
                getService().cancelAnnouncement(announcementType);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "cancelAnnouncement Exception : " + e.toString());
            }
        }
    }

    public void tuneto(int freq, int serviceId, int componentId) {
        UtilLog.d(TAG, "tuneto freq: " + freq + ", serviceId : " + serviceId + ", componentId : " + componentId);
        if (getService() != null) {
            try {
                getService().tuneto(freq, serviceId, componentId);
            } catch (Exception e) {
                UtilLog.e(TAG, "tuneto Exception : " + e.toString());
            }
        }
    }

    public void setDabDabLinkOn(boolean on) {
        UtilLog.d(TAG, "setDabDabLinkOn : " + on);
        if (getService() != null) {
            try {
                getService().setDabDabLinkOn(on);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "setDabDabLinkOn Exception : " + e.toString());
            }
        }
    }

    public void setDabFmLinkOn(boolean on) {
        UtilLog.d(TAG, "setDabFmLinkOn : " + on);
        if (getService() != null) {
            try {
                getService().setDabFmLinkOn(on);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "setDabFmLinkOn Exception : " + e.toString());
            }
        }
    }

    public void setTpOn(boolean on) {
        UtilLog.d(TAG, "setTpOn : " + on);
        if (getService() != null) {
            try {
                getService().setTpOn(on);
            } catch (RemoteException e) {
                UtilLog.e(TAG, "setTpOn Exception : " + e.toString());
            }
        }
    }

    public boolean isDabDabLinkOn() {
        if (getService() != null) {
            try {
                boolean on = getService().isDabDabLinkOn();
                UtilLog.d(TAG, "isDabDabLinkOn : " + on);
                return on;
            } catch (Exception ex) {
                UtilLog.e(TAG, "isDabDabLinkOn Exception : " + ex.toString());
                return false;
            }
        }
        return false;
    }

    public boolean isDabFmLinkOn() {
        if (getService() != null) {
            try {
                boolean on = getService().isDabFmLinkOn();
                UtilLog.d(TAG, "isDabFmLinkOn : " + on);
                return on;
            } catch (Exception ex) {
                UtilLog.e(TAG, "isDabFmLinkOn Exception : " + ex.toString());
                return false;
            }
        }
        return false;
    }

    public boolean isTpOn() {
        if (getService() != null) {
            try {
                boolean on = getService().isTpOn();
                UtilLog.d(TAG, "isTpOn : " + on);
                return on;
            } catch (Exception ex) {
                UtilLog.e(TAG, "isTpOn Exception : " + ex.toString());
                return false;
            }
        }
        return false;
    }

    public int getLinkSource() {
        if (getService() != null) {
            try {
                int linkSource = getService().getLinkSource();
                UtilLog.d(TAG, "getLinkSource : " + linkSource);
                return linkSource;
            } catch (Exception ex) {
                UtilLog.e(TAG, "getLinkSource Exception : " + ex.toString());
                return -1;
            }
        }
        return -1;
    }

    public void reset() {
        UtilLog.d(TAG, "reset");
        if (getService() != null) {
            try {
                getService().reset();
            } catch (Exception ex) {
                UtilLog.e(TAG, "reset Exception : " + ex.toString());
            }
        }
    }

    public void getSnrLevel() {
        UtilLog.d(TAG, "getSnrLevel");
        if (getService() != null) {
            try {
                getService().getSnrLevel();
            } catch (Exception ex) {
                UtilLog.e(TAG, "getSnrLevel Exception : " + ex.toString());
            }
        }
    }

    public void registerDabLisenter(DabListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener can not be null");
        }
        ensureDabCallbackImp();
        synchronized (this.mDabListeners) {
            if (this.mDabListeners.contains(listener)) {
                UtilLog.d(TAG, "registerDabLisenter faile, already registed");
                return;
            }
            this.mDabListeners.add(listener);
            UtilLog.d(TAG, "registerDabLisenter");
        }
    }

    public void unregisterDabLisenter(DabListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener can not be null");
        }
        synchronized (this.mDabListeners) {
            Iterator<DabListener> it = this.mDabListeners.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                DabListener l = it.next();
                if (l == listener) {
                    this.mDabListeners.remove(l);
                    break;
                }
            }
            if (this.mDabListeners.size() == 0 && this.mDabListenerImpl != null) {
                try {
                    getService().unregisterDabLisenter(this.mDabListenerImpl);
                    this.mDabListenerImpl = null;
                } catch (RemoteException ex) {
                    UtilLog.d(TAG, "unregisterDabLisenter RemoteException : " + ex.toString());
                }
            }
        }
    }

    private void ensureDabCallbackImp() {
        if (this.mDabListenerImpl != null) {
            return;
        }
        this.mDabListenerImpl = new DabListenerImpl();
        try {
            getService().registerDabLisenter(this.mDabListenerImpl);
        } catch (Exception ex) {
            UtilLog.d(TAG, "registerDabLisenter Exception : " + ex.toString());
        }
    }

    public boolean enterApp() {
        UtilLog.d(TAG, "enterApp...");
        if (getService() != null) {
            try {
                return getService().enterApp();
            } catch (Exception ex) {
                UtilLog.e(TAG, "enterApp Exception : " + ex.toString());
                return false;
            }
        }
        return false;
    }

    public void initDabChip() {
        UtilLog.d(TAG, "initDabChip...");
        if (getService() != null) {
            try {
                getService().initDabChip();
            } catch (Exception ex) {
                UtilLog.e(TAG, "initDabChip Exception : " + ex.toString());
            }
        }
    }

    public boolean exitApp() {
        UtilLog.d(TAG, "exitApp...");
        if (getService() != null) {
            try {
                return getService().exitApp();
            } catch (Exception ex) {
                UtilLog.e(TAG, "exitApp Exception : " + ex.toString());
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DabListenerImpl extends IDabListener.Stub {
        private static final int MSG_ANNOUNCEMENT_ENTER_COMPLETED = 1;
        private static final int MSG_ANNOUNCEMENT_ENTER_REQ = 6;
        private static final int MSG_ANNOUNCEMENT_EXIT = 2;
        private static final int MSG_DAB_CHIP_READY = 9;
        private static final int MSG_PROGRAM_LIST_CHANGED = 8;
        private static final int MSG_RADIO_INFO_CHANGED = 3;
        private static final int MSG_SLIDE_SHOW_CHANGED = 5;
        private static final int MSG_SNR_CHANGED = 7;
        private static final int MSG_STATION_LIST_CHANGED = 4;
        private final Handler mHandler;

        DabListenerImpl() {
            this.mHandler = new Handler(DabServiceMgr.this.mTargetLooper) { // from class: xp.hardware.dab.DabServiceMgr.DabListenerImpl.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    UtilLog.i(DabServiceMgr.TAG, "handleMessage what = " + msg.what);
                    switch (msg.what) {
                        case 1:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l : DabServiceMgr.this.mDabListeners) {
                                    l.onAnnouncementEnterCompleted(msg.arg1);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 1 end");
                            return;
                        case 2:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l2 : DabServiceMgr.this.mDabListeners) {
                                    l2.onAnnouncementExit(msg.arg1);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 2 end");
                            return;
                        case 3:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l3 : DabServiceMgr.this.mDabListeners) {
                                    l3.onDabInfoChanged((DabInfo) msg.obj);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 3 end");
                            return;
                        case 4:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l4 : DabServiceMgr.this.mDabListeners) {
                                    l4.onStationListChanged(msg.arg1, (ArrayList) msg.obj);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 4 end");
                            return;
                        case 5:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l5 : DabServiceMgr.this.mDabListeners) {
                                    l5.onSlideShowChanged(msg.arg1, (String) msg.obj);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 5 end");
                            return;
                        case 6:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l6 : DabServiceMgr.this.mDabListeners) {
                                    l6.onAnnouncementEnterRequest(msg.arg1);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 6 end");
                            return;
                        case 7:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l7 : DabServiceMgr.this.mDabListeners) {
                                    l7.onSnrChanged(msg.arg1, msg.arg2);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 7 end");
                            return;
                        case 8:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l8 : DabServiceMgr.this.mDabListeners) {
                                    l8.onProgramListChanged(msg.arg1, (ArrayList) msg.obj);
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 8 end");
                            return;
                        case 9:
                            synchronized (DabServiceMgr.this.mDabListeners) {
                                for (DabListener l9 : DabServiceMgr.this.mDabListeners) {
                                    l9.onDabChipReady();
                                }
                            }
                            UtilLog.i(DabServiceMgr.TAG, "handleMessage 9 end");
                            return;
                        default:
                            return;
                    }
                }
            };
        }

        @Override // xp.hardware.dab.IDabListener
        public void onAnnouncementEnterRequest(int type) throws RemoteException {
            UtilLog.d(DabServiceMgr.TAG, "onAnnouncementEnterRequest type : " + type);
            Message.obtain(this.mHandler, 6, type, 0, null).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onAnnouncementEnterCompleted(int type) throws RemoteException {
            UtilLog.d(DabServiceMgr.TAG, "onAnnouncementEnterCompleted type : " + type);
            Message.obtain(this.mHandler, 1, type, 0, null).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onAnnouncementExit(int type) {
            UtilLog.d(DabServiceMgr.TAG, "onAnnouncementExit type : " + type);
            Message.obtain(this.mHandler, 2, type, 0, null).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onDabInfoChanged(DabInfo dabInfo) throws RemoteException {
            UtilLog.d(DabServiceMgr.TAG, "onDabInfoChanged dabInfo : " + dabInfo.toString());
            Message.obtain(this.mHandler, 3, 0, 0, dabInfo).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onStationListChanged(int source, List<DabInfo> arrayList) throws RemoteException {
            Message.obtain(this.mHandler, 4, source, 0, arrayList).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onProgramListChanged(int source, List<DabInfo> arrayList) throws RemoteException {
            Message.obtain(this.mHandler, 8, source, 0, arrayList).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onSlideShowChanged(int source, String content) {
            UtilLog.d(DabServiceMgr.TAG, "onSlideShowChanged content : " + content);
            Message.obtain(this.mHandler, 5, source, 0, content).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onSnrChanged(int source, int snr) throws RemoteException {
            UtilLog.d(DabServiceMgr.TAG, "onSnrChanged snr : " + snr);
            Message.obtain(this.mHandler, 7, source, snr, null).sendToTarget();
        }

        @Override // xp.hardware.dab.IDabListener
        public void onDabChipReady() throws RemoteException {
            UtilLog.d(DabServiceMgr.TAG, "onDabChipReady");
            Message.obtain(this.mHandler, 9, 0, 0, null).sendToTarget();
        }
    }
}
