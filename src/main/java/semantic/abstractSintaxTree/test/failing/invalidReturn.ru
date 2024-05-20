/? InvalidMethodReturn
struct A {}
impl A{
    .(){}

    fn voidd() -> Parent{
        Child a;

        ret new Unrelated();
    }
}

struct Child:Parent {}
impl Child{
    .(){}
}

struct Parent {}
impl Parent{
    .(){}
}

struct Unrelated {}
impl Unrelated{
    .(){}
}

start{}