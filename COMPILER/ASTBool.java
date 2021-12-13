public class ASTBool implements ASTNode {

    boolean val;

    public LValue eval(Environment<LValue> e) { 

        return new LBool(val);
    }

    public void compile(CodeBlock c, Environment<int[]> e) {

    }

    public ASTBool(boolean b) {
        val = b;
    }

    public LType typecheck(Environment<LType> e) {

        return LBoolType.get();
    }
}

