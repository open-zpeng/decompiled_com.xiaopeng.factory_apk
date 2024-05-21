package io.sentry.android.core.internal.gestures;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;
import androidx.core.view.ScrollingView;
import io.sentry.util.Objects;
import java.util.ArrayDeque;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class ViewUtils {
    ViewUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static View findTarget(@NotNull View decorView, float x, float y, @NotNull ViewTargetSelector viewTargetSelector) {
        Queue<View> queue = new ArrayDeque<>();
        queue.add(decorView);
        View target = null;
        int[] coordinates = new int[2];
        while (queue.size() > 0) {
            View view = (View) Objects.requireNonNull(queue.poll(), "view is required");
            if (viewTargetSelector.select(view)) {
                target = view;
                if (viewTargetSelector.skipChildren()) {
                    return target;
                }
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    if (touchWithinBounds(child, x, y, coordinates)) {
                        queue.add(child);
                    }
                }
            }
        }
        return target;
    }

    private static boolean touchWithinBounds(@NotNull View view, float x, float y, int[] coords) {
        view.getLocationOnScreen(coords);
        int vx = coords[0];
        int vy = coords[1];
        int w = view.getWidth();
        int h = view.getHeight();
        if (x < vx || x > vx + w || y < vy || y > vy + h) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isViewTappable(@NotNull View view) {
        return view.isClickable() && view.getVisibility() == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isViewScrollable(@NotNull View view, boolean isAndroidXAvailable) {
        return (isJetpackScrollingView(view, isAndroidXAvailable) || AbsListView.class.isAssignableFrom(view.getClass()) || ScrollView.class.isAssignableFrom(view.getClass())) && view.getVisibility() == 0;
    }

    private static boolean isJetpackScrollingView(@NotNull View view, boolean isAndroidXAvailable) {
        if (!isAndroidXAvailable) {
            return false;
        }
        return ScrollingView.class.isAssignableFrom(view.getClass());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getResourceId(@NotNull View view) {
        int viewId = view.getId();
        Resources resources = view.getContext().getResources();
        if (resources == null) {
            return "";
        }
        try {
            String resourceId = resources.getResourceEntryName(viewId);
            return resourceId;
        } catch (Resources.NotFoundException e) {
            String resourceId2 = "0x" + Integer.toString(viewId, 16);
            return resourceId2;
        }
    }
}
