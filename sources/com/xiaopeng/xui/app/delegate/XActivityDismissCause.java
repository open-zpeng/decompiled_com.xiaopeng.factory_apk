package com.xiaopeng.xui.app.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.xiaopeng.xui.app.delegate.XActivityDismissCause;
import com.xiaopeng.xui.utils.XLogUtils;
/* loaded from: classes2.dex */
class XActivityDismissCause {
    private static final String TAG = "XActivityDismissCause";

    /* loaded from: classes2.dex */
    interface BackCause extends XActivityLifecycle {
        void onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface CallBack {
        void onTriggerDismiss(int i);
    }

    /* loaded from: classes2.dex */
    interface OnPauseCause extends XActivityLifecycle {
        void ignoreDismissOneshot();
    }

    /* loaded from: classes2.dex */
    interface OutSideCause extends XActivityLifecycle {
        boolean onTouchEvent(MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    interface SpeedTimeCause extends XActivityLifecycle {
        void dispatchTouchEvent();

        void dispatchUserEvent();
    }

    XActivityDismissCause() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BackCause createBack(Activity activity, CallBack callBack) {
        return new BackImpl(activity, callBack);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OnPauseCause createOnPause(Activity activity, CallBack callBack) {
        return new OnPauseImpl(activity, callBack);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SpeedTimeCause createSpeedTimeOut(Activity activity, CallBack callBack) {
        return new SpeedTimeOutImpl(activity, callBack);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OutSideCause createOutSide(Activity activity, CallBack callBack) {
        return new OutSideImpl(activity, callBack);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class BaseCause {
        Activity mActivity;
        private CallBack mCallBack;

        BaseCause(Activity activity, CallBack callBack) {
            this.mActivity = activity;
            this.mCallBack = callBack;
        }

        void dismiss(int cause) {
            CallBack callBack = this.mCallBack;
            if (callBack != null) {
                callBack.onTriggerDismiss(cause);
            }
        }
    }

    /* loaded from: classes2.dex */
    private static class BackImpl extends BaseCause implements BackCause {
        BackImpl(Activity activity, CallBack callBack) {
            super(activity, callBack);
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityDismissCause.BackCause
        public void onBackPressed() {
            XLogUtils.i(XActivityDismissCause.TAG, "onBackPressed");
            dismiss(1);
        }
    }

    /* loaded from: classes2.dex */
    private static class OnPauseImpl extends BaseCause implements OnPauseCause {
        private boolean mDismissOnPause;

        OnPauseImpl(Activity activity, CallBack callBack) {
            super(activity, callBack);
            this.mDismissOnPause = true;
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityLifecycle
        public void onRecreate() {
            XLogUtils.i(XActivityDismissCause.TAG, "onRecreate");
            this.mDismissOnPause = false;
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityLifecycle
        public void onPause() {
            boolean temp = this.mDismissOnPause;
            if (this.mDismissOnPause) {
                dismiss(2);
            }
            this.mDismissOnPause = true;
            XLogUtils.i(XActivityDismissCause.TAG, "onPause : last " + temp);
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityDismissCause.OnPauseCause
        public void ignoreDismissOneshot() {
            XLogUtils.i(XActivityDismissCause.TAG, "ignoreDismissOneshot mDismissOnPause false");
            this.mDismissOnPause = false;
        }
    }

    /* loaded from: classes2.dex */
    private static class OutSideImpl extends BaseCause implements OutSideCause {
        private boolean mCloseOnTouchOutside;

        OutSideImpl(Activity activity, CallBack callBack) {
            super(activity, callBack);
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityLifecycle
        public void onCreate(Bundle savedInstanceState) {
            TypedArray array = this.mActivity.getTheme().obtainStyledAttributes(new int[]{16843611});
            this.mCloseOnTouchOutside = array.getBoolean(0, false);
            array.recycle();
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityDismissCause.OutSideCause
        public boolean onTouchEvent(MotionEvent event) {
            if (shouldCloseOnTouch(event)) {
                dismiss(3);
                return true;
            }
            return false;
        }

        private boolean isOutOfBounds(Context context, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
            View decorView = this.mActivity.getWindow().getDecorView();
            return x < (-slop) || y < (-slop) || x > decorView.getWidth() + slop || y > decorView.getHeight() + slop;
        }

        private boolean shouldCloseOnTouch(MotionEvent event) {
            boolean isOutside = (event.getAction() == 0 && isOutOfBounds(this.mActivity, event)) || event.getAction() == 4;
            return this.mCloseOnTouchOutside && isOutside;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SpeedTimeOutImpl extends BaseCause implements SpeedTimeCause {
        private static final long PANEL_DISMISS_DELAY = 30000;
        private final Runnable mAutoDismissRunnable;
        final Handler mHandler;
        private float mLastSpeed;
        private ContentObserver mPanelObserver;
        private static final String KEY_PANEL_SPEED = "key_panel_car_speed";
        private static final Uri URI_PANEL_SPEED = Settings.System.getUriFor(KEY_PANEL_SPEED);

        public /* synthetic */ void lambda$new$0$XActivityDismissCause$SpeedTimeOutImpl() {
            float speed = getSpeed();
            if (speed > 0.0f) {
                dismiss(4);
            }
        }

        SpeedTimeOutImpl(Activity activity, CallBack callBack) {
            super(activity, callBack);
            this.mLastSpeed = 0.0f;
            this.mHandler = new Handler();
            this.mAutoDismissRunnable = new Runnable() { // from class: com.xiaopeng.xui.app.delegate.-$$Lambda$XActivityDismissCause$SpeedTimeOutImpl$P2r21C1d2zv3UzVrSyOkHTv02rw
                @Override // java.lang.Runnable
                public final void run() {
                    XActivityDismissCause.SpeedTimeOutImpl.this.lambda$new$0$XActivityDismissCause$SpeedTimeOutImpl();
                }
            };
            this.mPanelObserver = new ContentObserver(this.mHandler) { // from class: com.xiaopeng.xui.app.delegate.XActivityDismissCause.SpeedTimeOutImpl.1
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    SpeedTimeOutImpl.this.onPanelSpeedChanged();
                }
            };
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityLifecycle
        public void onResume() {
            postAutoDismissRunnable(false);
            registerPanelObserver(this.mActivity.getApplicationContext());
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityLifecycle
        public void onPause() {
            unregisterPanelObserver(this.mActivity.getApplicationContext());
            this.mHandler.removeCallbacks(this.mAutoDismissRunnable);
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityLifecycle
        public void onDestroy() {
            this.mHandler.removeCallbacks(this.mAutoDismissRunnable);
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityDismissCause.SpeedTimeCause
        public void dispatchUserEvent() {
            XLogUtils.i(XActivityDismissCause.TAG, "dispatchUserEvent");
            postAutoDismissRunnable(false);
        }

        @Override // com.xiaopeng.xui.app.delegate.XActivityDismissCause.SpeedTimeCause
        public void dispatchTouchEvent() {
            postAutoDismissRunnable(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onPanelSpeedChanged() {
            postAutoDismissRunnable(true);
        }

        private void postAutoDismissRunnable(boolean fromSpeed) {
            float speed = getSpeed();
            boolean changed = true;
            if (fromSpeed) {
                changed = this.mLastSpeed * speed <= 0.0f;
            }
            if (speed <= 0.0f) {
                this.mHandler.removeCallbacks(this.mAutoDismissRunnable);
            } else if (changed) {
                this.mHandler.removeCallbacks(this.mAutoDismissRunnable);
                this.mHandler.postDelayed(this.mAutoDismissRunnable, PANEL_DISMISS_DELAY);
            }
            this.mLastSpeed = speed;
        }

        private float getSpeed() {
            return getSpeed(this.mActivity);
        }

        static float getSpeed(Context context) {
            try {
                return Settings.System.getFloat(context.getContentResolver(), KEY_PANEL_SPEED, 0.0f);
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0f;
            }
        }

        private void registerPanelObserver(Context context) {
            try {
                context.getContentResolver().registerContentObserver(URI_PANEL_SPEED, true, this.mPanelObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void unregisterPanelObserver(Context context) {
            try {
                context.getContentResolver().unregisterContentObserver(this.mPanelObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
