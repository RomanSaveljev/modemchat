package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand

class BasicCommandMixinTest extends GroovyTestCase {
    void testAllMixinsAreOneCharacterLength() {
        def exec = new ExecuteCommand(null)
        BasicCommandMixin.mix(exec)

    }
    void testAddsAlphaAndAmpersandMixins() {
        def exec = new ExecuteCommand(null)
        BasicCommandMixin.mix(exec)
        exec.@mixins.keySet().each {
            assert it.
        }
    }
}
