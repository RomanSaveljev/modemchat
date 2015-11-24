package com.github.RomanSaveljev.modemchat.syntax

class EchoInputTest extends GroovyTestCase {
    private static Queue<Character> convert(String command) {
        command.toCharArray().collect({ it as Character }) as Queue<Character>
    }

    void testNoEcho() {
        def echo = new EchoInput() {
            @Override
            boolean getEchoEnabled() {
                return false
            }
        }
        def input = convert("ABC")
        def output = echo.consumeAndEchoOne(input)
        assert output.empty
        assert input == convert("BC")
    }
    void testEcho() {
        def echo = new EchoInput() {
            @Override
            boolean getEchoEnabled() {
                return true
            }
        }
        def input = convert("ABC")
        def output = echo.consumeAndEchoOne(input)
        assert output == convert("A")
        assert input == convert("BC")
    }
}
