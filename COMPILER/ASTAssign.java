public class ASTAssign extends AbstractASTNode implements ASTNodeX {

    ASTNode lhs, rhs;

    LType left_type, right_type;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = lhs.eval(e);
        LValue v2 = rhs.eval(e);
        ((LCell)v1).set(v2);

        return v2;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        lhs.compile(c, e); // Reference
        c.emit("dup");
        rhs.compile(c, e); // Value
        c.emit("putfield %s/v %s", left_type.getJVMTypeName(), right_type.getJVMTypeName());
        c.emit("getfield %s/v %s", left_type.getJVMTypeName(), right_type.getJVMTypeName());
        // c.emit("swap"); // ... maybe with swap?
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {
        compile(c, e);
        c.emit("ifeq %s", fl);
        c.emit("goto %s", tl);
    }

    public ASTAssign(ASTNode l, ASTNode r) {
        lhs = l; rhs = r;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {
        LType l = lhs.typecheck(e);
        if (!(l instanceof LRefType)) throw new TypeError("Illegal arguments to := operator, left operand is not a reference.");
        left_type = l;

        LType r = rhs.typecheck(e);
        if (!(r.equals(((LRefType)l).getInnerType()))) throw new TypeError("Right side of := operator must match inner type of left operand reference.");
        right_type = r;

        if (nodeType == null || nodeType.equals(r))
            nodeType = r;
        else
            throw new TypeError("Declared type and expression type differ!");

        return nodeType;
    }
}

