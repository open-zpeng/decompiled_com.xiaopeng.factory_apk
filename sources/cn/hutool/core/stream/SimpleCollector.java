package cn.hutool.core.stream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
/* loaded from: classes.dex */
public class SimpleCollector<T, A, R> implements Collector<T, A, R> {
    private final BiConsumer<A, T> accumulator;
    private final Set<Collector.Characteristics> characteristics;
    private final BinaryOperator<A> combiner;
    private final Function<A, R> finisher;
    private final Supplier<A> supplier;

    public SimpleCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher, Set<Collector.Characteristics> characteristics) {
        this.supplier = supplier;
        this.accumulator = accumulator;
        this.combiner = combiner;
        this.finisher = finisher;
        this.characteristics = characteristics;
    }

    public SimpleCollector(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Set<Collector.Characteristics> characteristics) {
        this(supplier, accumulator, combiner, new Function() { // from class: cn.hutool.core.stream.-$$Lambda$SimpleCollector$4IZ9SvPBFLrYnoc-jtW9BMI4cRM
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return SimpleCollector.lambda$new$0(obj);
            }
        }, characteristics);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object lambda$new$0(Object i) {
        return i;
    }

    @Override // java.util.stream.Collector
    public BiConsumer<A, T> accumulator() {
        return this.accumulator;
    }

    @Override // java.util.stream.Collector
    public Supplier<A> supplier() {
        return this.supplier;
    }

    @Override // java.util.stream.Collector
    public BinaryOperator<A> combiner() {
        return this.combiner;
    }

    @Override // java.util.stream.Collector
    public Function<A, R> finisher() {
        return this.finisher;
    }

    @Override // java.util.stream.Collector
    public Set<Collector.Characteristics> characteristics() {
        return this.characteristics;
    }
}
