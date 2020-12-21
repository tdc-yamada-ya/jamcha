package jp.co.tdc.jamcha.call.tree;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor(access = AccessLevel.MODULE)
@Getter
@Accessors(fluent = true)
public class Node<T> {
    private final Node<T> parent;
    private final List<Node<T>> childs = new ArrayList<>();
    @NonNull private final T data;

    public Node<T> add(T d) {
        var n = new Node<T>(this, d);
        childs.add(n);
        return n;
    }

    public Node<T> findAncestor(Predicate<Node<T>> p) {
        for (var c = this; c != null; c = c.parent()) {
            if (p.test(c)) {
                return c;
            }
        }

        return null;
    }

    public List<Node<T>> route(Node<T> ancestor) {
        var l = new ArrayList<Node<T>>();

        for (var c = parent; c != ancestor; c = c.parent()) {
            if (c == null) {
                return List.of();
            }

            l.add(0, c);
        }

        return l;
    }
}
