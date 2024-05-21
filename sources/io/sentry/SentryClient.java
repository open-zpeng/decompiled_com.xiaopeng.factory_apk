package io.sentry;

import io.sentry.Scope;
import io.sentry.SentryOptions;
import io.sentry.Session;
import io.sentry.hints.DiskFlushNotification;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.transport.ITransport;
import io.sentry.util.ApplyScopeUtils;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class SentryClient implements ISentryClient {
    static final String SENTRY_PROTOCOL_VERSION = "7";
    @NotNull
    private final SentryOptions options;
    @Nullable
    private final SecureRandom random;
    @NotNull
    private final ITransport transport;
    @NotNull
    private final SortBreadcrumbsByDate sortBreadcrumbsByDate = new SortBreadcrumbsByDate();
    private boolean enabled = true;

    @Override // io.sentry.ISentryClient
    public boolean isEnabled() {
        return this.enabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryClient(@NotNull SentryOptions options) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "SentryOptions is required.");
        ITransportFactory transportFactory = options.getTransportFactory();
        if (transportFactory instanceof NoOpTransportFactory) {
            transportFactory = new AsyncHttpTransportFactory();
            options.setTransportFactory(transportFactory);
        }
        RequestDetailsResolver requestDetailsResolver = new RequestDetailsResolver(options);
        this.transport = transportFactory.create(options, requestDetailsResolver.resolve());
        this.random = options.getSampleRate() != null ? new SecureRandom() : null;
    }

    private boolean shouldApplyScopeData(@NotNull SentryBaseEvent event, @Nullable Object hint) {
        if (ApplyScopeUtils.shouldApplyScopeData(hint)) {
            return true;
        }
        this.options.getLogger().log(SentryLevel.DEBUG, "Event was cached so not applying scope: %s", event.getEventId());
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00da A[Catch: IOException -> 0x00cb, TRY_LEAVE, TryCatch #0 {IOException -> 0x00cb, blocks: (B:29:0x00bc, B:31:0x00c2, B:35:0x00ce, B:37:0x00da), top: B:41:0x00bc }] */
    /* JADX WARN: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    @Override // io.sentry.ISentryClient
    @org.jetbrains.annotations.NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public io.sentry.protocol.SentryId captureEvent(@org.jetbrains.annotations.NotNull io.sentry.SentryEvent r8, @org.jetbrains.annotations.Nullable io.sentry.Scope r9, @org.jetbrains.annotations.Nullable java.lang.Object r10) {
        /*
            r7 = this;
            java.lang.String r0 = "SentryEvent is required."
            io.sentry.util.Objects.requireNonNull(r8, r0)
            io.sentry.SentryOptions r0 = r7.options
            io.sentry.ILogger r0 = r0.getLogger()
            io.sentry.SentryLevel r1 = io.sentry.SentryLevel.DEBUG
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]
            io.sentry.protocol.SentryId r4 = r8.getEventId()
            r5 = 0
            r3[r5] = r4
            java.lang.String r4 = "Capturing event: %s"
            r0.log(r1, r4, r3)
            boolean r0 = r7.shouldApplyScopeData(r8, r10)
            if (r0 == 0) goto L3a
            io.sentry.SentryEvent r8 = r7.applyScope(r8, r9, r10)
            if (r8 != 0) goto L3a
            io.sentry.SentryOptions r0 = r7.options
            io.sentry.ILogger r0 = r0.getLogger()
            io.sentry.SentryLevel r1 = io.sentry.SentryLevel.DEBUG
            java.lang.Object[] r2 = new java.lang.Object[r5]
            java.lang.String r3 = "Event was dropped by applyScope"
            r0.log(r1, r3, r2)
            io.sentry.protocol.SentryId r0 = io.sentry.protocol.SentryId.EMPTY_ID
            return r0
        L3a:
            io.sentry.SentryOptions r0 = r7.options
            java.util.List r0 = r0.getEventProcessors()
            io.sentry.SentryEvent r8 = r7.processEvent(r8, r10, r0)
            r0 = 0
            if (r8 == 0) goto L67
            io.sentry.Session r0 = r7.updateSessionData(r8, r10, r9)
            boolean r1 = r7.sample()
            if (r1 != 0) goto L67
            io.sentry.SentryOptions r1 = r7.options
            io.sentry.ILogger r1 = r1.getLogger()
            io.sentry.SentryLevel r3 = io.sentry.SentryLevel.DEBUG
            java.lang.Object[] r4 = new java.lang.Object[r2]
            io.sentry.protocol.SentryId r6 = r8.getEventId()
            r4[r5] = r6
            java.lang.String r6 = "Event %s was dropped due to sampling decision."
            r1.log(r3, r6, r4)
            r8 = 0
        L67:
            if (r8 == 0) goto Lac
            java.lang.Throwable r1 = r8.getThrowable()
            if (r1 == 0) goto L97
            io.sentry.SentryOptions r1 = r7.options
            java.lang.Throwable r3 = r8.getThrowable()
            boolean r1 = r1.containsIgnoredExceptionForType(r3)
            if (r1 == 0) goto L97
            io.sentry.SentryOptions r1 = r7.options
            io.sentry.ILogger r1 = r1.getLogger()
            io.sentry.SentryLevel r3 = io.sentry.SentryLevel.DEBUG
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.Throwable r4 = r8.getThrowable()
            java.lang.Class r4 = r4.getClass()
            r2[r5] = r4
            java.lang.String r4 = "Event was dropped as the exception %s is ignored"
            r1.log(r3, r4, r2)
            io.sentry.protocol.SentryId r1 = io.sentry.protocol.SentryId.EMPTY_ID
            return r1
        L97:
            io.sentry.SentryEvent r8 = r7.executeBeforeSend(r8, r10)
            if (r8 != 0) goto Lac
            io.sentry.SentryOptions r1 = r7.options
            io.sentry.ILogger r1 = r1.getLogger()
            io.sentry.SentryLevel r3 = io.sentry.SentryLevel.DEBUG
            java.lang.Object[] r4 = new java.lang.Object[r5]
            java.lang.String r6 = "Event was dropped by beforeSend"
            r1.log(r3, r6, r4)
        Lac:
            io.sentry.protocol.SentryId r1 = io.sentry.protocol.SentryId.EMPTY_ID
            if (r8 == 0) goto Lba
            io.sentry.protocol.SentryId r3 = r8.getEventId()
            if (r3 == 0) goto Lba
            io.sentry.protocol.SentryId r1 = r8.getEventId()
        Lba:
            if (r9 == 0) goto Lcd
            io.sentry.ITransaction r3 = r9.getTransaction()     // Catch: java.io.IOException -> Lcb
            if (r3 == 0) goto Lcd
            io.sentry.ITransaction r3 = r9.getTransaction()     // Catch: java.io.IOException -> Lcb
            io.sentry.TraceState r3 = r3.traceState()     // Catch: java.io.IOException -> Lcb
            goto Lce
        Lcb:
            r3 = move-exception
            goto Le0
        Lcd:
            r3 = 0
        Lce:
            java.util.List r4 = r7.getAttachmentsFromScope(r9)     // Catch: java.io.IOException -> Lcb
            io.sentry.SentryEnvelope r4 = r7.buildEnvelope(r8, r4, r0, r3)     // Catch: java.io.IOException -> Lcb
            if (r4 == 0) goto Ldf
            io.sentry.transport.ITransport r6 = r7.transport     // Catch: java.io.IOException -> Lcb
            r6.send(r4, r10)     // Catch: java.io.IOException -> Lcb
        Ldf:
            goto Lf3
        Le0:
            io.sentry.SentryOptions r4 = r7.options
            io.sentry.ILogger r4 = r4.getLogger()
            io.sentry.SentryLevel r6 = io.sentry.SentryLevel.WARNING
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r2[r5] = r1
            java.lang.String r5 = "Capturing event %s failed."
            r4.log(r6, r3, r5, r2)
            io.sentry.protocol.SentryId r1 = io.sentry.protocol.SentryId.EMPTY_ID
        Lf3:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.sentry.SentryClient.captureEvent(io.sentry.SentryEvent, io.sentry.Scope, java.lang.Object):io.sentry.protocol.SentryId");
    }

    @Nullable
    private List<Attachment> getAttachmentsFromScope(@Nullable Scope scope) {
        if (scope != null) {
            return scope.getAttachments();
        }
        return null;
    }

    @Nullable
    private SentryEnvelope buildEnvelope(@Nullable SentryBaseEvent event, @Nullable List<Attachment> attachments, @Nullable Session session, @Nullable TraceState traceState) throws IOException {
        SentryId sentryId = null;
        List<SentryEnvelopeItem> envelopeItems = new ArrayList<>();
        if (event != null) {
            SentryEnvelopeItem eventItem = SentryEnvelopeItem.fromEvent(this.options.getSerializer(), event);
            envelopeItems.add(eventItem);
            sentryId = event.getEventId();
        }
        if (session != null) {
            SentryEnvelopeItem sessionItem = SentryEnvelopeItem.fromSession(this.options.getSerializer(), session);
            envelopeItems.add(sessionItem);
        }
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                SentryEnvelopeItem attachmentItem = SentryEnvelopeItem.fromAttachment(attachment, this.options.getMaxAttachmentSize());
                envelopeItems.add(attachmentItem);
            }
        }
        if (!envelopeItems.isEmpty()) {
            SentryEnvelopeHeader envelopeHeader = new SentryEnvelopeHeader(sentryId, this.options.getSdkVersion(), traceState);
            return new SentryEnvelope(envelopeHeader, envelopeItems);
        }
        return null;
    }

    @Nullable
    private SentryEvent processEvent(@NotNull SentryEvent event, @Nullable Object hint, @NotNull List<EventProcessor> eventProcessors) {
        Iterator<EventProcessor> it = eventProcessors.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            EventProcessor processor = it.next();
            try {
                event = processor.process(event, hint);
                continue;
            } catch (Throwable e) {
                this.options.getLogger().log(SentryLevel.ERROR, e, "An exception occurred while processing event by processor: %s", processor.getClass().getName());
                continue;
            }
            if (event == null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Event was dropped by a processor: %s", processor.getClass().getName());
                break;
            }
        }
        return event;
    }

    @Nullable
    private SentryTransaction processTransaction(@NotNull SentryTransaction transaction, @Nullable Object hint, @NotNull List<EventProcessor> eventProcessors) {
        Iterator<EventProcessor> it = eventProcessors.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            EventProcessor processor = it.next();
            try {
                transaction = processor.process(transaction, hint);
                continue;
            } catch (Throwable e) {
                this.options.getLogger().log(SentryLevel.ERROR, e, "An exception occurred while processing transaction by processor: %s", processor.getClass().getName());
                continue;
            }
            if (transaction == null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Transaction was dropped by a processor: %s", processor.getClass().getName());
                break;
            }
        }
        return transaction;
    }

    @Override // io.sentry.ISentryClient
    public void captureUserFeedback(@NotNull UserFeedback userFeedback) {
        Objects.requireNonNull(userFeedback, "SentryEvent is required.");
        if (SentryId.EMPTY_ID.equals(userFeedback.getEventId())) {
            this.options.getLogger().log(SentryLevel.WARNING, "Capturing userFeedback without a Sentry Id.", new Object[0]);
            return;
        }
        this.options.getLogger().log(SentryLevel.DEBUG, "Capturing userFeedback: %s", userFeedback.getEventId());
        try {
            SentryEnvelope envelope = buildEnvelope(userFeedback);
            this.transport.send(envelope);
        } catch (IOException e) {
            this.options.getLogger().log(SentryLevel.WARNING, e, "Capturing user feedback %s failed.", userFeedback.getEventId());
        }
    }

    @NotNull
    private SentryEnvelope buildEnvelope(@NotNull UserFeedback userFeedback) {
        List<SentryEnvelopeItem> envelopeItems = new ArrayList<>();
        SentryEnvelopeItem userFeedbackItem = SentryEnvelopeItem.fromUserFeedback(this.options.getSerializer(), userFeedback);
        envelopeItems.add(userFeedbackItem);
        SentryEnvelopeHeader envelopeHeader = new SentryEnvelopeHeader(userFeedback.getEventId(), this.options.getSdkVersion());
        return new SentryEnvelope(envelopeHeader, envelopeItems);
    }

    @TestOnly
    @Nullable
    Session updateSessionData(@NotNull final SentryEvent event, @Nullable final Object hint, @Nullable Scope scope) {
        if (!ApplyScopeUtils.shouldApplyScopeData(hint)) {
            return null;
        }
        if (scope != null) {
            Session clonedSession = scope.withSession(new Scope.IWithSession() { // from class: io.sentry.-$$Lambda$SentryClient$95WLIRfJlDw7w3H0oQsRkrt03kA
                @Override // io.sentry.Scope.IWithSession
                public final void accept(Session session) {
                    SentryClient.this.lambda$updateSessionData$0$SentryClient(event, hint, session);
                }
            });
            return clonedSession;
        }
        this.options.getLogger().log(SentryLevel.INFO, "Scope is null on client.captureEvent", new Object[0]);
        return null;
    }

    public /* synthetic */ void lambda$updateSessionData$0$SentryClient(SentryEvent event, Object hint, Session session) {
        if (session != null) {
            Session.State status = null;
            if (event.isCrashed()) {
                status = Session.State.Crashed;
            }
            boolean crashedOrErrored = false;
            crashedOrErrored = (Session.State.Crashed == status || event.isErrored()) ? true : true;
            String userAgent = null;
            if (event.getRequest() != null && event.getRequest().getHeaders() != null && event.getRequest().getHeaders().containsKey("user-agent")) {
                userAgent = event.getRequest().getHeaders().get("user-agent");
            }
            if (session.update(status, userAgent, crashedOrErrored) && (hint instanceof DiskFlushNotification)) {
                session.end();
                return;
            }
            return;
        }
        this.options.getLogger().log(SentryLevel.INFO, "Session is null on scope.withSession", new Object[0]);
    }

    @Override // io.sentry.ISentryClient
    @ApiStatus.Internal
    public void captureSession(@NotNull Session session, @Nullable Object hint) {
        Objects.requireNonNull(session, "Session is required.");
        if (session.getRelease() == null || session.getRelease().isEmpty()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Sessions can't be captured without setting a release.", new Object[0]);
            return;
        }
        try {
            SentryEnvelope envelope = SentryEnvelope.from(this.options.getSerializer(), session, this.options.getSdkVersion());
            captureEnvelope(envelope, hint);
        } catch (IOException e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Failed to capture session.", e);
        }
    }

    @Override // io.sentry.ISentryClient
    @ApiStatus.Internal
    @NotNull
    public SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        Objects.requireNonNull(envelope, "SentryEnvelope is required.");
        try {
            this.transport.send(envelope, hint);
            SentryId eventId = envelope.getHeader().getEventId();
            if (eventId != null) {
                return eventId;
            }
            return SentryId.EMPTY_ID;
        } catch (IOException e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Failed to capture envelope.", e);
            return SentryId.EMPTY_ID;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // io.sentry.ISentryClient
    @NotNull
    public SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState, @Nullable Scope scope, @Nullable Object hint) {
        Objects.requireNonNull(transaction, "Transaction is required.");
        int i = 1;
        i = 1;
        this.options.getLogger().log(SentryLevel.DEBUG, "Capturing transaction: %s", transaction.getEventId());
        SentryId sentryId = SentryId.EMPTY_ID;
        if (transaction.getEventId() != null) {
            sentryId = transaction.getEventId();
        }
        if (shouldApplyScopeData(transaction, hint)) {
            transaction = (SentryTransaction) applyScope(transaction, scope);
            if (transaction != null && scope != null) {
                transaction = processTransaction(transaction, hint, scope.getEventProcessors());
            }
            if (transaction == null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "Transaction was dropped by applyScope", new Object[0]);
            }
        }
        if (transaction != null) {
            transaction = processTransaction(transaction, hint, this.options.getEventProcessors());
        }
        if (transaction == null) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Transaction was dropped by Event processors.", new Object[0]);
            return SentryId.EMPTY_ID;
        }
        try {
            SentryEnvelope envelope = buildEnvelope(transaction, filterForTransaction(getAttachmentsFromScope(scope)), null, traceState);
            if (envelope != null) {
                this.transport.send(envelope, hint);
            } else {
                SentryId sentryId2 = SentryId.EMPTY_ID;
                sentryId = sentryId2;
                i = sentryId2;
            }
            return sentryId;
        } catch (IOException e) {
            ILogger logger = this.options.getLogger();
            SentryLevel sentryLevel = SentryLevel.WARNING;
            Object[] objArr = new Object[i];
            objArr[0] = sentryId;
            logger.log(sentryLevel, e, "Capturing transaction %s failed.", objArr);
            SentryId sentryId3 = SentryId.EMPTY_ID;
            return sentryId3;
        }
    }

    @Nullable
    private List<Attachment> filterForTransaction(@Nullable List<Attachment> attachments) {
        if (attachments == null) {
            return null;
        }
        List<Attachment> attachmentsToSend = new ArrayList<>();
        for (Attachment attachment : attachments) {
            if (attachment.isAddToTransactions()) {
                attachmentsToSend.add(attachment);
            }
        }
        return attachmentsToSend;
    }

    @Nullable
    private SentryEvent applyScope(@NotNull SentryEvent event, @Nullable Scope scope, @Nullable Object hint) {
        if (scope != null) {
            applyScope(event, scope);
            if (event.getTransaction() == null) {
                event.setTransaction(scope.getTransactionName());
            }
            if (event.getFingerprints() == null) {
                event.setFingerprints(scope.getFingerprint());
            }
            if (scope.getLevel() != null) {
                event.setLevel(scope.getLevel());
            }
            ISpan span = scope.getSpan();
            if (event.getContexts().getTrace() == null && span != null) {
                event.getContexts().setTrace(span.getSpanContext());
            }
            return processEvent(event, hint, scope.getEventProcessors());
        }
        return event;
    }

    @NotNull
    private <T extends SentryBaseEvent> T applyScope(@NotNull T sentryBaseEvent, @Nullable Scope scope) {
        if (scope != null) {
            if (sentryBaseEvent.getRequest() == null) {
                sentryBaseEvent.setRequest(scope.getRequest());
            }
            if (sentryBaseEvent.getUser() == null) {
                sentryBaseEvent.setUser(scope.getUser());
            }
            if (sentryBaseEvent.getTags() == null) {
                sentryBaseEvent.setTags(new HashMap(scope.getTags()));
            } else {
                for (Map.Entry<String, String> item : scope.getTags().entrySet()) {
                    if (!sentryBaseEvent.getTags().containsKey(item.getKey())) {
                        sentryBaseEvent.getTags().put(item.getKey(), item.getValue());
                    }
                }
            }
            if (sentryBaseEvent.getBreadcrumbs() == null) {
                sentryBaseEvent.setBreadcrumbs(new ArrayList(scope.getBreadcrumbs()));
            } else {
                sortBreadcrumbsByDate(sentryBaseEvent, scope.getBreadcrumbs());
            }
            if (sentryBaseEvent.getExtras() == null) {
                sentryBaseEvent.setExtras(new HashMap(scope.getExtras()));
            } else {
                for (Map.Entry<String, Object> item2 : scope.getExtras().entrySet()) {
                    if (!sentryBaseEvent.getExtras().containsKey(item2.getKey())) {
                        sentryBaseEvent.getExtras().put(item2.getKey(), item2.getValue());
                    }
                }
            }
            Contexts contexts = sentryBaseEvent.getContexts();
            for (Map.Entry<String, Object> entry : new Contexts(scope.getContexts()).entrySet()) {
                if (!contexts.containsKey(entry.getKey())) {
                    contexts.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return sentryBaseEvent;
    }

    private void sortBreadcrumbsByDate(@NotNull SentryBaseEvent event, @NotNull Collection<Breadcrumb> breadcrumbs) {
        List<Breadcrumb> sortedBreadcrumbs = event.getBreadcrumbs();
        if (sortedBreadcrumbs != null && !breadcrumbs.isEmpty()) {
            sortedBreadcrumbs.addAll(breadcrumbs);
            Collections.sort(sortedBreadcrumbs, this.sortBreadcrumbsByDate);
        }
    }

    @Nullable
    private SentryEvent executeBeforeSend(@NotNull SentryEvent event, @Nullable Object hint) {
        SentryOptions.BeforeSendCallback beforeSend = this.options.getBeforeSend();
        if (beforeSend != null) {
            try {
                return beforeSend.execute(event, hint);
            } catch (Throwable e) {
                this.options.getLogger().log(SentryLevel.ERROR, "The BeforeSend callback threw an exception. It will be added as breadcrumb and continue.", e);
                Breadcrumb breadcrumb = new Breadcrumb();
                breadcrumb.setMessage("BeforeSend callback failed.");
                breadcrumb.setCategory("SentryClient");
                breadcrumb.setLevel(SentryLevel.ERROR);
                if (e.getMessage() != null) {
                    breadcrumb.setData("sentry:message", e.getMessage());
                }
                event.addBreadcrumb(breadcrumb);
                return event;
            }
        }
        return event;
    }

    @Override // io.sentry.ISentryClient
    public void close() {
        this.options.getLogger().log(SentryLevel.INFO, "Closing SentryClient.", new Object[0]);
        try {
            flush(this.options.getShutdownTimeout());
            this.transport.close();
        } catch (IOException e) {
            this.options.getLogger().log(SentryLevel.WARNING, "Failed to close the connection to the Sentry Server.", e);
        }
        for (EventProcessor eventProcessor : this.options.getEventProcessors()) {
            if (eventProcessor instanceof Closeable) {
                try {
                    ((Closeable) eventProcessor).close();
                } catch (IOException e2) {
                    this.options.getLogger().log(SentryLevel.WARNING, "Failed to close the event processor {}.", eventProcessor, e2);
                }
            }
        }
        this.enabled = false;
    }

    @Override // io.sentry.ISentryClient
    public void flush(long timeoutMillis) {
        this.transport.flush(timeoutMillis);
    }

    private boolean sample() {
        if (this.options.getSampleRate() == null || this.random == null) {
            return true;
        }
        double sampling = this.options.getSampleRate().doubleValue();
        return sampling >= this.random.nextDouble();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class SortBreadcrumbsByDate implements Comparator<Breadcrumb> {
        private SortBreadcrumbsByDate() {
        }

        @Override // java.util.Comparator
        public int compare(@NotNull Breadcrumb b1, @NotNull Breadcrumb b2) {
            return b1.getTimestamp().compareTo(b2.getTimestamp());
        }
    }
}
