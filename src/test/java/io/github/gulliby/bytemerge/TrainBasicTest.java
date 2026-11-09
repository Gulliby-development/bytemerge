package io.github.gulliby.bytemerge;

import static io.github.gulliby.bytemerge.Rows.row;

import java.util.List;
import org.junit.jupiter.api.Test;

class TrainBasicTest {
    @Test
    void rows() {
        row(List.of("train"), "low low low lower lowest", 0, "bytemerge-model 1\nl o\nlo w\n\\s low\n\\slow e\n", "");
        row(List.of("train"), "the the the the", 0, "bytemerge-model 1\nh e\nt he\n\\s the\n", "");
        row(List.of("train"), "a b a b a b", 0, "bytemerge-model 1\n\\s b\n\\s a\n", "");
        row(List.of("train"), "banana banana", 0, "bytemerge-model 1\na n\nb an\nan a\nban ana\n", "");
        row(List.of("train"), "hello hello world world", 0, "bytemerge-model 1\n\\s w\ne l\nh el\nl d\nl o\no r\n\\sw or\nhel lo\n\\swor ld\n", "");
        row(List.of("train"), "ab ab ab ab cd cd cd cd", 0, "bytemerge-model 1\n\\s c\na b\n\\sc d\n\\s ab\n", "");
        row(List.of("train"), "to be or not to be", 0, "bytemerge-model 1\n\\s b\nt o\n\\sb e\n", "");
        row(List.of("train"), "abab abab", 0, "bytemerge-model 1\na b\nab ab\n", "");
        row(List.of("train"), "mississippi mississippi", 0, "bytemerge-model 1\ni s\nis s\ni p\nm iss\np i\niss ip\nmiss issip\nmississip pi\n", "");
        row(List.of("train"), "aaaa aaaa", 0, "bytemerge-model 1\na a\naa aa\n", "");
        row(List.of("train"), "the cat the cat the cat sat", 0, "bytemerge-model 1\na t\n\\s c\nh e\nt he\n\\sc at\n\\s the\n", "");
        row(List.of("train"), "ab\nab\nab", 0, "bytemerge-model 1\na b\n\\n ab\n", "");
    }
}
