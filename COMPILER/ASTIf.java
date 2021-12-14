public class ASTIf implements ASTNode {

    ASTNode cond, lhs, rhs;

    public LValue eval(Environment<LValue> e) { 

        LValue c = cond.eval(e);

        if (((LBool)c).val())
            return lhs.eval(e);
        else
            return rhs.eval(e);

    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        String l1 = "IfTL" + GlobalCounter.inc(),
               l2 = "IfFL" + GlobalCounter.inc(),
               l3 = "IfEX" + GlobalCounter.inc();
        // Cast guaranteed by type system
        ((ASTNodeX)cond).compileShortCircuit(c, e, l1, l2);
        c.emit(l1 + ":");
        lhs.compile(c, e);
        c.emit("goto " + l3);
        c.emit(l2 + ":");
        rhs.compile(c, e);
        c.emit(l3 + ":");
    }

    public ASTIf(ASTNode c, ASTNode l, ASTNode r) {
        cond = c; lhs = l; rhs = r;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType c = cond.typecheck(e);
        if (!(c instanceof LBoolType)) throw new TypeError("If condition must be a boolean.");
        
        LType l = lhs.typecheck(e);
        LType r = rhs.typecheck(e);
        
        if (!(l.equals(r))) throw new TypeError("If: then and else branches must return the same type.");

        return l;
    }
}

