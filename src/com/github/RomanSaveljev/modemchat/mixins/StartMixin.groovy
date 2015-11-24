package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand

class StartMixin implements BehaviorMixin {
    static void mix(ExecuteCommand cmd) {
        cmd.mix(ExecuteCommand.Api.START, new StartMixin())
    }

    @Override
    List<Character> input(ExecuteCommand.Api api, Queue<Character> data) {
        // When the previous command is done, it will jump into start state
        // But then this state will check whether there is anything left to process
        if (!api.context.commandLine.empty) {
            def c = api.context.commandLine[0] as String
            // Previous command is responsible of fetching everything up to the point
            // where a next command may start, so the first character should always be
            // meaningful. Implementations may add general separator (;) stripper, if
            // they want to. Here we simply route by the first character to allow for
            // mixing and overrides
            if (api.hasMixin(c)) {
                api.goTo(c)
            } else {
                api.goTo(api.ERROR)
            }
        } else {
            api.changeState(null)
        }
        return []
    }
}
