package jp.co.tdc.jamcha.cmd.analyze;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Configuration {
    private final List<Path> targetSourceDirectories;
    private final List<Path> dependentSourceDirectories;
    private final List<Path> dependentJarDirectories;
    private final Path outputDirectory;
}
