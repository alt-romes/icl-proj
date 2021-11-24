public class ASTAdd implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue v1 = lhs.eval(e);
        if (!(v1 instanceof LInt)) throw new TypeError();

        LValue v2 = rhs.eval(e);
        if (!(v2 instanceof LInt)) throw new TypeError();

        return new LInt(((LInt)v1).val()+((LInt)v2).val());
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit("iadd");
    }

    public ASTAdd(ASTNode l, ASTNode r)
    {
        lhs = l; rhs = r;
    }
}

