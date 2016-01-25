package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand

// Does a final cleanup and prepares modem chat to receive a new command line (or handle next command from the same
// command line)
class EndMixin implements BehaviorMixin {
    static void mix(ExecuteCommand cmd) {
        cmd.mix(ExecuteCommand.Api.END, new EndMixin())
    }
    @Override
    List<Character> input(ExecuteCommand.Api api, List<Character> data) {
        data.clear()
        api.context.commandLine.clear()
        api.changeState(null)
        return []
    }
}
