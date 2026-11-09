package io.github.gulliby.bytemerge;

/** The vocab command. */
public final class VocabCommand {
    private VocabCommand() {
    }

    public static String run(Api.Options options, String stdin) throws Api.Failure {
        Model model = Model.parse(options.model());
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < model.merges(); index++) {
            int[] operands = model.merge(index);
            int id = 256 + index;
            result.append(id)
                    .append(' ')
                    .append(Text.show(model.bytes(operands[0])))
                    .append(' ')
                    .append(Text.show(model.bytes(operands[1])))
                    .append(' ')
                    .append(Text.show(model.bytes(id)))
                    .append('\n');
        }
        return result.toString();
    }
}
