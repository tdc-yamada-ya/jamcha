package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.expr.MethodCallExpr;
import jp.co.tdc.jamcha.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class CalleeResolverHelper {
    private final TestHelper testHelper = new TestHelper();

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class ResolveTestCase {
    	private final String displayName;
    	private final String code;
    	private final Callee callee;
    }

    @TestFactory
    Stream<DynamicNode> testResolve() {
    	return Stream.of(
    		new ResolveTestCase(
    		    "case1",
                """
            package p.q;
            class T {
              void a() { b(1, "2"); }
              void b(int a, String b) {}
            }""",
                new Callee(
                    new PackageName("p.q"),
                    new TypeQualifiedName("p.q.T"),
                    new TypeName("T"),
                    new MethodQualifiedSignature("p.q.T.b(int, java.lang.String)"),
                    new MethodSignature("b(int, java.lang.String)"),
                    new Position(3, 14)
                )
            ),
            new ResolveTestCase(
                "case2",
                """
            package p.q;
            class T {
              void a() { b(1, "2"); }
            }""",
                new Callee(
                    new PackageName(""),
                    new TypeQualifiedName(""),
                    new TypeName(""),
                    new MethodQualifiedSignature(""),
                    new MethodSignature("b"),
                    new Position(3, 14)
                )
            )
    	).map(tt -> DynamicTest.dynamicTest(
    		tt.displayName(),
    		() -> {
    		    var cu = testHelper.parse(tt.code());
                var mce = cu.findFirst(MethodCallExpr.class).orElseThrow();
                var cr = new CalleeResolver();
                var c = cr.resolve(mce);
                Assertions.assertEquals(tt.callee(), c);
    		}
    	));
    }
}
