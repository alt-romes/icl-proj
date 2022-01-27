public class ASTNum implements ASTNode {

    int val;

    public LValue eval(Environment<LValue> e) {
        return new LInt(val);
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        c.emit("sipush " + val);
    }

    public ASTNum(int n)
    {
        val = n;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        return LIntType.get();
    }
}

