/? RedefinedVariableException
struct A {}
impl A {
    .(){}
    fn someFn(Int a, Str a)-> void {}
}
start{}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 5 | 26 | Variable redefinida: a |