public class ASTPrint implements ASTNodeX {

    ASTNode x;

    LType x_type;

    public LValue eval(Environment<LValue> e) { 
        
        LValue v = x.eval(e);

        v.show();

        return v; // println returns the value printed
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        x.compile(c, e);
        c.emit("dup");
        c.emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
        c.emit("swap");
        c.emit("invokestatic java/lang/String/valueOf(%s)Ljava/lang/String;", x_type.getJVMFieldTypeName());
        c.emit("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {
        compile(c, e);
        c.emit("ifeq %s", fl);
        c.emit("goto %s", tl);
    }

    public ASTPrint(ASTNode x)
    {
        this.x = x;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        x_type = x.typecheck(e);
        return x_type;
    }
}

