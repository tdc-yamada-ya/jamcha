package jp.co.tdc.jamcha.reporter.table;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MemoryTable implements Table {
    private final List<Row> rows;

    @Override
    public Stream<Row> rows() {
        return rows.stream();
    }
}
