package cn.hutool.core.collection;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
/* loaded from: classes.dex */
public class TransSpliterator<F, T> implements Spliterator<T> {
    private final Spliterator<F> fromSpliterator;
    private final Function<? super F, ? extends T> function;

    public TransSpliterator(Spliterator<F> fromSpliterator, Function<? super F, ? extends T> function) {
        this.fromSpliterator = fromSpliterator;
        this.function = function;
    }

    @Override // java.util.Spliterator
    public boolean tryAdvance(final Consumer<? super T> action) {
        return this.fromSpliterator.tryAdvance(new Consumer() { // from class: cn.hutool.core.collection.-$$Lambda$TransSpliterator$UkSMztMKoMLfKL1msqKg0r0MSfs
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TransSpliterator.this.lambda$tryAdvance$0$TransSpliterator(action, obj);
            }
        });
    }

    public /* synthetic */ void lambda$tryAdvance$0$TransSpliterator(Consumer action, Object fromElement) {
        action.accept(this.function.apply(fromElement));
    }

    @Override // java.util.Spliterator
    public void forEachRemaining(final Consumer<? super T> action) {
        this.fromSpliterator.forEachRemaining(new Consumer() { // from class: cn.hutool.core.collection.-$$Lambda$TransSpliterator$4dBR9D2sjqGsZfCJmudqUSZuXJs
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TransSpliterator.this.lambda$forEachRemaining$1$TransSpliterator(action, obj);
            }
        });
    }

    public /* synthetic */ void lambda$forEachRemaining$1$TransSpliterator(Consumer action, Object fromElement) {
        action.accept(this.function.apply(fromElement));
    }

    @Override // java.util.Spliterator
    public Spliterator<T> trySplit() {
        Spliterator<F> fromSplit = this.fromSpliterator.trySplit();
        if (fromSplit != null) {
            return new TransSpliterator(fromSplit, this.function);
        }
        return null;
    }

    @Override // java.util.Spliterator
    public long estimateSize() {
        return this.fromSpliterator.estimateSize();
    }

    @Override // java.util.Spliterator
    public int characteristics() {
        return this.fromSpliterator.characteristics() & (-262);
    }
}
