package jp.co.tdc.jamcha.call.tree;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Tree<T> {
    private final Node<T> root;

    public Tree(T d) {
        root = newRoot(d);
    }

    Node<T> newRoot(T d) {
        return new Node<>(null, d);
    }
}
