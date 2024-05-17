/? VariableIsNotArrayException
struct A {
    Int num;
} impl A{ .(){}}

start {
    A a;

    a = new A();

    (IO.out_int(a[0]));
}