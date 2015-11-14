package com.github.RomanSaveljev.modemchat.syntax

import com.github.RomanSaveljev.modemchat.context.V250

trait InputEchoer implements V250.Uses {
    List<Character> consumeAndEchoOne(Queue<Character> data) {
        assert !data.empty
        def c = data.poll()
        if (v250.echo) {
            return [c]
        } else {
            return []
        }
    }
}