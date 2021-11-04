public class ASTUminus implements ASTNode {

    ASTNode x;

    public int eval(Environment<Integer> e)
    { 
        int v = x.eval(e);
        return -v; 
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        x.compile(c, e);
        c.emit("ineg");
    }

    public ASTUminus(ASTNode x)
    {
        this.x = x;
    }
}

