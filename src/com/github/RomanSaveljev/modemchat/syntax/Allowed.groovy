package com.github.RomanSaveljev.modemchat.syntax

// FSM for lazies
// Hard-ported from CoffeeScript implementation
class Allowed {
    private Map<Object, Boolean> flags

    Allowed(Object... enabled) {
        enableOnly(enabled)
    }

    Allowed() {
    }

    void enable(Object... what) {
        for (w in what) {
            flags[w] = true
        }
    }

    void disable(Object... what) {
        for (w in what) {
            flags[w] = false
        }
    }

    void enableOnly(Object... what) {
        disableAll()
        enable(what)
    }

    void disableAll() {
        flags = [:]
    }

    boolean isEnabled(Object flag) {
        flags.containsKey(flag) && flags[flag]
    }
}
