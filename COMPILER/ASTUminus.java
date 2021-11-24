public class ASTUminus implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) throws TypeError { 
        LValue v = x.eval(e);
        if (!(v instanceof LInt)) throw new TypeError();

        return new LInt(-((LInt)v).val()); 
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

