package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.model.Caller;
import jp.co.tdc.jamcha.model.MethodSignature;
import jp.co.tdc.jamcha.model.Type;
import jp.co.tdc.jamcha.model.TypeQualifiedName;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class TypeQualifiedNameMethodSignatureCallerMap {
    private final Map<Key, Caller> m;

    TypeQualifiedNameMethodSignatureCallerMap(List<Type> types) {
        m = new LinkedHashMap<>();

        for (var t : types) {
            for (var c : t.callers()) {
                var tqn = t.metadata().qualifiedName();
                var ms = c.metadata().methodSignature();

                if (tqn.value().isEmpty() || ms.value().isEmpty()) {
                    continue;
                }

                m.put(new Key(tqn, ms), c);
            }
        }
    }

    boolean containsKey(TypeQualifiedName tqn, MethodSignature ms) {
        return m.containsKey(new Key(tqn, ms));
    }

    Caller get(TypeQualifiedName tqn, MethodSignature ms) {
        return m.get(new Key(tqn, ms));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    static class Key {
        @NonNull private final TypeQualifiedName tqn;
        @NonNull private final MethodSignature ms;
    }
}
