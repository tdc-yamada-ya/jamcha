package jp.co.tdc.jamcha.cmd.analyze;

import com.github.javaparser.JavaParser;
import jp.co.tdc.jamcha.analyzer.JavaParserConfiguration;
import jp.co.tdc.jamcha.analyzer.JavaParserFactory;
import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.analyzer.SourceAnalyzer;
import jp.co.tdc.jamcha.reporter.CallReporter;
import jp.co.tdc.jamcha.reporter.TypeAnnotationReporter;
import jp.co.tdc.jamcha.reporter.table.CSVFileTableWriter;
import lombok.extern.flogger.Flogger;
import org.apache.commons.cli.DefaultParser;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Flogger
public class Main {
    public static void main(String[] args) {
        var m = new Main(args);
        m.run();
    }

    private static final int TARGET_DIRECTORY_MAX_DEPTH = 100;
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private final Configuration configuration;
    private final SourceAnalyzer analyzer;

    Main(String[] a) {
        configuration = configuration(a);
        var p = javaParser(configuration);
        analyzer = new SourceAnalyzer(p);
    }

    void run() {
        var r = analyzeWithDirectories(configuration.targetSourceDirectories());

        tryCreateDirectories(configuration.outputDirectory());

        var taw = new CSVFileTableWriter(configuration.outputDirectory().resolve("typeAnnotation.csv"));
        var tar = new TypeAnnotationReporter(taw);
        tar.report(r);

        var cw = new CSVFileTableWriter(configuration.outputDirectory().resolve("call.csv"));
        var cr = new CallReporter(cw);
        cr.report(r);
    }

    void tryCreateDirectories(Path p) {
        try {
            Files.createDirectories(configuration.outputDirectory());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Configuration configuration(String[] a) {
        var p = new DefaultParser();
        var cf = new ConfigurationFactory(p, new PrintWriter(System.out), a);
        return cf.createConfiguration();
    }

    JavaParser javaParser(Configuration c) {
        var jpc = new JavaParserConfiguration(c.dependentSourceDirectories(), c.dependentJarDirectories());
        var jpf = new JavaParserFactory(jpc);
        return jpf.createJavaParser();
    }

    List<SourceAnalyzeResult> analyzeWithDirectories(List<Path> p) {
        return p.stream().map(this::analyzeWithDirectory).flatMap(Function.identity()).collect(Collectors.toList());
    }

    Stream<SourceAnalyzeResult> analyzeWithDirectory(Path d) {
        return findJavaFiles(d).map(f -> analyzeWithFile(d, f));
    }

    SourceAnalyzeResult analyzeWithFile(Path d, Path f) {
        log.atInfo().log("analyze file: %s", f.toString());
        return analyzer.analyze(d, f, CHARSET);
    }

    Stream<Path> findJavaFiles(Path p) {
        try {
            return Files.find(p, Integer.MAX_VALUE, this::isJavaFile);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    boolean isJavaFile(Path p, BasicFileAttributes a) {
        return a.isRegularFile() && p.getFileName().toString().endsWith(".java");
    }
}
