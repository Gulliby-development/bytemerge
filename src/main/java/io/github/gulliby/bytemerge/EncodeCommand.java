package io.github.gulliby.bytemerge;

import java.util.List;

/** The encode command. */
public final class EncodeCommand {
    private EncodeCommand() {
    }

    public static String run(Api.Options options, String stdin) throws Api.Failure {
        Model model = Model.parse(options.model());
        StringBuilder output = new StringBuilder();
        for (byte[] word : Text.words(stdin)) {
            List<Integer> ids = Bpe.encodeWord(model, word);
            for (int index = 0; index < ids.size(); index++) {
                if (index > 0) {
                    output.append(' ');
                }
                int id = ids.get(index);
                if (options.ids()) {
                    output.append(id);
                } else {
                    output.append(Text.show(model.bytes(id)));
                }
            }
            output.append('\n');
        }
        return output.toString();
    }
}
