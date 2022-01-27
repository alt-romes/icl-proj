public class ASTBNeg implements ASTNodeX {

    ASTNode x;

    public LValue eval(Environment<LValue> e) { 
        LValue v = x.eval(e);

        return new LBool(!((LBool)v).val()); 
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        String l1 = "L" + GlobalCounter.inc(),
               l2 = "L" + GlobalCounter.inc();
        x.compile(c, e);
        c.emit("ifeq " + l1);
        c.emit("sipush 0");
        c.emit("goto " + l2);
        c.emit(l1 + ":");
        c.emit("sipush 1");
        c.emit(l2 + ":");
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {
        // Cast to ASTNodeX guaranteed by the type system
        ((ASTNodeX)x).compileShortCircuit(c, e, fl, tl);
    }

    public ASTBNeg(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = x.typecheck(e);
        if (!(t instanceof LBoolType)) throw new TypeError("Negation can only be applied to a boolean.");

        return t;
    }
}

