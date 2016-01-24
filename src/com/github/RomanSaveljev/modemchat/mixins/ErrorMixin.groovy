package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand
import com.github.RomanSaveljev.modemchat.syntax.FormatResultCode

// ERROR final result code (see T-REC-V.250 5.7.1)
// All remaining command buffer is discarded
class ErrorMixin implements BehaviorMixin {
    List<Character> input(ExecuteCommand.Api api, Queue<Character> data) {
        data.clear()
        api.context.commandLine.clear()
        def v250 = api.context.v250
        def result = (v250.verbose ? "ERROR" : "4").toCharArray().toList()
        def formatter = FormatResultCode.New(api.context.v250)
        api.goTo(api.END)
        formatter.formatResultCode(result)
    }
    static void mix(ExecuteCommand cmd) {
        cmd.mix(ExecuteCommand.Api.ERROR, new ErrorMixin())
    }
}
