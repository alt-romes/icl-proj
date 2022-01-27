public class ASTBool implements ASTNodeX {

    boolean val;

    public LValue eval(Environment<LValue> e) { 

        return new LBool(val);
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        c.emit("sipush " + (val ? 1 : 0));
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {
        c.emit("goto " + (val ? tl : fl));
    }

    public ASTBool(boolean b) {
        val = b;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        return LBoolType.get();
    }
}

