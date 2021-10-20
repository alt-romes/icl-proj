public class ASTNum implements ASTNode {

    int val;

    public int eval() { return val; }

    public void compile(CodeBlock c) {
        c.emit("sipush " + val);
    }

    public ASTNum(int n)
    {
        val = n;
    }

}

