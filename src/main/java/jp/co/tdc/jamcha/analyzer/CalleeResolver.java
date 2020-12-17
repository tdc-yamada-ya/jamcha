package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import jp.co.tdc.jamcha.model.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.flogger.Flogger;

@Flogger
public class CalleeResolver {
    private final ResolveMetadataHelper resolveMetadataHelper = new ResolveMetadataHelper();

    public Callee resolve(MethodCallExpr x) {
        var nr = resolveNameRecord(x);
        var b = resolveMetadataHelper.begin(x);
        return new Callee(
            nr.packageName(),
            nr.typeQualifiedName(),
            nr.typeName(),
            nr.methodQualifiedSignature(),
            nr.methodSignature(),
            b);
    }

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class NameRecord {
        @NonNull private final PackageName packageName;
        @NonNull private final TypeQualifiedName typeQualifiedName;
        @NonNull private final TypeName typeName;
        @NonNull private final MethodQualifiedSignature methodQualifiedSignature;
        @NonNull private final MethodSignature methodSignature;
    }

    public NameRecord resolveNameRecord(MethodCallExpr x) {
        try {
            return resolveNameRecordWithResolved(x.resolve());
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("callee resolve error");
            return resolveNameRecordWithUnresolved(x);
        }
    }

    NameRecord resolveNameRecordWithResolved(ResolvedMethodDeclaration d) {
        var pn = new PackageName(d.getPackageName());
        var tqn = new TypeQualifiedName(d.declaringType().getQualifiedName());
        var tn = new TypeName(d.declaringType().getName());
        var mqn = new MethodQualifiedSignature(d.getQualifiedSignature());
        var mn = new MethodSignature(d.getSignature());
        return new NameRecord(pn, tqn, tn, mqn, mn);
    }

    NameRecord resolveNameRecordWithUnresolved(MethodCallExpr x) {
        var pn = new PackageName("");
        var tqn = new TypeQualifiedName("");
        var tn = new TypeName("");
        var mqn = new MethodQualifiedSignature("");
        var mn = new MethodSignature(x.getNameAsString());
        return new NameRecord(pn, tqn, tn, mqn, mn);
    }
}
