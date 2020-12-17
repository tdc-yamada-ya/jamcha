package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import jp.co.tdc.jamcha.model.Callee;
import jp.co.tdc.jamcha.model.Caller;
import jp.co.tdc.jamcha.model.Type;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SourceAnalyzer {
    private final JavaParser javaParser;
    private final NodeWalker nodeWalker = new NodeWalker();
    private final TypeMetadataResolver typeMetadataResolver = new TypeMetadataResolver();
    private final CallerMetadataResolver callerMetadataResolver = new CallerMetadataResolver();
    private final CalleeResolver calleeResolver = new CalleeResolver();

    public SourceAnalyzeResult analyze(Path base, Path file, Charset charset) {
        var name = base.relativize(file).toString();
        try {
            try (var reader = Files.newBufferedReader(file, charset)) {
                return analyze(reader, name);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public SourceAnalyzeResult analyze(Reader in, String name) {
        var pr = javaParser.parse(in);

        if (!pr.isSuccessful()) {
            return new SourceAnalyzeResult(name, false, null);
        }

        var u = pr.getResult().orElseThrow(() -> new SourceAnalyzeException("result is null"));
        var t = typesWithNode(u);

        return new SourceAnalyzeResult(name, true, t);
    }

    List<Type> typesWithNode(Node n) {
        var d = findTypeDeclarations(n);
        return d.stream().map(this::typeWithTypeDeclaration).collect(Collectors.toList());
    }

    List<TypeDeclaration<?>> findTypeDeclarations(Node n) {
        var c = new TypeDeclarationCollector();
        nodeWalker.walk(n, c);
        return c.declarations();
    }

    Type typeWithTypeDeclaration(TypeDeclaration<?> td) {
        var m = typeMetadataResolver.resolve(td);
        var c = callersWithTypeDeclaration(td);
        return new Type(m, c);
    }

    List<Caller> callersWithTypeDeclaration(TypeDeclaration<?> d) {
        var b = findBodyDeclarations(d);
        return b.stream().map(this::callerWithBodyDeclaration).collect(Collectors.toList());
    }

    List<BodyDeclaration<?>> findBodyDeclarations(Node n) {
        var c = new BodyDeclarationCollector();
        nodeWalker.walk(n, c);
        return c.declarations();
    }

    Caller callerWithBodyDeclaration(BodyDeclaration<?> bd) {
        var m = callerMetadataResolver.resolve(bd);
        var c = calleesWithBodyDeclaration(bd);
        return new Caller(m, c);
    }

    List<Callee> calleesWithBodyDeclaration(BodyDeclaration<?> d) {
        var e = findMethodCallExprs(d);
        return e.stream().map(this::calleeWithMethodCallExpr).collect(Collectors.toList());
    }

    List<MethodCallExpr> findMethodCallExprs(Node n) {
        var c = new MethodCallExprCollector();
        nodeWalker.walk(n, c);
        return c.exprs();
    }

    Callee calleeWithMethodCallExpr(MethodCallExpr e) {
        return calleeResolver.resolve(e);
    }
}
