package io.sentry;

import io.sentry.util.Objects;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Stack {
    @NotNull
    private final Deque<StackItem> items;
    @NotNull
    private final ILogger logger;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class StackItem {
        @NotNull
        private volatile ISentryClient client;
        private final SentryOptions options;
        @NotNull
        private volatile Scope scope;

        /* JADX INFO: Access modifiers changed from: package-private */
        public StackItem(@NotNull SentryOptions options, @NotNull ISentryClient client, @NotNull Scope scope) {
            this.client = (ISentryClient) Objects.requireNonNull(client, "ISentryClient is required.");
            this.scope = (Scope) Objects.requireNonNull(scope, "Scope is required.");
            this.options = (SentryOptions) Objects.requireNonNull(options, "Options is required");
        }

        StackItem(@NotNull StackItem item) {
            this.options = item.options;
            this.client = item.client;
            this.scope = new Scope(item.scope);
        }

        @NotNull
        public ISentryClient getClient() {
            return this.client;
        }

        public void setClient(@NotNull ISentryClient client) {
            this.client = client;
        }

        @NotNull
        public Scope getScope() {
            return this.scope;
        }

        @NotNull
        public SentryOptions getOptions() {
            return this.options;
        }
    }

    public Stack(@NotNull ILogger logger, @NotNull StackItem rootStackItem) {
        this.items = new LinkedBlockingDeque();
        this.logger = (ILogger) Objects.requireNonNull(logger, "logger is required");
        this.items.push((StackItem) Objects.requireNonNull(rootStackItem, "rootStackItem is required"));
    }

    public Stack(@NotNull Stack stack) {
        this(stack.logger, new StackItem(stack.items.getLast()));
        Iterator<StackItem> iterator = stack.items.descendingIterator();
        if (iterator.hasNext()) {
            iterator.next();
        }
        while (iterator.hasNext()) {
            push(new StackItem(iterator.next()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public StackItem peek() {
        return this.items.peek();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pop() {
        synchronized (this.items) {
            if (this.items.size() != 1) {
                this.items.pop();
            } else {
                this.logger.log(SentryLevel.WARNING, "Attempt to pop the root scope.", new Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void push(@NotNull StackItem stackItem) {
        this.items.push(stackItem);
    }

    int size() {
        return this.items.size();
    }
}
