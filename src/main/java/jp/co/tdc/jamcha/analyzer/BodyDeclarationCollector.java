package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class BodyDeclarationCollector implements NodeWalker.Visitor {
    private final List<BodyDeclaration<?>> declarations = new ArrayList<>();

    @Override
    public boolean visit(Node n) {
        if (n instanceof TypeDeclaration) {
            return false;
        }

        if (!(n instanceof BodyDeclaration)) {
            return true;
        }

        var d = (BodyDeclaration<?>) n;

        if (d.isConstructorDeclaration() || d.isEnumConstantDeclaration() || d.isFieldDeclaration() || d.isInitializerDeclaration() || d.isMethodDeclaration()) {
            declarations.add(d);
        }

        return true;
    }
}
