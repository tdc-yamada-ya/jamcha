package jp.co.tdc.jamcha.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Java クラスを識別するためのシグネチャです。
 *
 * @author Yasuhiro Yamada
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Accessors(fluent = true)
public class TypeSignature {
    private final String type;
}
