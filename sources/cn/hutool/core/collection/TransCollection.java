package cn.hutool.core.collection;

import cn.hutool.core.lang.Assert;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class TransCollection<F, T> extends AbstractCollection<T> {
    private final Collection<F> fromCollection;
    private final Function<? super F, ? extends T> function;

    public TransCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
        this.fromCollection = (Collection) Assert.notNull(fromCollection);
        this.function = (Function) Assert.notNull(function);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<T> iterator() {
        return IterUtil.trans(this.fromCollection.iterator(), this.function);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.fromCollection.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.fromCollection.isEmpty();
    }

    @Override // java.lang.Iterable
    public void forEach(final Consumer<? super T> action) {
        Assert.notNull(action);
        this.fromCollection.forEach(new Consumer() { // from class: cn.hutool.core.collection.-$$Lambda$TransCollection$VCI_ae9diEGwgCN9WFAnGJxDvoc
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TransCollection.this.lambda$forEach$0$TransCollection(action, obj);
            }
        });
    }

    public /* synthetic */ void lambda$forEach$0$TransCollection(Consumer action, Object f) {
        action.accept(this.function.apply(f));
    }

    @Override // java.util.Collection
    public boolean removeIf(final Predicate<? super T> filter) {
        Assert.notNull(filter);
        return this.fromCollection.removeIf(new Predicate() { // from class: cn.hutool.core.collection.-$$Lambda$TransCollection$Ajte-kehNQZdDKHPLzDyKC9jOXE
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return TransCollection.this.lambda$removeIf$1$TransCollection(filter, obj);
            }
        });
    }

    public /* synthetic */ boolean lambda$removeIf$1$TransCollection(Predicate filter, Object element) {
        return filter.test(this.function.apply(element));
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<T> spliterator() {
        return SpliteratorUtil.trans(this.fromCollection.spliterator(), this.function);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.fromCollection.size();
    }
}
