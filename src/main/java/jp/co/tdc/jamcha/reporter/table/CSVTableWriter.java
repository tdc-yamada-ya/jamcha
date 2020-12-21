package jp.co.tdc.jamcha.reporter.table;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

@RequiredArgsConstructor
public class CSVTableWriter implements TableWriter {
    private final Writer writer;

    public void write(Table t) {
        try {
            var f = format();
            try (var p = new CSVPrinter(writer, f)) {
                t.rows().forEach(r -> tryPrintRecord(p, r.cells()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    CSVFormat format() {
        return CSVFormat.DEFAULT;
    }

    void tryPrintRecord(CSVPrinter p, Iterable<?> r) {
        try {
            p.printRecord(r);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
