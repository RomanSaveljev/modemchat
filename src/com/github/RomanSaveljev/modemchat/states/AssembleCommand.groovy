package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.syntax.CommandLinePreprocessor

/**
 * Created by user on 11/14/15.
 */
class AssembleCommand implements StateHandler {
    StatefulContext context
    InputEcho echo
    CommandLinePreprocessor preprocessor = new CommandLinePreprocessor()

    List<Character> onTerminationCharacter(Queue<Character> data) {
        preprocessor.process(context)
        context.stateHandler = context.factory.buildExecuteCommand(context)
        return context.stateHandler.input(data)
    }
    @SuppressWarnings("GroovyUnusedDeclaration")
    List<Character> onEditingCharacter(Queue<Character> data) {
        context.repeatable.pop()
        return []
    }
    @Override
    List<Character> input(Queue<Character> data) {
        assert !data.empty
        def output = [] as ArrayList<Character>
        def c = data.peek()
        echo.input(data)
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
