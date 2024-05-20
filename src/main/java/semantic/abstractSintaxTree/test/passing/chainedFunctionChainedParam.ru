struct A {
	Int x;
}

impl A {
	.() {}
	fn functionInt(Int x) -> void {}
}

struct B {
    Bool x;
}

impl B {
	.() {}
    fn function() -> void {
        Str x;
        A a;

        /? This var 'x' is the one declared in struct A;
        (a.functionInt(a.x));
    }
}

start {
}
