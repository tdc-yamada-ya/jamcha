package jp.co.tdc.jamcha.cmd;

import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ConfigurationFactory {
    private final CommandLineParser parser;
    private final PrintWriter helpWriter;
    private final String[] arguments;
    private final CommandLineHelper commandLineHelper = new CommandLineHelper();

    Configuration createConfiguration() {
        var targetSourceDirectoriesOption = Option.builder("i")
            .hasArgs()
            .required()
            .desc("target source directories")
            .build();

        var dependentSourceDirectoriesOption = Option.builder("s")
            .hasArgs()
            .desc("dependent source directories")
            .build();

        var dependentJarDirectoriesOption = Option.builder("l")
            .hasArgs()
            .desc("dependent jar directories")
            .build();

        var outputDirectoryOption = Option.builder("o")
            .hasArg()
            .required()
            .desc("output directory")
            .build();

        var options = new Options();
        options.addOption(targetSourceDirectoriesOption);
        options.addOption(dependentSourceDirectoriesOption);
        options.addOption(dependentJarDirectoriesOption);
        options.addOption(outputDirectoryOption);

        var line = commandLineHelper.tryParse(parser, options, arguments, helpWriter);
        var targetSourceDirectories = stringArrayToPathList(line.getOptionValues(targetSourceDirectoriesOption.getOpt()));
        var dependentSourceDirectories = stringArrayToPathList(line.getOptionValues(dependentSourceDirectoriesOption.getOpt()));
        var dependentJarDirectories = stringArrayToPathList(line.getOptionValues(dependentJarDirectoriesOption.getOpt()));
        var outputDirectory = Paths.get(line.getOptionValue(outputDirectoryOption.getOpt()));

        return new Configuration(
            targetSourceDirectories,
            dependentSourceDirectories,
            dependentJarDirectories,
            outputDirectory
        );
    }

    List<Path> stringArrayToPathList(String[] s) {
        if (s == null) {
            return List.of();
        }

        return Arrays.stream(s).map(Paths::get).collect(Collectors.toList());
    }
}
