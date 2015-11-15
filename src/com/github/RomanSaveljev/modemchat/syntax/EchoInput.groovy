package com.github.RomanSaveljev.modemchat.syntax

abstract class EchoInput {
    abstract boolean getEchoEnabled()
    List<Character> consumeAndEchoOne(Queue<Character> data) {
        assert !data.empty
        def c = data.poll()
        if (echoEnabled) {
            return [c]
        } else {
            return []
        }
    }
}
