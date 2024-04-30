/? OverridenMethodException
struct A {}
struct B:A{}

impl A {
    .(){}

    fn f(Int a, Str b) -> Int {}
}

impl B {
    .(){}

    fn f(Int c, Str d) -> Str {}
}

start{}



/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 14 | 8 | Sobrecarga invalida para el metodo f |