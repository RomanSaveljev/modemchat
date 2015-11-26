package com.github.RomanSaveljev.modemchat.mixins

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand

class FinalResultCodeMixin implements BehaviorMixin {

    @Override
    List<Character> input(ExecuteCommand.Api api, Queue<Character> data) {
        return null
    }
}
