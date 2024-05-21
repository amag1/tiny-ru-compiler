/? UnaryTypeMismatchException
struct A {}
impl A{
    .(){
        Int a;
        a = 1;
        (IO.out_int(a));
    }

    fn hola(Int a, Str b) -> Int{
        if (b == "b") {
            ret 1;
        }
        else {
            ret 2;
        }
    }
}

start{
    Int a;
    Str b;

    A n;

    n = new A();
    if (!n.hola(1,"a")) {
        (IO.out_int(a));}
}