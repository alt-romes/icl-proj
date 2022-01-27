import java.util.List;
public class LFunc implements LValue {

    List<Pair<String, LType>> args;
    Environment<LValue> e;
    public ASTNode body;

    LFunc(List<Pair<String, LType>> args, ASTNode b, Environment<LValue> e) {
        this.args = args;
        this.e = e;
        body = b;
    }

    // public LValue get() { return val; } 

    // public void set(LValue v) { val = v; }

    public Environment<LValue> beginScope() {

        return e.beginScope();
    }

    public void show() { System.out.print("->"); }
}


