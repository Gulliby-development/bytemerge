package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class EncodeIdsTest {
    @Test
    void rows() {
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n", "--ids"), "low", 0, "257\n", "");
        row(List.of("encode", "--ids", "--model", "bytemerge-model 1\nl o\nlo w\n"), "low lower", 0, "257\n32 257 101 114\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\n\\s t\n\\st h\n\\sth e\nt h\n", "--ids"), "the then", 0, "259 101\n258 110\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\n", "--ids"), "ab", 0, "97 98\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nh e\nhe l\nl o\nhel lo\n", "--ids"), "hello", 0, "259\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na b\nc ab\nab c\n\\xc3 \\xa9\n", "--ids"), "abc cab \u00e9", 0, "258\n32 257\n32 259\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n", "--ids"), "", 0, "", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n", "--ids"), "\u20ac", 0, "226 130 172\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n", "--ids", "--ids"), "low", 2, "", "bytemerge: --ids given twice\n");
        row(List.of("encode", "--model", "bytemerge-model 1\nl o\nlo w\n", "--ids"), "a\n\nb", 0, "97\n10\n10 98\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\na a\naa aa\n", "--ids"), "aaaaa", 0, "257 97\n", "");
        row(List.of("encode", "--model", "bytemerge-model 1\n\\s t\n\\st h\n\\sth e\nt h\n", "--ids"), "xthe", 0, "120 259 101\n", "");
    }
}
