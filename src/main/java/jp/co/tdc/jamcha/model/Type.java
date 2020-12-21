package jp.co.tdc.jamcha.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Type {
    @NonNull private final TypeMetadata metadata;
    @NonNull private final List<Caller> callers;
}
