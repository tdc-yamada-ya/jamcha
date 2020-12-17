package jp.co.tdc.jamcha.reporter.table;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class CSVFileTableWriter implements TableWriter {
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private final Path file;

    public void write(Table t) {
        try {
            try (var p = new CSVPrinter(Files.newBufferedWriter(file, CHARSET), CSVFormat.EXCEL)) {
                t.rows().forEach(r -> tryPrintRecord(p, r.cells()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    void tryPrintRecord(CSVPrinter p, Iterable<?> r) {
        try {
            p.printRecord(r);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
