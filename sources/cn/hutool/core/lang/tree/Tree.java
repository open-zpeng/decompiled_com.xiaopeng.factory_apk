package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
/* loaded from: classes.dex */
public class Tree<T> extends LinkedHashMap<String, Object> implements Node<T> {
    private static final long serialVersionUID = 1;
    private Tree<T> parent;
    private final TreeNodeConfig treeNodeConfig;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.hutool.core.lang.tree.Node
    public /* bridge */ /* synthetic */ Node setId(Object obj) {
        return setId((Tree<T>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.hutool.core.lang.tree.Node
    public /* bridge */ /* synthetic */ Node setParentId(Object obj) {
        return setParentId((Tree<T>) obj);
    }

    @Override // cn.hutool.core.lang.tree.Node
    public /* bridge */ /* synthetic */ Node setWeight(Comparable comparable) {
        return setWeight((Comparable<?>) comparable);
    }

    public Tree() {
        this(null);
    }

    public Tree(TreeNodeConfig treeNodeConfig) {
        this.treeNodeConfig = (TreeNodeConfig) ObjectUtil.defaultIfNull(treeNodeConfig, TreeNodeConfig.DEFAULT_CONFIG);
    }

    public TreeNodeConfig getConfig() {
        return this.treeNodeConfig;
    }

    public Tree<T> getParent() {
        return this.parent;
    }

    public Tree<T> getNode(T id) {
        return TreeUtil.getNode(this, id);
    }

    public List<CharSequence> getParentsName(T id, boolean includeCurrentNode) {
        return TreeUtil.getParentsName(getNode(id), includeCurrentNode);
    }

    public List<CharSequence> getParentsName(boolean includeCurrentNode) {
        return TreeUtil.getParentsName(this, includeCurrentNode);
    }

    public Tree<T> setParent(Tree<T> parent) {
        this.parent = parent;
        if (parent != null) {
            setParentId((Tree<T>) parent.getId());
        }
        return this;
    }

    @Override // cn.hutool.core.lang.tree.Node
    public T getId() {
        return (T) get(this.treeNodeConfig.getIdKey());
    }

    @Override // cn.hutool.core.lang.tree.Node
    public Tree<T> setId(T id) {
        put(this.treeNodeConfig.getIdKey(), id);
        return this;
    }

    @Override // cn.hutool.core.lang.tree.Node
    public T getParentId() {
        return (T) get(this.treeNodeConfig.getParentIdKey());
    }

    @Override // cn.hutool.core.lang.tree.Node
    public Tree<T> setParentId(T parentId) {
        put(this.treeNodeConfig.getParentIdKey(), parentId);
        return this;
    }

    @Override // cn.hutool.core.lang.tree.Node
    public CharSequence getName() {
        return (CharSequence) get(this.treeNodeConfig.getNameKey());
    }

    @Override // cn.hutool.core.lang.tree.Node
    public Tree<T> setName(CharSequence name) {
        put(this.treeNodeConfig.getNameKey(), name);
        return this;
    }

    @Override // cn.hutool.core.lang.tree.Node
    public Comparable<?> getWeight() {
        return (Comparable) get(this.treeNodeConfig.getWeightKey());
    }

    @Override // cn.hutool.core.lang.tree.Node
    public Tree<T> setWeight(Comparable<?> weight) {
        put(this.treeNodeConfig.getWeightKey(), weight);
        return this;
    }

    public List<Tree<T>> getChildren() {
        return (List) get(this.treeNodeConfig.getChildrenKey());
    }

    public Tree<T> setChildren(List<Tree<T>> children) {
        put(this.treeNodeConfig.getChildrenKey(), children);
        return this;
    }

    @SafeVarargs
    public final Tree<T> addChildren(Tree<T>... children) {
        if (ArrayUtil.isNotEmpty((Object[]) children)) {
            List<Tree<T>> childrenList = getChildren();
            if (childrenList == null) {
                childrenList = new ArrayList();
                setChildren(childrenList);
            }
            for (Tree<T> child : children) {
                child.setParent(this);
                childrenList.add(child);
            }
        }
        return this;
    }

    public void putExtra(String key, Object value) {
        Assert.notEmpty(key, "Key must be not empty !", new Object[0]);
        put(key, value);
    }

    @Override // java.util.AbstractMap
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        printTree(this, new PrintWriter(stringWriter), 0);
        return stringWriter.toString();
    }

    private static void printTree(Tree<?> tree, PrintWriter writer, int intent) {
        writer.println(StrUtil.format("{}{}[{}]", StrUtil.repeat(' ', intent), tree.getName(), tree.getId()));
        writer.flush();
        List<? extends Tree<?>> children = tree.getChildren();
        if (CollUtil.isNotEmpty((Collection<?>) children)) {
            for (Tree<?> child : children) {
                printTree(child, writer, intent + 2);
            }
        }
    }
}
