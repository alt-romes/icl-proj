public class ASTId implements ASTNodeX {

    String id;

    public LValue eval(Environment<LValue> e) { 
        return e.find(id); 
    }

    public void compile(CodeBlock c, Environment<int[]> e)
    {
        int[] coords = e.find(id);  /* TODO: could remove find and just while
                                     * loop until the id in the environment association */

        c.emit(CodeBlock.LOAD_SL);

        int depth = e.depth;
        for (int i = coords[0]; i < depth; i++) {
            c.emit("getfield %s/sl L%s;", e.frame.type, e.parent.frame.type);
            e = e.parent;
        }
    
        c.emit("getfield %s/s_%d I", e.frame.type, coords[1]);
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {

        compile(c, e); // Put bool value on top of stack
        c.emit("ifeq L1"); // If false
        c.emit("goto " + tl); // Else jump to true
        c.emit("L1:"); // Jump to false
        c.emit("goto " + fl);
    }

    public ASTId(String id)
    {
        this.id = id;
    }

    public LType typecheck(Environment<LType> e) {
        return e.find(id);
    }
}

