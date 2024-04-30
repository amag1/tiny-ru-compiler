/? RedefinedConstructorException
impl A {
    .() {}
    fn someFunction() -> void {}
    .(Int a) {}
}

start {}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 5 | 5 | Ya se encontró un constructor definido para el struct: A |