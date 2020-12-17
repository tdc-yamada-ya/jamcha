package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
public class MethodQualifiedSignature {
    @NonNull private final String value;

    public CallerLabel asCallerLabel() {
        return new CallerLabel(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
