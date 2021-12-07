public class ASTPrintln implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) throws TypeError { 
        
        LValue v = x.eval(e);

        v.show();

        return v; // println returns the value printed
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTPrintln(ASTNode x)
    {
        this.x = x;
    }
}

