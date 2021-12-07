public class ASTNew implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) throws TypeError { 

        return new LCell(x.eval(e));
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTNew(ASTNode x)
    {
        this.x = x;
    }
}


