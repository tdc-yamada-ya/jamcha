package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;
import com.github.javaparser.resolution.types.ResolvedType;
import jp.co.tdc.jamcha.model.CallerSignature;
import lombok.extern.flogger.Flogger;

@Flogger
public class CallerSignatureResolver {
    public CallerSignature resolve(BodyDeclaration d) {
        if (d.isConstructorDeclaration()) {
            return resolveInternal(d.asConstructorDeclaration());
        } else if (d.isEnumConstantDeclaration()) {
            return resolveInternal(d.asEnumConstantDeclaration());
        } else if (d.isFieldDeclaration()) {
            return resolveInternal(d.asFieldDeclaration());
        } else if (d.isInitializerDeclaration()) {
            return resolveInternal(d.asInitializerDeclaration());
        } else if (d.isMethodDeclaration()) {
            return resolveInternal(d.asMethodDeclaration());
        }

        var c = "[unresolved caller]";
        return new CallerSignature(c);
    }

    CallerSignature resolveInternal(ConstructorDeclaration d) {
        try {
            var r = d.resolve();
            var c = r.getQualifiedSignature();
            return new CallerSignature(c);
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("caller (constructor) resolve error");
            return unresolved(d);
        }
    }

    CallerSignature unresolved(ConstructorDeclaration d) {
        var n = d.getNameAsString();
        var c = String.format("[unresolved constructor](%s)", n);
        return new CallerSignature(c);
    }

    CallerSignature resolveInternal(EnumConstantDeclaration d) {
        return nodeWithRangeLabel(d, "[enum constant]");
    }

    CallerSignature resolveInternal(FieldDeclaration d) {
        return nodeWithRangeLabel(d, "[field]");
    }

    CallerSignature resolveInternal(InitializerDeclaration d) {
        return nodeWithRangeLabel(d, "[initializer]");
    }

    CallerSignature resolveInternal(MethodDeclaration d) {
        try {
            var r = d.resolve();
            var c = r.getQualifiedSignature();
            return new CallerSignature(c);
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("caller (method) resolve error");
            return unresolved(d);
        }
    }

    CallerSignature unresolved(MethodDeclaration d) {
        var n = d.getNameAsString();
        var c = String.format("[unresolved method](%s)", n);
        return new CallerSignature(c);
    }

    CallerSignature nodeWithRangeLabel(NodeWithRange<?> r, String prefix) {
        var c = r.getBegin().map(p -> String.format("%s(%d, %d)", prefix, p.line, p.column)).orElse(prefix);
        return new CallerSignature(c);
    }

    CallerSignature resolvedTypeLabel(ResolvedType t) {
        if (t.isReferenceType()) {
            var c = t.asReferenceType().getQualifiedName();
            return new CallerSignature(c);
        }

        var c = "[unresolved type]";
        return new CallerSignature(c);
    }
}
