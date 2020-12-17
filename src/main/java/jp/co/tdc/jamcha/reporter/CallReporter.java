package jp.co.tdc.jamcha.reporter;

import jp.co.tdc.jamcha.analyzer.SourceAnalyzeResult;
import jp.co.tdc.jamcha.model.Callee;
import jp.co.tdc.jamcha.model.Caller;
import jp.co.tdc.jamcha.model.Type;
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
public class CallReporter {
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
            new Cell("callerSignature"),
            new Cell("callerBeginLine"),
            new Cell("callerBeginColumn"),
            new Cell("calleeSignatureType"),
            new Cell("calleeSignatureCallee"),
            new Cell("calleeBeginLine"),
            new Cell("calleeBeginColumn")
        ));
    }

    Stream<Row> rowsWithSourceAnalyzeResult(SourceAnalyzeResult r) {
        return r.types().stream().map(t -> rowsWithType(r, t)).flatMap(Function.identity());
    }

    Stream<Row> rowsWithType(SourceAnalyzeResult r, Type t) {
        return t.callers().stream().map(c -> rowsWithCaller(r, t, c)).flatMap(Function.identity());
    }

    Stream<Row> rowsWithCaller(SourceAnalyzeResult r, Type t, Caller caller) {
        return caller.callees().stream().map(c -> rowWithCallee(r, t, caller, c));
    }

    Row rowWithCallee(SourceAnalyzeResult r, Type t, Caller caller, Callee callee) {
        return new Row(List.of(
            new Cell(r.name()),
            new Cell(t.signature().type()),
            new Cell(caller.signature().caller()),
            new Cell(Integer.toString(caller.begin().line())),
            new Cell(Integer.toString(caller.begin().column())),
            new Cell(callee.signature().type()),
            new Cell(callee.signature().method()),
            new Cell(Integer.toString(callee.begin().line())),
            new Cell(Integer.toString(callee.begin().column()))
        ));
    }
}
