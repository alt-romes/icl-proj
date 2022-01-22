public class ASTRelOp extends AbstractASTNode implements ASTNodeX {

    ASTNode lhs, rhs;
    int op;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = lhs.eval(e);
        int v1i = ((LInt)v1).val(); // TODO: Can also be boolean

        LValue v2 = rhs.eval(e);
        int v2i = ((LInt)v2).val(); // TODO: Ditto

        switch (op) {
            case ParserConstants.RELEQ:
                return new LBool(v1i == v2i);
            case ParserConstants.RELL:
                return new LBool(v1i < v2i);
            case ParserConstants.RELLE:
                return new LBool(v1i <= v2i);
            case ParserConstants.RELG:
                return new LBool(v1i > v2i);
            case ParserConstants.RELGE:
                return new LBool(v1i >= v2i);
        }

        assert(false);
        return null;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        String l1 = "L" + GlobalCounter.inc(),
               l2 = "L" + GlobalCounter.inc();
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit("isub");
        switch (op) {
            case ParserConstants.RELEQ:
                c.emit("ifeq " + l1);
                break;
            /* case  RELNEQ ... c.emit("ifne " + tl); */
            case ParserConstants.RELL:
                c.emit("iflt " + l1);
                break;
            case ParserConstants.RELLE:
                c.emit("ifle " + l1);
                break;
            case ParserConstants.RELG:
                c.emit("ifgt " + l1);
                break;
            case ParserConstants.RELGE:
                c.emit("ifge " + l1);
                break;
        }
        c.emit("sipush 0");
        c.emit("goto " + l2);
        c.emit(l1 + ":");
        c.emit("sipush 1");
        c.emit(l2 + ":");
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit("isub");
        switch (op) {
            case ParserConstants.RELEQ:
                c.emit("ifeq " + tl);
                break;
            /* case  RELNEQ ... c.emit("ifne " + tl); */
            case ParserConstants.RELL:
                c.emit("iflt " + tl);
                break;
            case ParserConstants.RELLE:
                c.emit("ifle " + tl);
                break;
            case ParserConstants.RELG:
                c.emit("ifgt " + tl);
                break;
            case ParserConstants.RELGE:
                c.emit("ifge " + tl);
                break;
        }
        c.emit("goto " + fl);
    }

    public ASTRelOp(ASTNode l, ASTNode r, int op)
    {
        lhs = l; rhs = r; this.op=op;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType l = lhs.typecheck(e);
        LType r = rhs.typecheck(e);

        switch (op) {
            // TODO: Different from
            case ParserConstants.RELEQ:
                if (!l.equals(r)) throw new TypeError("(==) takes two values of the same type.");
                break;
            case ParserConstants.RELL:
            case ParserConstants.RELLE:
            case ParserConstants.RELG:
            case ParserConstants.RELGE:
                if (!(l instanceof LIntType && r instanceof LIntType)) throw new TypeError("Relational greater than/lesser than take two integers.");
                break;
        }

        if (nodeType == null || nodeType.equals(LBoolType.get()))
            nodeType = LBoolType.get();
        else
            throw new TypeError("Declared type and expression type differ!");

        return nodeType;
    }

}

