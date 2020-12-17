package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class BodyDeclarationCollector {
    private final List<BodyDeclaration<?>> declarations = new ArrayList<>();

    public void collect(Node n) {
        if (!(n instanceof BodyDeclaration)) {
            return;
        }

        BodyDeclaration d = (BodyDeclaration) n;

        if (d.isConstructorDeclaration() || d.isEnumConstantDeclaration() || d.isFieldDeclaration() || d.isInitializerDeclaration() || d.isMethodDeclaration()) {
            declarations.add(d);
        }
    }
}
