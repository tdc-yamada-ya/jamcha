package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.call.tree.Node;
import jp.co.tdc.jamcha.call.tree.Tree;
import jp.co.tdc.jamcha.model.*;

import java.util.List;

public class TreeBuilder {
    private final Tree<NodeData> tree;
    private MethodQualifiedSignatureCallerMap methodQualifiedSignatureCallerMap;
    private SubclassMap subclassMap;
    private TypeQualifiedNameMethodSignatureCallerMap typeQualifiedNameMethodSignatureCallerMap;

    public TreeBuilder() {
        tree = new Tree<>(newRootNodeData());
    }

    NodeData newRootNodeData() {
        return new NodeData(
            new TypeQualifiedName(""),
            new CallerLabel(""),
            new MethodQualifiedSignature(""),
            new MethodSignature("")
        );
    }

    public Tree<NodeData> build(List<Type> types) {
        methodQualifiedSignatureCallerMap = new MethodQualifiedSignatureCallerMap(types);
        subclassMap = new SubclassMap(types);
        typeQualifiedNameMethodSignatureCallerMap = new TypeQualifiedNameMethodSignatureCallerMap(types);
        types.forEach(t -> build(tree.root(), t));
        return tree;
    }

    void build(Node<NodeData> n, Type t) {
        for (var caller : t.callers()) {
            var cn = n.add(newRootNodeData(t, caller));
            build(cn, caller);
        }
    }

    NodeData newRootNodeData(Type t, Caller c) {
        return new NodeData(
            t.metadata().qualifiedName(),
            c.metadata().label(),
            c.metadata().methodQualifiedSignature(),
            c.metadata().methodSignature());
    }

    void build(Node<NodeData> n, Caller caller) {
        for (var callee : caller.callees()) {
            build(n, callee);
            buildForSubclasses(n, callee);
        }
    }

    void build(Node<NodeData> n, Callee callee) {
        var mqs = callee.methodQualifiedSignature();

        if (mqs.value().isEmpty()) {
            return;
        }

        if (n.findAncestor(c -> c.data().methodQualifiedSignature().equals(mqs)) != null) {
            return;
        }

        var cn = n.add(newNodeData(callee));

        if (!methodQualifiedSignatureCallerMap.containsKey(mqs)) {
            return;
        }

        build(cn, methodQualifiedSignatureCallerMap.get(mqs));
    }

    NodeData newNodeData(Callee c) {
        return new NodeData(
            c.typeQualifiedName(),
            c.methodQualifiedSignature().asCallerLabel(),
            c.methodQualifiedSignature(),
            c.methodSignature());
    }

    void buildForSubclasses(Node<NodeData> n, Callee callee) {
        if (!subclassMap.containsKey(callee.typeQualifiedName())) {
            return;
        }

        for (var tqn : subclassMap.get(callee.typeQualifiedName())) {
            buildForSubclasses(n, tqn, callee.methodSignature());
        }
    }

    void buildForSubclasses(Node<NodeData> n, TypeQualifiedName tqn, MethodSignature ms) {
        if (!typeQualifiedNameMethodSignatureCallerMap.containsKey(tqn, ms)) {
            return;
        }

        var caller = typeQualifiedNameMethodSignatureCallerMap.get(tqn, ms);
        var cn = n.add(newNodeDataForSubclass(tqn, caller));
        build(cn, caller);
    }

    NodeData newNodeDataForSubclass(TypeQualifiedName tqn, Caller c) {
        return new NodeData(
            tqn,
            c.metadata().methodQualifiedSignature().asCallerLabel(),
            c.metadata().methodQualifiedSignature(),
            c.metadata().methodSignature());
    }
}
