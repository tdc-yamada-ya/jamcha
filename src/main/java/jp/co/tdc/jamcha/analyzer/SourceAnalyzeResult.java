package jp.co.tdc.jamcha.analyzer;

import jp.co.tdc.jamcha.model.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class SourceAnalyzeResult {
    private final String name;
    private final boolean successful;
    private final List<Type> types;
}
