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
        // TODO
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

