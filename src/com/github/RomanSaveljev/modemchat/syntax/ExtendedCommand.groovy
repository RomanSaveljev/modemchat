package com.github.RomanSaveljev.modemchat.syntax

class ExtendedCommand {
    private static final char PLUS = '+'
    private static final String ALPHA = 'alpha'
    private static final String EXTENDED = 'extended'
    private static final char EQUALS = '='
    private static final char QUESTION = '?'

    private static boolean isExtendedCommandCharacter(char c) {
        final char[] ADDITIONAL_EXTENDED_CHARACTERS = "!%-./:_".toCharArray()
        Character.isLetter(c) || Character.isDigit(c) || ADDITIONAL_EXTENDED_CHARACTERS.find({it == c}) != null
    }

    static List<Character> next(List<Character> command) {
        def result = [] as List<Character>
        def allowed = new Allowed(PLUS)
        while (!command.empty) {
            def c = command.head()
            if (allowed.isEnabled(PLUS)) {
                assert c == PLUS : new Error("BUG! calling ExtendedCommand#next() requires a command to start with +")
                result.push(command.removeAt(0))
                allowed.enableOnly(ALPHA, EXTENDED)
                continue
            }
            if (allowed.isEnabled(ALPHA)) {
                if (Character.isLetter(c)) {
                    result.push(command.removeAt(0))
                    allowed.enableOnly(EXTENDED, EQUALS, QUESTION)
                    continue
                } else {
                    // This is an error, first character is always [A-Z] and single + is not
                    // allowed for command
                    result.clear()
                    break
                }
            }
            if (allowed.isEnabled(EXTENDED)) {
                if (isExtendedCommandCharacter(c)) {
                    result.push(command.removeAt(0))
                } else {
                    // Try to consume the character again in the next iteration
                    allowed.enableOnly(EQUALS, QUESTION)
                }
                continue
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
            // The character did not meet any of criteria - can not continue
            break
        }
        return result
    }
}
