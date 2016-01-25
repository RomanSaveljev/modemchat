package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// A command line passed to the modem may include multiple individual commands.
// ExecuteCommand state executes only one command, because executing a command
// may switch the mode completely, i.e. new set of mixins is required.
class Begin implements StateHandler {
    StatefulContext context
    final Logger logger = LoggerFactory.getLogger(Begin.class)

    Begin(StatefulContext context) {
        this.context = context
    }

    List<Character> input(List<Character> data) {
        if (context.commandLine.isEmpty()) {
            context.stateHandler = context.stateFactory.buildPrefix(context)
        } else {
            // Continuing with the existing command line
            context.stateHandler = context.stateFactory.buildStripSemicolon(context)
        }
        return []
    }
}
