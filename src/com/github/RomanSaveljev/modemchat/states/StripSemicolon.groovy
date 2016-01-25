package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext

// Removes one semicolon character, if the collected command line starts with it
// This state is entered only when Begin starts with non-empty command line, i.e.
// it runs next command from the command line
class StripSemicolon implements StateHandler {
    static final char SEMICOLON = ';'
    StatefulContext context
    StripSemicolon(StatefulContext context) {
        this.context = context
    }
    List<Character> input(List<Character> data) {
        if (context.commandLine.head() == SEMICOLON) {
            context.commandLine.removeAt(0)
        }
        // no else - sometimes it is OK not to have a semicolon between commands
        context.stateHandler = context.stateFactory.buildExecuteCommand(context)
        return []
    }
}
