package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class DecodeTest {
    @Test
    void rows() {
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "108 111 119", 0, "low", "");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "257", 0, "low", "");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "257 108 111 119", 0, "lowlow", "");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "", 0, "", "");
        row(List.of("decode", "--model", "bytemerge-model 1\n\\s t\n\\st h\n\\sth e\nt h\n"), "256\n257\t258 259", 0, " t th theth", "");
        row(List.of("decode", "--model", "bytemerge-model 1\na b\nc ab\nab c\n\\xc3 \\xa9\n"), "259", 0, "\u00e9", "");
        row(List.of("decode", "--model", "bytemerge-model 1\n"), "104 105", 0, "hi", "");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "195", 1, "", "bytemerge: decoded bytes are not valid UTF-8\n");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "195 169", 0, "\u00e9", "");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "258", 1, "", "bytemerge: unknown id 258\n");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "104 x 105", 1, "", "bytemerge: bad id \"x\"\n");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "-1", 1, "", "bytemerge: bad id \"-1\"\n");
        row(List.of("decode", "--model", "bytemerge-model 1\nl o\nlo w\n"), "104 12345678", 1, "", "bytemerge: bad id \"12345678\"\n");
    }
}
