package com.github.RomanSaveljev.modemchat.context

import com.github.RomanSaveljev.modemchat.states.StateFactory
import com.github.RomanSaveljev.modemchat.states.StateHandler

/**
 * Created by user on 11/14/15.
 */
class StatefulContext {
    V250 v250
    StateHandler stateHandler
    ArrayList<Character> repeatable
    final StateFactory factory
    ArrayList<Character> commandLine
}