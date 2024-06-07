/? 58

struct A {
    Int x;
    Str y;
}

impl A {
    .(Int x, Str y) {
        self.x = x;
        self.y = y;
    }

    fn m1() -> Int { ret x; }
}

struct B:A {
    Int z;
}

impl B {
    .(Int z) {self.z = z;}

    fn m1() -> Int { ret z; }
}

start {
    Int x, y;
    A a;

    x = 5;

    a = new A(5, "hola");
    (IO.out_int(a.m1()));

    a = new B(8);
    (IO.out_int(a.m1()));
}
