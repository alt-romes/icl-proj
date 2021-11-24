public class ASTRelOp implements ASTNode {

    ASTNode lhs, rhs;
    int op;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue v1 = lhs.eval(e);
        if (!(v1 instanceof LInt)) throw new TypeError();
        int v1i = ((LInt)v1).val();

        LValue v2 = rhs.eval(e);
        if (!(v2 instanceof LInt)) throw new TypeError();
        int v2i = ((LInt)v2).val();

        switch (op) {
            case ParserConstants.RELEQ:
                return new LBool(v1i == v2i);
            case ParserConstants.RELL:
                return new LBool(v1i < v2i);
            case ParserConstants.RELLE:
                return new LBool(v1i <= v2i);
            case ParserConstants.RELG:
                return new LBool(v1i > v2i);
            case ParserConstants.RELGE:
                return new LBool(v1i >= v2i);
            default:
                int x = 0/0; // lol
                return null;
        }

    }

    public void compile(CodeBlock c, Environment<int[]> e) {

    }

    public ASTRelOp(ASTNode l, ASTNode r, int op)
    {
        lhs = l; rhs = r; this.op=op;
    }
}

