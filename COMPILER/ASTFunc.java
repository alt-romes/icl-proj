import java.util.*;
import java.util.Collection.*;
import java.util.stream.*;
import java.util.stream.Collectors.*;

public class ASTFunc implements ASTNode {

    List<Pair<String, LType>> args;
    ASTNode body;

    public LValue eval(Environment<LValue> e) { 

        return new LFunc(args, body, e);
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        // c.addRefCellType((LRefType) self_type);
        // c.emit("new %s", self_type.getJVMTypeName());
        // c.emit("dup");
        // c.emit("invokespecial %s/<init>()V", self_type.getJVMTypeName());
        // c.emit("dup");
        // x.compile(c, e);
        // c.emit("putfield %s/v %s", self_type.getJVMTypeName(), ((LRefType)self_type).getJVMInnerValueTypeName());
    }

    public ASTFunc(List<Pair<String, LType>> args, ASTNode body)
    {
        this.args = args;
        this.body = body;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        List<LType> argsTypes = args.stream().map(Pair::second).collect(Collectors.toList());

        var scope_env = e.beginScope(); 

        for (var entry : args)
            scope_env.assoc(entry.first(), entry.second());

        LType retType = body.typecheck(scope_env);

        e = scope_env.endScope(); // useless

        return new LFunType(argsTypes, retType);

    }
}

