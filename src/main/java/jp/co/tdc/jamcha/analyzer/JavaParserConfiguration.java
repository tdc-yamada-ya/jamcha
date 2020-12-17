package jp.co.tdc.jamcha.analyzer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class JavaParserConfiguration {
    private final List<Path> dependentSourceDirectories;
    private final List<Path> dependentJarDirectories;
}
