/? VoidAccessException
start {
    IO hola;

    hola = new IO();

    (hola.out_int(1).invalidMethod());
}