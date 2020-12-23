package jp.co.tdc.jamcha.cmd;

import com.github.javaparser.JavaParser;
import jp.co.tdc.jamcha.analyzer.JavaParserConfiguration;
import jp.co.tdc.jamcha.analyzer.JavaParserFactory;
import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.analyzer.SourceAnalyzer;
import jp.co.tdc.jamcha.call.CallExpander;
import jp.co.tdc.jamcha.reporter.*;
import jp.co.tdc.jamcha.reporter.table.CSVTableWriter;
import jp.co.tdc.jamcha.reporter.table.TableWriter;
import lombok.extern.flogger.Flogger;
import org.apache.commons.cli.DefaultParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Flogger
public class Main {
    public static void main(String[] args) {
        var m = new Main(args);
        m.run();
    }

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private final Configuration configuration;

    Main(String[] a) {
        configuration = configuration(a);
    }

    void run() {
        var p = javaParser(configuration);
        var analyzer = new SourceAnalyzer(p);
        var r = analyzeWithDirectories(analyzer, configuration.targetSourceDirectories());
        var e = new CallExpander();
        var ecr = e.expand(r);

        tryCreateDirectories(configuration.outputDirectory());

        new TypeAnnotationReporter(newFileTableWriter("typeAnnotation.csv")).report(r);
        new SuperTypeReporter(newFileTableWriter("superType.csv")).report(r);
        new AncestorSuperTypeReporter(newFileTableWriter("ancestorSuperType.csv")).report(r);
        new CallerAnnotationReporter(newFileTableWriter("callerAnnotation.csv")).report(r);
        new CallReporter(newFileTableWriter("call.csv")).report(r);
        new ExpandedCallReporter(newFileTableWriter("expandedCall.csv")).report(ecr);
    }

    TableWriter newFileTableWriter(String file) {
        var p = configuration.outputDirectory().resolve(file);
        return newFileTableWriter(p);
    }

    TableWriter newFileTableWriter(Path p) {
        try {
            return new CSVTableWriter(Files.newBufferedWriter(p, CHARSET));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    void tryCreateDirectories(Path p) {
        try {
            Files.createDirectories(p);
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

    List<SourceAnalyzeResult> analyzeWithDirectories(SourceAnalyzer a, List<Path> p) {
        return p.stream().flatMap(d -> analyzeWithDirectory(a, d)).collect(Collectors.toList());
    }

    Stream<SourceAnalyzeResult> analyzeWithDirectory(SourceAnalyzer a, Path d) {
        return findJavaFiles(d).peek(this::log).map(f -> analyzeWithFile(a, d, f));
    }

    void log(Path p) {
        log.atInfo().log("analyze file: %s", p.toString());
    }

    SourceAnalyzeResult analyzeWithFile(SourceAnalyzer a, Path d, Path f) {
        return a.analyze(d, f, CHARSET);
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
