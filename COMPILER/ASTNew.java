public class ASTNew implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) { 

        return new LCell(x.eval(e));
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        // TODO
    }

    public ASTNew(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        return new LRefType(x.typecheck(e));
    }
}

