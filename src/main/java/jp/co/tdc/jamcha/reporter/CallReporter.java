package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.model.Callee;
import jp.co.tdc.jamcha.model.Caller;
import jp.co.tdc.jamcha.model.Type;
import jp.co.tdc.jamcha.reporter.table.Cell;
import jp.co.tdc.jamcha.reporter.table.Row;
import jp.co.tdc.jamcha.reporter.table.Table;
import jp.co.tdc.jamcha.reporter.table.TableWriter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CallReporter {
    private final TableWriter writer;

    public void report(List<SourceAnalyzeResult> results) {
        var rows = rows(results).collect(Collectors.toList());
        var t = new Table(rows);
        writer.write(t);
    }

    Stream<Row> rows(List<SourceAnalyzeResult> results) {
        return Stream.concat(
            Stream.of(header()),
            results.stream().flatMap(this::rows)
        );
    }

    Row header() {
        return new Row(List.of(
            new Cell("name"),
            new Cell("typePackageName"),
            new Cell("typeQualifiedName"),
            new Cell("typeName"),
            new Cell("callerLabel"),
            new Cell("callerMethodQualifiedSignature"),
            new Cell("callerMethodSignature"),
            new Cell("callerBeginLine"),
            new Cell("callerBeginColumn"),
            new Cell("calleePackageName"),
            new Cell("calleeTypeQualifiedName"),
            new Cell("calleeTypeName"),
            new Cell("calleeMethodQualifiedSignature"),
            new Cell("calleeMethodSignature"),
            new Cell("calleeBeginLine"),
            new Cell("calleeBeginColumn")
        ));
    }

    Stream<Row> rows(SourceAnalyzeResult r) {
        return r.types().stream().flatMap(t -> rows(r, t));
    }

    Stream<Row> rows(SourceAnalyzeResult r, Type t) {
        return t.callers().stream().flatMap(c -> rows(r, t, c));
    }

    Stream<Row> rows(SourceAnalyzeResult r, Type t, Caller caller) {
        return caller.callees().stream().map(c -> row(r, t, caller, c));
    }

    Row row(SourceAnalyzeResult r, Type t, Caller cr, Callee ce) {
        return new Row(
            List.of(
                new Cell(r.name()),
                new Cell(t.metadata().packageName().value()),
                new Cell(t.metadata().qualifiedName().value()),
                new Cell(t.metadata().name().value()),
                new Cell(cr.metadata().label().value()),
                new Cell(cr.metadata().methodQualifiedSignature().value()),
                new Cell(cr.metadata().methodSignature().value()),
                new Cell(Integer.toString(cr.metadata().begin().line())),
                new Cell(Integer.toString(cr.metadata().begin().column())),
                new Cell(ce.packageName().value()),
                new Cell(ce.typeQualifiedName().value()),
                new Cell(ce.typeName().value()),
                new Cell(ce.methodQualifiedSignature().value()),
                new Cell(ce.methodSignature().value()),
                new Cell(Integer.toString(ce.begin().line())),
                new Cell(Integer.toString(ce.begin().column()))
            )
        );
    }
}
