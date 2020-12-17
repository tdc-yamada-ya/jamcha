package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Callee {
    @NonNull private final PackageName packageName;
    @NonNull private final TypeQualifiedName typeQualifiedName;
    @NonNull private final TypeName typeName;
    @NonNull private final MethodQualifiedSignature methodQualifiedSignature;
    @NonNull private final MethodSignature methodSignature;
    @NonNull private final Position begin;
}
