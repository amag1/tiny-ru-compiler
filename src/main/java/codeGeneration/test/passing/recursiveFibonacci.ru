/? 1123581321
struct Fibonacci {}
impl Fibonacci {
    .() {}

    fn fib(Int n) -> Int {
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
    Fibonacci f;
    f = new Fibonacci();

    (IO.out_int(f.fib(0)));
    (IO.out_int(f.fib(1)));
    (IO.out_int(f.fib(2)));
    (IO.out_int(f.fib(3)));
    (IO.out_int(f.fib(4)));
    (IO.out_int(f.fib(5)));
    (IO.out_int(f.fib(6)));
    (IO.out_int(f.fib(7)));
}