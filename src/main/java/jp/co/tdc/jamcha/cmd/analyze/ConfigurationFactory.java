package jp.co.tdc.jamcha.cmd.analyze;

import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ConfigurationFactory {
    private static final String DEFAULT_TARGET_SOURCE_DIRECTORY_MAX_DEPTH = "100";
    private static final String DEFAULT_DEPENDENT_LIBRARY_DIRECTORY_MAX_DEPTH = "100";

    private final CommandLineParser parser;
    private final PrintWriter helpWriter;
    private final String[] arguments;

    Configuration createConfiguration() {
        var targetSourceDirectoriesOption = Option.builder("i")
            .hasArgs()
            .required()
            .desc("target source directories")
            .build();
        var dependentSourceDirectoriesOption = Option.builder("s")
            .hasArgs()
            .desc("target source directories")
            .build();
        var dependentJarDirectoriesOption = Option.builder("l")
            .hasArgs()
            .desc("dependent jar directories")
            .build();
        var outputDirectoryOption = Option.builder("o")
            .hasArg()
            .required()
            .desc("dependent library directories")
            .build();
        var options = new Options();
        options.addOption(targetSourceDirectoriesOption);
        options.addOption(dependentSourceDirectoriesOption);
        options.addOption(dependentJarDirectoriesOption);
        options.addOption(outputDirectoryOption);

        var line = tryParse(options, arguments);
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

    CommandLine tryParse(Options options, String[] arguments) {
        try {
            return parser.parse(options, arguments);
        } catch (ParseException e) {
            printHelp(options);
            throw new RuntimeException("command line parse error", e);
        }
    }

    void printHelp(Options options) {
        var hf = new HelpFormatter();
        hf.printHelp(
            helpWriter,
            hf.getWidth(),
            "Main",
            "",
            options,
            0,
            0,
            "");
        helpWriter.flush();
    }

    List<Path> stringArrayToPathList(String[] s) {
        if (s == null) {
            return List.of();
        }

        return Arrays.stream(s).map(Paths::get).collect(Collectors.toList());
    }
}
