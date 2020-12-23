package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.body.*;
import jp.co.tdc.jamcha.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

public class CallerMetadataResolverTest {
    private final TestHelper testHelper = new TestHelper();

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class ResolveTestCase {
        private final String displayName;
        private final String code;
        private final Class<? extends BodyDeclaration<?>> nodeClass;
        private final CallerMetadata metadata;
    }

    @TestFactory
    Stream<DynamicNode> testResolve() {
        return Stream.of(
            new ResolveTestCase(
                "case1",
                """
            package p.q;
            class T {
              @A("a")
              @B("b")
              T(int a, String b) {}
            }""",
                ConstructorDeclaration.class,
                new CallerMetadata(
                    new CallerLabel("p.q.T.T(int, java.lang.String)"),
                    new MethodQualifiedSignature("p.q.T.T(int, java.lang.String)"),
                    new MethodSignature("T(int, java.lang.String)"),
                    new Position(3, 3),
                    List.of(
                        new CallerAnnotationExpr("@A(\"a\")"),
                        new CallerAnnotationExpr("@B(\"b\")")
                    )
                )
            ),
            new ResolveTestCase(
                "case2",
                """
            package p.q;
            enum T {
              A;
            }""",
                EnumConstantDeclaration.class,
                new CallerMetadata(
                    new CallerLabel("A"),
                    new MethodQualifiedSignature(""),
                    new MethodSignature(""),
                    new Position(3, 3),
                    List.of()
                )
            ),
            new ResolveTestCase(
                "case3",
                """
            package p.q;
            class T {
              @A("a")
              @B("b")
              String a;
            }""",
                FieldDeclaration.class,
                new CallerMetadata(
                    new CallerLabel("p.q.T a"),
                    new MethodQualifiedSignature(""),
                    new MethodSignature(""),
                    new Position(3, 3),
                    List.of(
                        new CallerAnnotationExpr("@A(\"a\")"),
                        new CallerAnnotationExpr("@B(\"b\")")
                    )
                )
            ),
            new ResolveTestCase(
                "case4",
                """
            package p.q;
            class T {
              static {}
            }""",
                InitializerDeclaration.class,
                new CallerMetadata(
                    new CallerLabel("Initializer"),
                    new MethodQualifiedSignature(""),
                    new MethodSignature(""),
                    new Position(3, 3),
                    List.of()
                )
            ),
            new ResolveTestCase(
                "case5",
                """
            package p.q;
            class T {
              @A("a")
              @B("b")
              void m(int a, String b) {}
            }""",
                MethodDeclaration.class,
                new CallerMetadata(
                    new CallerLabel("p.q.T.m(int, java.lang.String)"),
                    new MethodQualifiedSignature("p.q.T.m(int, java.lang.String)"),
                    new MethodSignature("m(int, java.lang.String)"),
                    new Position(3, 3),
                    List.of(
                        new CallerAnnotationExpr("@A(\"a\")"),
                        new CallerAnnotationExpr("@B(\"b\")")
                    )
                )
            )
        ).map(tt -> DynamicTest.dynamicTest(
            tt.displayName(),
            () -> {
                var u = testHelper.parse(tt.code());
                var b = u.findFirst(tt.nodeClass()).orElseThrow();
                var r = new CallerMetadataResolver();
                var m = r.resolve(b);
                Assertions.assertEquals(tt.metadata(), m);
            }
        ));
    }
}
