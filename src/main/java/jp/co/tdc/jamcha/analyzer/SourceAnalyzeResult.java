package jp.co.tdc.jamcha.analyzer;

import jp.co.tdc.jamcha.model.Type;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class SourceAnalyzeResult {
    @NonNull private final String name;
    private final boolean successful;
    @NonNull private final List<Type> types;
}
