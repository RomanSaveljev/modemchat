package com.github.RomanSaveljev.modemchat.syntax

class EchoInputTest extends GroovyTestCase {
    void testNoEcho() {
        def echo = new EchoInput() {
            @Override
            boolean getEchoEnabled() {
                return false
            }
        }
        def input = [1, 2, 3] as Queue
        def output = echo.consumeAndEchoOne(input)
        assert output.empty
        assert input.poll() == 2
        assert input.poll() == 3
        assert input.empty
    }
    void testEcho() {
        def echo = new EchoInput() {
            @Override
            boolean getEchoEnabled() {
                return true
            }
        }
        def input = [1, 2, 3] as Queue
        def output = echo.consumeAndEchoOne(input)
        assert output[0] == 1
        assert output.size() == 1
        assert input.poll() == 2
        assert input.poll() == 3
        assert input.empty
    }
}
