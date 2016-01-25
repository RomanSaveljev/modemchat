package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand
import com.github.RomanSaveljev.modemchat.syntax.BasicCommand

class BasicCommandMixin {
    private static final def EXTRACT_BASIC_COMMAND = new BehaviorMixin() {
        @Override
        List<Character> input(ExecuteCommand.Api api, List<Character> data) {
            assert !api.context.commandLine.empty
            def basic = BasicCommand.next(api.context.commandLine)
            if (!basic.empty) {
                def name = " " + basic.join("")
                if (api.hasMixin(name)) {
                    api.goTo(name)
                } else {
                    api.goTo(api.ERROR)
                }
            } else {
                api.goTo(api.ERROR)
            }
            return []
        }
    }
    static void mix(ExecuteCommand cmd) {
        // Some weird modems will allow commands starting with #
        // Try to remain open to possibilities.
        // Basic commands are special in that they do not have a prefix. An artificial " "
        // will be added in front, so a mixin should declare E command handler as " E".
        for (it in "ABCDEFGHIJKLMNOPQRSTUVWXYZ&".toCharArray()) {
            cmd.mix(it as String, EXTRACT_BASIC_COMMAND)
        }
    }
}
