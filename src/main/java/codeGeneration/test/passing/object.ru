/? 5hola

struct A {
    Int x;
    Str y;
}

impl A {
    .(Int x, Str y) {
        self.x = x;
        self.y = y;
    }

    fn getX() -> Int { ret x; }
}

start {
    Int x, y;
    A a;

    x = 5;
    a = new A(5, "hola");
    y = a.getX();

    (IO.out_int(y));
    (IO.out_str(a.y));
}
