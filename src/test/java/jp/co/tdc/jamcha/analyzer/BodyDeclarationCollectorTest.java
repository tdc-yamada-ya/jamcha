package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class BodyDeclarationCollectorTest {
    private final TestHelper testHelper = new TestHelper();

    @Test
    void testWalk() {
        var u = testHelper.parse("""
            class T {
              void m() { a(); b(); }
              void n() { c(); d(); }
              class U {
                void o() { e(); f(); }
                void p() { g(); h(); }
              }
            }""");
        var t = u.getTypes().getFirst().orElseThrow();
        var c = new BodyDeclarationCollector();

        var w = new NodeWalker();
        w.walk(t, c);

        var l = c.declarations().stream().map(Object::getClass).collect(Collectors.toList());
        Assertions.assertEquals(
            List.of(
                MethodDeclaration.class,
                MethodDeclaration.class
            ),
            l
        );
    }
}
