package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class MethodCallExprCollector implements NodeWalker.Visitor {
    private final List<MethodCallExpr> exprs = new ArrayList<>();

    @Override
    public boolean visit(Node n) {
        if (n instanceof TypeDeclaration) {
            return false;
        }

        if (n instanceof MethodCallExpr) {
            exprs.add((MethodCallExpr) n);
        }

        return true;
    }
}
