package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.V250Settings

abst InputEcho {
    static InputEcho New(V250Settings.V250 v250) {
        if (v250.echo) {
            return new InputEcho() {
                @Override
                List<Character> echoInput(Queue<Character> data) {
                    assert !data.empty
                    return [data.poll()]
                }
            }
        } else {
            return NewNoEcho()
        }
    }
    static InputEcho NewNoEcho() {
        return new InputEcho() {
            @Override
            List<Character> echoInput(Queue<Character> data) {
                assert !data.empty
                data.poll()
                return []
            }
        }
    }
    List<Character> echoInput(Queue<Character> data)
}
