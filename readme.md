# TinyRu Compiler

- Nahuel Arrieta
- Andrés Maglione

## Descripción

Compilador dirigido por la sintaxis para un lenguaje orientado a objetos basado en la sintaxis de Rust.

Implementa un analizador sintáctico recursivo predictivo LL(1).

Genera código intermedio ejecutable en procesadores MIPS. En el proyecto se incluye un simulador de MIPS para ejecutar
el código generado.

## Funcionalidades principales

- Herencia simple
- Polimorfismo
- Métodos estáticos
- Métodos de instancia
- Atributos de instancia

## Uso

Compilar el proyecto usando Maven

```bash
    mvn clean package
```

Ejecutar el compilador

```bash
    java -jar target/tinyRU.jar <archivo.ru>
```

Como resultado, se genera un archivo `archivo.asm` con el código MIPS generado.

También se generan dos JSON con la tabla de símbolos y el AST.

Para ejecutar el código generado, se puede usar el simulador de MIPS incluido en el proyecto.

```bash
    java -jar Mars4_5.jar <archivo.asm>
```

## Ejemplo

```rust
class A {
    Int a;
    Int b;
}

impl A {
    /? A constructor 
    .(Int a, Int b) {
        self.a = a;
        self.b = b;
    }
    
    /? Sum method
    fn sum() -> int {
        ret self.a + self.b;
    }
}

start {
    A a;
    a = new A(1, 2);
    (IO.out_int(a.sum()));
}
```
