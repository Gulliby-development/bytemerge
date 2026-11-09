package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class TrainTiesTest {
    @Test
    void rows() {
        row(List.of("train", "--merges", "1"), "ab cd ab cd", 0, "bytemerge-model 1\n\\s c\n", "");
        row(List.of("train", "--merges", "1"), "ba ab ba ab", 0, "bytemerge-model 1\n\\s a\n", "");
        row(List.of("train", "--merges", "3"), "aaa aaa", 0, "bytemerge-model 1\na a\naa a\n", "");
        row(List.of("train", "--merges", "2"), "aaaa aaaa", 0, "bytemerge-model 1\na a\naa aa\n", "");
        row(List.of("train", "--merges", "4"), "abc abc abc abc", 0, "bytemerge-model 1\na b\nab c\n\\s abc\n", "");
        row(List.of("train", "--merges", "3"), "abcd abcd", 0, "bytemerge-model 1\na b\nc d\nab cd\n", "");
        row(List.of("train", "--merges", "5"), "xy xy zxy zxy", 0, "bytemerge-model 1\nx y\n\\s z\n\\sz xy\n", "");
        row(List.of("train", "--merges", "2"), "bb bb bb aa aa aa", 0, "bytemerge-model 1\n\\s a\nb b\n", "");
        row(List.of("train", "--merges", "2"), "ab ab ab ba ba", 0, "bytemerge-model 1\na b\n\\s b\n", "");
        row(List.of("train", "--merges", "3"), "abab abab abab", 0, "bytemerge-model 1\na b\nab ab\n\\s abab\n", "");
        row(List.of("train", "--merges", "6"), "abcabc abcabc", 0, "bytemerge-model 1\na b\nab c\nabc abc\n", "");
        row(List.of("train", "--merges", "3"), "\u00e9\u00e9 \u00e9\u00e9 ee ee", 0, "bytemerge-model 1\n\\xc3 \\xa9\n\\s e\n\\xc3\\xa9 \\xc3\\xa9\n", "");
    }
}
