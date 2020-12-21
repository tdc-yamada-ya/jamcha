package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.Node;

public class NodeWalker {
    public void walk(Node n, Visitor v) {
        for (var c : n.getChildNodes()) {
            if (!v.visit(c)) {
                break;
            }
            walk(c, v);
        }
    }

    public interface Visitor {
        boolean visit(Node n);
    }
}
