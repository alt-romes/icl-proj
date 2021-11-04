public class ASTId implements ASTNode {

    String id;

    public int eval(Environment<Integer> e)
    { 
        return e.find(id); 
    }

    public void compile(CodeBlock c, Environment<int[]> e)
    {
        int[] coords = e.find(id);
        c.emit(CodeBlock.LOAD_SL);
        for (int i = coords[0]; i < e.depth; i++)
            c.emit("getfield %s/sl L%s;", e.frame.type, e.parent.frame.type);
    
        c.emit("getfield %s/s_%d I", e.frame.type, coords[1]);
    }

    public ASTId(String id)
    {
        this.id = id;
    }
}

