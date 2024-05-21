/? SelfAccessInStaticMethod
struct A {
    Int x;
}

impl A {
    .() {
    }
    st fn function() -> void {self.x = 5;}
}

start {
}