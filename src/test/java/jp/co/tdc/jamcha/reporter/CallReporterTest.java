package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
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

public class CallReporterTest {
    private final TestHelper testHelper = new TestHelper();

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class ReportTestCase {
        private final String displayName;
        private final List<SourceAnalyzeResult> result;
        private final List<Row> table;
    }

    @TestFactory
    Stream<DynamicNode> testReport() {
        return Stream.of(
            new ReportTestCase(
                "case1",
                List.of(),
                List.of()
            ),
            new ReportTestCase(
                "case2",
                testHelper.results(),
                List.of(
                    new Row(
                        List.of(
                            new Cell("name1"),
                            new Cell("packageName1"),
                            new Cell("typeQualifiedName1"),
                            new Cell("typeName1"),
                            new Cell("callerLabel1"),
                            new Cell("methodQualifiedSignature1"),
                            new Cell("methodSignature1"),
                            new Cell("1"),
                            new Cell("2"),
                            new Cell("packageName1"),
                            new Cell("typeQualifiedName1"),
                            new Cell("typeName1"),
                            new Cell("methodQualifiedSignature1"),
                            new Cell("methodSignature1"),
                            new Cell("1"),
                            new Cell("2")
                        )
                    )
                )
            )
        ).map(tt -> DynamicTest.dynamicTest(
            tt.displayName(),
            () -> {
                var tw = Mockito.mock(TableWriter.class);
                var r = new CallReporter(tw);

                r.report(tt.result());

                var eh = List.of(r.header());
                var et = new Table(Stream.concat(eh.stream(), tt.table.stream()).collect(Collectors.toList()));
                Mockito.verify(tw).write(et);
            }
        ));
    }
}
