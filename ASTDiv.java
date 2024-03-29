public class ASTDiv implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = lhs.eval(e);
        LValue v2 = rhs.eval(e);

        return new LInt(((LInt)v1).val()/((LInt)v2).val());
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit("idiv");
    }

    public ASTDiv(ASTNode l, ASTNode r)
    {
        lhs = l; rhs = r;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType l = lhs.typecheck(e);
        LType r = rhs.typecheck(e);
        if (!(l instanceof LIntType && r instanceof LIntType))
            throw new TypeError("Division must be done with two booleans");

        return LIntType.get();
    }
}

