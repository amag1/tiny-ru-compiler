/? UnaccesibleVariableException
struct A:Parent {}
impl A{
    .(){}

    fn voidd() -> void{
        a = 1;

        (IO.out_str(a));
    }
}


struct Parent {
    pri Int a;
}
impl Parent{
.(){}
}

start{}