package io.github.gulliby.bytemerge;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

/** The decode command. */
public final class DecodeCommand {
    private DecodeCommand() {
    }

    public static String run(Api.Options options, String stdin) throws Api.Failure {
        Model model = Model.parse(options.model());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        int index = 0;
        while (index < stdin.length()) {
            while (index < stdin.length() && isSeparator(stdin.charAt(index))) {
                index++;
            }
            if (index == stdin.length()) {
                break;
            }
            int start = index;
            while (index < stdin.length() && !isSeparator(stdin.charAt(index))) {
                index++;
            }
            String token = stdin.substring(start, index);
            if (token.length() < 1 || token.length() > 7 || !isDecimal(token)) {
                throw new Api.Failure(1, "bad id \"" + token + "\"");
            }
            int id = Integer.parseInt(token);
            if (id >= model.size()) {
                throw new Api.Failure(1, "unknown id " + id);
            }
            bytes.writeBytes(model.bytes(id));
        }

        try {
            return StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes.toByteArray()))
                    .toString();
        } catch (CharacterCodingException exception) {
            throw new Api.Failure(1, "decoded bytes are not valid UTF-8");
        }
    }

    private static boolean isSeparator(char value) {
        return value == ' ' || value == '\t' || value == '\n' || value == '\r';
    }

    private static boolean isDecimal(String token) {
        for (int index = 0; index < token.length(); index++) {
            char value = token.charAt(index);
            if (value < '0' || value > '9') {
                return false;
            }
        }
        return true;
    }
}
