package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class DisplayTest {
    @Test
    void rows() {
        row(List.of("count"), "a\\b", 0, "1 a\\\\b\n", "");
        row(List.of("count"), "\u0001", 0, "1 \\x01\n", "");
        row(List.of("count"), "x\u007f", 0, "1 x\\x7f\n", "");
        row(List.of("count"), "\u00e9", 0, "1 \\xc3\\xa9\n", "");
        row(List.of("count"), "caf\u00e9 \u20ac", 0, "1 \\s\\xe2\\x82\\xac\n1 caf\\xc3\\xa9\n", "");
        row(List.of("count"), "\ud83d\ude00", 0, "1 \\xf0\\x9f\\x98\\x80\n", "");
        row(List.of("count"), "b a b a \u00e9 \u00e9 z", 0, "2 \\sa\n2 \\s\\xc3\\xa9\n1 \\sb\n1 \\sz\n1 b\n", "");
        row(List.of("count", "--top", "1"), "a b a c", 0, "1 \\sa\n", "");
        row(List.of("count", "--top", "2"), "a b a c b a", 0, "2 \\sa\n2 \\sb\n", "");
        row(List.of("count", "--top", "9"), "a b", 0, "1 \\sb\n1 a\n", "");
        row(List.of("count"), "b a c", 0, "1 \\sa\n1 \\sc\n1 b\n", "");
    }
}
