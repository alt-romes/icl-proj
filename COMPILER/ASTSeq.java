public class ASTSeq implements ASTNodeX {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = lhs.eval(e);

        LValue v2 = rhs.eval(e);

        return v2;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        lhs.compile(c, e);
        c.emit("pop");
        rhs.compile(c, e);
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {
        compile(c, e);
        c.emit("ifeq %s", fl);
        c.emit("goto %s", tl);
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

