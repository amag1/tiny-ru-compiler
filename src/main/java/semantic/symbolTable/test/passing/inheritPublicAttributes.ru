struct A:B {
    pri Int a;
}

struct B:C {
    Array Str b;
}

struct C {
    Bool another;
}

impl A{.(){}}
impl B{.(){}}
impl C{.(){}}

start{}