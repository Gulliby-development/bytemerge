package io.github.gulliby.bytemerge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** The byte-token vocabulary and ordered merge rules of a model. */
public final class Model {
    public static final String HEADER = "bytemerge-model 1";

    private final List<byte[]> tokens;
    private final List<int[]> merges;

    private Model(List<byte[]> tokens, List<int[]> merges) {
        this.tokens = tokens;
        this.merges = merges;
    }

    /**
     * Parses model text and validates each merge in order.
     *
     * @param text model text
     * @return the parsed model
     * @throws Api.Failure when the first invalid line is encountered
     */
    public static Model parse(String text) throws Api.Failure {
        String[] lines = text.split("\n", -1);
        int lineCount = lines.length;
        if (text.endsWith("\n")) {
            lineCount--;
        }
        if (lineCount == 0 || !HEADER.equals(lines[0])) {
            throw failure(1, "expected \"" + HEADER + "\"");
        }

        List<byte[]> tokens = new ArrayList<>();
        List<int[]> merges = new ArrayList<>();
        Map<String, Integer> idsByBytes = new HashMap<>();
        for (int value = 0; value < 256; value++) {
            byte[] token = {(byte) value};
            tokens.add(token);
            idsByBytes.put(Text.show(token), value);
        }

        for (int lineIndex = 1; lineIndex < lineCount; lineIndex++) {
            int lineNumber = lineIndex + 1;
            String line = lines[lineIndex];
            int separator = line.indexOf(' ');
            if (separator <= 0 || separator != line.lastIndexOf(' ') || separator == line.length() - 1) {
                throw failure(lineNumber, "expected two tokens");
            }

            String leftField = line.substring(0, separator);
            String rightField = line.substring(separator + 1);
            byte[] leftBytes = decode(leftField, lineNumber);
            Integer leftId = idsByBytes.get(Text.show(leftBytes));
            if (leftId == null) {
                throw failure(lineNumber, "unknown token \"" + leftField + "\"");
            }

            byte[] rightBytes = decode(rightField, lineNumber);
            Integer rightId = idsByBytes.get(Text.show(rightBytes));
            if (rightId == null) {
                throw failure(lineNumber, "unknown token \"" + rightField + "\"");
            }

            byte[] joined = new byte[leftBytes.length + rightBytes.length];
            System.arraycopy(leftBytes, 0, joined, 0, leftBytes.length);
            System.arraycopy(rightBytes, 0, joined, leftBytes.length, rightBytes.length);
            String key = Text.show(joined);
            if (idsByBytes.containsKey(key)) {
                throw failure(lineNumber, "duplicate token");
            }

            int mergedId = tokens.size();
            tokens.add(joined);
            merges.add(new int[] {leftId, rightId});
            idsByBytes.put(key, mergedId);
        }
        return new Model(tokens, merges);
    }

    /** Returns the number of tokens, including the 256 byte tokens. */
    public int size() {
        return tokens.size();
    }

    /**
     * Returns the bytes for a token id.
     *
     * @param id token id
     * @return a copy of the token bytes
     */
    public byte[] bytes(int id) {
        return tokens.get(id).clone();
    }

    /** Returns the number of merge rules. */
    public int merges() {
        return merges.size();
    }

    /**
     * Returns the operand ids for merge k.
     *
     * @param k zero-based merge index
     * @return the left and right token ids
     */
    public int[] merge(int k) {
        return merges.get(k).clone();
    }

    private static byte[] decode(String field, int lineNumber) throws Api.Failure {
        byte[] bytes = Text.unshow(field);
        if (bytes == null) {
            throw failure(lineNumber, "bad token \"" + field + "\"");
        }
        return bytes;
    }

    private static Api.Failure failure(int lineNumber, String reason) {
        return new Api.Failure(1, "bad model: line " + lineNumber + ": " + reason);
    }
}
