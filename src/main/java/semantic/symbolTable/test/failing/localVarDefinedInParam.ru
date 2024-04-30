/? RedefinedVariableException
struct A {}
impl A {
    .(){}
    fn someFn(Int a)-> void {
        Str a;
    }
}
start{}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 6 | 13 | Variable redefinida: a |