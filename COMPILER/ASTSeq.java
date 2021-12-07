public class ASTSeq implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

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
}


