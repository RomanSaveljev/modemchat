package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.syntax.EchoInput

class StateFactory {
    static final char CAPITAL_T = 'T'
    static final char SMALL_T = 't'

    @SuppressWarnings("GrMethodMayBeStatic")
    StateHandler buildBegin(StatefulContext context) {
        new Begin(context)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    StateHandler buildPrefix(StatefulContext context) {
        new Prefix(context)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    StateHandler buildCapitalA(StatefulContext context) {
        new PrefixA(context) {
            @Override
            boolean isAttention(char c) {
                c == CAPITAL_T
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    StateHandler buildSmallA(StatefulContext context) {
        new PrefixA(context) {
            @Override
            boolean isAttention(char c) {
                c == SMALL_T
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    StateHandler buildAssembleCommand(StatefulContext context) {
        new AssembleCommand(context)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    StateHandler buildRepeatCommand(StatefulContext context) {
        def noEcho = new EchoInput() {
            @Override
            boolean getEchoEnabled() {
                return false
            }
        }
        new AssembleCommand(context, noEcho)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    StateHandler buildExecuteCommand(StatefulContext context) {
        new ExecuteCommand(context)
    }
}
