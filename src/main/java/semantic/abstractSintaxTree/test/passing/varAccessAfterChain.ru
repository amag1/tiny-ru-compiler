struct A {}
impl A {
    .() {}

    st fn retB(Int x) -> B {
        if (x > 5) {
            ret new B(0);
        } else {
            ret new B(1);
        }
    }
}

struct B {
    Bool x;

    Any y;
}
impl B {
    .(Int x) {
        if (x > 5) {
            self.x = false;
        } else {
            self.x = true;
        }

        self.y = new Any();
    }

    fn getX() -> Bool {
        ret self.x;
    }
}

struct Any {
    Str hola;
}
impl Any {
    .() {
        self.hola = "hola";
    }
}

start {
    A a;
    IO io;

    io = new IO();
    a = new A();
    (io.out_str(a.retB(10).y.hola));
}