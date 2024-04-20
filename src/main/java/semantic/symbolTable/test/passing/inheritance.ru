struct A:B {
    pri Int a;}

struct B {
    pri Int b;}

struct C:B {
    pri Int c;
}

impl A{
    .() {}
}

impl B{
    .() {}
}

impl C{
    .() {}
}

start{}