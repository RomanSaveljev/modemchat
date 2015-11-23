package com.github.RomanSaveljev.modemchat.syntax

class BasicCommandTest extends GroovyTestCase {
    private static Queue<Character> convert(String command) {
        command.toCharArray().collect({ it as Character }) as Queue<Character>
    }

    void testFindSimpleCommand() {
        def input = convert("A")
        def result = BasicCommand.next(input)
        assert result == convert("A")
        assert input.empty
    }
    void testFindWithAmpersandPrefix() {
        def input = convert("&V")
        def result = BasicCommand.next(input)
        assert result == convert("&V")
        assert input.empty
    }
    void testFindDCommand() {
        def input = convert("D")
        def result = BasicCommand.next(input)
        assert result == convert("D")
        assert input.empty
    }
    void testFindDWithDialString() {
        def input = convert("D123445#ABD;")
        def result = BasicCommand.next(input)
        assert result == convert("D")
        assert input == convert("123445#ABD;")
    }
    void testDoesNotHandleDRead() {
        def input = convert("D?")
        def result = BasicCommand.next(input)
        assert result == convert("D")
        assert input == convert("?")
    }
    void testDoesNotHandleDTest() {
        def input = convert("D=?")
        def result = BasicCommand.next(input)
        assert result == convert("D")
        assert input == convert("=?")
    }
    void testBasicReadCommand() {
        def input = convert("Z?")
        def result = BasicCommand.next(input)
        assert result == convert("Z?")
        assert input.empty
    }
    void testBasicTestCommand() {
        def input = convert("Y=?")
        def result = BasicCommand.next(input)
        assert result == convert("Y=?")
        assert input.empty
    }
    void testAmpersandReadCommand() {
        def input = convert("&X?")
        def result = BasicCommand.next(input)
        assert result == convert("&X?")
        assert input.empty
    }
    void testAmpersandTestCommand() {
        def input = convert("&W=?")
        def result = BasicCommand.next(input)
        assert result == convert("&W=?")
        assert input.empty
    }
    void testTwoActionCommands() {
        def input = convert("EH")
        def result = BasicCommand.next(input)
        assert result == convert("E")
        assert input == convert("H")
    }
    void testAmpersandCommandFollowedByAction() {
        def input = convert("&EG")
        def result = BasicCommand.next(input)
        assert result == convert("&E")
        assert input == convert("G")
    }
    void testAssignValue() {
        def input = convert("G11")
        def result = BasicCommand.next(input)
        assert result == convert("G")
        assert input == convert("11")
    }
    void testReadCommandFollowedByAction() {
        def input = convert("B?&M")
        def result = BasicCommand.next(input)
        assert result == convert("B?")
        assert input == convert("&M")
    }
    void testTestCommandFollowedByAction() {
        def input = convert("C=?U")
        def result = BasicCommand.next(input)
        assert result == convert("C=?")
        assert input == convert("U")
    }
    void testAmpersandDial() {
        def input = convert("&D")
        def result = BasicCommand.next(input)
        assert result == convert("&D")
        assert input.empty
    }
    void testReadAmpersandDial() {
        def input = convert("&D?")
        def result = BasicCommand.next(input)
        assert result == convert("&D?")
        assert input.empty
    }
    void testTestAmpersandDial() {
        def input = convert("&D=?")
        def result = BasicCommand.next(input)
        assert result == convert("&D=?")
        assert input.empty
    }
    void testAssignAmpersandDial() {
        def input = convert("&D111")
        def result = BasicCommand.next(input)
        assert result == convert("&D")
        assert input == convert("111")
    }
    void testSimpleSCommand() {
        def input = convert("S2=")
        def result = BasicCommand.next(input)
        assert result == convert("S2=")
        assert input.empty
    }
    void testLongerIndexS() {
        def input = convert("S222222=")
        def result = BasicCommand.next(input)
        assert result == convert("S222222=")
        assert input.empty
    }
    void testLongerIndexReadS() {
        def input = convert("S123?E")
        def result = BasicCommand.next(input)
        assert result == convert("S123?")
        assert input == convert("E")
    }
    void testTestS() {
        def input = convert("S7=?")
        def result = BasicCommand.next(input)
        assert result == convert("S7=?")
        assert input.empty
    }
    void testErrorFirstNotAlpha() {
        def input = convert("1")
        def result = BasicCommand.next(input)
        assert result.empty
    }
    void testErrorDoubleAmpersand() {
        def input = convert("&&")
        def result = BasicCommand.next(input)
        assert result.empty
    }
    void testErrorNotAlphaAfterAmpersand() {
        def input = convert("&#")
        def result = BasicCommand.next(input)
        assert result.empty
    }
    void testErrorNoIndexAfterS() {
        def input = convert("SS")
        def result = BasicCommand.next(input)
        assert result.empty
    }
}
