struct A {}
impl A {
    .() {}

    fn retB(Int x) -> B {
        if (x > 5) {
            ret new B(0);
        } else {
            ret new B(1);
        }
    }
}

struct B {
    pri Bool x;
}
impl B {
    .(Int x) {
        if (x > 5) {
            self.x = false;
        } else {
            self.x = true;
        }
    }

    fn getX() -> Bool {
        ret self.x;
    }
}

start {
    Bool test;

    test = new A().retB(10).getX();
}