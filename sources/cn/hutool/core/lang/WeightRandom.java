package cn.hutool.core.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import java.io.Serializable;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class WeightRandom<T> implements Serializable {
    private static final long serialVersionUID = -8244697995702786499L;
    private final TreeMap<Double, T> weightMap;

    public static <T> WeightRandom<T> create() {
        return new WeightRandom<>();
    }

    public WeightRandom() {
        this.weightMap = new TreeMap<>();
    }

    public WeightRandom(WeightObj<T> weightObj) {
        this();
        if (weightObj != null) {
            add(weightObj);
        }
    }

    public WeightRandom(Iterable<WeightObj<T>> weightObjs) {
        this();
        if (CollUtil.isNotEmpty(weightObjs)) {
            for (WeightObj<T> weightObj : weightObjs) {
                add(weightObj);
            }
        }
    }

    public WeightRandom(WeightObj<T>[] weightObjs) {
        this();
        for (WeightObj<T> weightObj : weightObjs) {
            add(weightObj);
        }
    }

    public WeightRandom<T> add(T obj, double weight) {
        return add(new WeightObj<>(obj, weight));
    }

    public WeightRandom<T> add(WeightObj<T> weightObj) {
        if (weightObj != null) {
            double weight = weightObj.getWeight();
            if (weightObj.getWeight() > 0.0d) {
                double lastWeight = this.weightMap.size() != 0 ? this.weightMap.lastKey().doubleValue() : 0.0d;
                this.weightMap.put(Double.valueOf(weight + lastWeight), weightObj.getObj());
            }
        }
        return this;
    }

    public WeightRandom<T> clear() {
        TreeMap<Double, T> treeMap = this.weightMap;
        if (treeMap != null) {
            treeMap.clear();
        }
        return this;
    }

    public T next() {
        if (MapUtil.isEmpty(this.weightMap)) {
            return null;
        }
        Random random = RandomUtil.getRandom();
        double randomWeight = this.weightMap.lastKey().doubleValue() * random.nextDouble();
        SortedMap<Double, T> tailMap = this.weightMap.tailMap(Double.valueOf(randomWeight), false);
        return this.weightMap.get(tailMap.firstKey());
    }

    /* loaded from: classes.dex */
    public static class WeightObj<T> {
        private T obj;
        private final double weight;

        public WeightObj(T obj, double weight) {
            this.obj = obj;
            this.weight = weight;
        }

        public T getObj() {
            return this.obj;
        }

        public void setObj(T obj) {
            this.obj = obj;
        }

        public double getWeight() {
            return this.weight;
        }

        public int hashCode() {
            int i = 1 * 31;
            T t = this.obj;
            int result = i + (t == null ? 0 : t.hashCode());
            long temp = Double.doubleToLongBits(this.weight);
            return (result * 31) + ((int) ((temp >>> 32) ^ temp));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            WeightObj<?> other = (WeightObj) obj;
            T t = this.obj;
            if (t == null) {
                if (other.obj != null) {
                    return false;
                }
            } else if (!t.equals(other.obj)) {
                return false;
            }
            if (Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(other.weight)) {
                return true;
            }
            return false;
        }
    }
}
