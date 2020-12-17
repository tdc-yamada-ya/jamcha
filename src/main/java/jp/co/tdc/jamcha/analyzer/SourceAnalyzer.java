package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;
import jp.co.tdc.jamcha.model.*;
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
    private final TypeSignatureResolver typeSignatureResolver = new TypeSignatureResolver();
    private final CallerSignatureResolver callerSignatureResolver = new CallerSignatureResolver();
    private final CalleeSignatureResolver calleeSignatureResolver = new CalleeSignatureResolver();

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
        n.walk(c::collect);
        return c.declarations();
    }

    Type typeWithTypeDeclaration(TypeDeclaration<?> td) {
        var s = typeSignatureResolver.resolve(td);
        var a = typeAnnotationExprs(td);
        var c = callersWithTypeDeclaration(td);
        var b = beginWithNodeWithRange(td);
        return new Type(s, a, c, b);
    }

    List<TypeAnnotationExpr> typeAnnotationExprs(TypeDeclaration<?> d) {
        return d.getAnnotations().stream().map(a -> new TypeAnnotationExpr(a.toString())).collect(Collectors.toList());
    }

    List<Caller> callersWithTypeDeclaration(TypeDeclaration<?> d) {
        var b = findBodyDeclarations(d);
        return b.stream().map(this::callerWithBodyDeclaration).collect(Collectors.toList());
    }

    List<BodyDeclaration<?>> findBodyDeclarations(Node n) {
        var c = new BodyDeclarationCollector();
        n.walk(c::collect);
        return c.declarations();
    }

    Caller callerWithBodyDeclaration(BodyDeclaration<?> bd) {
        var s = callerSignatureResolver.resolve(bd);
        var a = callerAnnotationExprs(bd);
        var c = calleesWithBodyDeclaration(bd);
        var b = beginWithNodeWithRange(bd);
        return new Caller(s, a, c, b);
    }

    List<Callee> calleesWithBodyDeclaration(BodyDeclaration<?> d) {
        var e = findMethodCallExprs(d);
        return e.stream().map(this::calleeWithMethodCallExpr).collect(Collectors.toList());
    }

    List<CallerAnnotationExpr> callerAnnotationExprs(BodyDeclaration<?> d) {
        return d.getAnnotations().stream().map(a -> new CallerAnnotationExpr(a.toString())).collect(Collectors.toList());
    }

    List<MethodCallExpr> findMethodCallExprs(Node n) {
        var c = new MethodCallExprCollector();
        n.walk(c::collect);
        return c.exprs();
    }

    Callee calleeWithMethodCallExpr(MethodCallExpr e) {
        var s = calleeSignatureResolver.resolve(e);
        var b = beginWithNodeWithRange(e);
        return new Callee(s, b);
    }

    Position beginWithNodeWithRange(NodeWithRange<?> r) {
        return r.getBegin().map(p -> new Position(p.line, p.column)).orElse(new Position(0, 0));
    }
}
