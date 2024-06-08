/? f_0=0f_1=1f_2=1f_3=2f_4=4f_5=88
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
(IO.out_str(""));
}
}
start{
Fibonacci fib;
Int n;
fib = new Fibonacci();
n = 5;
(IO.out_int(fib.sucesion_fib(n)));
}