package cn.hutool.core.lang.tree;

import cn.hutool.core.comparator.CompareUtil;
import java.io.Serializable;
/* loaded from: classes.dex */
public interface Node<T> extends Comparable<Node<T>>, Serializable {
    T getId();

    CharSequence getName();

    T getParentId();

    Comparable<?> getWeight();

    Node<T> setId(T t);

    Node<T> setName(CharSequence charSequence);

    Node<T> setParentId(T t);

    Node<T> setWeight(Comparable<?> comparable);

    @Override // java.lang.Comparable
    default int compareTo(Node node) {
        if (node == null) {
            return 1;
        }
        Comparable weight = getWeight();
        Comparable weightOther = node.getWeight();
        return CompareUtil.compare(weight, weightOther);
    }
}
