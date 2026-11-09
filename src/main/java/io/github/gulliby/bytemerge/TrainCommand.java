package io.github.gulliby.bytemerge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** The train command. */
public final class TrainCommand {
    private TrainCommand() {
    }

    public static String run(Api.Options options, String stdin) throws Api.Failure {
        Map<String, WordState> wordsByBytes = new LinkedHashMap<>();
        for (byte[] word : Text.words(stdin)) {
            String key = Text.show(word);
            WordState state = wordsByBytes.get(key);
            if (state == null) {
                List<Integer> ids = new ArrayList<>(word.length);
                for (byte value : word) {
                    ids.add(value & 0xff);
                }
                wordsByBytes.put(key, new WordState(ids));
            } else {
                state.frequency++;
            }
        }

        List<WordState> words = new ArrayList<>(wordsByBytes.values());
        List<byte[]> tokens = new ArrayList<>();
        Set<String> tokenKeys = new HashSet<>();
        for (int value = 0; value < 256; value++) {
            byte[] token = {(byte) value};
            tokens.add(token);
            tokenKeys.add(Text.show(token));
        }

        int mergeLimit = options.merges() == null ? 10 : options.merges();
        int minCount = options.minCount() == null ? 2 : options.minCount();
        StringBuilder result = new StringBuilder(Model.HEADER).append('\n');
        for (int mergeCount = 0; mergeCount < mergeLimit; mergeCount++) {
            Map<Pair, Long> totals = new HashMap<>();
            for (WordState word : words) {
                for (int index = 0; index + 1 < word.ids.size(); index++) {
                    int left = word.ids.get(index);
                    int right = word.ids.get(index + 1);
                    byte[] joined = concatenate(tokens.get(left), tokens.get(right));
                    if (!tokenKeys.contains(Text.show(joined))) {
                        totals.merge(new Pair(left, right), word.frequency, Long::sum);
                    }
                }
            }

            Pair best = null;
            long bestTotal = Long.MIN_VALUE;
            for (Map.Entry<Pair, Long> entry : totals.entrySet()) {
                Pair pair = entry.getKey();
                long total = entry.getValue();
                if (best == null || total > bestTotal
                        || (total == bestTotal && (pair.left < best.left
                        || (pair.left == best.left && pair.right < best.right)))) {
                    best = pair;
                    bestTotal = total;
                }
            }
            if (best == null || bestTotal < minCount) {
                break;
            }

            byte[] leftBytes = tokens.get(best.left);
            byte[] rightBytes = tokens.get(best.right);
            byte[] mergedBytes = concatenate(leftBytes, rightBytes);
            int mergedId = tokens.size();
            result.append(Text.show(leftBytes)).append(' ')
                    .append(Text.show(rightBytes)).append('\n');
            tokens.add(mergedBytes);
            tokenKeys.add(Text.show(mergedBytes));
            for (WordState word : words) {
                word.ids = Bpe.applyMerge(word.ids, best.left, best.right, mergedId);
            }
        }
        return result.toString();
    }

    private static byte[] concatenate(byte[] left, byte[] right) {
        byte[] joined = new byte[left.length + right.length];
        System.arraycopy(left, 0, joined, 0, left.length);
        System.arraycopy(right, 0, joined, left.length, right.length);
        return joined;
    }

    private static final class WordState {
        private List<Integer> ids;
        private long frequency = 1;

        private WordState(List<Integer> ids) {
            this.ids = ids;
        }
    }

    private record Pair(int left, int right) {
    }
}
