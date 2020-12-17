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
public class MethodSignature {
    @NonNull private final String value;

    @Override
    public String toString() {
        return value;
    }
}
