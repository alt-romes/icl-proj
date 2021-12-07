public class ASTIf implements ASTNode {

    ASTNode cond, lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue c = cond.eval(e);
        if (!(c instanceof LBool)) throw new TypeError();

        if (((LBool)c).val())
            return lhs.eval(e);
        else
            return rhs.eval(e);

    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTIf(ASTNode c, ASTNode l, ASTNode r)
    {
        cond = c; lhs = l; rhs = r;
    }
}


