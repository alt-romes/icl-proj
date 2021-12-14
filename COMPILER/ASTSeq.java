public class ASTSeq implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = lhs.eval(e);

        LValue v2 = rhs.eval(e);

        return v2;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTSeq(ASTNode l, ASTNode r)
    {
        lhs = l; rhs = r;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        lhs.typecheck(e);
        return rhs.typecheck(e);
    }
}

