public class Pair<A, B> {

    A a;
    B b;

    public Pair(A a, B b) {

        this.a = a;
        this.b = b;
    }

    public A first() { return a; }
    public B second() { return b; }
}

