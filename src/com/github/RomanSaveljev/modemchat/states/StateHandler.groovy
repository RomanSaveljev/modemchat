package com.github.RomanSaveljev.modemchat.states

interface StateHandler {
    List<Character> input(Queue<Character> data)
}
