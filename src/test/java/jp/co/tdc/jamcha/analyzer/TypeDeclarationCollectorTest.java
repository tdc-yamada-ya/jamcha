package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TypeDeclarationCollectorTest {
    private final TestHelper testHelper = new TestHelper();

    @Test
    void testVisit() {
        var u = testHelper.parse("""
            class T {
              class U {}
            }
            interface V {}
            enum W {}
            """);
        var c = new TypeDeclarationCollector();
        var w = new NodeWalker();

        w.walk(u, c);

        var l = c.declarations().stream().map(Object::getClass).collect(Collectors.toList());

        Assertions.assertEquals(
            List.of(
                ClassOrInterfaceDeclaration.class,
                ClassOrInterfaceDeclaration.class,
                ClassOrInterfaceDeclaration.class,
                EnumDeclaration.class
            ),
            l
        );
    }
}
