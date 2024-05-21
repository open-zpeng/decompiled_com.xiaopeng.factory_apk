package cn.hutool.core.lang.tree;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class TreeBuilder<E> implements Builder<Tree<E>> {
    private static final long serialVersionUID = 1;
    private final Map<E, Tree<E>> idTreeMap;
    private boolean isBuild;
    private final Tree<E> root;

    public static <T> TreeBuilder<T> of(T rootId) {
        return of(rootId, null);
    }

    public static <T> TreeBuilder<T> of(T rootId, TreeNodeConfig config) {
        return new TreeBuilder<>(rootId, config);
    }

    public TreeBuilder(E rootId, TreeNodeConfig config) {
        this.root = new Tree<>(config);
        this.root.setId((Tree<E>) rootId);
        this.idTreeMap = new HashMap();
    }

    public TreeBuilder<E> append(Map<E, Tree<E>> map) {
        checkBuilt();
        Assert.isFalse(this.isBuild, "Current tree has been built.", new Object[0]);
        this.idTreeMap.putAll(map);
        return this;
    }

    public TreeBuilder<E> append(Iterable<Tree<E>> trees) {
        checkBuilt();
        for (Tree<E> tree : trees) {
            this.idTreeMap.put(tree.getId(), tree);
        }
        return this;
    }

    public <T> TreeBuilder<E> append(List<T> list, NodeParser<T, E> nodeParser) {
        checkBuilt();
        TreeNodeConfig config = this.root.getConfig();
        Map<E, Tree<E>> map = new LinkedHashMap<>(list.size(), 1.0f);
        for (T t : list) {
            Tree<E> node = new Tree<>(config);
            nodeParser.parse(t, node);
            map.put(node.getId(), node);
        }
        return append(map);
    }

    public TreeBuilder<E> reset() {
        this.idTreeMap.clear();
        this.root.setChildren(null);
        this.isBuild = false;
        return this;
    }

    @Override // cn.hutool.core.builder.Builder
    public Tree<E> build() {
        checkBuilt();
        buildFromMap();
        cutTree();
        this.isBuild = true;
        this.idTreeMap.clear();
        return this.root;
    }

    public List<Tree<E>> buildList() {
        if (this.isBuild) {
            return this.root.getChildren();
        }
        return build().getChildren();
    }

    private void buildFromMap() {
        if (MapUtil.isEmpty(this.idTreeMap)) {
            return;
        }
        Map<E, Tree<E>> eTreeMap = MapUtil.sortByValue(this.idTreeMap, false);
        List<Tree<E>> rootTreeList = CollUtil.newArrayList(new Tree[0]);
        for (Tree<E> node : eTreeMap.values()) {
            if (node != null) {
                E parentId = node.getParentId();
                if (ObjectUtil.equals(this.root.getId(), parentId)) {
                    this.root.addChildren(node);
                    rootTreeList.add(node);
                } else {
                    Tree<E> parentNode = eTreeMap.get(parentId);
                    if (parentNode != null) {
                        parentNode.addChildren(node);
                    }
                }
            }
        }
    }

    private void cutTree() {
        TreeNodeConfig config = this.root.getConfig();
        Integer deep = config.getDeep();
        if (deep == null || deep.intValue() < 0) {
            return;
        }
        cutTree(this.root, 0, deep.intValue());
    }

    private void cutTree(Tree<E> tree, int currentDepp, int maxDeep) {
        if (tree == null) {
            return;
        }
        if (currentDepp == maxDeep) {
            tree.setChildren(null);
            return;
        }
        List<Tree<E>> children = tree.getChildren();
        if (CollUtil.isNotEmpty((Collection<?>) children)) {
            for (Tree<E> child : children) {
                cutTree(child, currentDepp + 1, maxDeep);
            }
        }
    }

    private void checkBuilt() {
        Assert.isFalse(this.isBuild, "Current tree has been built.", new Object[0]);
    }
}
