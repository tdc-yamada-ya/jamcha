package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.call.tree.Node;
import jp.co.tdc.jamcha.model.CallDescendant;
import jp.co.tdc.jamcha.model.MethodQualifiedSignature;
import jp.co.tdc.jamcha.model.Type;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CallExpander {
    private final List<CallDescendant> callDescendants = new ArrayList<>();
    private final Set<CallDescendant.Key> callDescendantKeys = new LinkedHashSet<>();

    public ExpandCallResult expand(List<SourceAnalyzeResult> l) {
        var tb = new TreeBuilder();
        var t = types(l);
        var tree = tb.build(t);
        walk(tree.root());
        return new ExpandCallResult(callDescendants);
    }

    List<Type> types(List<SourceAnalyzeResult> l) {
        return l.stream().flatMap(r -> r.types().stream()).collect(Collectors.toList());
    }

    void walk(Node<NodeData> n) {
        for (var a = n.parent(); a != null && a.parent() != null; a = a.parent()) {
            var cad = newCallDescendant(a, n);

            if (callDescendantKeys.contains(cad.key())) {
                continue;
            }

            callDescendants.add(cad);
            callDescendantKeys.add(cad.key());
        }

        n.childs().forEach(this::walk);
    }

    CallDescendant newCallDescendant(Node<NodeData> ancestor, Node<NodeData> descendant) {
        return new CallDescendant(
            new CallDescendant.Key(
                ancestor.data().typeQualifiedName(),
                ancestor.data().callerLabel(),
                descendant.data().methodQualifiedSignature()
            ),
            newRoute(ancestor, descendant));
    }

    List<MethodQualifiedSignature> newRoute(Node<NodeData> ancestor, Node<NodeData> descendant) {
        return descendant.route(ancestor)
            .stream()
            .map(n -> n.data().methodQualifiedSignature())
            .collect(Collectors.toList());
    }
}
