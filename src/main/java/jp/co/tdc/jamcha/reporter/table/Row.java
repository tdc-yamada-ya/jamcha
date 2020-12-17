package jp.co.tdc.jamcha.reporter.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Row {
    private final List<Cell> cells;
}
