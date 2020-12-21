package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.model.Type;
import jp.co.tdc.jamcha.model.TypeAnnotationExpr;
import jp.co.tdc.jamcha.reporter.table.Cell;
import jp.co.tdc.jamcha.reporter.table.Row;
import jp.co.tdc.jamcha.reporter.table.Table;
import jp.co.tdc.jamcha.reporter.table.TableWriter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypeAnnotationReporter {
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
            new Cell("typeBeginLine"),
            new Cell("typeBeginColumn"),
            new Cell("typeAnnotationExpr")
        ));
    }

    Stream<Row> rows(SourceAnalyzeResult r) {
        return r.types().stream().flatMap(t -> rows(r, t));
    }

    Stream<Row> rows(SourceAnalyzeResult r, Type t) {
        return t.metadata().annotationExprs().stream().map(a -> row(r, t, a));
    }

    Row row(SourceAnalyzeResult r, Type t, TypeAnnotationExpr a) {
        return new Row(List.of(
            new Cell(r.name()),
            new Cell(t.metadata().packageName().value()),
            new Cell(t.metadata().qualifiedName().value()),
            new Cell(t.metadata().name().value()),
            new Cell(Integer.toString(t.metadata().begin().line())),
            new Cell(Integer.toString(t.metadata().begin().column())),
            new Cell(a.value())
        ));
    }
}
