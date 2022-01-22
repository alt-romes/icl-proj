public class ASTNum extends AbstractASTNode implements ASTNode {

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

        if (nodeType == null || nodeType.equals(LIntType.get()))
            nodeType = LIntType.get();
        else
            throw new TypeError("Declared type and expression type differ!");

        return nodeType;
    }
}

