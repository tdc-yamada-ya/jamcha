package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.call.ExpandCallResult;
import jp.co.tdc.jamcha.model.CallDescendant;
import jp.co.tdc.jamcha.model.MethodQualifiedSignature;
import jp.co.tdc.jamcha.reporter.table.Cell;
import jp.co.tdc.jamcha.reporter.table.Row;
import jp.co.tdc.jamcha.reporter.table.Table;
import jp.co.tdc.jamcha.reporter.table.TableWriter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ExpandedCallReporter {
    private final TableWriter writer;

    public void report(ExpandCallResult r) {
        var l = r.callDescendants();
        var rows = rows(l).collect(Collectors.toList());
        var t = new Table(rows);
        writer.write(t);
    }

    Stream<Row> rows(List<CallDescendant> l) {
        return Stream.concat(
            Stream.of(header()),
            l.stream().map(this::row)
        );
    }

    Row header() {
        return new Row(List.of(
            new Cell("typeQualifiedName"),
            new Cell("callerLabel"),
            new Cell("descendantMethodQualifiedSignature"),
            new Cell("route")
        ));
    }

    Row row(CallDescendant cad) {
        return new Row(
            List.of(
                new Cell(cad.key().typeQualifiedName().value()),
                new Cell(cad.key().callerLabel().value()),
                new Cell(cad.key().descendantMethodQualifiedSignature().value()),
                new Cell(cad.route().stream().map(MethodQualifiedSignature::value).collect(Collectors.joining(" / ")))
            )
        );
    }
}
