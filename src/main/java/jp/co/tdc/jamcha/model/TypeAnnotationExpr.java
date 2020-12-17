package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class TypeAnnotationExpr {
    private final String expr;

    @Override
    public String toString() {
        return expr;
    }
}
