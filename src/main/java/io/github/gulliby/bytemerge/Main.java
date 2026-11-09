package io.github.gulliby.bytemerge;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** The program entry point: standard input is read only when the command line is a valid use of a command that reads it. */
public final class Main {
    private Main() {
    }

    public static void main(String[] argv) throws IOException {
        List<String> args = List.of(argv);
        String stdin = "";
        if (Cli.readsStdin(args)) {
            stdin = new String(System.in.readAllBytes(), StandardCharsets.UTF_8);
        }
        Api.Result result = Cli.run(args, stdin);
        OutputStream out = new FileOutputStream(FileDescriptor.out);
        out.write(result.out().getBytes(StandardCharsets.UTF_8));
        out.flush();
        OutputStream err = new FileOutputStream(FileDescriptor.err);
        err.write(result.err().getBytes(StandardCharsets.UTF_8));
        err.flush();
        System.exit(result.status());
    }
}
