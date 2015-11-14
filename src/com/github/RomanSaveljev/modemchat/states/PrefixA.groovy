package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext

/**
 * Created by user on 11/14/15.
 */
abstract class PrefixA implements StateHandler {
    private StatefulContext context
    private InputEcho echo
    List<Character> input(Queue<Character> data) {
        assert data.size()
        def c = data.peek()
        if (isAttention(c)) {
            context.stateHandler = context.factory.buildAssembleCommand(context)
            return echo.input(data)
        } else if (c == '/') {
            def output = echo.input(data)
            data.offer(context.v250.s3)
            // previously assembled command line is fed in again as if a real modem produced it
            // helps to remove code duplication
            context.repeatable?.reverse().each {data.offer(it)}
            context.stateHandler = context.factory.buildRepeatCommand(context)
            return output
        } else {
            // character is not consumed - could be another A
            context.stateHandler = context.factory.buildPrefix(context)
            return []
        }
    }
    abstract boolean isAttention(char c)
}
