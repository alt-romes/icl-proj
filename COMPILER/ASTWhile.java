public class ASTWhile implements ASTNode {

    ASTNode cond, bod;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        LValue c = cond.eval(e);
        if (!(c instanceof LBool)) throw new TypeError();

        LValue l = null;
        while (((LBool)c).val())
            l = bod.eval(e);

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



