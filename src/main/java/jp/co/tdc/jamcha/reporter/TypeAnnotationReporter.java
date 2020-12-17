package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.model.Type;
import jp.co.tdc.jamcha.model.TypeAnnotationExpr;
import jp.co.tdc.jamcha.reporter.table.Cell;
import jp.co.tdc.jamcha.reporter.table.MemoryTable;
import jp.co.tdc.jamcha.reporter.table.Row;
import jp.co.tdc.jamcha.reporter.table.TableWriter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class TypeAnnotationReporter {
    private final TableWriter writer;

    public void report(List<SourceAnalyzeResult> results) {
        var rows = rows(results).collect(Collectors.toList());
        var t = new MemoryTable(rows);
        writer.write(t);
    }

    Stream<Row> rows(List<SourceAnalyzeResult> results) {
        return Stream.concat(
            Stream.of(header()),
            results.stream().map(r -> rowsWithSourceAnalyzeResult(r)).flatMap(Function.identity())
        );
    }

    Row header() {
        return new Row(List.of(
            new Cell("name"),
            new Cell("typeSignature"),
            new Cell("typeAnnotationExpr")
        ));
    }

    Stream<Row> rowsWithSourceAnalyzeResult(SourceAnalyzeResult r) {
        return r.types().stream().map(t -> rowsWithType(r, t)).flatMap(Function.identity());
    }

    Stream<Row> rowsWithType(SourceAnalyzeResult r, Type t) {
        return t.annotationExprs().stream().map(a -> rowWithTypeAnnotationExpr(r, t, a));
    }

    Row rowWithTypeAnnotationExpr(SourceAnalyzeResult r, Type t, TypeAnnotationExpr a) {
        return new Row(List.of(
            new Cell(r.name()),
            new Cell(t.signature().toString()),
            new Cell(a.toString())
        ));
    }
}
