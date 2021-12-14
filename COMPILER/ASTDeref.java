public class ASTDeref implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) { 

        LValue v = x.eval(e);

        return ((LCell)v).get();
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTDeref(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = x.typecheck(e);
        if (!(t instanceof LRefType)) throw new TypeError("Can only dereference references!");

        return ((LRefType)t).getInnerType();
    }
}

