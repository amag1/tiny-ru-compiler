struct A:B {
    pri Int a;
}

struct B:C {
    Array Bool b;
}

struct C {
    Str c;
}

impl A{
    .() {}
}
impl B{
.() {}
}
impl C{
.() {}}

start{}