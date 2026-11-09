package io.github.gulliby.bytemerge;

import java.util.ArrayList;
import java.util.List;

/** Byte-pair encoding primitives for ordered token merges. */
public final class Bpe {
    private Bpe() {
    }

    /**
     * Applies one merge rule from left to right, without overlapping replacements.
     *
     * @param word token IDs to process
     * @param left left operand ID
     * @param right right operand ID
     * @param merged replacement token ID
     * @return a fresh list containing the result
     */
    public static List<Integer> applyMerge(List<Integer> word, int left, int right, int merged) {
        List<Integer> result = new ArrayList<>(word.size());
        int index = 0;
        while (index < word.size()) {
            if (index + 1 < word.size()
                    && word.get(index).intValue() == left
                    && word.get(index + 1).intValue() == right) {
                result.add(merged);
                index += 2;
            } else {
                result.add(word.get(index));
                index++;
            }
        }
        return result;
    }

    /**
     * Encodes a byte word by applying the model's merges once each in order.
     *
     * @param model model whose ordered merges are applied
     * @param word bytes to encode
     * @return the resulting token IDs
     */
    public static List<Integer> encodeWord(Model model, byte[] word) {
        List<Integer> result = new ArrayList<>(word.length);
        for (byte value : word) {
            result.add(value & 0xff);
        }
        for (int k = 0; k < model.merges(); k++) {
            int[] merge = model.merge(k);
            result = applyMerge(result, merge[0], merge[1], 256 + k);
        }
        return result;
    }
}
