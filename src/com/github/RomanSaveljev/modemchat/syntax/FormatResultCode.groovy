package com.github.RomanSaveljev.modemchat.syntax

import com.github.RomanSaveljev.modemchat.context.V250

abstract class FormatResultCode {
    static FormatResultCode New(V250 v250) {
        new FormatResultCode() {
            @Override
            boolean getSuppressed() {
                v250.suppressed
            }

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

    abstract boolean getSuppressed()

    abstract boolean getVerbose()

    abstract def getS3()

    abstract def getS4()

    List formatResultCode(List result) {
        assert !result.empty
        def output = []
        if (!suppressed) {
            if (verbose) {
                output.addAll([s3, s4])
            }
            output.addAll(result)
            output.add(s3)
            if (verbose) {
                output.add(s4)
            }
        }
        return output
    }
}