struct A {
	Int x;
}

impl A {
	.() {}
	fn functionBool(Bool x) -> void {}
}

struct B {
    Bool x;
}

impl B {
	.() {}
    fn function() -> void {
        Str x;
        A a;

        /? This var 'x' is the one declared in struct B;
        (a.functionBool(self.x));
    }
}

start {
}
