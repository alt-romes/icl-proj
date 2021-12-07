public class ASTAssign implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue v1 = lhs.eval(e);
        if (!(v1 instanceof LCell)) throw new TypeError("Illegal arguments to := operator");

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
}


