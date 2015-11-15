package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.syntax.EchoInput

class Prefix implements StateHandler {
    static final char CAPITAL_A = 'A'
    static final char SMALL_A = 'a'
    private StatefulContext context
    private EchoInput echo = new EchoInput() {
        @Override
        boolean getEchoEnabled() {
            context.v250.echo
        }
    }

    Prefix(StatefulContext context) {
        this.context = context
    }

    @Override
    List<Character> input(Queue<Character> data) {
        data.empty ? [] : doInput(data)
    }

    private List<Character> doInput(Queue<Character> data) {
        def c = data.peek()
        if (c == CAPITAL_A) {
            context.stateHandler = context.stateFactory.buildCapitalA(context)
        } else if (c == SMALL_A) {
            context.stateHandler = context.stateFactory.buildSmallA(context)
        }
        return echo.consumeAndEchoOne(data)
    }
}
