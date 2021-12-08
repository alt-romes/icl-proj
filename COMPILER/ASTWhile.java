public class ASTWhile implements ASTNode {

    ASTNode cond, bod;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue l = null;

        LValue c = cond.eval(e);
        if (!(c instanceof LBool)) throw new TypeError();

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
}

