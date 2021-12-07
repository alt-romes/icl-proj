public class ASTBNeg implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) throws TypeError { 
        LValue v = x.eval(e);
        if (!(v instanceof LBool)) throw new TypeError();

        return new LBool(!((LBool)v).val()); 
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
    }

    public ASTBNeg(ASTNode x)
    {
        this.x = x;
    }
}


