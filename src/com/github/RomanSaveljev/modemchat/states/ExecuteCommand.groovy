package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250Settings
import com.github.RomanSaveljev.modemchat.mixins.BehaviorMixin
import com.github.RomanSaveljev.modemchat.syntax.ResultCodeFormatter

/**
 * Created by user on 11/14/15.
 */
class ExecuteCommand implements StateHandler, ResultCodeFormatter {
    StatefulContext context
    private String activeMixin = "start"
    private def mixins = [] as Map
    ExecuteCommand(StatefulContext context) {
        this.context = context
    }
    @Override
    List<Character> input(Queue<Character> data) {
        return null
    }
    void postNotification() {

    }
    void mix(BehaviorMixin... mixins) {

    }
    void mix(def... mixins) {

    }
    void goTo(String mixin) {
        activeMixin = mixin
        postNotification()
    }
    void changeState(StateHandler state) {
        context.stateHandler = state
        postNotification()
    }
    void resetState() {
        changeState(null)
    }
    List<Character> formatResultCode(List<Character> result) {
        def output = []
        context.v250.with {
            if (!suppressed) {
                if (verbose) {
                    output.addAll([s3, s4])
                }
                output.addAll(result)

            }
        }
        if (!context.v250.suppressed) {
            if (context.v250.verbose) {
                output.addAll([context.v250.s3, context.v250.s4])
            }
        }
        return output
    }

    @Override
    V250Settings.V250 getV250() {
        context.v250
    }
}
