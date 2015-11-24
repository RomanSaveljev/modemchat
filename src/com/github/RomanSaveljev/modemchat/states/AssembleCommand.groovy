package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.syntax.CompactCommandLine
import com.github.RomanSaveljev.modemchat.syntax.EchoInput
import groovy.json.StringEscapeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AssembleCommand implements StateHandler {
    private StatefulContext context
    private def echo = new EchoInput() {
        @Override
        boolean getEchoEnabled() {
            context.v250.echo
        }
    }
    private def compactor = new CompactCommandLine() {
        @Override
        List<Character> getRepeatable() {
            context.repeatable
        }

        @Override
        Queue<Character> getCommandLine() {
            context.commandLine
        }
    }
    final Logger logger = LoggerFactory.getLogger(AssembleCommand.class)

    AssembleCommand(StatefulContext context) {
        this.context = context
        context.repeatable.clear()
    }

    AssembleCommand(StatefulContext context, EchoInput echo) {
        this(context)
        this.echo = echo
    }

    List<Character> onTerminationCharacter(Queue<Character> data) {
        compactor.compactCommandLine()
        context.stateHandler = context.stateFactory.buildExecuteCommand(context)
        return context.stateHandler.input(data)
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    List<Character> onEditingCharacter(Queue<Character> data) {
        context.repeatable.pop()
        return []
    }

    @Override
    List<Character> input(Queue<Character> data) {
        logger.debug("input(${StringEscapeUtils.escapeJava(data.join(""))})")
        data.empty ? [] : doInput(data)
    }

    private List<Character> doInput(Queue<Character> data) {
        def c = data.peek()
        def output = echo.consumeAndEchoOne(data)
        switch (c) {
            case context.v250.s3:
                output.addAll(onTerminationCharacter(data))
                break
            case context.v250.s5:
                output.addAll(onEditingCharacter(data))
                break
            default:
                context.repeatable.add(c)
                break
        }
        return output
    }
}
