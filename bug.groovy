/**
 * Created by romasave on 11/17/15.
 */

class Main {
    static abstract class Inner {
        abstract int findA()
        int doSomething() {
            findA()
        }
    }
    static abstract class A {
        final private int a = 7
        def inner = new Inner() {
            @Override
            int findA() {
                return A.this.@a
            }
        }
        int doSomething() {
            inner.doSomething()
        }
        abstract void toBeImplemented()
    }
    static class Factory {
        A create() {
            new A() {
                @Override
                void toBeImplemented() {
                }
            }
        }
    }
    static void main(String[] args) {
        //def a = new A()
        //println a.doSomething()
        Factory f = new Factory()
        def aa = f.create()
        //println aa.@a
        println aa.doSomething()
    }
}