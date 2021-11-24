public class ASTBOr implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue v1 = lhs.eval(e);
        if (!(v1 instanceof LBool)) throw new TypeError();

        LValue v2 = rhs.eval(e);
        if (!(v2 instanceof LBool)) throw new TypeError();

        return new LBool(((LBool)v1).val() || ((LBool)v2).val());
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
        // lhs.compile(c, e);
        // rhs.compile(c, e);
        // c.emit("iadd");
    }

    public ASTBOr(ASTNode l, ASTNode r)
    {
        lhs = l; rhs = r;
    }
}


