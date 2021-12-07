public class ASTDeref implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue v = x.eval(e);
        if (!(v instanceof LCell)) throw new TypeError();

        return ((LCell)v).get();
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTDeref(ASTNode x)
    {
        this.x = x;
    }
}

