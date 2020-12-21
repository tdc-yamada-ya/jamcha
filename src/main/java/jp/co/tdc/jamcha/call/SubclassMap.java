package jp.co.tdc.jamcha.call;

import jp.co.tdc.jamcha.model.Type;
import jp.co.tdc.jamcha.model.TypeQualifiedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class SubclassMap {
    private final Map<TypeQualifiedName, List<TypeQualifiedName>> m;

    SubclassMap(List<Type> types) {
        m = new LinkedHashMap<>();

        for (var t : types) {
            var ctqn = t.metadata().qualifiedName();
            for (var p : t.metadata().ancestorSuperTypes()) {
                var tqn = p.qualifiedName();

                if (!m.containsKey(tqn)) {
                    m.put(tqn, new ArrayList<>());
                }

                m.get(tqn).add(ctqn);
            }
        }
    }

    boolean containsKey(TypeQualifiedName k) {
        return m.containsKey(k);
    }

    List<TypeQualifiedName> get(TypeQualifiedName k) {
        return m.get(k);
    }
}
