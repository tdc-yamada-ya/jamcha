package jp.co.tdc.jamcha.call.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NodeTest {
    @Test
    void testAdd() {
        var a = new Node<>(null, "a");
        var b = a.add("b");
        Assertions.assertEquals(b.parent(), a);
        Assertions.assertEquals("b", b.data());
    }

    @Test
    void testFindAncestor() {
        var a = new Node<>(null, "a");
        var b = a.add("b");
        var c = b.add("c");
        Assertions.assertEquals(a, c.findAncestor(n -> n.data().equals("a")));
        Assertions.assertNull(c.findAncestor(n -> n.data().equals("d")));
    }

    @Test
    void testRoute() {
        var a = new Node<>(null, "a");
        var b = a.add("b");
        var c = b.add("c");
        var d = c.add("d");
        Assertions.assertEquals(List.of(b, c), d.route(a));
        Assertions.assertEquals(List.of(), d.route(new Node<>(null, "e")));
    }
}
