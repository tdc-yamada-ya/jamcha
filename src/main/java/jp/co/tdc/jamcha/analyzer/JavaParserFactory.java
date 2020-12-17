package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class JavaParserFactory {
    private static final int JAR_DIRECTORY_MAX_DEPTH = 100;

    private final JavaParserConfiguration configuration;

    public JavaParser createJavaParser() {
        var jc = createJavaParserConfiguration(configuration);
        var p = new JavaParser(jc);
        return p;
    }

    com.github.javaparser.ParserConfiguration createJavaParserConfiguration(JavaParserConfiguration c) {
        var r = createSymbolResolver(c);
        var jc = new com.github.javaparser.ParserConfiguration();
        jc.setSymbolResolver(r);
        return jc;
    }

    SymbolResolver createSymbolResolver(JavaParserConfiguration c) {
        var combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        typeSolverStreamWithSourceDirectories(c.dependentSourceDirectories()).forEach(s -> combinedTypeSolver.add(s));
        typeSolverStreamWithJarDirectories(c.dependentJarDirectories()).forEach(s -> combinedTypeSolver.add(s));
        return new JavaSymbolSolver(combinedTypeSolver);
    }

    Stream<TypeSolver> typeSolverStreamWithSourceDirectories(List<Path> d) {
        return d.stream().map(JavaParserTypeSolver::new);
    }

    Stream<TypeSolver> typeSolverStreamWithJarDirectories(List<Path> d) {
        return d.stream().map(p -> findJarFiles(p)).flatMap(Function.identity()).map(this::tryNewJarTypeSolver);
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
