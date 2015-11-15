/**
 * Created by user on 11/15/15.
 */

class Main {
    static List func(def a) {
        a == 'cd' ? [a] : [null]
    }
    static class V250 {
        interface Uses {
            V250 getV250()
        }
        boolean echo
        char s3
        char s4
        char s5
        boolean suppressed
        boolean verbose
    }
    static void main(String[] arg) {
        V250 v250 = new V250()
        println v250.echo.getClass()
    }
}