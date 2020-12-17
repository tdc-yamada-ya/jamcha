package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class CallerSignatureTest {
    @Test
    void testToString() {
        var s = new CallerSignature("foo");
        Assertions.assertEquals("foo", s.toString());
    }

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    static class EqualsTestCase {
        private final String displayName;
        private final String a, b;
        private final boolean equals;
    }

    @Test
    Stream<DynamicTest> testEquals() {
        return Stream.of(
            new EqualsTestCase("equals", "foo", "foo", true),
            new EqualsTestCase("not equals", "foo", "bar", false)
        ).map(tt -> DynamicTest.dynamicTest(
            tt.displayName(),
            () -> {
                var a = new CallerSignature(tt.a());
                var b = new CallerSignature(tt.b());
                Assertions.assertEquals(tt.equals(), a.equals(b));
            }
        ));
    }
}
