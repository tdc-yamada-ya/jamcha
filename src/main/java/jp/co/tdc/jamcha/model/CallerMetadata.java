package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Accessors(fluent = true)
public class CallerMetadata {
    @NonNull private final CallerLabel label;
    @NonNull private final MethodQualifiedSignature methodQualifiedSignature;
    @NonNull private final MethodSignature methodSignature;
    @NonNull private final Position begin;
    @NonNull private final List<CallerAnnotationExpr> annotationExprs;
}
