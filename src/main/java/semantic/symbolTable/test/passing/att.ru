struct A {
    pri Int a;
    Array Bool b;
    Int c;
    Int alteraOrden;
}

impl A {.(Object c){Bool a; (IO.out_str("hola"));}}

struct B:A {
    Int d;
}

impl B {
    .(Object c){Bool a; (IO.out_str("chat"));}
    fn hola(Int a, Int b) -> Int {
        Int c;
        c = a + b;
        ret c;
    }
}
start{}