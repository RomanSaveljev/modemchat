package com.github.RomanSaveljev.modemchat.states

/**
 * Created by user on 11/11/15.
 */
interface StateHandler {
    List<Character> input(Queue<Character> data)
}
