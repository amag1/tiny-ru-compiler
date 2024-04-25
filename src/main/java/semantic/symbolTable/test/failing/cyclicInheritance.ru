/? CyclicInheritanceException
struct A:C {}
struct B:A {}
struct C:B {}

impl A {.(){}}
impl B{.(){}}
impl C {.(){}}
start{}