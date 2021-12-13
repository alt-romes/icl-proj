public class ASTBNeg implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) throws TypeError { 
        LValue v = x.eval(e);

        return new LBool(!((LBool)v).val()); 
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
    }

    public ASTBNeg(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = x.typecheck(e);
        if (!(t instanceof LBoolType)) throw new TypeError("Negation can only be applied to a boolean.");

        return t;
    }
}

