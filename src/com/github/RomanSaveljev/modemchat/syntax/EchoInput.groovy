package com.github.RomanSaveljev.modemchat.syntax

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class EchoInput {
    private final Logger logger = LoggerFactory.getLogger(EchoInput.class)

    abstract boolean getEchoEnabled()
    List<Character> consumeAndEchoOne(Queue<Character> data) {
        assert !data.empty
        def c = data.poll()
        logger.debug("${c} consumed")
        if (echoEnabled) {
            return [c]
        } else {
            return []
        }
    }
}
