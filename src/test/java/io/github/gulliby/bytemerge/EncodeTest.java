package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class EncodeTest {
    @Test
    void rows() {
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "low", 0, "low\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "low lower", 0, "low\n\\s low e r\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\n\\s t\n\\st h\n\\sth e\nt h\n"), "the then", 0, "th e\n\\sthe n\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "", 0, "", "");
        row(List.of("encode", "--model", "bytemerge-model 1\n"), "ab", 0, "a b\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nh e\nhe l\nl o\nhel lo\n"), "hello", 0, "hello\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nh e\nhe l\nl o\nhel lo\n"), "hell", 0, "hel l\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "a\nlow", 0, "a\n\\n low\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "  low", 0, "\\s\n\\s low\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na b\nc ab\nab c\n\\xc3 \\xa9\n"), "abc cab \u00e9", 0, "abc\n\\s cab\n\\s \\xc3\\xa9\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\n\\s t\n\\st h\n\\sth e\nt h\n"), "the the", 0, "th e\n\\sthe\n", "");
        row(List.of("encode", "--model", "bad"), "a", 1, "", "bytemerge: bad model: line 1: expected \"bytemerge-model 1\"\n");
    }
}
