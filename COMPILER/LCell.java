public class LCell implements LValue {

    LValue val;

    LCell(LValue v) { val = v; }

    public LValue get() { return val; } 

    public void set(LValue v) { val = v; }

    public void show() { System.out.println("Cell["+val+"]"); }
}

interface LValue {

    void show();

}
