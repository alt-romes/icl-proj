public class ASTAssign implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue v1 = lhs.eval(e);
        LValue v2 = rhs.eval(e);
        ((LCell)v1).set(v2);

        return v2;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        //TODO
    }

    public ASTAssign(ASTNode l, ASTNode r)
    {
        lhs = l; rhs = r;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {
        LType l = lhs.typecheck(e);
        if (!(l instanceof LRefType)) throw new TypeError("Illegal arguments to := operator, left operand is not a reference.");

        return rhs.typecheck(e);
    }
}

