package jp.co.tdc.jamcha.reporter.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Stream;

public class TableTest {
    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class EqualsestCase {
    	private final String displayName;
    	private final Table a;
    	private final Table b;
    	private final boolean equals;
    }

    @TestFactory
    Stream<DynamicNode> testFoo() {
    	return Stream.of(
    		new EqualsestCase(
    		    "case1",
                new Table(List.of()),
                new Table(List.of()),
                true
            ),
            new EqualsestCase(
                "case2",
                new Table(List.of(new Row(List.of(new Cell("aaa"))))),
                new Table(List.of(new Row(List.of(new Cell("aaa"))))),
                true
            ),
            new EqualsestCase(
                "case3",
                new Table(List.of(new Row(List.of(new Cell("aaa"))))),
                new Table(List.of(new Row(List.of(new Cell("bbb"))))),
                false
            ),
            new EqualsestCase(
                "case4",
                new Table(List.of(
                    new Row(List.of(new Cell("aaa"), new Cell("bbb"))),
                    new Row(List.of(new Cell("ccc"), new Cell("ddd")))
                )),
                new Table(List.of(
                    new Row(List.of(new Cell("aaa"), new Cell("bbb"))),
                    new Row(List.of(new Cell("ccc"), new Cell("ddd")))
                )),
                true
            ),
            new EqualsestCase(
                "case5",
                new Table(List.of(
                    new Row(List.of(new Cell("aaa"), new Cell("bbb"))),
                    new Row(List.of(new Cell("ccc"), new Cell("ddd")))
                )),
                new Table(List.of(
                    new Row(List.of(new Cell("aaa"), new Cell("bbb"))),
                    new Row(List.of(new Cell("ccc"), new Cell("eee")))
                )),
                false
            )
    	).map(tt -> DynamicTest.dynamicTest(
    		tt.displayName(),
    		() -> {
                Assertions.assertEquals(tt.equals(), tt.a().equals(tt.b()));
    		}
    	));
    }
}
