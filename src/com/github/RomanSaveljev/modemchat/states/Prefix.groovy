package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.syntax.InputEchoer

class Prefix implements StateHandler, InputEchoer {
    private StatefulContext context
    private InputEcho echo

    @Override
    List<Character> input(Queue<Character> data) {
        assert !data.empty
        def c = data.peek()
        if (c == 'A') {
            context.stateHandler = context.factory.buildCapitalA(context)
        } else if (c == 'a') {
            context.stateHandler = context.factory.buildSmallA(context)
        }
        return echo.input(data)
    }

    @Override
    V250 getV250() {
        return context.v250
    }
}
