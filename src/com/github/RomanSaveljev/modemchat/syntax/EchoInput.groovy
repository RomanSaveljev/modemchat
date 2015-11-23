package com.github.RomanSaveljev.modemchat.syntax

import groovy.json.StringEscapeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class EchoInput {
    private final Logger logger = LoggerFactory.getLogger(EchoInput.class)

    abstract boolean getEchoEnabled()
    List<Character> consumeAndEchoOne(Queue<Character> data) {
        assert !data.empty
        def c = data.poll()
        logger.debug("${StringEscapeUtils.escapeJava(c.toString())} consumed")
        if (echoEnabled) {
            return [c]
        } else {
            return []
        }
    }
}
