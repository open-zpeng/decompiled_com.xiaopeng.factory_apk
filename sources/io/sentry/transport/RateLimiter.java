package io.sentry.transport;

import androidx.core.app.NotificationCompat;
import com.xiaopeng.commonfunc.utils.ProcessUtil;
import io.sentry.ILogger;
import io.sentry.SentryEnvelope;
import io.sentry.SentryEnvelopeItem;
import io.sentry.SentryLevel;
import io.sentry.cache.EnvelopeCache;
import io.sentry.hints.Retryable;
import io.sentry.hints.SubmissionResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class RateLimiter {
    private static final int HTTP_RETRY_AFTER_DEFAULT_DELAY_MILLIS = 60000;
    @NotNull
    private final ICurrentDateProvider currentDateProvider;
    @NotNull
    private final ILogger logger;
    @NotNull
    private final Map<DataCategory, Date> sentryRetryAfterLimit;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum DataCategory {
        All("__all__"),
        Default("default"),
        Error(ProcessUtil.RESULT_ERROR),
        Session(EnvelopeCache.PREFIX_CURRENT_SESSION_FILE),
        Attachment("attachment"),
        Transaction("transaction"),
        Security("security"),
        Unknown("unknown");
        
        private final String category;

        DataCategory(@NotNull String category) {
            this.category = category;
        }

        public String getCategory() {
            return this.category;
        }
    }

    public RateLimiter(@NotNull ICurrentDateProvider currentDateProvider, @NotNull ILogger logger) {
        this.sentryRetryAfterLimit = new ConcurrentHashMap();
        this.currentDateProvider = currentDateProvider;
        this.logger = logger;
    }

    public RateLimiter(@NotNull ILogger logger) {
        this(CurrentDateProvider.getInstance(), logger);
    }

    @Nullable
    public SentryEnvelope filter(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        List<SentryEnvelopeItem> dropItems = null;
        for (SentryEnvelopeItem item : envelope.getItems()) {
            if (isRetryAfter(item.getHeader().getType().getItemType())) {
                if (dropItems == null) {
                    dropItems = new ArrayList<>();
                }
                dropItems.add(item);
            }
        }
        if (dropItems != null) {
            this.logger.log(SentryLevel.INFO, "%d items will be dropped due rate limiting.", Integer.valueOf(dropItems.size()));
            List<SentryEnvelopeItem> toSend = new ArrayList<>();
            for (SentryEnvelopeItem item2 : envelope.getItems()) {
                if (!dropItems.contains(item2)) {
                    toSend.add(item2);
                }
            }
            if (toSend.isEmpty()) {
                this.logger.log(SentryLevel.INFO, "Envelope discarded due all items rate limited.", new Object[0]);
                markHintWhenSendingFailed(hint, false);
                return null;
            }
            return new SentryEnvelope(envelope.getHeader(), toSend);
        }
        return envelope;
    }

    private static void markHintWhenSendingFailed(@Nullable Object hint, boolean retry) {
        if (hint instanceof SubmissionResult) {
            ((SubmissionResult) hint).setResult(false);
        }
        if (hint instanceof Retryable) {
            ((Retryable) hint).setRetry(retry);
        }
    }

    private boolean isRetryAfter(@NotNull String itemType) {
        Date dateCategory;
        DataCategory dataCategory = getCategoryFromItemType(itemType);
        Date currentDate = new Date(this.currentDateProvider.getCurrentTimeMillis());
        Date dateAllCategories = this.sentryRetryAfterLimit.get(DataCategory.All);
        if (dateAllCategories != null && !currentDate.after(dateAllCategories)) {
            return true;
        }
        if (DataCategory.Unknown.equals(dataCategory) || (dateCategory = this.sentryRetryAfterLimit.get(dataCategory)) == null) {
            return false;
        }
        return true ^ currentDate.after(dateCategory);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @NotNull
    private DataCategory getCategoryFromItemType(@NotNull String itemType) {
        char c;
        switch (itemType.hashCode()) {
            case -1963501277:
                if (itemType.equals("attachment")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 96891546:
                if (itemType.equals(NotificationCompat.CATEGORY_EVENT)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1984987798:
                if (itemType.equals(EnvelopeCache.PREFIX_CURRENT_SESSION_FILE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 2141246174:
                if (itemType.equals("transaction")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c == 3) {
                        return DataCategory.Transaction;
                    }
                    return DataCategory.Unknown;
                }
                return DataCategory.Attachment;
            }
            return DataCategory.Session;
        }
        return DataCategory.Error;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00a8 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateRetryAfterLimits(@org.jetbrains.annotations.Nullable java.lang.String r26, @org.jetbrains.annotations.Nullable java.lang.String r27, int r28) {
        /*
            Method dump skipped, instructions count: 265
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.sentry.transport.RateLimiter.updateRetryAfterLimits(java.lang.String, java.lang.String, int):void");
    }

    private void applyRetryAfterOnlyIfLonger(@NotNull DataCategory dataCategory, @NotNull Date date) {
        Date oldDate = this.sentryRetryAfterLimit.get(dataCategory);
        if (oldDate == null || date.after(oldDate)) {
            this.sentryRetryAfterLimit.put(dataCategory, date);
        }
    }

    private long parseRetryAfterOrDefault(@Nullable String retryAfterHeader) {
        if (retryAfterHeader == null) {
            return 60000L;
        }
        try {
            long retryAfterMillis = (long) (Double.parseDouble(retryAfterHeader) * 1000.0d);
            return retryAfterMillis;
        } catch (NumberFormatException e) {
            return 60000L;
        }
    }
}
