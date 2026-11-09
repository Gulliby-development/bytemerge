package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class TrainLimitsTest {
    @Test
    void rows() {
        row(List.of("train", "--merges", "0"), "aa aa aa", 0, "bytemerge-model 1\n", "");
        row(List.of("train", "--merges", "1"), "aa aa aa", 0, "bytemerge-model 1\na a\n", "");
        row(List.of("train", "--merges", "5"), "aa aa aa", 0, "bytemerge-model 1\na a\n\\s aa\n", "");
        row(List.of("train", "--min-count", "1"), "ab cd", 0, "bytemerge-model 1\n\\s c\na b\n\\sc d\n", "");
        row(List.of("train", "--min-count", "3"), "ab ab cd cd", 0, "bytemerge-model 1\n", "");
        row(List.of("train", "--min-count", "2"), "ab cd ef", 0, "bytemerge-model 1\n", "");
        row(List.of("train", "--min-count", "100"), "ab ab ab", 0, "bytemerge-model 1\n", "");
        row(List.of("train"), "", 0, "bytemerge-model 1\n", "");
        row(List.of("train"), "   ", 0, "bytemerge-model 1\n\\s \\s\n", "");
        row(List.of("train", "--min-count", "1", "--merges", "20"), "abc", 0, "bytemerge-model 1\na b\nab c\n", "");
        row(List.of("train", "--merges", "1000", "--min-count", "1"), "hello", 0, "bytemerge-model 1\ne l\nh el\nl o\nhel lo\n", "");
        row(List.of("train", "--min-count", "2", "--merges", "2"), "ab ab ab ab ab ab", 0, "bytemerge-model 1\na b\n\\s ab\n", "");
    }
}
