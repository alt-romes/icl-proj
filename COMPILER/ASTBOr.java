public class ASTBOr implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue v1 = lhs.eval(e);
        LValue v2 = rhs.eval(e);

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

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType l = lhs.typecheck(e);
        LType r = rhs.typecheck(e);
        if (l instanceof LBoolType && r instanceof LBoolType)
            return LBoolType.get();

        throw new TypeError("Logical OR must be done with two booleans");
    }
}

