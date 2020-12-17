package jp.co.tdc.jamcha.reporter.table;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Table {
    @NonNull private final List<Row> rows;
}
