package io.github.gulliby.bytemerge;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** The stats command. */
public final class StatsCommand {
    private StatsCommand() {
    }

    public static String run(Api.Options options, String stdin) throws Api.Failure {
        Model model = Model.parse(options.model());
        List<byte[]> words = Text.words(stdin);
        long wordCount = 0;
        long bytes = 0;
        long tokens = 0;
        Set<Integer> unique = new HashSet<>();
        for (byte[] word : words) {
            wordCount++;
            bytes += word.length;
            List<Integer> encoded = Bpe.encodeWord(model, word);
            tokens += encoded.size();
            unique.addAll(encoded);
        }

        String ratio = "0.00";
        if (tokens != 0) {
            BigInteger tokenCount = BigInteger.valueOf(tokens);
            BigInteger q = BigInteger.valueOf(bytes).multiply(BigInteger.valueOf(200))
                    .add(tokenCount).divide(tokenCount.multiply(BigInteger.valueOf(2)));
            BigInteger[] parts = q.divideAndRemainder(BigInteger.valueOf(100));
            ratio = parts[0] + "." + (parts[1].intValue() < 10 ? "0" : "") + parts[1];
        }

        return "words " + wordCount + "\n"
                + "bytes " + bytes + "\n"
                + "tokens " + tokens + "\n"
                + "unique " + unique.size() + "\n"
                + "ratio " + ratio + "\n";
    }
}
