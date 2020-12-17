package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 型の完全修飾名の値オブジェクトです。
 *
 * @author Yasuhiro Yamada
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Accessors(fluent = true)
public class TypeQualifiedName {
    @NonNull private final String value;

    @Override
    public String toString() {
        return value;
    }
}
