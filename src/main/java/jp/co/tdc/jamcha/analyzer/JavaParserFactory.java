package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Stream;

@Flogger
@RequiredArgsConstructor
public class JavaParserFactory {
    private static final int JAR_DIRECTORY_MAX_DEPTH = 100;

    private final JavaParserConfiguration configuration;

    public JavaParser createJavaParser() {
        var c = createJavaParserConfiguration(configuration);
        return new JavaParser(c);
    }

    com.github.javaparser.ParserConfiguration createJavaParserConfiguration(JavaParserConfiguration c) {
        var r = createSymbolResolver(c);
        var jc = new com.github.javaparser.ParserConfiguration();
        jc.setSymbolResolver(r);
        return jc;
    }

    SymbolResolver createSymbolResolver(JavaParserConfiguration c) {
        var cts = new CombinedTypeSolver();
        cts.add(new ReflectionTypeSolver());
        typeSolverStreamWithSourceDirectories(c.dependentSourceDirectories()).forEach(cts::add);
        typeSolverStreamWithJarDirectories(c.dependentJarDirectories()).forEach(cts::add);

        return new JavaSymbolSolver(cts);
    }

    void log(Path p) {
        log.atInfo().log("add solver path: %s", p);
    }

    Stream<TypeSolver> typeSolverStreamWithSourceDirectories(List<Path> d) {
        return d.stream().peek(this::log).map(JavaParserTypeSolver::new);
    }

    Stream<TypeSolver> typeSolverStreamWithJarDirectories(List<Path> d) {
        return d.stream().flatMap(this::findJarFiles).peek(this::log).map(this::tryNewJarTypeSolver);
    }

    Stream<Path> findJarFiles(Path p) {
        try {
            return Files.find(p, JAR_DIRECTORY_MAX_DEPTH, this::isJarFile);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    boolean isJarFile(Path p, BasicFileAttributes a) {
        return a.isRegularFile() && p.getFileName().toString().endsWith(".jar");
    }

    JarTypeSolver tryNewJarTypeSolver(Path p) {
        try {
            return new JarTypeSolver(p);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
