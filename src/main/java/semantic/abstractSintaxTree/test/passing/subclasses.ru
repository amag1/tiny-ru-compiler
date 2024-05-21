struct A {}
impl A { 
    .() {}
    
    fn all() -> Int {
        (IO.out_str("A.all\n"));
        
        ret 0;
    }        
    }

struct B:A {}
impl B { 
   .() {}
   
   fn all() -> Int {
       (IO.out_str("B.all\n"));
       
       ret 1;
   }

   fn some() -> Int {
          (IO.out_str("some\n"));

          ret 100;
      }
}

struct C:A {}
impl C { 
   .() {}
   
   fn all() -> Int {
       (IO.out_str("C.all\n"));
       
       ret 2;
   }        
}

struct D:B {}
impl D { 
   .() {}
   
   fn all() -> Int {
       (IO.out_str("D.all\n"));
       
       ret 3;
   }        
}

start {
    A a;

    a = new D();

    (a.all());
}