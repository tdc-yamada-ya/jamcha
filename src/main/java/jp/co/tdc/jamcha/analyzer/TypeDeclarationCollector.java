package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class TypeDeclarationCollector implements NodeWalker.Visitor {
    private final List<TypeDeclaration<?>> declarations = new ArrayList<>();

    @Override
    public boolean visit(Node n) {
        if (n instanceof TypeDeclaration) {
            declarations.add((TypeDeclaration<?>) n);
        }
        return true;
    }
}
