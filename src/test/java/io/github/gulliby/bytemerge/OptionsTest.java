package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.reads;
import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class OptionsTest {
    @Test
    void rows() {
        reads(List.of("count"), true);
        reads(List.of(), false);
        reads(List.of("count", "--top", "5"), true);
        reads(List.of("count", "--top", "0"), false);
        reads(List.of("vocab"), false);
        reads(List.of("vocab", "--model", "x"), true);
        reads(List.of("encode", "--model", "x", "--ids"), true);
        reads(List.of("train", "--merges", "1001"), false);
        row(List.of("count", "--top", "100001"), "", 2, "", "bytemerge: --top needs an integer from 1 to 100000\n");
        row(List.of("train", "--min-count", "0"), "", 2, "", "bytemerge: --min-count needs an integer from 1 to 1000000\n");
        row(List.of("train", "--merges", "-1"), "", 2, "", "bytemerge: --merges needs an integer from 0 to 1000\n");
        row(List.of("encode", "--model"), "", 2, "", "bytemerge: --model needs a value\n");
        row(List.of("count", "--top", "2", "--top", "3"), "", 2, "", "bytemerge: --top given twice\n");
        row(List.of("count", "--ids"), "", 2, "", "bytemerge: unknown option --ids\n");
        row(List.of("decode", "--ids", "--model", "m"), "", 2, "", "bytemerge: unknown option --ids\n");
    }
}
