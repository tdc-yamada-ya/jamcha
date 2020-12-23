package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Accessors(fluent = true)
public class SuperType implements Comparable<SuperType> {
    @NonNull private final TypeQualifiedName qualifiedName;

    @Override
    public int compareTo(SuperType o) {
        return qualifiedName.compareTo(o.qualifiedName);
    }
}
