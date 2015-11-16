package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.mixins.BehaviorMixin

class ExecuteCommand implements StateHandler {
    interface Api {
        void goTo(String mixin)

        void changeState(StateHandler state)

        void resetState()

        StatefulContext getContext()
    }
    StatefulContext context
    private String activeMixin = "start"
    private def mixins = [] as Map<String, BehaviorMixin>
    private def api = new Api() {
        @Override
        void goTo(String mixin) {
            ExecuteCommand.this.goTo(mixin)
        }

        @Override
        void changeState(StateHandler state) {
            ExecuteCommand.this.changeState(state)
        }

        @Override
        void resetState() {
            changeState(null)
        }

        @Override
        StatefulContext getContext() {
            return context
        }
    }

    ExecuteCommand(StatefulContext context) {
        this.context = context
    }

    @Override
    List<Character> input(Queue<Character> data) {
        if (data.empty) {
            return []
        } else {
            // must be there unless goTo was neglected
            assert mixins.containsKey(activeMixin)
            return mixins[activeMixin].input(api, data)
        }
    }
    // Have to keep the interface simple, so other JVM language could tap into it
    ExecuteCommand mix(String name, BehaviorMixin mixin) {
        mixins[name] = mixin
        return this
    }

    private void postNotification() {

    }

    private void goTo(String mixin) {
        if (!mixins.containsKey(mixin)) {
            throw new Exception("${mixin} must name an existing key")
        }
        activeMixin = mixin
        postNotification()
    }

    private void changeState(StateHandler state) {
        context.stateHandler = state
        postNotification()
    }
}
