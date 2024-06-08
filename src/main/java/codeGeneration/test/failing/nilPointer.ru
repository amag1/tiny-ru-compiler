/? Error: Nil pointer

struct A {
    Int x;
}

impl A {
    .() { }
}

start {
    A a;

    (IO.out_int(a.x));
}
