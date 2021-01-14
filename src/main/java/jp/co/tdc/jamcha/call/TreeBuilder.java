package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.call.tree.Node;
import jp.co.tdc.jamcha.call.tree.Tree;
import jp.co.tdc.jamcha.model.*;
import lombok.extern.flogger.Flogger;

import java.util.List;

public class TreeBuilder {
    private static final int MAX_DEPTH = 20;

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
            build(cn, caller, 0);
        }
    }

    NodeData newRootNodeData(Type t, Caller c) {
        return new NodeData(
            t.metadata().qualifiedName(),
            c.metadata().label(),
            c.metadata().methodQualifiedSignature(),
            c.metadata().methodSignature());
    }

    void build(Node<NodeData> n, Caller caller, int depth) {
        if (depth >= MAX_DEPTH) {
            return;
        }

        for (var callee : caller.callees()) {
            build(n, callee, depth);
            buildForSubclasses(n, callee, depth);
        }
    }

    void build(Node<NodeData> n, Callee callee, int depth) {
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

        build(cn, methodQualifiedSignatureCallerMap.get(mqs), depth + 1);
    }

    NodeData newNodeData(Callee c) {
        return new NodeData(
            c.typeQualifiedName(),
            c.methodQualifiedSignature().asCallerLabel(),
            c.methodQualifiedSignature(),
            c.methodSignature());
    }

    void buildForSubclasses(Node<NodeData> n, Callee callee, int depth) {
        if (!subclassMap.containsKey(callee.typeQualifiedName())) {
            return;
        }

        for (var tqn : subclassMap.get(callee.typeQualifiedName())) {
            buildForSubclasses(n, tqn, callee.methodSignature(), depth);
        }
    }

    void buildForSubclasses(Node<NodeData> n, TypeQualifiedName tqn, MethodSignature ms, int depth) {
        if (!typeQualifiedNameMethodSignatureCallerMap.containsKey(tqn, ms)) {
            return;
        }

        var caller = typeQualifiedNameMethodSignatureCallerMap.get(tqn, ms);

        if (n.findAncestor(c -> c.data().methodQualifiedSignature().equals(caller.metadata().methodQualifiedSignature())) != null) {
            return;
        }

        var cn = n.add(newNodeDataForSubclass(tqn, caller));
        build(cn, caller, depth + 1);
    }

    NodeData newNodeDataForSubclass(TypeQualifiedName tqn, Caller c) {
        return new NodeData(
            tqn,
            c.metadata().methodQualifiedSignature().asCallerLabel(),
            c.metadata().methodQualifiedSignature(),
            c.metadata().methodSignature());
    }
}
