package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Java クラスあるいはインタフェースに関する解析情報です。
 *
 * @author Yasuhiro Yamada
 */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Type {
    private final TypeSignature signature;
    private final List<TypeAnnotationExpr> annotationExprs;
    private final List<Caller> callers;
    private final Position begin;
}
