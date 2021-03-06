package com.github.RomanSaveljev.modemchat.context

import com.github.RomanSaveljev.modemchat.states.StateFactory
import com.github.RomanSaveljev.modemchat.states.StateHandler

interface StatefulContext {
    V250 getV250()
    void setV250(V250 v250)
    StateHandler getStateHandler()
    void setStateHandler(StateHandler stateHandler)
    List<Character> getRepeatable()
    List<Character> getCommandLine()
    StateFactory getStateFactory()
    NotificationListener getListener()
}