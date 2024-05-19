/? ReturnInConstructorException
struct A {}
impl A{
    .(){
        ret new A();
    }
}

start{}