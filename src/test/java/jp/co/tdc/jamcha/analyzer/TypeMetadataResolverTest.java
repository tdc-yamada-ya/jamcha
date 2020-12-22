package jp.co.tdc.jamcha.analyzer;

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

public class TypeMetadataResolverTest {
    private final TestHelper testHelper = new TestHelper();

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class ResolveTestCase {
    	private final String displayName;
    	private final String code;
    	private final TypeMetadata metadata;
    }

    @TestFactory
    Stream<DynamicNode> testResolve() {
    	return Stream.of(
    		new ResolveTestCase(
    		    "case1",
                """
            package p.q;
            import java.lang.Readable;
            @A("a")
            @B("b")
            class T extends String implements Readable {}""",
                new TypeMetadata(
                    new PackageName("p.q"),
                    new TypeQualifiedName("p.q.T"),
                    new TypeName("T"),
                    new Position(3, 1),
                    List.of(
                        new SuperType(new TypeQualifiedName("java.lang.String")),
                        new SuperType(new TypeQualifiedName("java.lang.Readable"))
                    ),
                    List.of(
                        new SuperType(new TypeQualifiedName("java.io.Serializable")),
                        new SuperType(new TypeQualifiedName("java.lang.CharSequence")),
                        new SuperType(new TypeQualifiedName("java.lang.Comparable")),
                        new SuperType(new TypeQualifiedName("java.lang.Object")),
                        new SuperType(new TypeQualifiedName("java.lang.Readable")),
                        new SuperType(new TypeQualifiedName("java.lang.String")),
                        new SuperType(new TypeQualifiedName("java.lang.constant.Constable")),
                        new SuperType(new TypeQualifiedName("java.lang.constant.ConstantDesc"))
                    ),
                    List.of(
                        new TypeAnnotationExpr("@A(\"a\")"),
                        new TypeAnnotationExpr("@B(\"b\")")
                    )
                )
            )
    	).map(tt -> DynamicTest.dynamicTest(
    		tt.displayName(),
    		() -> {
    		    var u = testHelper.parse(tt.code());
    		    var t = u.getTypes().getFirst().orElseThrow();
                var r = new TypeMetadataResolver();
                var m = r.resolve(t);
                Assertions.assertEquals(tt.metadata(), m);
    		}
    	));
    }
}
