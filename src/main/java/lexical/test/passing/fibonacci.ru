struct Fibonacci {
Int suma;
Int i,j;
}
impl Fibonacci {
fn sucesion_fib(Int n)-> Int{
i=0; j=0; suma=0;
while (i<= n){
if (i==0){
(imprimo_numero(i));
(imprimo_sucesion(suma));
}
else
if(i==1){
(imprimo_numero(i));
suma=suma+i;
(imprimo_sucesion(suma));
}
else{
(imprimo_numero(i));
suma=suma+j;
j=suma;
(imprimo_sucesion(suma));
}
(++i);
}
ret suma;
}
.(){
i=0; /? inicializo i
j=0; /? inicializo j
suma=0; /? inicializo suma
}
fn imprimo_numero(Int num) -> void{
(IO.out_str("f_"));
(IO.out_int(num));
(IO.out_str("="));
}
fn imprimo_sucesion(Int s) -> void{
/?El valor es:
(IO.out_int(s));
(IO.out_str("\n"));
}
}
start{
Fibonacci fib;
Int n;
fib = new Fibonacci();
n = IO.in_int();
(IO.out_int(fib.sucesion_fib(n)));
}

/? CORRECTO: ANALISIS LEXICO
/? | TOKEN | LEXEMA | NUMERO DE LINEA (NUMERO DE COLUMNA) |
/? | KW_STRUCT | struct | 1 (1) |
/? | ID_CLASS | Fibonacci | 1 (8) |
/? | OPEN_CURLY | { | 1 (18) |
/? | TYPE_INT | Int | 2 (1) |
/? | ID | suma | 2 (5) |
/? | SEMICOLON | ; | 2 (9) |
/? | TYPE_INT | Int | 3 (1) |
/? | ID | i | 3 (5) |
/? | COMMA | , | 3 (6) |
/? | ID | j | 3 (7) |
/? | SEMICOLON | ; | 3 (8) |
/? | CLOSE_CURLY | } | 4 (1) |
/? | KW_IMPL | impl | 5 (1) |
/? | ID_CLASS | Fibonacci | 5 (6) |
/? | OPEN_CURLY | { | 5 (16) |
/? | KW_FN | fn | 6 (1) |
/? | ID | sucesion_fib | 6 (4) |
/? | OPEN_PAR | ( | 6 (16) |
/? | TYPE_INT | Int | 6 (17) |
/? | ID | n | 6 (21) |
/? | CLOSE_PAR | ) | 6 (22) |
/? | ARROW | -> | 6 (23) |
/? | TYPE_INT | Int | 6 (26) |
/? | OPEN_CURLY | { | 6 (29) |
/? | ID | i | 7 (1) |
/? | ASSIGN | = | 7 (2) |
/? | INT_LITERAL | 0 | 7 (3) |
/? | SEMICOLON | ; | 7 (4) |
/? | ID | j | 7 (6) |
/? | ASSIGN | = | 7 (7) |
/? | INT_LITERAL | 0 | 7 (8) |
/? | SEMICOLON | ; | 7 (9) |
/? | ID | suma | 7 (11) |
/? | ASSIGN | = | 7 (15) |
/? | INT_LITERAL | 0 | 7 (16) |
/? | SEMICOLON | ; | 7 (17) |
/? | KW_WHILE | while | 8 (1) |
/? | OPEN_PAR | ( | 8 (7) |
/? | ID | i | 8 (8) |
/? | LESS_EQUAL | <= | 8 (9) |
/? | ID | n | 8 (12) |
/? | CLOSE_PAR | ) | 8 (13) |
/? | OPEN_CURLY | { | 8 (14) |
/? | KW_IF | if | 9 (1) |
/? | OPEN_PAR | ( | 9 (4) |
/? | ID | i | 9 (5) |
/? | EQUAL | == | 9 (6) |
/? | INT_LITERAL | 0 | 9 (8) |
/? | CLOSE_PAR | ) | 9 (9) |
/? | OPEN_CURLY | { | 9 (10) |
/? | OPEN_PAR | ( | 10 (1) |
/? | ID | imprimo_numero | 10 (2) |
/? | OPEN_PAR | ( | 10 (16) |
/? | ID | i | 10 (17) |
/? | CLOSE_PAR | ) | 10 (18) |
/? | CLOSE_PAR | ) | 10 (19) |
/? | SEMICOLON | ; | 10 (20) |
/? | OPEN_PAR | ( | 11 (1) |
/? | ID | imprimo_sucesion | 11 (2) |
/? | OPEN_PAR | ( | 11 (18) |
/? | ID | suma | 11 (19) |
/? | CLOSE_PAR | ) | 11 (23) |
/? | CLOSE_PAR | ) | 11 (24) |
/? | SEMICOLON | ; | 11 (25) |
/? | CLOSE_CURLY | } | 12 (1) |
/? | KW_ELSE | else | 13 (1) |
/? | KW_IF | if | 14 (1) |
/? | OPEN_PAR | ( | 14 (3) |
/? | ID | i | 14 (4) |
/? | EQUAL | == | 14 (5) |
/? | INT_LITERAL | 1 | 14 (7) |
/? | CLOSE_PAR | ) | 14 (8) |
/? | OPEN_CURLY | { | 14 (9) |
/? | OPEN_PAR | ( | 15 (1) |
/? | ID | imprimo_numero | 15 (2) |
/? | OPEN_PAR | ( | 15 (16) |
/? | ID | i | 15 (17) |
/? | CLOSE_PAR | ) | 15 (18) |
/? | CLOSE_PAR | ) | 15 (19) |
/? | SEMICOLON | ; | 15 (20) |
/? | ID | suma | 16 (1) |
/? | ASSIGN | = | 16 (5) |
/? | ID | suma | 16 (6) |
/? | PLUS | + | 16 (10) |
/? | ID | i | 16 (11) |
/? | SEMICOLON | ; | 16 (12) |
/? | OPEN_PAR | ( | 17 (1) |
/? | ID | imprimo_sucesion | 17 (2) |
/? | OPEN_PAR | ( | 17 (18) |
/? | ID | suma | 17 (19) |
/? | CLOSE_PAR | ) | 17 (23) |
/? | CLOSE_PAR | ) | 17 (24) |
/? | SEMICOLON | ; | 17 (25) |
/? | CLOSE_CURLY | } | 18 (1) |
/? | KW_ELSE | else | 19 (1) |
/? | OPEN_CURLY | { | 19 (5) |
/? | OPEN_PAR | ( | 20 (1) |
/? | ID | imprimo_numero | 20 (2) |
/? | OPEN_PAR | ( | 20 (16) |
/? | ID | i | 20 (17) |
/? | CLOSE_PAR | ) | 20 (18) |
/? | CLOSE_PAR | ) | 20 (19) |
/? | SEMICOLON | ; | 20 (20) |
/? | ID | suma | 21 (1) |
/? | ASSIGN | = | 21 (5) |
/? | ID | suma | 21 (6) |
/? | PLUS | + | 21 (10) |
/? | ID | j | 21 (11) |
/? | SEMICOLON | ; | 21 (12) |
/? | ID | j | 22 (1) |
/? | ASSIGN | = | 22 (2) |
/? | ID | suma | 22 (3) |
/? | SEMICOLON | ; | 22 (7) |
/? | OPEN_PAR | ( | 23 (1) |
/? | ID | imprimo_sucesion | 23 (2) |
/? | OPEN_PAR | ( | 23 (18) |
/? | ID | suma | 23 (19) |
/? | CLOSE_PAR | ) | 23 (23) |
/? | CLOSE_PAR | ) | 23 (24) |
/? | SEMICOLON | ; | 23 (25) |
/? | CLOSE_CURLY | } | 24 (1) |
/? | OPEN_PAR | ( | 25 (1) |
/? | DPLUS | ++ | 25 (2) |
/? | ID | i | 25 (4) |
/? | CLOSE_PAR | ) | 25 (5) |
/? | SEMICOLON | ; | 25 (6) |
/? | CLOSE_CURLY | } | 26 (1) |
/? | KW_RET | ret | 27 (1) |
/? | ID | suma | 27 (5) |
/? | SEMICOLON | ; | 27 (9) |
/? | CLOSE_CURLY | } | 28 (1) |
/? | DOT | . | 29 (1) |
/? | OPEN_PAR | ( | 29 (2) |
/? | CLOSE_PAR | ) | 29 (3) |
/? | OPEN_CURLY | { | 29 (4) |
/? | ID | i | 30 (1) |
/? | ASSIGN | = | 30 (2) |
/? | INT_LITERAL | 0 | 30 (3) |
/? | SEMICOLON | ; | 30 (4) |
/? | ID | j | 31 (1) |
/? | ASSIGN | = | 31 (2) |
/? | INT_LITERAL | 0 | 31 (3) |
/? | SEMICOLON | ; | 31 (4) |
/? | ID | suma | 32 (1) |
/? | ASSIGN | = | 32 (5) |
/? | INT_LITERAL | 0 | 32 (6) |
/? | SEMICOLON | ; | 32 (7) |
/? | CLOSE_CURLY | } | 33 (1) |
/? | KW_FN | fn | 34 (1) |
/? | ID | imprimo_numero | 34 (4) |
/? | OPEN_PAR | ( | 34 (18) |
/? | TYPE_INT | Int | 34 (19) |
/? | ID | num | 34 (23) |
/? | CLOSE_PAR | ) | 34 (26) |
/? | ARROW | -> | 34 (28) |
/? | TYPE_VOID | void | 34 (31) |
/? | OPEN_CURLY | { | 34 (35) |
/? | OPEN_PAR | ( | 35 (1) |
/? | ID_CLASS | IO | 35 (2) |
/? | DOT | . | 35 (4) |
/? | ID | out_str | 35 (5) |
/? | OPEN_PAR | ( | 35 (12) |
/? | STRING_LITERAL | f_ | 35 (13) |
/? | CLOSE_PAR | ) | 35 (17) |
/? | CLOSE_PAR | ) | 35 (18) |
/? | SEMICOLON | ; | 35 (19) |
/? | OPEN_PAR | ( | 36 (1) |
/? | ID_CLASS | IO | 36 (2) |
/? | DOT | . | 36 (4) |
/? | ID | out_int | 36 (5) |
/? | OPEN_PAR | ( | 36 (12) |
/? | ID | num | 36 (13) |
/? | CLOSE_PAR | ) | 36 (16) |
/? | CLOSE_PAR | ) | 36 (17) |
/? | SEMICOLON | ; | 36 (18) |
/? | OPEN_PAR | ( | 37 (1) |
/? | ID_CLASS | IO | 37 (2) |
/? | DOT | . | 37 (4) |
/? | ID | out_str | 37 (5) |
/? | OPEN_PAR | ( | 37 (12) |
/? | STRING_LITERAL | = | 37 (13) |
/? | CLOSE_PAR | ) | 37 (16) |
/? | CLOSE_PAR | ) | 37 (17) |
/? | SEMICOLON | ; | 37 (18) |
/? | CLOSE_CURLY | } | 38 (1) |
/? | KW_FN | fn | 39 (1) |
/? | ID | imprimo_sucesion | 39 (4) |
/? | OPEN_PAR | ( | 39 (20) |
/? | TYPE_INT | Int | 39 (21) |
/? | ID | s | 39 (25) |
/? | CLOSE_PAR | ) | 39 (26) |
/? | ARROW | -> | 39 (28) |
/? | TYPE_VOID | void | 39 (31) |
/? | OPEN_CURLY | { | 39 (35) |
/? | OPEN_PAR | ( | 41 (1) |
/? | ID_CLASS | IO | 41 (2) |
/? | DOT | . | 41 (4) |
/? | ID | out_int | 41 (5) |
/? | OPEN_PAR | ( | 41 (12) |
/? | ID | s | 41 (13) |
/? | CLOSE_PAR | ) | 41 (14) |
/? | CLOSE_PAR | ) | 41 (15) |
/? | SEMICOLON | ; | 41 (16) |
/? | OPEN_PAR | ( | 42 (1) |
/? | ID_CLASS | IO | 42 (2) |
/? | DOT | . | 42 (4) |
/? | ID | out_str | 42 (5) |
/? | OPEN_PAR | ( | 42 (12) |
/? | STRING_LITERAL |
/?  | 42 (13) |
/? | CLOSE_PAR | ) | 42 (17) |
/? | CLOSE_PAR | ) | 42 (18) |
/? | SEMICOLON | ; | 42 (19) |
/? | CLOSE_CURLY | } | 43 (1) |
/? | CLOSE_CURLY | } | 44 (1) |
/? | KW_START | start | 45 (1) |
/? | OPEN_CURLY | { | 45 (6) |
/? | ID_CLASS | Fibonacci | 46 (1) |
/? | ID | fib | 46 (11) |
/? | SEMICOLON | ; | 46 (14) |
/? | TYPE_INT | Int | 47 (1) |
/? | ID | n | 47 (5) |
/? | SEMICOLON | ; | 47 (6) |
/? | ID | fib | 48 (1) |
/? | ASSIGN | = | 48 (5) |
/? | KW_NEW | new | 48 (7) |
/? | ID_CLASS | Fibonacci | 48 (11) |
/? | OPEN_PAR | ( | 48 (20) |
/? | CLOSE_PAR | ) | 48 (21) |
/? | SEMICOLON | ; | 48 (22) |
/? | ID | n | 49 (1) |
/? | ASSIGN | = | 49 (3) |
/? | ID_CLASS | IO | 49 (5) |
/? | DOT | . | 49 (7) |
/? | ID | in_int | 49 (8) |
/? | OPEN_PAR | ( | 49 (14) |
/? | CLOSE_PAR | ) | 49 (15) |
/? | SEMICOLON | ; | 49 (16) |
/? | OPEN_PAR | ( | 50 (1) |
/? | ID_CLASS | IO | 50 (2) |
/? | DOT | . | 50 (4) |
/? | ID | out_int | 50 (5) |
/? | OPEN_PAR | ( | 50 (12) |
/? | ID | fib | 50 (13) |
/? | DOT | . | 50 (16) |
/? | ID | sucesion_fib | 50 (17) |
/? | OPEN_PAR | ( | 50 (29) |
/? | ID | n | 50 (30) |
/? | CLOSE_PAR | ) | 50 (31) |
/? | CLOSE_PAR | ) | 50 (32) |
/? | CLOSE_PAR | ) | 50 (33) |
/? | SEMICOLON | ; | 50 (34) |
/? | CLOSE_CURLY | } | 51 (1) |