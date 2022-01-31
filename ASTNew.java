public class ASTNew implements ASTNode {

    ASTNode x;

    LType self_type;

    public LValue eval(Environment<LValue> e) { 

        return new LCell(x.eval(e));
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        c.addRefCellType((LRefType) self_type);
        c.emit("new %s", self_type.getJVMTypeName());
        c.emit("dup");
        c.emit("invokespecial %s/<init>()V", self_type.getJVMTypeName());
        c.emit("dup");
        x.compile(c, e);
        c.emit("putfield %s/v %s", self_type.getJVMTypeName(), ((LRefType)self_type).getJVMInnerValueTypeName());
    }

    public ASTNew(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        self_type = new LRefType(x.typecheck(e));

        return self_type;
    }
}

