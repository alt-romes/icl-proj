import java.util.List;
import java.util.LinkedList;
public class ASTApply extends AbstractASTNode implements ASTNodeX {

    ASTNode f;
    List<ASTNode> params;

    LFunType f_type;
    LType self_type;

    public LValue eval(Environment<LValue> e) { 

        LValue v1 = f.eval(e);
        List<LValue> params = new LinkedList<>();
        for (var p : this.params)
            params.add(p.eval(e));

        // Type checker guarantees cast (agora estou a pensar que se calhar o
        // interpretador devia ser dynamically typechecked...)

        LFunc f = (LFunc)v1;  
        Environment<LValue> e2 = f.beginScope();
        for (int i = 0; i < params.size(); i++)
            e2.assoc(f.args.get(i).first(), params.get(i));

        LValue res = f.body.eval(e2);

        e2.endScope();

        return res;
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        // x.compile(c, e);
        // c.emit("getfield %s/v %s", x_type.getJVMTypeName(), self_type.getJVMTypeName());
    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {

        compile(c, e);
        c.emit("ifeq %s", fl);
        c.emit("goto %s", tl);
    }

    public ASTApply(ASTNode f, List<ASTNode> params)
    {
        this.f = f;
        this.params = params;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        LType t = f.typecheck(e);

        if (!(t instanceof LFunType)) throw new TypeError("Can only call apply on functions!");

        List<LType> paramTypes = new LinkedList<>();
        for (var tp : params)
            paramTypes.add(tp.typecheck(e));

        for (int i = 0; i < paramTypes.size(); i++)
            if (!paramTypes.get(i).equals(((LFunType)t).argsTypes.get(i)))
                throw new TypeError("Function arguments and passed parameters types don't match.");

        f_type = ((LFunType)t);
        self_type = ((LFunType)t).retType;

        if (nodeType == null || nodeType.equals(self_type))
            nodeType = self_type;
        else
            throw new TypeError("Declared type and expression type differ!");

        return self_type;
    }
}


