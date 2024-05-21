/? InvalidStaticMethodCallException
struct A {
    Int num;
}

impl A{
    .(){
        (IO.out_int(1));
    }
    fn hola() -> void{
        (IO.out_int(1));
    }
}

start {
    (A.hola());
}