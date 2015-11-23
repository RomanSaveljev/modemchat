package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand

class BasicCommand {
    private static final def EXTRACT_BASIC_COMMAND = new BehaviorMixin() {
        @Override
        List<Character> input(ExecuteCommand.Api api, Queue<Character> data) {
            if (!api.context.commandLine.empty) {
                def name = " " + api.context.commandLine.join("")
                if (api.hasMixin(name)) {
                    api.goTo(name)
                } else {
                    api.goTo(api.ERROR)
                }
            } else {
                api.changeState(null)
            }
        }
    }
    static void mix(ExecuteCommand cmd) {
        // Some weird modems will allow commands starting with #
        // Try to remain open to possibilities.
        // Basic commands are special in that they do not have a prefix. An artificial " "
        // will be added in front, so a mixin should declare E command handler as " E".
        for (it in "ABCDEFGHIJKLMNOPQRSTUVWXYZ&".split()) {
            cmd.mix(it, EXTRACT_BASIC_COMMAND)
        }
    }
}
