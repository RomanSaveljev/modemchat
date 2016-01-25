package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand
import com.github.RomanSaveljev.modemchat.syntax.FormatResultCode

// OK final result code (see T-REC-V.250 5.7.1)
// Depending on the remaining command line contents it may or may not
// actually get OK printed. The OK status is printed only for the last
// command.
// There is one corner case, when a command is followed by a semicolon,
// but there is no command afterwards. For example, `ATE?;`. In this case,
// OK has to be printed, but it does it in response to empty space after
// `;`. The implementation uses the same logic as for `AT`, which also
// results in OK.
class OkMixin implements BehaviorMixin {
    List<Character> input(ExecuteCommand.Api api, List<Character> data) {
        def context = api.context
        def ret = []
        if (context.commandLine.empty) {
            def v250 = context.v250
            def result = (v250.verbose ? "OK" : "0").toCharArray().toList()
            def formatter = FormatResultCode.New(api.context.v250)
            ret = formatter.formatResultCode(result)
        }
        // no else - empty array is returned
        api.goTo(api.END)
        return ret
    }
    static void mix(ExecuteCommand cmd) {
        cmd.mix(ExecuteCommand.Api.OK, new OkMixin())
    }
}
