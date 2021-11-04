public class ASTNum implements ASTNode {

    int val;

    public int eval(Environment<Integer> e) { return val; }

    public void compile(CodeBlock c, Environment<int[]> e) {
        c.emit("sipush " + val);
    }

    public ASTNum(int n)
    {
        val = n;
    }

}

