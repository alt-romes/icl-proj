public class ASTPreinc implements ASTNode {

    ASTNode x;

    public LValue eval(Environment<LValue> e) { 

        LValue v = x.eval(e);

//         if (!(v instanceof LCell))
//             throw new TypeError("Preinc operation can only be done on reference cells!");
//         if (!(((LCell)v).get() instanceof LInt))
//             throw new TypeError("Preinc operation can only be done on reference cells of type int.");

        LCell c = (LCell)v;
        LInt newValue = new LInt(((LInt)c.get()).val() + 1);
        c.set(newValue);

        return newValue;
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        x.compile(c, e); // Reference
        c.emit("dup");
        c.emit("dup");
        c.emit("getfield ref_of_I/v I"); // Value
        c.emit("sipush 1");
        c.emit("iadd"); // Modified value
        c.emit("putfield ref_of_I/v I"); // Save value
        c.emit("getfield ref_of_I/v I"); // Get value to return again
    }

    public ASTPreinc(ASTNode x) { this.x = x; }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = x.typecheck(e);
        if (!(t instanceof LRefType))
            throw new TypeError("Preinc operator can only be done on reference cells!");
        if (!(((LRefType)t).getInnerType() instanceof LIntType))
            throw new TypeError("Preinc operator can only be done on reference cells of type int.");

        return LIntType.get();
    }
}
