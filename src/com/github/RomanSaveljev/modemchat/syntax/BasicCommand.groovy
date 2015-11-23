package com.github.RomanSaveljev.modemchat.syntax

class BasicCommand {
    private static final String AMP = "&"
    private static final String CMD = "cmd"
    private static final String D = "D"
    private static final String S = "S"
    private static final String S_INDEX = "sIndex"
    private static final String S_INDEX_MORE = "sIndexMore"
    private static final String EQUALS = "="
    private static final String QUESTION = "?"

    static List<Character> next(Queue<Character> command) {
        def result = [] as List<Character>
        def allowed = new Allowed(AMP, CMD, D, S)
        allowed.enableOnly(AMP, CMD, D, S)
        while (!command.empty) {
            def c = command.peek()
            if (allowed.isEnabled(D)) {
                if (c == D as char) {
                    // Dial command does not understand =?, dial string follows immediately
                    // D is consumed and the rest is expected to be handled by the command itself
                    result.push(command.poll())
                    break
                }
            }
            if (allowed.isEnabled(S)) {
                if (c == S as char) {
                    result.push(command.poll())
                    allowed.enableOnly(S_INDEX)
                    continue
                }
            }
            if (allowed.isEnabled(AMP)) {
                if (c == AMP as char) {
                    result.push(command.poll())
                    allowed.enableOnly(CMD)
                    continue
                }
            }
            if (allowed.isEnabled(CMD)) {
                if (Character.isAlphabetic(c)) {
                    result.push(command.poll())
                    allowed.enableOnly(EQUALS, QUESTION)
                    continue
                } else {
                    // If the command character is not found where it should be, it is an error
                    // otherwise it is hard to avoid endless loops when parsing
                    result.clear()
                    break
                }
            }
            if (allowed.isEnabled())
        }
    }
}
