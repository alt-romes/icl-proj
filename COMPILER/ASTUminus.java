public class ASTUminus extends AbstractASTNode implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) { 
        LValue v = x.eval(e);

        return new LInt(-((LInt)v).val()); 
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        x.compile(c, e);
        c.emit("ineg");
    }

    public ASTUminus(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = x.typecheck(e);
        if (!(t instanceof LIntType)) throw new TypeError("Unary minus operator takes an int.");

        if (nodeType == null || nodeType.equals(t))
            nodeType = t;
        else
            throw new TypeError("Declared type and expression type differ!");

        return t;
    }
}

