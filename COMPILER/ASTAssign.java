public class ASTAssign implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) { 

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

        LType r = rhs.typecheck(e);
        if (!(r.equals(((LRefType)l).getInnerType()))) throw new TypeError("Right side of := operator must match inner type of left operand reference.");

        return r;
    }
}

