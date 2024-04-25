/? RedefinedVariableException
struct A {}
impl A {
    .(){}
    fn someFn()-> void {
        Int a;
        Str a;
    }
}
start{}