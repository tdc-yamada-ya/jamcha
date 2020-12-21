package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class SuperType {
    @NonNull private final TypeQualifiedName qualifiedName;
}
