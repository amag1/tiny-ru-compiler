/? RedefinedVariableException
struct A {}
impl A {
    .(){}
    fn someFn()-> void {
        Int a;
        Str a;
    }
}
start{}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 7 | 13 | Variable redefinida: a |