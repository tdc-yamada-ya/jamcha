package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.model.*;

import java.util.List;

public class TestHelper {
    List<SourceAnalyzeResult> results() {
        return List.of(
            new SourceAnalyzeResult(
                "name1",
                true,
                List.of(
                    new Type(
                        new TypeMetadata(
                            new PackageName("packageName1"),
                            new TypeQualifiedName("typeQualifiedName1"),
                            new TypeName("typeName1"),
                            new Position(1, 2),
                            List.of(
                                new SuperType(new TypeQualifiedName("typeQualifiedName1"))
                            ),
                            List.of(
                                new SuperType(new TypeQualifiedName("typeQualifiedName1"))
                            ),
                            List.of(
                                new TypeAnnotationExpr("typeAnnotationExpr1")
                            )
                        ),
                        List.of(
                            new Caller(
                                new CallerMetadata(
                                    new CallerLabel("callerLabel1"),
                                    new MethodQualifiedSignature("methodQualifiedSignature1"),
                                    new MethodSignature("methodSignature1"),
                                    new Position(1, 2),
                                    List.of(
                                        new CallerAnnotationExpr("callerAnnotationExpr1")
                                    )
                                ),
                                List.of(
                                    new Callee(
                                        new PackageName("packageName1"),
                                        new TypeQualifiedName("typeQualifiedName1"),
                                        new TypeName("typeName1"),
                                        new MethodQualifiedSignature("methodQualifiedSignature1"),
                                        new MethodSignature("methodSignature1"),
                                        new Position(1, 2)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }
}
