package io.github.gulliby.bytemerge;

import io.github.gulliby.bytemerge.Api.Command;
import io.github.gulliby.bytemerge.Api.Failure;
import io.github.gulliby.bytemerge.Api.Options;
import io.github.gulliby.bytemerge.Api.Result;
import java.util.List;

/** Parses and dispatches bytemerge command lines. */
public final class Cli {
    private static final String USAGE = "usage: bytemerge count [--top N]\n"
            + "       bytemerge vocab --model MODEL\n"
            + "       bytemerge train [--merges N] [--min-count K]\n"
            + "       bytemerge encode --model MODEL [--ids]\n"
            + "       bytemerge decode --model MODEL\n"
            + "       bytemerge stats --model MODEL\n";

    private Cli() {
    }

    /** Runs a command line and returns its status and output text. */
    public static Result run(List<String> args, String stdin) {
        try {
            if (args == null) {
                return internalError();
            }
            if (args.isEmpty()) {
                return new Result(2, "", USAGE);
            }
            String first = args.get(0);
            if ("help".equals(first) || "-h".equals(first) || "--help".equals(first)) {
                return new Result(0, USAGE, "");
            }
            if ("--version".equals(first)) {
                return new Result(0, "bytemerge 0.1.0\n", "");
            }

            ParseResult parsed = parse(args);
            if (parsed.error != null) {
                return new Result(2, "", "bytemerge: " + parsed.error + "\n");
            }
            try {
                return new Result(0, parsed.invocation.command.handler().run(parsed.invocation.options, stdin), "");
            } catch (Failure failure) {
                return new Result(failure.status, "", "bytemerge: " + failure.getMessage() + "\n");
            }
        } catch (RuntimeException exception) {
            return internalError();
        }
    }

    /** Returns whether the argument list is a syntactically valid command that reads standard input. */
    public static boolean readsStdin(List<String> args) {
        try {
            if (args == null || args.isEmpty()) {
                return false;
            }
            String first = args.get(0);
            if ("help".equals(first) || "-h".equals(first) || "--help".equals(first)
                    || "--version".equals(first)) {
                return false;
            }
            return parse(args).error == null;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private static ParseResult parse(List<String> args) {
        Command command = findCommand(args.get(0));
        if (command == null) {
            return ParseResult.error("unknown command " + args.get(0));
        }

        Integer top = null;
        Integer merges = null;
        Integer minCount = null;
        String model = null;
        boolean modelGiven = false;
        boolean ids = false;
        for (int i = 1; i < args.size(); i++) {
            String word = args.get(i);
            if (!word.startsWith("-") || word.equals("-")) {
                return ParseResult.error("unexpected argument " + word);
            }
            if (!command.options().contains(word)) {
                return ParseResult.error("unknown option " + word);
            }
            if (word.equals("--ids")) {
                if (ids) {
                    return ParseResult.error("--ids given twice");
                }
                ids = true;
                continue;
            }
            if (word.equals("--top") && top != null) {
                return ParseResult.error("--top given twice");
            }
            if (word.equals("--merges") && merges != null) {
                return ParseResult.error("--merges given twice");
            }
            if (word.equals("--min-count") && minCount != null) {
                return ParseResult.error("--min-count given twice");
            }
            if (word.equals("--model") && modelGiven) {
                return ParseResult.error("--model given twice");
            }

            if (i + 1 >= args.size()) {
                return ParseResult.error(missingValue(word));
            }
            String value = args.get(++i);
            switch (word) {
                case "--top" -> {
                    top = parseNumber(value, 1, 100000);
                    if (top == null) {
                        return ParseResult.error("--top needs an integer from 1 to 100000");
                    }
                }
                case "--merges" -> {
                    merges = parseNumber(value, 0, 1000);
                    if (merges == null) {
                        return ParseResult.error("--merges needs an integer from 0 to 1000");
                    }
                }
                case "--min-count" -> {
                    minCount = parseNumber(value, 1, 1000000);
                    if (minCount == null) {
                        return ParseResult.error("--min-count needs an integer from 1 to 1000000");
                    }
                }
                case "--model" -> {
                    model = value;
                    modelGiven = true;
                }
                default -> {
                    return ParseResult.error("unknown option " + word);
                }
            }
        }

        if (command.needsModel() && !modelGiven) {
            return ParseResult.error(command.name() + " needs --model MODEL");
        }
        return ParseResult.success(new Invocation(command, new Options(top, merges, minCount, model, ids)));
    }

    private static String missingValue(String option) {
        return switch (option) {
            case "--top" -> "--top needs an integer from 1 to 100000";
            case "--merges" -> "--merges needs an integer from 0 to 1000";
            case "--min-count" -> "--min-count needs an integer from 1 to 1000000";
            case "--model" -> "--model needs a value";
            default -> "unknown option " + option;
        };
    }

    private static Integer parseNumber(String value, int minimum, int maximum) {
        if (value.length() < 1 || value.length() > 7) {
            return null;
        }
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(i);
            if (digit < '0' || digit > '9') {
                return null;
            }
        }
        int number = Integer.parseInt(value);
        return number < minimum || number > maximum ? null : number;
    }

    private static Command findCommand(String name) {
        for (Command command : Registry.COMMANDS) {
            if (command.name().equals(name)) {
                return command;
            }
        }
        return null;
    }

    private static Result internalError() {
        return new Result(1, "", "bytemerge: internal error\n");
    }

    private record Invocation(Command command, Options options) {
    }

    private static final class ParseResult {
        private final Invocation invocation;
        private final String error;

        private ParseResult(Invocation invocation, String error) {
            this.invocation = invocation;
            this.error = error;
        }

        private static ParseResult success(Invocation invocation) {
            return new ParseResult(invocation, null);
        }

        private static ParseResult error(String error) {
            return new ParseResult(null, error);
        }
    }
}
