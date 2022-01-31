public class Triple<A, B, C> {

    A a;
    B b;
    C c;

    public Triple(A a, B b, C c) {

        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A first()  { return a; }
    public B second() { return b; }
    public C third()  { return c; }
}

