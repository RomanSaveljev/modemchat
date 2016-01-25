package com.github.RomanSaveljev.modemchat.syntax

class BasicCommand {
    private static final char AMP = '&'
    private static final String CMD = "cmd"
    private static final char D = 'D'
    private static final char S = 'S'
    private static final String S_INDEX = "sIndex"
    private static final String S_INDEX_MORE = "sIndexMore"
    private static final char EQUALS = '='
    private static final char QUESTION = '?'

    static List<Character> next(List<Character> command) {
        def result = [] as List<Character>
        def allowed = new Allowed(AMP, CMD, D, S)
        allowed.enableOnly(AMP, CMD, D, S)
        while (!command.empty) {
            def c = command.first()
            if (allowed.isEnabled(D)) {
                if (c == D) {
                    // Dial command does not understand =?, dial string follows immediately
                    // D is consumed and the rest is expected to be handled by the command itself
                    result.push(command.removeAt(0))
                    break
                }
            }
            if (allowed.isEnabled(S)) {
                if (c == S) {
                    result.push(command.removeAt(0))
                    allowed.enableOnly(S_INDEX)
                    continue
                }
            }
            if (allowed.isEnabled(AMP)) {
                if (c == AMP) {
                    result.push(command.removeAt(0))
                    allowed.enableOnly(CMD)
                    continue
                }
            }
            if (allowed.isEnabled(CMD)) {
                if (Character.isLetter(c)) {
                    result.push(command.removeAt(0))
                    allowed.enableOnly(EQUALS, QUESTION)
                    continue
                } else {
                    // If the command character is not found where it should be, it is an error
                    // otherwise it is hard to avoid endless loops when parsing
                    result.clear()
                    break
                }
            }
            if (allowed.isEnabled(S_INDEX)) {
                if (Character.isDigit(c)) {
                    result.push(command.removeAt(0))
                    allowed.enableOnly(S_INDEX_MORE, EQUALS, QUESTION)
                    continue
                } else {
                    // Index shall follow every S command
                    result.clear()
                    break
                }
            }
            if (allowed.isEnabled(S_INDEX_MORE)) {
                if (Character.isDigit(c)) {
                    result.push(command.removeAt(0))
                    continue
                } else {
                    // this is not a digit, maybe this is = or ?
                    allowed.enableOnly(EQUALS, QUESTION)
                }
            }
            if (allowed.isEnabled(EQUALS)) {
                if (c == EQUALS) {
                    result.push(command.removeAt(0))
                    allowed.enableOnly(QUESTION)
                    continue
                }
            }
            if (allowed.isEnabled(QUESTION)) {
                if (c == QUESTION) {
                    result.push(command.removeAt(0))
                    allowed.disableAll()
                    continue
                }
            }
            // Did not meet any criteria - can not continue
            break
        }
        return result
    }
}
