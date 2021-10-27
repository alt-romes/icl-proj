public class ASTDef implements ASTNode {

    String id;
    ASTNode e1, e2;

    public int eval(Environment<Integer> e)
    {
        var v1 = e1.eval(e);
        e = e.beginScope();
        e.assoc(id, v1);
        var val = e2.eval(e);
        e = e.endScope(); // useless
        return val;
    }

    public void compile(CodeBlock c) {

    }

    public ASTDef(String id, ASTNode e1, ASTNode e2)
    {
        this.id = id;
        this.e1 = e1;
        this.e2 = e2;
    }
}

