package com.github.RomanSaveljev.modemchat

import com.github.RomanSaveljev.modemchat.context.V250Settings
import com.github.RomanSaveljev.modemchat.states.InputEcho

/**
 * Created by user on 11/11/15.
 */
class InputEchoTest extends GroovyTestCase {
    def context = new V250Settings() {
        V250Settings.V250 v250 = new V250Settings.V250()
    }
    void testForceNoEcho() {
        def echo = InputEcho.NewNoEcho()
        def input = [1, 2, 3] as Queue
        def output = echo.input(input)
        assert output == []
        assert input == [2, 3]
    }
    void testDoesNotEcho() {
        context.v250.echo = false
        def echo = InputEcho.New(context)
        def input = [1, 2, 3] as Queue
        def output = echo.input(input)
        assert output == []
        assert input == [2, 3]
    }
    void testDoesEcho() {
        context.v250.echo = true
        def echo = InputEcho.New(context)
        def input = [1, 2, 3] as Queue
        def output = echo.input(input)
        assert output == [1]
        assert input == [2, 3]
    }

}
