public class LBool implements LValue {

    boolean val;

    LBool(boolean b) { val = b; }

    public boolean val() { return val; } 

    public void show() { System.out.println(val); }
}

