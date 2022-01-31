public class ASTBAnd implements ASTNodeX {

    ASTNode lhs, rhs;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = lhs.eval(e);
        LValue v2 = rhs.eval(e);

        return new LBool(((LBool)v1).val() && ((LBool)v2).val());
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit("iand");
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {
        String laux = tl + "aux" + GlobalCounter.inc();
        ((ASTNodeX)lhs).compileShortCircuit(c, e, laux, fl);
        c.emit(laux + ":");
        ((ASTNodeX)rhs).compileShortCircuit(c, e, tl, fl);
    }
    
    public ASTBAnd(ASTNode l, ASTNode r)
    {
        lhs = l; rhs = r;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType l = lhs.typecheck(e);
        LType r = rhs.typecheck(e);
        if (!(l instanceof LBoolType && r instanceof LBoolType))
            throw new TypeError("Logical AND must be done with two booleans");

        return LBoolType.get();
    }
}

