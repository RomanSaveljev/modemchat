package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.syntax.EchoInput

class StateFactory {
    static final char CAPITAL_T = 'T'
    static final char SMALL_T = 't'
    StateHandler buildPrefix(StatefulContext context) {
        new Prefix(context)
    }

    StateHandler buildCapitalA(StatefulContext context) {
        new PrefixA(context) {
            @Override
            boolean isAttention(char c) {
                c == CAPITAL_T
            }
        }
    }

    StateHandler buildSmallA(StatefulContext context) {
        new PrefixA(context) {
            @Override
            boolean isAttention(char c) {
                c == SMALL_T
            }
        }
    }

    StateHandler buildAssembleCommand(StatefulContext context) {
        new AssembleCommand(context)
    }

    StateHandler buildRepeatCommand(StatefulContext context) {
        def noEcho = new EchoInput() {
            @Override
            boolean getEchoEnabled() {
                return false
            }
        }
        new AssembleCommand(context, noEcho)
    }

    StateHandler buildExecuteCommand(StatefulContext context) {
        new ExecuteCommand(context)
    }
}
