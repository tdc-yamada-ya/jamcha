package jp.co.tdc.jamcha.reporter.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.StringWriter;
import java.util.List;
import java.util.stream.Stream;

public class CSVTableWriterTest {
    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class WriteTestCase {
        private final String displayName;
        private final Table table;
        private final String s;
    }

    @TestFactory
    Stream<DynamicNode> testWrite() {
        return Stream.of(
            new WriteTestCase(
                "case1",
                new Table(List.of()),
                ""
            ),
            new WriteTestCase(
                "case2",
                new Table(List.of(
                    new Row(List.of(
                        new Cell("aaa"),
                        new Cell("bbb"),
                        new Cell(("ccc"))
                    ))
                )),
                "aaa,bbb,ccc\r\n"
            )
        ).map(tt -> DynamicTest.dynamicTest(
            tt.displayName(),
            () -> {
                var sw = new StringWriter();
                var cw = new CSVTableWriter(sw);
                cw.write(tt.table());
                Assertions.assertEquals(tt.s(), sw.toString());
            }
        ));
    }
}
