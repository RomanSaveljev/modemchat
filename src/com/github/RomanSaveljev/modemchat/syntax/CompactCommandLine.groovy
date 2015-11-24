package com.github.RomanSaveljev.modemchat.syntax

abstract class CompactCommandLine {
    static final char DOUBLE_QUOTE = '"'
    static final char SPACE = ' '

    abstract List<Character> getRepeatable()

    abstract Queue<Character> getCommandLine()

    void compactCommandLine() {
        commandLine.clear()
        boolean inString = false
        for (c in repeatable) {
            inString = c == DOUBLE_QUOTE ? !inString : inString
            if (inString) {
                commandLine.offer(c)
            } else if (c != SPACE) {
                commandLine.offer(Character.toUpperCase(c))
            }
            // no else - spaces outside string constants are stripped
        }
    }
}
