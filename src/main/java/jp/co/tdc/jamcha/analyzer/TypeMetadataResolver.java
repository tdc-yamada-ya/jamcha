package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import jp.co.tdc.jamcha.model.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.flogger.Flogger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Flogger
public class TypeMetadataResolver {
    private final ResolveMetadataHelper resolveMetadataHelper = new ResolveMetadataHelper();

    public TypeMetadata resolve(TypeDeclaration<?> d) {
        var nr = qualifiedName(d);
        var b = resolveMetadataHelper.begin(d);
        var s = superTypes(d);
        var a = typeAnnotationExprs(d);
        return new TypeMetadata(
            nr.packageName(),
            nr.typeQualifiedName(),
            nr.typeName(),
            b,
            s,
            nr.ancestorSuperTypes(),
            a);
    }

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class NameRecord {
        @NonNull private final PackageName packageName;
        @NonNull private final TypeQualifiedName typeQualifiedName;
        @NonNull private final TypeName typeName;
        @NonNull private final List<SuperType> ancestorSuperTypes;
    }

    NameRecord qualifiedName(TypeDeclaration<?> d) {
        try {
            return qualifiedNameWithResolved(d.resolve());
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("type resolve error");
            return qualifiedNameWithUnresolved(d);
        }
    }

    NameRecord qualifiedNameWithResolved(ResolvedReferenceTypeDeclaration d) {
        var pn = new PackageName(d.getPackageName());
        var qn = new TypeQualifiedName(d.getQualifiedName());
        var n = new TypeName(d.getName());
        var ast = ancestorSuperTypes(d.getAllAncestors());
        return new NameRecord(pn, qn, n, ast);
    }

    List<SuperType> ancestorSuperTypes(List<ResolvedReferenceType> l) {
        return l.stream().map(this::superTypeWithResolved).collect(Collectors.toList());
    }

    NameRecord qualifiedNameWithUnresolved(TypeDeclaration<?> d) {
        var pn = new PackageName("");
        var qn = new TypeQualifiedName("");
        var n = new TypeName(d.getNameAsString());
        List<SuperType> ast = List.of();
        return new NameRecord(pn, qn, n, ast);
    }

    List<SuperType> superTypes(TypeDeclaration<?> d) {
        if (!d.isClassOrInterfaceDeclaration()) {
            return List.of();
        }

        var coid = d.asClassOrInterfaceDeclaration();
        return superTypes(coid).collect(Collectors.toList());
    }

    Stream<SuperType> superTypes(ClassOrInterfaceDeclaration d) {
        return Stream.concat(superTypes(d.getExtendedTypes()), superTypes(d.getImplementedTypes()));
    }

    Stream<SuperType> superTypes(NodeList<ClassOrInterfaceType> l) {
        return l.stream().map(this::superType);
    }

    SuperType superType(ClassOrInterfaceType t) {
        try {
            return superTypeWithResolved(t.resolve());
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("super type resolve error");
            return superTypeWithUnesolved();
        }
    }

    SuperType superTypeWithResolved(ResolvedReferenceType t) {
        var tqn = new TypeQualifiedName(t.getQualifiedName());
        return new SuperType(tqn);
    }

    SuperType superTypeWithUnesolved() {
        var tqn = new TypeQualifiedName("");
        return new SuperType(tqn);
    }

    List<TypeAnnotationExpr> typeAnnotationExprs(TypeDeclaration<?> d) {
        return d.getAnnotations().stream().map(a -> new TypeAnnotationExpr(a.toString())).collect(Collectors.toList());
    }
}
