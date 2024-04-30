/? MissingConstructorException
impl A {
    fn someFunction() -> void {}
    fn otherFunction() -> void {}
    fn anyFunctionButConstructor() -> void {}
}

start {}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 2 | 6 | No se encontró el método constructor para el struct: A |