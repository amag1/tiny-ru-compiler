struct Test {}
impl Test {
    fn testMethod() -> void {}
}

/? ERROR: SINTACTICO
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 4 | 2 | Se esperaba [KW_START] pero se encontró EOF:  |