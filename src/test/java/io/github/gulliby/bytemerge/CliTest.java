package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class CliTest {
    @Test
    void rows() {
        row(List.of(), "", 2, "", "usage: bytemerge count [--top N]\n       bytemerge vocab --model MODEL\n       bytemerge train [--merges N] [--min-count K]\n       bytemerge encode --model MODEL [--ids]\n       bytemerge decode --model MODEL\n       bytemerge stats --model MODEL\n");
        row(List.of("help"), "", 0, "usage: bytemerge count [--top N]\n       bytemerge vocab --model MODEL\n       bytemerge train [--merges N] [--min-count K]\n       bytemerge encode --model MODEL [--ids]\n       bytemerge decode --model MODEL\n       bytemerge stats --model MODEL\n", "");
        row(List.of("-h", "x"), "", 0, "usage: bytemerge count [--top N]\n       bytemerge vocab --model MODEL\n       bytemerge train [--merges N] [--min-count K]\n       bytemerge encode --model MODEL [--ids]\n       bytemerge decode --model MODEL\n       bytemerge stats --model MODEL\n", "");
        row(List.of("--version"), "", 0, "bytemerge 0.1.0\n", "");
        row(List.of("--version", "x"), "", 0, "bytemerge 0.1.0\n", "");
        row(List.of("frob"), "", 2, "", "bytemerge: unknown command frob\n");
        row(List.of(""), "", 2, "", "bytemerge: unknown command \n");
        row(List.of("Count"), "", 2, "", "bytemerge: unknown command Count\n");
        row(List.of("count", "extra"), "", 2, "", "bytemerge: unexpected argument extra\n");
        row(List.of("vocab"), "", 2, "", "bytemerge: vocab needs --model MODEL\n");
    }
}
