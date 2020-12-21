package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.model.CallDescendant;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class ExpandCallResult {
    @NonNull private final List<CallDescendant> callDescendants;
}
