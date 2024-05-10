/? CyclicInheritanceException
struct A:C {}
struct B:A {}
struct C:B {}

impl A {.(){}}
impl B{.(){}}
impl C {.(){}}
start{}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 3 | 8 | Herencia cíclica detectada entre las clases: A, B, C |