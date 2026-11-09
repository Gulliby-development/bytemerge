package io.github.gulliby.bytemerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

/** The row helper of the tests. */
final class Rows {
    private Rows() {
    }

    /** Runs the command line with the text as standard input and compares the exit status, the standard output and the standard error. */
    static void row(List<String> args, String stdin, int status, String out, String err) {
        Api.Result result = Cli.run(args, stdin);
        assertEquals(new Api.Result(status, out, err), result, () -> "row " + args);
    }

    /** Compares Cli.readsStdin with the expected value. */
    static void reads(List<String> args, boolean expected) {
        assertEquals(expected, Cli.readsStdin(args), () -> "reads " + args);
    }
}
