package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.model.Caller;
import jp.co.tdc.jamcha.model.CallerAnnotationExpr;
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
public class CallerAnnotationReporter {
    private final TableWriter writer;

    public void report(List<SourceAnalyzeResult> r) {
        var rows = rows(r).collect(Collectors.toList());
        var t = new Table(rows);
        writer.write(t);
    }

    Stream<Row> rows(List<SourceAnalyzeResult> r) {
        return Stream.concat(Stream.of(header()), r.stream().flatMap(this::rows));
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
            new Cell("callerAnnotationExpr")
        ));
    }

    Stream<Row> rows(SourceAnalyzeResult r) {
        return r.types().stream().flatMap(t -> rows(r, t));
    }

    Stream<Row> rows(SourceAnalyzeResult r, Type t) {
        return t.callers().stream().flatMap(c -> rows(r, t, c));
    }

    Stream<Row> rows(SourceAnalyzeResult r, Type t, Caller c) {
        return c.metadata().annotationExprs().stream().map(a -> row(r, t, c, a));
    }

    Row row(SourceAnalyzeResult r, Type t, Caller c, CallerAnnotationExpr a) {
        return new Row(List.of(
            new Cell(r.name()),
            new Cell(t.metadata().packageName().value()),
            new Cell(t.metadata().qualifiedName().value()),
            new Cell(t.metadata().name().value()),
            new Cell(c.metadata().label().value()),
            new Cell(c.metadata().methodQualifiedSignature().value()),
            new Cell(c.metadata().methodSignature().value()),
            new Cell(Integer.toString(c.metadata().begin().line())),
            new Cell(Integer.toString(c.metadata().begin().column())),
            new Cell(a.value())
        ));
    }
}
