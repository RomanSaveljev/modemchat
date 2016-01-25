package com.github.RomanSaveljev.modemchat.mixins.v250

import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.states.ExecuteCommand
import com.github.RomanSaveljev.modemchat.syntax.FormatInformationResponse

// Various mandatory mixins implementing T-REC-V.250 6.1
class GenericDceControl {
    final static char AMPERSAND_F_SUPPORTED_CONFIGURATION = '0'
    final static String RESET_TO_DEFAULTS = "resetToDefaults"
    static List<Character> resetToDefaults(ExecuteCommand.Api api, List<Character> data) {
        api.context.v250 = new V250()
        api.goTo(api.OK)
        return []
    }
    static List<Character> z(ExecuteCommand.Api api, List<Character> data) {
        // commands following Z may be ignored and, yes, they will be
        data.clear()
        api.context.commandLine.clear()
        // no value is expected, but there may be in some implementations
        api.goTo(RESET_TO_DEFAULTS)
        return []
    }
    static List<Character> ampF(ExecuteCommand.Api api, List<Character> data) {
        // here we only recognize 0 for configuration number
        if (api.context.commandLine.empty) {
            api.goTo(RESET_TO_DEFAULTS)
        } else if (api.context.commandLine.head() == AMPERSAND_F_SUPPORTED_CONFIGURATION) {
            api.context.commandLine.removeAt(0)
            api.goTo(RESET_TO_DEFAULTS)
        } else {
            api.goTo(api.ERROR)
        }
        return []
    }
    static List<Character> ampFTest(ExecuteCommand.Api api, List<Character> data) {
        api.goTo(api.OK)
        def formatter = FormatInformationResponse.New(api.context.v250)
        return formatter.formatInformationResponse("&F: (${AMPERSAND_F_SUPPORTED_CONFIGURATION})")
    }
    static List<Character> plusGMI(ExecuteCommand.Api api, List<Character> data) {
        api.goTo(api.OK)
        def format = FormatInformationResponse.New(api.context.v250)
        return format.formatInformationResponse("Haltian")
    }
    static List<Character> plusGMITest(ExecuteCommand.Api api, List<Character> data) {
        // there is no parameters to describe, but it is OK to ask
        api.goTo(api.OK)
    }
    static List<Character> plusGMM(ExecuteCommand.Api api, List<Character> data) {
        api.goTo(api.OK)
        def format = FormatInformationResponse.New(api.context.v250)
        return format.formatInformationResponse("ModemChat")
    }
    static List<Character> plusGMMTest(ExecuteCommand.Api api, List<Character> data) {
        // there is no parameters to describe, but it is OK to ask
        api.goTo(api.OK)
    }
    static List<Character> plusGMR(ExecuteCommand.Api api, List<Character> data) {
        api.goTo(api.OK)
        def format = FormatInformationResponse.New(api.context.v250)
        // TODO: grab actual version from `git descibe`
        return format.formatInformationResponse("v0.0.1")
    }
    static List<Character> plusGMRTest(ExecuteCommand.Api api, List<Character> data) {
        // there is no parameters to describe, but it is OK to ask
        api.goTo(api.OK)
    }
    static List<Character> plusGCAP(ExecuteCommand.Api api, List<Character> data) {
        api.goTo(api.OK)
        def format = FormatInformationResponse.New(api.context.v250)
        // bare minimum of what would make sense - implementations are welcome to override
        return format.formatInformationResponse("+GCAP: +CGSM, +DS")
    }
    static List<Character> plusGCAPTest(ExecuteCommand.Api api, List<Character> data) {
        // there is no parameters to describe, but it is OK to ask
        api.goTo(api.OK)
    }
    static void mix(ExecuteCommand cmd) {
        cmd.mix(RESET_TO_DEFAULTS, GenericDceControl.&resetToDefaults)
        cmd.mix(' Z', GenericDceControl.&z)
        cmd.mix(' &F', GenericDceControl.&ampF)
        cmd.mix(' &F=?', GenericDceControl.&ampFTest)
        cmd.mix('+GMI', GenericDceControl.&plusGMI)
        cmd.mix('+GMI=?', GenericDceControl.&plusGMITest)
        cmd.mix('+GMM', GenericDceControl.&plusGMM)
        cmd.mix('+GMM=?', GenericDceControl.&plusGMMTest)
        cmd.mix('+GMR', GenericDceControl.&plusGMR)
        cmd.mix('+GMR=?', GenericDceControl.&plusGMRTest)
        cmd.mix('+GCAP', GenericDceControl.&plusGCAP)
        cmd.mix('+GCAP=?', GenericDceControl.&plusGCAPTest)
    }
}
