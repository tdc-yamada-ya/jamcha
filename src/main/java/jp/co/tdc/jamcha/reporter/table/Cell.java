package jp.co.tdc.jamcha.reporter.table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Accessors(fluent = true)
public class Cell {
    @NonNull private final String value;

    @Override
    public String toString() {
        return value;
    }
}
