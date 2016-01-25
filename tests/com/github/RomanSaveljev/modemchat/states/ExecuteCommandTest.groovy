package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.NotificationListener
import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.mixins.BehaviorMixin

class ExecuteCommandTest extends GroovyTestCase {
    private class Notification implements NotificationListener {
        List<Character> buffer = []
        ExecuteCommand exec
        @Override
        void postNotification() {
            def result = exec.input([])
            buffer.addAll(result)
        }
    }

    private class Context implements StatefulContext {
        V250 v250
        StateHandler stateHandler
        List<Character> repeatable
        List<Character> commandLine
        StateFactory stateFactory
        NotificationListener listener
    }

    void testThrowsOnBadMixin() {
        def context = new Context()
        def exec = new ExecuteCommand(context)
        context.listener = new Notification(exec: exec)
        context.commandLine = "A".toCharArray()
        shouldFail({exec.input([])})
    }

    void testBasicCommand() {
        def context = new Context()
        def exec = new ExecuteCommand(context)
        exec.mix(" A", new BehaviorMixin() {
            @Override
            List<Character> input(ExecuteCommand.Api api, List<Character> data) {
                return "HELLO!".toCharArray()
            }
        })
        def notification = new Notification(exec: exec)
        context.listener = notification
        context.commandLine = "A".toCharArray()
        exec.input([])
        assert notification.buffer == "HELLO!".toCharArray().toList()
    }

    void testExtendedCommand() {
        def context = new Context()
        def exec = new ExecuteCommand(context)
        exec.mix("+CGMI", new BehaviorMixin() {
            @Override
            List<Character> input(ExecuteCommand.Api api, List<Character> data) {
                "BOO!".toCharArray()
            }
        })
        def notification = new Notification(exec: exec)
        context.listener = notification
        context.commandLine = "+CGMI".toCharArray()
        exec.input([])
        assert notification.buffer == "BOO!".toCharArray().toList()
    }
}
