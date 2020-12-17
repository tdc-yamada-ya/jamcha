package jp.co.tdc.jamcha.reporter.table;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Row {
    @NonNull private final List<Cell> cells;
}
