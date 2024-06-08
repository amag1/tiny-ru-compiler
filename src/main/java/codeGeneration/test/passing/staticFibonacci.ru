/? 1123581321
struct Fibonacci {}
impl Fibonacci {
    .() {}

    st fn fib(Int n) -> Int {
        if (n < 0) {
            ret 0;
        }
        if (n < 2) {
            ret 1;
        }

        ret (fib(n - 1) + fib(n - 2));
    }
}

start {
    (IO.out_int(Fibonacci.fib(0)));
    (IO.out_int(Fibonacci.fib(1)));
    (IO.out_int(Fibonacci.fib(2)));
    (IO.out_int(Fibonacci.fib(3)));
    (IO.out_int(Fibonacci.fib(4)));
    (IO.out_int(Fibonacci.fib(5)));
    (IO.out_int(Fibonacci.fib(6)));
    (IO.out_int(Fibonacci.fib(7)));
}