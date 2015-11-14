package com.github.RomanSaveljev.modemchat.syntax

import com.github.RomanSaveljev.modemchat.context.V250

trait ResultCodeFormatter implements V250.Uses {
    List<Character> formatResultCode(List<Character> result) {
        def output = []
        v250.with {
            if (!suppressed) {
                if (verbose) {
                    output.addAll([s3, s4])
                }
                output.addAll(result)
                if (verbose) {
                    output.add(s4)
                }
            }
        }
        return output
    }
}