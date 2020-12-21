package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
public class Position {
    private final int line;
    private final int column;

    @Override
    public String toString() {
        return String.format("%d, %d", line, column);
    }
}
