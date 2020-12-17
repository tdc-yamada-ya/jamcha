package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Java メソッドに関する解析情報です。
 *
 * @author Yasuhiro Yamada
 */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Caller {
    private final CallerSignature signature;
    private final List<CallerAnnotationExpr> annotationExprs;
    private final List<Callee> callees;
    private final Position begin;
}
