public class ASTUminus implements ASTNode {

    ASTNode x;

    public int eval()
    { 
        int v = x.eval();
        return -v; 
    }
    
    public void compile(CodeBlock c) {
        x.compile(c);
        c.emit("ineg");
    }

    public ASTUminus(ASTNode x)
    {
        this.x = x;
    }
}

