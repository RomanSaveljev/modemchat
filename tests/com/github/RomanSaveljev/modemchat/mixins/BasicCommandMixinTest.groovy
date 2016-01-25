package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.context.NotificationListener
import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.states.ExecuteCommand
import com.github.RomanSaveljev.modemchat.states.StateFactory
import com.github.RomanSaveljev.modemchat.states.StateHandler

class BasicCommandMixinTest extends GroovyTestCase {
    private class DummyApi implements ExecuteCommand.Api {
        @Override
        void goTo(String mixin) {

        }

        @Override
        boolean hasMixin(String mixin) {
            return false
        }

        @Override
        void changeState(StateHandler state) {

        }

        @Override
        StatefulContext getContext() {
            return new StatefulContext() {
                @Override
                V250 getV250() {
                    return null
                }

                @Override
                StateHandler getStateHandler() {
                    return null
                }

                @Override
                void setStateHandler(StateHandler stateHandler) {

                }

                @Override
                List<Character> getRepeatable() {
                    return [] as List<Character>
                }

                @Override
                List<Character> getCommandLine() {
                    ['a' as Character]
                }

                @Override
                StateFactory getStateFactory() {
                    return null
                }

                @Override
                NotificationListener getListener() {
                    return null
                }
            }
        }
    }
    boolean testing

    void setUp() {
        testing = false
    }

    void testAllMixinsAreOneCharacterLength() {
        def exec = new ExecuteCommand(null) {
            @Override
            ExecuteCommand mix(String name, BehaviorMixin mixin) {
                if (testing) {
                    assert name.length() == 1
                } else {
                    super.mix(name, mixin)
                }
                return this
            }
        }
        testing = true
        BasicCommandMixin.mix(exec)
    }

    void testAddsAlphaAndAmpersandMixins() {
        def exec = new ExecuteCommand(null) {
            @Override
            ExecuteCommand mix(String name, BehaviorMixin mixin) {
                if (testing) {
                    def c = name.charAt(0)
                    assert c.isLetter() && c.isUpperCase() || c == '&' as char
                } else {
                    super.mix(name, mixin)
                }
                return this
            }
        }
        testing = true
        BasicCommandMixin.mix(exec)
    }

    void testMixinDoesNotTouchData() {
        def api = new DummyApi()
        def exec = new ExecuteCommand(null) {
            @Override
            ExecuteCommand mix(String name, BehaviorMixin mixin) {
                def input = ['z' as Character]
                if (testing) {
                    mixin.input(api, input)
                    assert !input.empty
                } else {
                    super.mix(name, mixin)
                }
                return this
            }
        }
        testing = true
        BasicCommandMixin.mix(exec)
    }
}
