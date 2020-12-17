package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.A;

/**
 * Java メソッドの呼び出し元を識別するためのシグネチャです。
 *
 * @author Yasuhiro Yamada
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Accessors(fluent = true)
public class CallerSignature {
    private final String caller;
}
