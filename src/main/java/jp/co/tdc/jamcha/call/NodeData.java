package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.model.CallerLabel;
import jp.co.tdc.jamcha.model.MethodQualifiedSignature;
import jp.co.tdc.jamcha.model.MethodSignature;
import jp.co.tdc.jamcha.model.TypeQualifiedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
public class NodeData {
    @NonNull private final TypeQualifiedName typeQualifiedName;
    @NonNull private final CallerLabel callerLabel;
    @NonNull private final MethodQualifiedSignature methodQualifiedSignature;
    @NonNull private final MethodSignature methodSignature;
}
