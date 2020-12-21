package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class CallDescendant {
    @NonNull private final Key key;
    @NonNull private final List<MethodQualifiedSignature> route;

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    @EqualsAndHashCode
    public static class Key {
        @NonNull private final TypeQualifiedName typeQualifiedName;
        @NonNull private final CallerLabel callerLabel;
        @NonNull private final MethodQualifiedSignature descendantMethodQualifiedSignature;
    }
}
