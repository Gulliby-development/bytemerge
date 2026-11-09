package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class PretokTest {
    @Test
    void rows() {
        row(List.of("count"), "", 0, "", "");
        row(List.of("count"), "a", 0, "1 a\n", "");
        row(List.of("count"), "a b", 0, "1 \\sb\n1 a\n", "");
        row(List.of("count"), "a  b", 0, "1 \\s\n1 \\sb\n1 a\n", "");
        row(List.of("count"), " a", 0, "1 \\sa\n", "");
        row(List.of("count"), "  a", 0, "1 \\s\n1 \\sa\n", "");
        row(List.of("count"), "a ", 0, "1 \\s\n1 a\n", "");
        row(List.of("count"), "a\nb", 0, "1 \\nb\n1 a\n", "");
        row(List.of("count"), "a\r\nb", 0, "1 \\nb\n1 \\r\n1 a\n", "");
        row(List.of("count"), "a\tb", 0, "1 \\tb\n1 a\n", "");
        row(List.of("count"), "   ", 0, "1 \\s\\s\\s\n", "");
        row(List.of("count"), "\n\n", 0, "1 \\n\\n\n", "");
        row(List.of("count"), "x x x", 0, "2 \\sx\n1 x\n", "");
        row(List.of("count"), "a\n\nb", 0, "1 \\n\n1 \\nb\n1 a\n", "");
    }
}
