package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class StatsTest {
    @Test
    void rows() {
        row(List.of("stats", "--model", "bytemerge-model 1\nl o\nlo w\n"), "low low lower", 0, "words 3\nbytes 13\ntokens 7\nunique 4\nratio 1.86\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\nl o\nlo w\n"), "", 0, "words 0\nbytes 0\ntokens 0\nunique 0\nratio 0.00\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\n"), "ab", 0, "words 1\nbytes 2\ntokens 2\nunique 2\nratio 1.00\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\n\\s t\n\\st h\n\\sth e\nt h\n"), "the the the", 0, "words 3\nbytes 11\ntokens 4\nunique 3\nratio 2.75\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\nl o\nlo w\n"), "xyz", 0, "words 1\nbytes 3\ntokens 3\nunique 3\nratio 1.00\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\na a\naa aa\n"), "aaaa aaaa", 0, "words 2\nbytes 9\ntokens 3\nunique 2\nratio 3.00\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\nh e\nhe l\nl o\nhel lo\n"), "hello hello hell", 0, "words 3\nbytes 16\ntokens 6\nunique 4\nratio 2.67\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\na b\nc ab\nab c\n\\xc3 \\xa9\n"), "\u00e9\u00e9", 0, "words 1\nbytes 4\ntokens 2\nunique 1\nratio 2.00\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\na a\naa aa\n"), "aaaaaaa", 0, "words 1\nbytes 7\ntokens 3\nunique 3\nratio 2.33\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\nl o\nlo w\n"), "lo lo lo\n", 0, "words 4\nbytes 9\ntokens 6\nunique 3\nratio 1.50\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\n"), "abc", 0, "words 1\nbytes 3\ntokens 3\nunique 3\nratio 1.00\n", "");
        row(List.of("stats", "--model", "bytemerge-model 1\na a\naa aa\n"), "aaaaaaaaaaa", 0, "words 1\nbytes 11\ntokens 4\nunique 3\nratio 2.75\n", "");
    }
}
