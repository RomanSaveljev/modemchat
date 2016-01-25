package com.github.RomanSaveljev.modemchat.mixins.v250

import com.github.RomanSaveljev.modemchat.states.ExecuteCommand
import com.github.RomanSaveljev.modemchat.syntax.FormatInformationResponse

// Various mandatory mixins implementing T-REC-V.250 6.2
class DteDceInterface {
    static class CharacterRange {
        char min
        char max
    }
    static char parseDecimalCodePoint(List<Character> cmd, CharacterRange range) {
        char value = 0
        while (cmd.head().isDigit()) {
            value = value * 10 + Integer.parseInt(cmd.removeAt(0).toString())
            if (value < range.min) {
                throw new IllegalArgumentException("Value becomes less than ${min}")
            }
            if (value > range.max) {
                throw new IllegalArgumentException("Value becomes more than ${max}")
            }
        }
        return value
    }
    static String formatDecimalCodePoint(char codePoint) {
        return String.sprintf('%03d', codePoint)
    }
    static abstract class CharacterHandling {
        abstract void saveValue(char value, ExecuteCommand.Api api)
        abstract char loadValue(ExecuteCommand.Api api)
        abstract CharacterRange getRange()
        List<Character> handleEquals(ExecuteCommand.Api api, List<Character> data) {
            def cmd = api.context.commandLine
            try {
                if (!cmd.head().isDigit()) {
                    throw new IllegalArgumentException("First argument character is not a digit")
                }
                char newValue = parseDecimalCodePoint(cmd, range)
                saveValue(newValue, api)
                api.goTo(api.OK)
            } catch (IllegalArgumentException ignored) {
                api.goTo(api.ERROR)
            }
            return []
        }
        List<Character> handleQuery(ExecuteCommand.Api api, List<Character> data) {
            api.goTo(api.OK)
            def v250 = api.context.v250
            def format = FormatInformationResponse.New(v250)
            return format.formatInformationResponse(formatDecimalCodePoint(loadValue(api)))
        }
        List<Character> handleTest(ExecuteCommand.Api api, List<Character> data) {
            api.goTo(api.OK)
            def format = FormatInformationResponse.New(api.context.v250)
            return format.formatInformationResponse("S3: (${range.min}-${range.max}")
        }
    }
    static final CharacterRange WHOLE_RANGE = new CharacterRange(min: 0, max: 127)
    CharacterRange s3 = WHOLE_RANGE, s4 = WHOLE_RANGE, s5 = WHOLE_RANGE
    def s3Handling = new CharacterHandling() {
        @Override
        void saveValue(char value, ExecuteCommand.Api api) {
            api.context.v250.s3 = value
        }
        @Override
        char loadValue(ExecuteCommand.Api api) {
            return api.context.v250.s3
        }
        @Override
        CharacterRange getRange() {
            return DteDceInterface.this.s3
        }
    }
    def s4Handling = new CharacterHandling() {
        @Override
        void saveValue(char value, ExecuteCommand.Api api) {
            api.context.v250.s4 = value
        }
        @Override
        char loadValue(ExecuteCommand.Api api) {
            return api.context.v250.s4
        }
        @Override
        CharacterRange getRange() {
            return DteDceInterface.this.s4
        }
    }
    def s5Handling = new CharacterHandling() {
        @Override
        void saveValue(char value, ExecuteCommand.Api api) {
            api.context.v250.s5 = value
        }
        @Override
        char loadValue(ExecuteCommand.Api api) {
            return api.context.v250.s5
        }
        @Override
        CharacterRange getRange() {
            return DteDceInterface.this.s5
        }
    }
    List<Character> e(ExecuteCommand.Api api, List<Character> data) {
        def cmd = api.context.commandLine
        if (cmd.empty)
    }
    static void mix(ExecuteCommand cmd, DteDceInterface instance = new DteDceInterface()) {
        cmd.mix(' S3=', instance.s3Handling.&handleEquals)
        cmd.mix(' S3?', instance.s3Handling.&handleQuery)
        cmd.mix(' S3=?', instance.s3Handling.&handleTest)
        cmd.mix(' S4=', instance.s4Handling.&handleEquals)
        cmd.mix(' S4?', instance.s4Handling.&handleQuery)
        cmd.mix(' S4=?', instance.s4Handling.&handleTest)
        cmd.mix(' S5=', instance.s5Handling.&handleEquals)
        cmd.mix(' S5?', instance.s5Handling.&handleQuery)
        cmd.mix(' S5=?', instance.s5Handling.&handleTest)
    }
}
