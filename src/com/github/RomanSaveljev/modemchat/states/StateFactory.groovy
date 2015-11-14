package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext

/**
 * Created by user on 11/14/15.
 */
class StateFactory {
    StateHandler buildPrefix(StatefulContext context) {
        new Prefix(context: context, echo: InputEcho.New(context))
    }
    StateHandler buildCapitalA(StatefulContext context) {
        new PrefixA(context: context, echo: InputEcho.New(context)) {
            @Override
            boolean isAttention(char c) {
                return c == 'T'
            }
        }
    }
    StateHandler buildSmallA(StatefulContext context) {
        new PrefixA(context: context, echo: InputEcho.New(context)) {
            @Override
            boolean isAttention(char c) {
                return c == 't'
            }
        }
    }
    StateHandler buildAssembleCommand(StatefulContext context) {
        new AssembleCommand(context: context, echo: InputEcho.New(context))
    }
    StateHandler buildRepeatCommand(StatefulContext context) {
        new AssembleCommand(context: context, echo: InputEcho.NewNoEcho())
    }
    StateHandler buildExecuteCommand(StatefulContext context) {
        new ExecuteCommand(context: context, echo: InputEcho.New(context))
    }
}
