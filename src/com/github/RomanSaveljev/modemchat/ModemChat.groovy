package com.github.RomanSaveljev.modemchat

import com.github.RomanSaveljev.modemchat.context.NotificationListener
import com.github.RomanSaveljev.modemchat.context.StatefulContext
import com.github.RomanSaveljev.modemchat.context.V250
import com.github.RomanSaveljev.modemchat.states.StateFactory
import com.github.RomanSaveljev.modemchat.states.StateHandler

import java.util.concurrent.Executors

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

    void postNotification() {
        executor.execute(new Runnable() {
            @Override
            void run() {
                def buffer = stateHandler.input([] as Queue<Character>)
                writeOutput(buffer)
            }
        })
    }

    void loop() {
        def inputThread = new Thread(new Runnable() {
            @Override
            void run() {
                while (true) {
                    int b = input.read()
                    if (b == -1) {
                        break
                    } else {
                        postData(b as byte)
                    }
                }
            }
            private postData(byte b) {
                executor.execute(new Runnable() {
                    @Override
                    void run() {
                        def buffer = stateHandler.input([b] as Queue<Character>)
                        writeOutput(buffer)
                    }
                })
            }
        })
        inputThread.start()
        executor.
    }
}
