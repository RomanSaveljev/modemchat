package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand
import com.github.RomanSaveljev.modemchat.syntax.ExtendedCommand

class ExtendedCommandMixin {
    static void mix(ExecuteCommand cmd) {
        cmd.mix("+", new BehaviorMixin() {
            @Override
            List<Character> input(ExecuteCommand.Api api, Queue<Character> data) {
                assert !api.context.commandLine.empty
                def extended = ExtendedCommand.next(api.context.commandLine)
                if (!extended.empty) {
                    def name = extended.join('')
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
        })
    }
}