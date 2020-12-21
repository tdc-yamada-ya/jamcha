package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.model.Caller;
import jp.co.tdc.jamcha.model.MethodQualifiedSignature;
import jp.co.tdc.jamcha.model.Type;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class MethodQualifiedSignatureCallerMap {
    private final Map<MethodQualifiedSignature, Caller> m;

    MethodQualifiedSignatureCallerMap(List<Type> types) {
        m = new LinkedHashMap<>();

        for (var t : types) {
            for (var c : t.callers()) {
                if (c.metadata().methodQualifiedSignature().value().isEmpty()) {
                    continue;
                }
                m.put(c.metadata().methodQualifiedSignature(), c);
            }
        }
    }

    boolean containsKey(MethodQualifiedSignature k) {
        return m.containsKey(k);
    }

    Caller get(MethodQualifiedSignature k) {
        return m.get(k);
    }
}
