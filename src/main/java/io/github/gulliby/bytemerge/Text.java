package io.github.gulliby.bytemerge;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** UTF-8 word splitting and byte display conversion. */
public final class Text {
    private Text() {
    }

    /**
     * Splits text into words while attaching the final whitespace byte before each run.
     *
     * @param text UTF-8 text to split
     * @return copied byte slices whose concatenation is the input
     */
    public static List<byte[]> words(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        List<byte[]> result = new ArrayList<>();
        int index = 0;
        while (index < bytes.length) {
            if (isWhitespace(bytes[index] & 0xff)) {
                int end = index + 1;
                while (end < bytes.length && isWhitespace(bytes[end] & 0xff)) {
                    end++;
                }
                if (end == bytes.length) {
                    result.add(slice(bytes, index, end));
                    index = end;
                } else {
                    if (end - index > 1) {
                        result.add(slice(bytes, index, end - 1));
                    }
                    int wordEnd = end + 1;
                    while (wordEnd < bytes.length && !isWhitespace(bytes[wordEnd] & 0xff)) {
                        wordEnd++;
                    }
                    result.add(slice(bytes, end - 1, wordEnd));
                    index = wordEnd;
                }
            } else {
                int end = index + 1;
                while (end < bytes.length && !isWhitespace(bytes[end] & 0xff)) {
                    end++;
                }
                result.add(slice(bytes, index, end));
                index = end;
            }
        }
        return result;
    }

    /**
     * Converts bytes to the canonical, unambiguous display form.
     *
     * @param bytes bytes to display
     * @return escaped display text
     */
    public static String show(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        char[] hex = "0123456789abcdef".toCharArray();
        for (byte value : bytes) {
            int current = value & 0xff;
            if (current == '\\') {
                result.append("\\\\");
            } else if (current == ' ') {
                result.append("\\s");
            } else if (current == '\n') {
                result.append("\\n");
            } else if (current == '\r') {
                result.append("\\r");
            } else if (current == '\t') {
                result.append("\\t");
            } else if (current >= '!' && current <= '~') {
                result.append((char) current);
            } else {
                result.append("\\x").append(hex[current >>> 4]).append(hex[current & 0x0f]);
            }
        }
        return result.toString();
    }

    /**
     * Parses a display form, returning null when its syntax is invalid.
     *
     * @param display display text to parse
     * @return decoded bytes, or null for invalid syntax
     */
    public static byte[] unshow(String display) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        for (int index = 0; index < display.length();) {
            char current = display.charAt(index);
            if (current >= '!' && current <= '~' && current != '\\') {
                result.write(current);
                index++;
                continue;
            }
            if (current != '\\' || index + 1 >= display.length()) {
                return null;
            }
            char escape = display.charAt(index + 1);
            switch (escape) {
                case '\\' -> {
                    result.write('\\');
                    index += 2;
                }
                case 's' -> {
                    result.write(' ');
                    index += 2;
                }
                case 'n' -> {
                    result.write('\n');
                    index += 2;
                }
                case 'r' -> {
                    result.write('\r');
                    index += 2;
                }
                case 't' -> {
                    result.write('\t');
                    index += 2;
                }
                case 'x' -> {
                    if (index + 3 >= display.length()) {
                        return null;
                    }
                    int high = lowerHex(display.charAt(index + 2));
                    int low = lowerHex(display.charAt(index + 3));
                    if (high < 0 || low < 0) {
                        return null;
                    }
                    result.write((high << 4) | low);
                    index += 4;
                }
                default -> {
                    return null;
                }
            }
        }
        return result.toByteArray();
    }

    private static boolean isWhitespace(int value) {
        return value == 32 || value == 9 || value == 10 || value == 13;
    }

    private static byte[] slice(byte[] bytes, int start, int end) {
        byte[] result = new byte[end - start];
        System.arraycopy(bytes, start, result, 0, result.length);
        return result;
    }

    private static int lowerHex(char value) {
        if (value >= '0' && value <= '9') {
            return value - '0';
        }
        if (value >= 'a' && value <= 'f') {
            return value - 'a' + 10;
        }
        return -1;
    }
}
