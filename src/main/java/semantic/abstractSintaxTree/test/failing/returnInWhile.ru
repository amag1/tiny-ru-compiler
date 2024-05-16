/? MissingReturnException
struct A {}
impl A{
    .(){

    }

    fn loop() -> Int{
        Int a;
        while (true) {
           (++a);

            ret a;
        }
    }
}

start{}