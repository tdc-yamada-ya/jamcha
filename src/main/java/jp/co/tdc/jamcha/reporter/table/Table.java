package jp.co.tdc.jamcha.reporter.table;

import java.util.stream.Stream;

public interface Table {
    Stream<Row> rows();
}
