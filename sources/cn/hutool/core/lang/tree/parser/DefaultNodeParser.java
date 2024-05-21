package cn.hutool.core.lang.tree.parser;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.map.MapUtil;
import java.util.Map;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public class DefaultNodeParser<T> implements NodeParser<TreeNode<T>, T> {
    @Override // cn.hutool.core.lang.tree.parser.NodeParser
    public /* bridge */ /* synthetic */ void parse(Object obj, Tree tree) {
        parse((TreeNode) ((TreeNode) obj), tree);
    }

    public void parse(TreeNode<T> treeNode, final Tree<T> tree) {
        tree.setId((Tree<T>) treeNode.getId());
        tree.setParentId((Tree<T>) treeNode.getParentId());
        tree.setWeight(treeNode.getWeight());
        tree.setName(treeNode.getName());
        Map<String, Object> extra = treeNode.getExtra();
        if (MapUtil.isNotEmpty(extra)) {
            tree.getClass();
            extra.forEach(new BiConsumer() { // from class: cn.hutool.core.lang.tree.parser.-$$Lambda$n2ynwBp0tE9Dpfk4RiN0qDjIIXM
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    Tree.this.putExtra((String) obj, obj2);
                }
            });
        }
    }
}
