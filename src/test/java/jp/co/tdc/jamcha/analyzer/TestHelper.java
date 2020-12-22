package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class TestHelper {
    CompilationUnit parse(String c) {
        var pc = new ParserConfiguration();
        var cts = new CombinedTypeSolver();
        cts.add(new ReflectionTypeSolver());
        pc.setSymbolResolver(new JavaSymbolSolver(cts));
        var jp = new JavaParser(pc);
        return jp.parse(c).getResult().orElseThrow();
    }
}
