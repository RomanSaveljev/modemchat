package com.github.RomanSaveljev.modemchat.syntax

class FormatResultCodeTest extends GroovyTestCase {
    void testSuppressed() {
        def format = new FormatResultCode() {
            @Override
            boolean getSuppressed() {
                return true
            }

            @Override
            boolean getVerbose() {
                return true
            }

            @Override
            def getS3() {
                return '\r' as char
            }

            @Override
            def getS4() {
                return '\n' as char
            }
        }
        def output = format.formatResultCode([1, 2, 3])
        assert output.empty
    }
    void testVerbose() {
        def format = new FormatResultCode() {
            @Override
            boolean getSuppressed() {
                return false
            }

            @Override
            boolean getVerbose() {
                return true
            }

            @Override
            def getS3() {
                return 's3'
            }

            @Override
            def getS4() {
                return 's4'
            }
        }
        def output = format.formatResultCode([1, 2, 3])
        assert output == ['s3', 's4', 1, 2, 3, 's3', 's4']
    }
    void testNotVerbose() {
        def format = new FormatResultCode() {
            @Override
            boolean getSuppressed() {
                return false
            }

            @Override
            boolean getVerbose() {
                return false
            }

            @Override
            def getS3() {
                return 's3'
            }

            @Override
            def getS4() {
                return 's4'
            }
        }
        def output = format.formatResultCode([1, 2, 3])
        assert output == [1, 2, 3, 's3']
    }
}
