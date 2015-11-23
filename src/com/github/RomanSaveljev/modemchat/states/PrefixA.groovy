package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.syntax.EchoInput
import groovy.json.StringEscapeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class PrefixA implements StateHandler {
    static final char SOLIDUS = '/'
    protected StatefulContext context
    private EchoInput echo = new EchoInput() {
        @Override
        boolean getEchoEnabled() {
            PrefixA.this.@context.v250.echo
        }
    }
    final Logger logger = LoggerFactory.getLogger(PrefixA.class)

    PrefixA(StatefulContext context) {
        this.context = context
    }

    List<Character> input(Queue<Character> data) {
        logger.debug("input(${StringEscapeUtils.escapeJava(data.join(""))})")
        data.empty ? [] : doInput(data)
    }

    private List<Character> doInput(Queue<Character> data) {
        Character c = data.peek()
        if (isAttention(c)) {
            context.stateHandler = context.stateFactory.buildAssembleCommand(context)
            return echo.consumeAndEchoOne(data)
        } else if (c == SOLIDUS) {
            def output = echo.consumeAndEchoOne(data)
            data.offer(context.v250.s3)
            // previously assembled command line is fed in again as if a real modem produced it
            // helps to remove code duplication
            def repeatable = [] as ArrayList<Character>
            repeatable.addAll(context.repeatable)
            repeatable.reverse().each { data.offer(it) }
            context.stateHandler = context.stateFactory.buildRepeatCommand(context)
            return output
        } else {
            // character is not consumed - could be another A
            context.stateHandler = context.stateFactory.buildPrefix(context)
            return []
        }
    }

    protected abstract boolean isAttention(char c)
}
