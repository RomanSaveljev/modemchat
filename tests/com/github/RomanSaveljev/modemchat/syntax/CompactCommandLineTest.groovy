package com.github.RomanSaveljev.modemchat.syntax

class CompactCommandLineTest extends GroovyTestCase {
    private List<Character> repeatable
    private List<Character> commandLine
    def compactor = new CompactCommandLine() {
        @Override
        List<Character> getRepeatable() {
            CompactCommandLineTest.this.repeatable
        }

        @Override
        List<Character> getCommandLine() {
            CompactCommandLineTest.this.commandLine
        }
    }

    @Override
    void setUp() {
        repeatable = []
        commandLine = []
    }

    void testUpperCase() {
        repeatable.addAll('abc'.toCharArray() as Collection)
        compactor.compactCommandLine()
        assert commandLine == 'ABC'.toCharArray() as Collection
    }

    void testUpperCase2() {
        repeatable.addAll('Dg#7*@hz0'.toCharArray() as Collection)
        compactor.compactCommandLine()
        assert commandLine == 'DG#7*@HZ0'.toCharArray() as Collection
    }

    void testNoUpperCaseInsideString() {
        repeatable.addAll('a"bc"d'.toCharArray() as Collection)
        compactor.compactCommandLine()
        assert commandLine == 'A"bc"D'.toCharArray() as Collection
    }

    void testStripSpace() {
        repeatable.addAll('A B    C'.toCharArray() as Collection)
        compactor.compactCommandLine()
        assert commandLine == 'ABC'.toCharArray() as Collection
    }

    void testNoStripSpaceInsideString() {
        repeatable.addAll('A "B    C"'.toCharArray() as Collection)
        compactor.compactCommandLine()
        assert commandLine == 'A"B    C"'.toCharArray() as Collection
    }
}
