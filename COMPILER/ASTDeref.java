public class ASTDeref implements ASTNode {

    ASTNode x;

    LRefType x_type;
    LType self_type;

    public LValue eval(Environment<LValue> e) { 

        LValue v = x.eval(e);

        return ((LCell)v).get();
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        x.compile(c, e);
        c.emit("getfield %s/v %s", x_type, self_type);
    }

    public ASTDeref(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = x.typecheck(e);
        if (!(t instanceof LRefType)) throw new TypeError("Can only dereference references!");

        x_type = ((LRefType)t);
        self_type = ((LRefType)t).getInnerType();

        return self_type;
    }
}

