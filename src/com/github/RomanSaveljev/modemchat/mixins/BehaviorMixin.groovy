package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand

interface BehaviorMixin {
    List<Character> input(ExecuteCommand.Api api, List<Character> data)
}