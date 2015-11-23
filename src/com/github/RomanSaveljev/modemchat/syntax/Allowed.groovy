package com.github.RomanSaveljev.modemchat.syntax

// FSM for lazies
// Hard-ported from CoffeeScript implementation
class Allowed {
    private def flags = [] as Map<String, Boolean>

    Allowed(String... enabled) {
        enableOnly(enabled)
    }

    Allowed() {
    }

    void enable(String... what) {
        for (w in what) {
            flags[w] = true
        }
    }

    void disable(String... what) {
        for (w in what) {
            flags[w] = false
        }
    }

    void enableOnly(String... what) {
        disableAll()
        enable(what)
    }

    void disableAll() {
        flags = [] as Map<String, Boolean>
    }

    boolean isEnabled(String flag) {
        flags.containsKey(flag) && flags[flag]
    }
}
