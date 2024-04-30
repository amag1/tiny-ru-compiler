/? RedefinedInheritedAttributeException
struct B : A {
	Str a;
}

struct A {
	pri Int a;
}

impl B   {
	.() {}
}

impl A  {
	.() {}
}

start {
}

/? ERROR: SEMANTICO - DECLARACIONES
/? | NUMERO DE LINEA: | NUMERO DE COLUMNA: | DESCRIPCION: |
/? | 2 | 6 | Se redefinió un atributo heredado: a |