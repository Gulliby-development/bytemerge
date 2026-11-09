package io.github.gulliby.bytemerge;

import io.github.gulliby.bytemerge.Api.Command;
import java.util.List;
import java.util.Set;

/** The command table, in the order of the usage text. */
final class Registry {
    private Registry() {
    }

    static final List<Command> COMMANDS = List.of(
            new Command("count", Set.of("--top"), false, CountCommand::run),
            new Command("vocab", Set.of("--model"), true, VocabCommand::run),
            new Command("train", Set.of("--merges", "--min-count"), false, TrainCommand::run),
            new Command("encode", Set.of("--model", "--ids"), true, EncodeCommand::run),
            new Command("decode", Set.of("--model"), true, DecodeCommand::run),
            new Command("stats", Set.of("--model"), true, StatsCommand::run));
}
