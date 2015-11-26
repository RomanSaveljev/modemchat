class A {
    A() {
        func()
    }
    void func() {

    }
}
void func() {
    def t = false
    def a = new A() {
        @Override
        void func() {
            assert t != null
            println "t is ${t}"
        }
    }
    //t = true
    a.func()
}

func()