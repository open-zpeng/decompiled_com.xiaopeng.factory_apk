package io.sentry.android.core.internal.gestures;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.android.core.SentryAndroidOptions;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryGestureListener implements GestureDetector.OnGestureListener {
    @NotNull
    private final IHub hub;
    private final boolean isAndroidXAvailable;
    @NotNull
    private final SentryAndroidOptions options;
    private final ScrollState scrollState = new ScrollState();
    @NotNull
    private final WeakReference<Window> windowRef;

    public SentryGestureListener(@NotNull WeakReference<Window> windowRef, @NotNull IHub hub, @NotNull SentryAndroidOptions options, boolean isAndroidXAvailable) {
        this.windowRef = windowRef;
        this.hub = hub;
        this.options = options;
        this.isAndroidXAvailable = isAndroidXAvailable;
    }

    public void onUp(@NotNull MotionEvent motionEvent) {
        View decorView = ensureWindowDecorView("onUp");
        View scrollTarget = (View) this.scrollState.targetRef.get();
        if (decorView == null || scrollTarget == null) {
            return;
        }
        if (this.scrollState.type == null) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Unable to define scroll type. No breadcrumb captured.", new Object[0]);
            return;
        }
        String direction = this.scrollState.calculateDirection(motionEvent);
        addBreadcrumb(scrollTarget, this.scrollState.type, Collections.singletonMap("direction", direction));
        this.scrollState.reset();
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null) {
            return false;
        }
        this.scrollState.reset();
        this.scrollState.startX = motionEvent.getX();
        this.scrollState.startY = motionEvent.getY();
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(@Nullable MotionEvent motionEvent) {
        View decorView = ensureWindowDecorView("onSingleTapUp");
        if (decorView == null || motionEvent == null) {
            return false;
        }
        View target = ViewUtils.findTarget(decorView, motionEvent.getX(), motionEvent.getY(), new ViewTargetSelector() { // from class: io.sentry.android.core.internal.gestures.-$$Lambda$SentryGestureListener$Dlziee0kXxNFNSggpiT5nxSbJ9Y
            @Override // io.sentry.android.core.internal.gestures.ViewTargetSelector
            public final boolean select(View view) {
                boolean isViewTappable;
                isViewTappable = ViewUtils.isViewTappable(view);
                return isViewTappable;
            }
        });
        if (target == null) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Unable to find click target. No breadcrumb captured.", new Object[0]);
            return false;
        }
        addBreadcrumb(target, "click", Collections.emptyMap());
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(@Nullable MotionEvent firstEvent, @Nullable MotionEvent currentEvent, float distX, float distY) {
        View decorView = ensureWindowDecorView("onScroll");
        if (decorView != null && firstEvent != null && this.scrollState.type == null) {
            View target = ViewUtils.findTarget(decorView, firstEvent.getX(), firstEvent.getY(), new ViewTargetSelector() { // from class: io.sentry.android.core.internal.gestures.SentryGestureListener.1
                @Override // io.sentry.android.core.internal.gestures.ViewTargetSelector
                public boolean select(@NotNull View view) {
                    return ViewUtils.isViewScrollable(view, SentryGestureListener.this.isAndroidXAvailable);
                }

                @Override // io.sentry.android.core.internal.gestures.ViewTargetSelector
                public boolean skipChildren() {
                    return true;
                }
            });
            if (target == null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Unable to find scroll target. No breadcrumb captured.", new Object[0]);
                return false;
            }
            this.scrollState.setTarget(target);
            this.scrollState.type = "scroll";
        }
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(@Nullable MotionEvent motionEvent, @Nullable MotionEvent motionEvent1, float v, float v1) {
        this.scrollState.type = "swipe";
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
    }

    private void addBreadcrumb(@NotNull View target, @NotNull String eventType, @NotNull Map<String, Object> additionalData) {
        String className;
        String canonicalName = target.getClass().getCanonicalName();
        if (canonicalName != null) {
            className = canonicalName;
        } else {
            className = target.getClass().getSimpleName();
        }
        this.hub.addBreadcrumb(Breadcrumb.userInteraction(eventType, ViewUtils.getResourceId(target), className, additionalData));
    }

    @Nullable
    private View ensureWindowDecorView(@NotNull String caller) {
        Window window = this.windowRef.get();
        if (window == null) {
            ILogger logger = this.options.getLogger();
            SentryLevel sentryLevel = SentryLevel.DEBUG;
            logger.log(sentryLevel, "Window is null in " + caller + ". No breadcrumb captured.", new Object[0]);
            return null;
        }
        View decorView = window.getDecorView();
        if (decorView == null) {
            ILogger logger2 = this.options.getLogger();
            SentryLevel sentryLevel2 = SentryLevel.DEBUG;
            logger2.log(sentryLevel2, "DecorView is null in " + caller + ". No breadcrumb captured.", new Object[0]);
            return null;
        }
        return decorView;
    }

    /* loaded from: classes2.dex */
    private static final class ScrollState {
        private float startX;
        private float startY;
        private WeakReference<View> targetRef;
        @Nullable
        private String type;

        private ScrollState() {
            this.type = null;
            this.targetRef = new WeakReference<>(null);
            this.startX = 0.0f;
            this.startY = 0.0f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTarget(@NotNull View target) {
            this.targetRef = new WeakReference<>(target);
        }

        /* JADX INFO: Access modifiers changed from: private */
        @NotNull
        public String calculateDirection(MotionEvent endEvent) {
            float diffX = endEvent.getX() - this.startX;
            float diffY = endEvent.getY() - this.startY;
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (diffX > 0.0f) {
                    return AccountConfig.FaceIDRegisterAction.ORIENTATION_RIGHT;
                }
                return AccountConfig.FaceIDRegisterAction.ORIENTATION_LEFT;
            } else if (diffY > 0.0f) {
                return AccountConfig.FaceIDRegisterAction.ORIENTATION_DOWN;
            } else {
                return AccountConfig.FaceIDRegisterAction.ORIENTATION_UP;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.targetRef.clear();
            this.type = null;
            this.startX = 0.0f;
            this.startY = 0.0f;
        }
    }
}
