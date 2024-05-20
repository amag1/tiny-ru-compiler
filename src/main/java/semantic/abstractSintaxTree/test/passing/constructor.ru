struct A {
    Int x;
    Str y;
}

impl A {
    .(Int x, Str y) {
        self.x = x;
        self.y = y;
    }
}

start {
    A a;
    a = new A (1, "hola");
}