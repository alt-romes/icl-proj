public class ASTIf implements ASTNode {

    ASTNode cond, lhs, rhs;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue c = cond.eval(e);

        if (((LBool)c).val())
            return lhs.eval(e);
        else
            return rhs.eval(e);

    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTIf(ASTNode c, ASTNode l, ASTNode r)
    {
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

