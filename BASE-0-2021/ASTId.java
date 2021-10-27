public class ASTId implements ASTNode {

    String id;

    public int eval(Environment<Integer> e)
    { 
        return e.find(id); 
    }

    public void compile(CodeBlock c) {
    }

    public ASTId(String id)
    {
        this.id = id;
    }
}

