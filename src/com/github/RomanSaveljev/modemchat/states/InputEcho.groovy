package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.V250Settings

/**
 * Created by user on 11/11/15.
 */
class InputEcho implements StateHandler {
    V250Settings context

    @Override
    List input(Queue data) {
        def output = [] as ArrayList
        assert data.size() > 0
        def c = data.poll()
        if (context.v250.echo) {
            output << c
        }
        return output
    }
}
