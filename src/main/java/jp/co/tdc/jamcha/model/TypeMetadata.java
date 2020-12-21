package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class TypeMetadata {
    @NonNull private final PackageName packageName;
    @NonNull private final TypeQualifiedName qualifiedName;
    @NonNull private final TypeName name;
    @NonNull private final Position begin;
    @NonNull private final List<SuperType> superTypes;
    @NonNull private final List<SuperType> ancestorSuperTypes;
    @NonNull private final List<TypeAnnotationExpr> annotationExprs;
}
