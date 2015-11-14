package com.github.RomanSaveljev.modemchat.syntax

import com.github.RomanSaveljev.modemchat.context.CommandLine

trait CommandLinePreprocessor {
    void process(CommandLine command) {
        command.commandLine.clear()
        boolean inString = false
        for (c in command.repeatable) {
            inString = c == '"' ? !inString : inString
            if (inString) {
                command.commandLine.push(c)
            } else if (c != ' ') {
                command.commandLine.push(Character.toUpperCase(c))
            }
        }
    }
}
