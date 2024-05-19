/? InvalidMethodReturn
struct A {}
impl A{
    .(){}

    fn voidd() -> void{
        ret 1;
    }
}

start{}