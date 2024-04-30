/? ClassNotFoundException
struct A:B {
    pri Int a;
}

impl A {
    .(){}
}

start{}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 2 | 8 | Herencia inválida: la clase B no existe |