struct A {
	Int x;
}

impl A {
	.() {}
	fn functionStr(Str x) -> void {}
}

struct B {
    Bool x;
}

impl B {
	.() {}
    fn function() -> void {
        Str x;
        A a;

        /? This var 'x' is the one declared in this function;
        (a.functionStr(x));
    }
}

start {
}
