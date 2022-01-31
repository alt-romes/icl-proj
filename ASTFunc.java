import java.util.*;
import java.util.Collection.*;
import java.util.stream.*;
import java.util.stream.Collectors.*;

public class ASTFunc implements ASTNode {

    List<Pair<String, LType>> args;
    ASTNode body;

    LType retType;
    public LFunType self_type = null;

    public LValue eval(Environment<LValue> e) { 

        return new LFunc(args, body, e);
    }
    
    public void compile(CodeBlock c, Environment<int[]> e) {
        CodeBlock closure = c.addClosure(self_type, e);
        c.emit("new %s", self_type.getJVMTypeName());
        c.emit("dup");
        c.emit("invokespecial %s/<init>()V", self_type.getJVMTypeName());
        c.emit("dup");
        c.emit(CodeBlock.LOAD_SL);
        c.emit("putfield %s/sl L%s;", self_type.getJVMTypeName(), e.frame.type);

        var scope_env = e.beginScope();

        Frame frame = c.addFrameType(args.stream().map(Pair::second).collect(Collectors.toList()), scope_env);
        scope_env.assocFrameType(frame);

        closure.emit("new %s", scope_env.frame.type);
        closure.emit("dup");
        closure.emit("invokespecial %s/<init>()V", scope_env.frame.type);
        closure.emit("dup");
        closure.emit("aload 0"); 
        closure.emit("getfield %s/sl L%s;", self_type.getJVMTypeName(), e.frame.type); 
        closure.emit("putfield %s/sl L%s;", scope_env.frame.type, e.frame.type); 

        int i = 0;
        for (var entry : args) {
            scope_env.assoc(entry.first(), new int[]{scope_env.depth, i});
            closure.emit("dup");
            if(entry.second() instanceof LRefType || entry.second() instanceof LFunType)
                closure.emit(String.format("aload %d", i+1));
            else
                closure.emit(String.format("iload %d", i+1));
            closure.emit("putfield %s/s_%d %s", scope_env.frame.type, i, entry.second().getJVMFieldTypeName());
            i++;
        }

        closure.emit(CodeBlock.STORE_SL);

        body.compile(closure, scope_env);

        if(retType instanceof LRefType || retType instanceof LFunType)
            closure.emit("areturn");
        else
            closure.emit("ireturn");

        e.endScope();
    }

    public ASTFunc(List<Pair<String, LType>> args, ASTNode body) {
        this.args = args;
        this.body = body;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        List<LType> argsTypes = args.stream().map(Pair::second).collect(Collectors.toList());

        var scope_env = e.beginScope(); 

        for (var entry : args)
            scope_env.assoc(entry.first(), entry.second());

        retType = body.typecheck(scope_env);

        e = scope_env.endScope(); // useless

        self_type = new LFunType(argsTypes, retType, false);

        return self_type;

    }
}

