struct A : B {
}
struct B{
 pri Int a;
}

impl A {
 .(){}

 fn retB() -> B {
    Int b;
    (IO.out_int(self.a));
 }
}

impl B {
    .(){}

    fn print(Str a) -> void {
        (IO.out_str(a));
    }
}

start{
    A a;
    (a.retB().print("Hola"));
}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 3 | 8 | Herencia cíclica detectada entre las clases: A, B, C |