package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

public class PositionTest {
    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class ToStringTestCase {
        private final int line;
        private final int column;
        private final String s;
    }

    @TestFactory
    Stream<DynamicNode> toStringTest() {
        return Stream.of(
            new ToStringTestCase(0, 0, "0, 0"),
            new ToStringTestCase(1, 2, "1, 2")
        ).map(tt -> DynamicTest.dynamicTest(
            String.format("%s, %s", tt.line(), tt.column()),
            () -> {
                Assertions.assertEquals(tt.s(), new Position(tt.line(), tt.column()).toString());
            }
        ));
    }
}
