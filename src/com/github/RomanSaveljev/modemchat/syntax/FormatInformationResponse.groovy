package com.github.RomanSaveljev.modemchat.syntax

import com.github.RomanSaveljev.modemchat.context.V250

abstract class FormatInformationResponse {
    static FormatInformationResponse New(V250 v250) {
        new FormatInformationResponse() {
            @Override
            boolean getVerbose() {
                v250.verbose
            }

            @Override
            def getS3() {
                v250.s3
            }

            @Override
            def getS4() {
                v250.s4
            }
        }
    }

    abstract boolean getVerbose()

    abstract def getS3()

    abstract def getS4()

    List<Character> formatInformationResponse(List<Character> result) {
        assert !result.empty
        def output = []
        if (verbose) {
            output.addAll([s3, s4])
        }
        output.addAll(result)
        output.add(s3)
        output.add(s4)
        return output
    }

    List<Character> formatInformationResponse(String result) {
        return formatInformationResponse(result.toCharArray().toList())
    }
}