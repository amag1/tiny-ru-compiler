/? CyclicInheritanceException
struct A {}
struct B:A {}

impl A {
.(){
    (IO.print("hola"));
}

fn f1() -> void {
    (IO.print("capo"));

    ret 0;
    }
}
impl B{.(){}}

start{
    (a.hola(1,2,3));

    if (a > b) {
        (++a + b);
        ret 1;
    }
    else ret 2;

    while (a > b) (IO.print("hola capo"));

    a.b.c = (1*2+5);


    ret (!(a > b));

}
/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 3 | 8 | Herencia cíclica detectada entre las clases: A, B, C |