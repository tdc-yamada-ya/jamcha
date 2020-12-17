package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Callee {
    private final CalleeSignature signature;
    private final Position begin;
}
