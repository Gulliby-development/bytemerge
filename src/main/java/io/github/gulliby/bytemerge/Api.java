package io.github.gulliby.bytemerge;

import java.util.Set;

/** The shared types of bytemerge. */
public final class Api {
    private Api() {
    }

    /** What one command line produced: the exit status and the text for standard output and standard error. */
    public record Result(int status, String out, String err) {
    }

    /** Thrown by a command or the model reader: the exit status and the message without "bytemerge: " and the line feed. */
    public static final class Failure extends Exception {
        private static final long serialVersionUID = 1L;

        public final int status;

        public Failure(int status, String message) {
            super(message);
            this.status = status;
        }
    }

    /** The options of a command line: an option that was not given is null (ids is false). */
    public record Options(Integer top, Integer merges, Integer minCount, String model, boolean ids) {
    }

    /** A command returns the text for standard output or throws Failure. */
    @FunctionalInterface
    public interface Handler {
        String run(Options options, String stdin) throws Failure;
    }

    /** A command: its name, the options it accepts and whether it needs --model. */
    public record Command(String name, Set<String> options, boolean needsModel, Handler handler) {
    }
}
