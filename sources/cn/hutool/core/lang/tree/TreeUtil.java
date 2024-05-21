package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.lang.tree.parser.NodeParser;
import cn.hutool.core.util.ObjectUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class TreeUtil {
    public static Tree<Integer> buildSingle(List<TreeNode<Integer>> list) {
        return buildSingle((List<TreeNode<int>>) list, 0);
    }

    public static List<Tree<Integer>> build(List<TreeNode<Integer>> list) {
        return build((List<TreeNode<int>>) list, 0);
    }

    public static <E> Tree<E> buildSingle(List<TreeNode<E>> list, E parentId) {
        return buildSingle(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, new DefaultNodeParser());
    }

    public static <E> List<Tree<E>> build(List<TreeNode<E>> list, E parentId) {
        return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, new DefaultNodeParser());
    }

    public static <T, E> Tree<E> buildSingle(List<T> list, E parentId, NodeParser<T, E> nodeParser) {
        return buildSingle(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
    }

    public static <T, E> List<Tree<E>> build(List<T> list, E parentId, NodeParser<T, E> nodeParser) {
        return build(list, parentId, TreeNodeConfig.DEFAULT_CONFIG, nodeParser);
    }

    public static <T, E> List<Tree<E>> build(List<T> list, E rootId, TreeNodeConfig treeNodeConfig, NodeParser<T, E> nodeParser) {
        return buildSingle(list, rootId, treeNodeConfig, nodeParser).getChildren();
    }

    public static <T, E> Tree<E> buildSingle(List<T> list, E rootId, TreeNodeConfig treeNodeConfig, NodeParser<T, E> nodeParser) {
        return TreeBuilder.of(rootId, treeNodeConfig).append(list, nodeParser).build();
    }

    public static <E> List<Tree<E>> build(Map<E, Tree<E>> map, E rootId) {
        return buildSingle(map, rootId).getChildren();
    }

    public static <E> Tree<E> buildSingle(Map<E, Tree<E>> map, E rootId) {
        Tree<E> tree = (Tree) IterUtil.getFirstNoneNull(map.values());
        if (tree != null) {
            TreeNodeConfig config = tree.getConfig();
            return TreeBuilder.of(rootId, config).append(map).build();
        }
        return createEmptyNode(rootId);
    }

    public static <T> Tree<T> getNode(Tree<T> node, T id) {
        if (ObjectUtil.equal(id, node.getId())) {
            return node;
        }
        List<Tree<T>> children = node.getChildren();
        if (children == null) {
            return null;
        }
        for (Tree<T> child : children) {
            Tree<T> childNode = child.getNode(id);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }

    public static <T> List<CharSequence> getParentsName(Tree<T> node, boolean includeCurrentNode) {
        List<CharSequence> result = new ArrayList<>();
        if (node == null) {
            return result;
        }
        if (includeCurrentNode) {
            result.add(node.getName());
        }
        for (Tree<T> parent = node.getParent(); parent != null; parent = parent.getParent()) {
            result.add(parent.getName());
        }
        return result;
    }

    public static <E> Tree<E> createEmptyNode(E id) {
        return new Tree().setId((Tree) id);
    }
}
