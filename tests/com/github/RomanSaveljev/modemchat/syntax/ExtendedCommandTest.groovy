package com.github.RomanSaveljev.modemchat.syntax

class ExtendedCommandTest extends GroovyTestCase {
    private static Queue<Character> convert(String command) {
        command.toCharArray().collect({ it as Character }) as Queue<Character>
    }

    void testOneCharacterCommand() {
        def input = convert("+A")
        def result = ExtendedCommand.next(input)
        assert result == convert("+A")
        assert input.empty
    }

    void testLeavesSemicolon() {
        def input = convert("+ABCD;")
        def result = ExtendedCommand.next(input)
        assert result == convert("+ABCD")
        assert input == convert(";")
    }

    void testStopsAtSemicolon() {
        def input = convert("+ABCD;ZZZZ")
        def result = ExtendedCommand.next(input)
        assert result == convert("+ABCD")
        assert input == convert(";ZZZZ")
    }

    void testDetectsLastReadCommand() {
        def input = convert("+A?")
        def result = ExtendedCommand.next(input)
        assert result == convert("+A?")
        assert input.empty
    }

    void testDetectsFirstReadCommand() {
        def input = convert("+A?;")
        def result = ExtendedCommand.next(input)
        assert result == convert("+A?")
        assert input == convert(";")
    }

    void testEmptyValueInParameterCommand() {
        def input = convert("+A=")
        def result = ExtendedCommand.next(input)
        assert result == convert("+A=")
        assert input.empty
    }

    void testTestCommand() {
        def input = convert("+ABCD=?")
        def result = ExtendedCommand.next(input)
        assert result == convert("+ABCD=?")
        assert input.empty
    }

    void testSemicolonAfterEmptyParameterCommand() {
        def input = convert("+A=;")
        def result = ExtendedCommand.next(input)
        assert result == convert("+A=")
        assert input == convert(";")
    }

    void testValueInParameterCommand() {
        def input = convert("+A=1,22,333")
        def result = ExtendedCommand.next(input)
        assert result == convert("+A=")
        assert input == convert("1,22,333")
    }

    void testStopsAtForbiddenCharacter() {
        // Real modem given "ATE?#" still runs E? and only then throws the error
        def input = convert("+AB&C")
        def result = ExtendedCommand.next(input)
        assert result == convert("+AB")
        assert input == convert("&C")
    }

    void testStopsAtForbiddenCharacterInParameterCommand() {
        def input = convert("+ABC==")
        def result = ExtendedCommand.next(input)
        assert result == convert("+ABC=")
        assert input == convert("=")
    }

    void testStopsAtForbiddenCharacterAfterReadCommand() {
        def input = convert("+AB??")
        def result = ExtendedCommand.next(input)
        assert result == convert("+AB?")
        assert input == convert("?")
    }

    void testErrorWhenStartsInvalidCharacter() {
        def input = convert("+&")
        def result = ExtendedCommand.next(input)
        assert result.empty
    }
}
