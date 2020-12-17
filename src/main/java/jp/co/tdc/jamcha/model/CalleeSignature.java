package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Java メソッドの呼び出し先を識別するためのシグネチャです。
 *
 * @author Yasuhiro Yamada
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Accessors(fluent = true)
public class CalleeSignature {
    private final String type;
    private final String method;
}
