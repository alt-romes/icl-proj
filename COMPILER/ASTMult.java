public class ASTMult extends AbstractASTNode implements ASTNode {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = lhs.eval(e);
        LValue v2 = rhs.eval(e);

        return new LInt(((LInt)v1).val()*((LInt)v2).val()); 
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit("imul");
    }

    public ASTMult(ASTNode l, ASTNode r) {
        lhs = l; rhs = r;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType l = lhs.typecheck(e);
        LType r = rhs.typecheck(e);
        if (!(l instanceof LIntType && r instanceof LIntType))
            throw new TypeError("Multiplication must be done with two booleans");

        if (nodeType == null || nodeType.equals(LIntType.get()))
            nodeType = LIntType.get();
        else
            throw new TypeError("Declared type and expression type differ!");

        return nodeType;
    }

}

