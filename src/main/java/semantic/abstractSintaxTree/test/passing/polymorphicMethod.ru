struct A {}
impl A { .() {} }

struct B:A {}
impl B { .() {} }

struct Utils {}
impl Utils {
    .(){}
    fn function(A a) -> void {}
}

start {
    Utils u;
    (u.function(new B()));
}

