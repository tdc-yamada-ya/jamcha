package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.call.ExpandCallResult;
import jp.co.tdc.jamcha.model.CallDescendant;
import jp.co.tdc.jamcha.model.CallerLabel;
import jp.co.tdc.jamcha.model.MethodQualifiedSignature;
import jp.co.tdc.jamcha.model.TypeQualifiedName;
import jp.co.tdc.jamcha.reporter.table.Cell;
import jp.co.tdc.jamcha.reporter.table.Row;
import jp.co.tdc.jamcha.reporter.table.Table;
import jp.co.tdc.jamcha.reporter.table.TableWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpandedCallReporterTest {
    private final TestHelper testHelper = new TestHelper();

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class ReportTestCase {
        private final String displayName;
        private final ExpandCallResult result;
        private final List<Row> table;
    }

    @TestFactory
    Stream<DynamicNode> testReport() {
        return Stream.of(
            new ReportTestCase(
                "case1",
                new ExpandCallResult(List.of()),
                List.of()
            ),
            new ReportTestCase(
                "case2",
                new ExpandCallResult(
                    List.of(
                        new CallDescendant(
                            new CallDescendant.Key(
                                new TypeQualifiedName("typeQualifiedName1"),
                                new CallerLabel("callerLabel1"),
                                new MethodQualifiedSignature("methodQualifiedSignature1")
                            ),
                            List.of(
                                new MethodQualifiedSignature("methodQualifiedSignature1"),
                                new MethodQualifiedSignature("methodQualifiedSignature2")
                            )
                        )
                    )
                ),
                List.of(
                    new Row(
                        List.of(
                            new Cell("typeQualifiedName1"),
                            new Cell("callerLabel1"),
                            new Cell("methodQualifiedSignature1"),
                            new Cell("methodQualifiedSignature1 / methodQualifiedSignature2")
                        )
                    )
                )
            )
        ).map(tt -> DynamicTest.dynamicTest(
            tt.displayName(),
            () -> {
                var tw = Mockito.mock(TableWriter.class);
                var r = new ExpandedCallReporter(tw);

                r.report(tt.result());

                var eh = List.of(r.header());
                var et = new Table(Stream.concat(eh.stream(), tt.table.stream()).collect(Collectors.toList()));
                Mockito.verify(tw).write(et);
            }
        ));
    }
}
