package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class ModelTest {
    @Test
    void rows() {
        row(List.of("vocab", "--model", "bytemerge-model 1\n"), "", 0, "", "");
        row(List.of("vocab", "--model", "bytemerge-model 1\nh e\nhe l\n"), "", 0, "256 h e he\n257 he l hel\n", "");
        row(List.of("vocab", "--model", "bytemerge-model 1\n\\s a\n"), "", 0, "256 \\s a \\sa\n", "");
        row(List.of("vocab", "--model", "bytemerge-model 1\n\\x41 \\x42\n"), "", 0, "256 A B AB\n", "");
        row(List.of("vocab", "--model", "bytemerge-model 1\n\\xc3 \\xa9\n"), "", 0, "256 \\xc3 \\xa9 \\xc3\\xa9\n", "");
        row(List.of("vocab", "--model", "bytemerge-model 1\na b\nab c\nc ab\n"), "", 0, "256 a b ab\n257 ab c abc\n258 c ab cab\n", "");
        row(List.of("vocab", "--model", "bytemerge-model 1\n\\\\ n\n\\n \\t\n"), "", 0, "256 \\\\ n \\\\n\n257 \\n \\t \\n\\t\n", "");
        row(List.of("vocab", "--model", ""), "", 1, "", "bytemerge: bad model: line 1: expected \"bytemerge-model 1\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 2\n"), "", 1, "", "bytemerge: bad model: line 1: expected \"bytemerge-model 1\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\r\n"), "", 1, "", "bytemerge: bad model: line 1: expected \"bytemerge-model 1\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\na\n"), "", 1, "", "bytemerge: bad model: line 2: expected two tokens\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\n\na b\n"), "", 1, "", "bytemerge: bad model: line 2: expected two tokens\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\na b c\n"), "", 1, "", "bytemerge: bad model: line 2: expected two tokens\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\na \\q\n"), "", 1, "", "bytemerge: bad model: line 2: bad token \"\\q\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\n\\x4A a\n"), "", 1, "", "bytemerge: bad model: line 2: bad token \"\\x4A\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\nab c\n"), "", 1, "", "bytemerge: bad model: line 2: unknown token \"ab\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\nab \\q\n"), "", 1, "", "bytemerge: bad model: line 2: unknown token \"ab\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\nb c\na bc\nab c\n"), "", 1, "", "bytemerge: bad model: line 4: unknown token \"ab\"\n");
        row(List.of("vocab", "--model", "bytemerge-model 1\na b\nc d\nab zz\n"), "", 1, "", "bytemerge: bad model: line 4: unknown token \"zz\"\n");
    }
}
