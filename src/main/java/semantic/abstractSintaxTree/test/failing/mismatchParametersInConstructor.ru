/? ParameterCountMismatchException
struct A {
    Int num;
}

impl A {
    .(Int a) {
    }
}

start {
    A a;

    a = new A();
}