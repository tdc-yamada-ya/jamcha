package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.nodeTypes.NodeWithRange;
import jp.co.tdc.jamcha.model.Position;

public class ResolveMetadataHelper {
    Position begin(NodeWithRange<?> r) {
        return r.getBegin().map(p -> new Position(p.line, p.column)).orElse(new Position(0, 0));
    }
}
