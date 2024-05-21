package io.sentry;

import androidx.core.app.NotificationCompat;
import io.sentry.cache.EnvelopeCache;
import io.sentry.protocol.SentryTransaction;
import org.jetbrains.annotations.ApiStatus;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public enum SentryItemType {
    Session(EnvelopeCache.PREFIX_CURRENT_SESSION_FILE),
    Event(NotificationCompat.CATEGORY_EVENT),
    UserFeedback("user_report"),
    Attachment("attachment"),
    Transaction("transaction"),
    Unknown("__unknown__");
    
    private final String itemType;

    public static SentryItemType resolve(Object item) {
        if (item instanceof SentryEvent) {
            return Event;
        }
        if (item instanceof SentryTransaction) {
            return Transaction;
        }
        if (item instanceof Session) {
            return Session;
        }
        return Attachment;
    }

    SentryItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return this.itemType;
    }
}
