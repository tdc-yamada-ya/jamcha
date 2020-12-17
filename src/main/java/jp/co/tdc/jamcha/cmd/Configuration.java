package jp.co.tdc.jamcha.cmd;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Configuration {
    @NonNull private final List<Path> targetSourceDirectories;
    @NonNull private final List<Path> dependentSourceDirectories;
    @NonNull private final List<Path> dependentJarDirectories;
    @NonNull private final Path outputDirectory;
}
