public class ASTWhile implements ASTNode {

    ASTNode cond, bod;

    public LValue eval(Environment<LValue> e) { 

        LValue l = null;

        LValue c = cond.eval(e);

        while (((LBool)c).val()) {
            l = bod.eval(e);
            c = cond.eval(e);
        }

        return c;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        String l1 = "LWhileStart" + GlobalCounter.inc(),
               l2 = "LWhileTL"    + GlobalCounter.inc(),
               l3 = "LWhileFL"    + GlobalCounter.inc();
        c.emit(l1 + ":");
        // Cast guaranteed by type system
        ((ASTNodeX)cond).compileShortCircuit(c, e, l2, l3);
        c.emit(l2 + ":");
        bod.compile(c, e);
        c.emit("pop");
        c.emit("goto " + l1);
        c.emit(l3  + ":");
        c.emit("sipush 0"); // push false
    }

    public ASTWhile(ASTNode c, ASTNode b)
    {
        cond = c; bod = b;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {
        
        LType c = cond.typecheck(e);

        if (!(c instanceof LBoolType)) throw new TypeError("While condition must be a boolean.");

        bod.typecheck(e);

        return LBoolType.get();
    }
}

