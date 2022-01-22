public class ASTDeref extends AbstractASTNode implements ASTNodeX {

    ASTNode x;

    LRefType x_type;
    LType self_type;

    public LValue eval(Environment<LValue> e) { 

        LValue v = x.eval(e);

        return ((LCell)v).get();
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        x.compile(c, e);
        c.emit("getfield %s/v %s", x_type.getJVMTypeName(), self_type.getJVMTypeName());
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {

        compile(c, e);
        c.emit("ifeq %s", fl);
        c.emit("goto %s", tl);
    }

    public ASTDeref(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = x.typecheck(e);
        if (!(t instanceof LRefType)) throw new TypeError("Can only dereference references!");

        x_type = ((LRefType)t);
        self_type = ((LRefType)t).getInnerType();

        if (nodeType == null || nodeType.equals(self_type))
            nodeType = self_type;
        else
            throw new TypeError("Declared type and expression type differ!");

        return self_type;
    }
}

