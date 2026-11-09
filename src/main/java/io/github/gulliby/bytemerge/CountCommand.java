package io.github.gulliby.bytemerge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** The count command. */
public final class CountCommand {
    private CountCommand() {
    }

    public static String run(Api.Options options, String stdin) throws Api.Failure {
        Map<byte[], Integer> counts = new TreeMap<>(CountCommand::compareUnsigned);
        for (byte[] word : Text.words(stdin)) {
            counts.merge(word, 1, Integer::sum);
        }

        List<Map.Entry<byte[], Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Comparator.<Map.Entry<byte[], Integer>>comparingInt(Map.Entry::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey, CountCommand::compareUnsigned));

        StringBuilder result = new StringBuilder();
        int limit = options.top() == null ? entries.size() : Math.min(options.top(), entries.size());
        for (int index = 0; index < limit; index++) {
            Map.Entry<byte[], Integer> entry = entries.get(index);
            result.append(entry.getValue()).append(' ').append(Text.show(entry.getKey())).append('\n');
        }
        return result.toString();
    }

    private static int compareUnsigned(byte[] left, byte[] right) {
        int sharedLength = Math.min(left.length, right.length);
        for (int index = 0; index < sharedLength; index++) {
            int comparison = (left[index] & 0xff) - (right[index] & 0xff);
            if (comparison != 0) {
                return comparison;
            }
        }
        return Integer.compare(left.length, right.length);
    }
}
