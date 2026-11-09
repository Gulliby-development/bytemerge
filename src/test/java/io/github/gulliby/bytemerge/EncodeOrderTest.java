package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class EncodeOrderTest {
    @Test
    void rows() {
        row(List.of("encode", "--model", "bytemerge-model 1\na a\naa aa\n"), "aa", 0, "aa\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na a\naa aa\n"), "aaa", 0, "aa a\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na a\naa aa\n"), "aaaa", 0, "aaaa\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na a\naa aa\n"), "aaaaa", 0, "aaaa a\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na a\naa aa\n"), "aaaaaaaa", 0, "aaaa aaaa\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nb a\na b\n"), "aba", 0, "a ba\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na b\nb a\n"), "aba", 0, "ab a\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nb c\na bc\na b\n"), "abc", 0, "abc\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na b\nab a\naba b\n"), "ababab", 0, "ab ab ab\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na b\na b\n"), "ab", 1, "", "bytemerge: bad model: line 3: duplicate token\n");
    }
}
