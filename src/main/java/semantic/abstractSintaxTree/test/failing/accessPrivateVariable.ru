/? UnaccesibleVariableException
struct A {}
impl A{
    .(){}

    fn voidd() -> void{
        Unrelated a;

        (IO.out_int(a.a));
    }
}


struct Unrelated {
    pri Int a;
}
impl Unrelated{
    .(){}
}

start{}