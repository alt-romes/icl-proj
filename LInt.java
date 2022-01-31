public class LInt implements LValue {

    int val;

    LInt(int n) { val = n; }

    public int val() { return val; } 

    public void show() { System.out.println(val); }
}
