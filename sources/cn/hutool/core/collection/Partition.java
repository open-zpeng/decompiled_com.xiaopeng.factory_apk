package cn.hutool.core.collection;

import java.util.AbstractList;
import java.util.List;
/* loaded from: classes.dex */
public class Partition<T> extends AbstractList<List<T>> {
    protected final List<T> list;
    protected final int size;

    public Partition(List<T> list, int size) {
        this.list = list;
        this.size = Math.min(size, list.size());
    }

    @Override // java.util.AbstractList, java.util.List
    public List<T> get(int index) {
        int i = this.size;
        int start = index * i;
        int end = Math.min(i + start, this.list.size());
        return this.list.subList(start, end);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        int size = this.size;
        int total = this.list.size();
        int length = total / size;
        if (total % size > 0) {
            return length + 1;
        }
        return length;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return this.list.isEmpty();
    }
}
