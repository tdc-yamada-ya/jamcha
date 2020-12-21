package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import jp.co.tdc.jamcha.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.flogger.Flogger;

import java.util.List;
import java.util.stream.Collectors;

@Flogger
public class CallerMetadataResolver {
    private final ResolveMetadataHelper resolveMetadataHelper = new ResolveMetadataHelper();

    public CallerMetadata resolve(BodyDeclaration<?> d) {
        var nr = resolveNameRecord(d);
        var b = resolveMetadataHelper.begin(d);
        var a = callerAnnotationExpr(d);
        return new CallerMetadata(nr.label(), nr.methodQualifiedSignature(), nr.methodSignature(), b, a);
    }

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class NameRecord {
        private final CallerLabel label;
        private final MethodQualifiedSignature methodQualifiedSignature;
        private final MethodSignature methodSignature;
    }

    NameRecord resolveNameRecord(BodyDeclaration<?> d) {
        if (d.isConstructorDeclaration()) { return resolveNameRecordInternal(d.asConstructorDeclaration()); }
        if (d.isEnumConstantDeclaration()) { return resolveNameRecordInternal(d.asEnumConstantDeclaration()); }
        if (d.isFieldDeclaration()) { return resolveNameRecordInternal(d.asFieldDeclaration()); }
        if (d.isInitializerDeclaration()) { return resolveNameRecordInternal(); }
        if (d.isMethodDeclaration()) { return resolveNameRecordInternal(d.asMethodDeclaration()); }
        return new NameRecord(new CallerLabel("Unknown"), new MethodQualifiedSignature(""), new MethodSignature(""));
    }

    NameRecord resolveNameRecordInternal(ConstructorDeclaration d) {
        try {
            return resolveNameRecordWithResolved(d.resolve());
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("caller constructor resolve error");
            return resolveNameRecordWithUnresolved(d);
        }
    }

    NameRecord resolveNameRecordWithResolved(ResolvedConstructorDeclaration d) {
        var l = new CallerLabel(d.getQualifiedSignature());
        var mqs = new MethodQualifiedSignature(d.getQualifiedSignature());
        var ms = new MethodSignature(d.getSignature());
        return new NameRecord(l, mqs, ms);
    }

    NameRecord resolveNameRecordWithUnresolved(ConstructorDeclaration d) {
        var s = d.getDeclarationAsString();
        var c = String.format("Unresolved: %s", s);

        var l = new CallerLabel(d.getDeclarationAsString());
        var mqs = new MethodQualifiedSignature("");
        var ms = new MethodSignature("");
        return new NameRecord(l, mqs, ms);
    }

    NameRecord resolveNameRecordInternal(EnumConstantDeclaration d) {
        var l = new CallerLabel(d.getNameAsString());
        var mqs = new MethodQualifiedSignature("");
        var ms = new MethodSignature("");
        return new NameRecord(l, mqs, ms);
    }

    NameRecord resolveNameRecordInternal(FieldDeclaration d) {
        try {
            return resolveNameRecordWithResolved(d.resolve());
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("caller field resolve error");
            return resolveNameRecordWithUnresolved(d);
        }
    }

    NameRecord resolveNameRecordWithResolved(ResolvedFieldDeclaration d) {
        var l = new CallerLabel(String.format("%s %s", d.declaringType().getQualifiedName(), d.getName()));
        var mqs = new MethodQualifiedSignature("");
        var ms = new MethodSignature("");
        return new NameRecord(l, mqs, ms);
    }

    NameRecord resolveNameRecordWithUnresolved(FieldDeclaration d) {
        var l = new CallerLabel(String.format("Unresolved: %s", variablesString(d.getVariables())));
        var mqs = new MethodQualifiedSignature("");
        var ms = new MethodSignature("");
        return new NameRecord(l, mqs, ms);
    }

    String variablesString(NodeList<VariableDeclarator> l) {
        return l.stream().map(NodeWithSimpleName::getNameAsString).collect(Collectors.joining(", "));
    }

    NameRecord resolveNameRecordInternal() {
        var l = new CallerLabel("Initializer");
        var mqs = new MethodQualifiedSignature("");
        var ms = new MethodSignature("");
        return new NameRecord(l, mqs, ms);
    }

    NameRecord resolveNameRecordInternal(MethodDeclaration d) {
        try {
            return resolveNameRecordWithResolved(d.resolve());
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("caller method resolve error");
            return resolveNameRecordWithUnresolved(d);
        }
    }

    NameRecord resolveNameRecordWithResolved(ResolvedMethodDeclaration d) {
        var l = new CallerLabel(d.getQualifiedSignature());
        var mqs = new MethodQualifiedSignature(d.getQualifiedSignature());
        var ms = new MethodSignature(d.getSignature());
        return new NameRecord(l, mqs, ms);
    }

    NameRecord resolveNameRecordWithUnresolved(MethodDeclaration d) {
        var l = new CallerLabel(d.getDeclarationAsString());
        var mqs = new MethodQualifiedSignature("");
        var ms = new MethodSignature("");
        return new NameRecord(l, mqs, ms);
    }

    List<CallerAnnotationExpr> callerAnnotationExpr(BodyDeclaration<?> d) {
        return d.getAnnotations().stream().map(a -> new CallerAnnotationExpr(a.toString())).collect(Collectors.toList());
    }
}
