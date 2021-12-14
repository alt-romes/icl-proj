public class ASTWhile implements ASTNode {

    ASTNode cond, bod;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue l = null;

        LValue c = cond.eval(e);

        while (((LBool)c).val()) {
            l = bod.eval(e);
            c = cond.eval(e);
        }

        return l;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        c.emit("LStart:");
        // Cast guaranteed by type system
        ((ASTNodeX)cond).compileShortCircuit(c, e, "TL", "FL");
        c.emit("TL:");
        bod.compile(c, e);
        c.emit("pop");
        c.emit("goto LStart");
        c.emit("FL:");
    }

    public ASTWhile(ASTNode c, ASTNode b)
    {
        cond = c; bod = b;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {
        
        LType c = cond.typecheck(e);

        if (!(c instanceof LBoolType)) throw new TypeError("While condition must be a boolean.");

        return bod.typecheck(e);
    }
}

