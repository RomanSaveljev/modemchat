package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.mixins.BasicCommandMixin
import com.github.RomanSaveljev.modemchat.mixins.BehaviorMixin
import com.github.RomanSaveljev.modemchat.mixins.StartMixin
import groovy.json.StringEscapeUtils
import org.codehaus.groovy.runtime.MethodClosure
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ExecuteCommand implements StateHandler {
    interface Api {
        final static String ERROR = "error"
        final static String START = "start"

        void goTo(String mixin)

        boolean hasMixin(String mixin)

        void changeState(StateHandler state)

        StatefulContext getContext()
    }
    StatefulContext context
    private String activeMixin = Api.START
    private def mixins = [:] as Map<String, BehaviorMixin>
    private def api = new Api() {
        @Override
        void goTo(String mixin) {
            ExecuteCommand.this.goTo(mixin)
        }

        @Override
        boolean hasMixin(String mixin) {
            return ExecuteCommand.this.hasMixin(mixin)
        }

        @Override
        void changeState(StateHandler state) {
            ExecuteCommand.this.changeState(state)
        }

        @Override
        StatefulContext getContext() {
            return ExecuteCommand.this.context
        }
    }
    final Logger logger = LoggerFactory.getLogger(ExecuteCommand.class)

    ExecuteCommand(StatefulContext context) {
        this.context = context
        StartMixin.mix(this)
        BasicCommandMixin.mix(this)
    }

    @Override
    List<Character> input(Queue<Character> data) {
        logger.debug("input(${StringEscapeUtils.escapeJava(data.join(""))})")
        // must be there unless goTo was skipped
        assert mixins.containsKey(activeMixin)
        // every mixin must be able to handle empty input
        return mixins[activeMixin].input(api, data)
    }

    // Have to keep the interface simple, so other JVM language could tap into it
    ExecuteCommand mix(String name, BehaviorMixin mixin) {
        mixins[name] = mixin
        return this
    }

    ExecuteCommand mix(String name, MethodClosure mixin) {
        def obj = new BehaviorMixin() {
            @Override
            List<Character> input(Api api, Queue<Character> data) {
                mixin(api, data) as List<Character>
            }
        }
        mix(name, obj)
    }

    private void postNotification() {
        context.listener.postNotification()
    }

    private void goTo(String mixin) {
        if (!mixins.containsKey(mixin)) {
            throw new Exception("'${mixin}' must name existing mixin")
        }
        activeMixin = mixin
        postNotification()
    }

    private boolean hasMixin(String mixin) {
        mixins.containsKey(mixin)
    }

    private void changeState(StateHandler state) {
        context.stateHandler = state
        postNotification()
    }
}
