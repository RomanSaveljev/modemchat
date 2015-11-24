package com.github.RomanSaveljev.modemchat.states

import com.github.RomanSaveljev.modemchat.context.NotificationListener
import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.mixins.BehaviorMixin

class ExecuteCommandTest extends GroovyTestCase {
    private static Queue<Character> convert(String command) {
        command.toCharArray().collect({ it as Character }) as Queue<Character>
    }

    private class Notification implements NotificationListener {
        ArrayList<Character> buffer = []
        ExecuteCommand exec
        @Override
        void postNotification() {
            def result = exec.input([] as Queue<Character>)
            buffer.addAll(result)
        }
    }

    private class Context implements StatefulContext {
        V250 v250
        StateHandler stateHandler
        List<Character> repeatable
        Queue<Character> commandLine
        StateFactory stateFactory
        NotificationListener listener
    }

    void testThrowsOnBadMixin() {
        def context = new Context()
        def exec = new ExecuteCommand(context)
        context.listener = new Notification(exec: exec)
        context.commandLine = convert("A")
        shouldFail({exec.input([] as Queue<Character>)})
    }

    void testBasicCommand() {
        def context = new Context()
        def exec = new ExecuteCommand(context)
        exec.mix(" A", new BehaviorMixin() {
            @Override
            List<Character> input(ExecuteCommand.Api api, Queue<Character> data) {
                return convert("HELLO!")
            }
        })
        def notification = new Notification(exec: exec)
        context.listener = notification
        context.commandLine = convert("A")
        exec.input([] as Queue<Character>)
        assert notification.buffer == convert("HELLO!")
    }

    void testExtendedCommand() {
        def context = new Context()
        def exec = new ExecuteCommand(context)
        exec.mix("+CGMI", new BehaviorMixin() {
            @Override
            List<Character> input(ExecuteCommand.Api api, Queue<Character> data) {
                return convert("BOO!")
            }
        })
        def notification = new Notification(exec: exec)
        context.listener = notification
        context.commandLine = convert("+CGMI")
        exec.input([] as Queue<Character>)
        assert notification.buffer == convert("BOO!")
    }
}
