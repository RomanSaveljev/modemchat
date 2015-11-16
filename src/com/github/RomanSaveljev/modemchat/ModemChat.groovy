package com.github.RomanSaveljev.modemchat

import com.github.RomanSaveljev.modemchat.context.NotificationListener
import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.states.StateFactory
import com.github.RomanSaveljev.modemchat.states.StateHandler

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ModemChat implements StatefulContext, NotificationListener {
    V250 v250 = new V250()
    StateFactory stateFactory = new StateFactory()
    StateHandler stateHandler
    List<Character> repeatable = []
    List<Character> commandLine = []
    NotificationListener listener = this
    private def executor = Executors.newSingleThreadExecutor()
    private InputStream input
    private OutputStream output

    ModemChat(InputStream input, OutputStream output) {
        this.input = input
        this.output = output
    }

    private void writeOutput(List<Character> buffer) {
        output.write(new String(buffer as char[]).bytes)
        output.flush()
    }

    private void singleDataLoop(Queue<Character> data) {
        def buffer = stateHandler.input(data)
        writeOutput(buffer)
    }

    private void feedData(List<Byte> data) {
        def characters = data.collect({new Character(it as char)}) as Queue<Character>
        // null is a special state, which means starting state
        if (!stateHandler) {
            stateHandler = stateFactory.buildPrefix(this)
        }
        // always kick the handler at least once - could be a notification
        singleDataLoop(characters)
        while (!characters.empty) {
            singleDataLoop(characters)
        }
    }

    void postNotification() {
        executor.execute(new Runnable() {
            @Override
            void run() {
                feedData([])
            }
        })
    }

    void loop() {
        def inputThread = new Thread(new Runnable() {
            @Override
            void run() {
                try {
                    //noinspection GroovyInfiniteLoopStatement
                    while (true) {
                        int b = input.read()
                        if (b == -1) {
                            throw new InterruptedException()
                        }
                        postData(b as byte)
                    }
                } catch(Exception ignored) {
                    if (!executor.shutdown) {
                        executor.shutdown()
                    }
                }
            }
            private postData(byte b) {
                executor.execute(new Runnable() {
                    @Override
                    void run() {
                        feedData([b])
                    }
                })
            }
        })
        inputThread.start()
        while (!executor.awaitTermination(60, TimeUnit.SECONDS));
    }
}
