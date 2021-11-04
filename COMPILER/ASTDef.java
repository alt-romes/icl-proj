import java.util.*;

public class ASTDef implements ASTNode {

    Map<String, ASTNode> associations = new HashMap<>();
    ASTNode ef;

    public int eval(Environment<Integer> e) {

        var scope_env = e.beginScope(); 

        for (var entry : associations.entrySet())
            scope_env.assoc(entry.getKey(), entry.getValue().eval(e));

        var val = ef.eval(scope_env);

        e = scope_env.endScope(); // useless

        return val;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {

        var scope_env = e.beginScope();

        int i = 0;
        for (var key : associations.keySet()) {
            scope_env.assoc(key, new int[]{scope_env.depth, i});
            i++;
        }

        Frame frame = c.addFrameType(scope_env);
        scope_env.assocFrameType(frame);

        // Frame creation and linkage into environment stack
        c.emit("new %s", scope_env.frame.type);
        c.emit("dup");
        c.emit("invokespecial %s/<init>()V", scope_env.frame.type);
        c.emit("dup");
        c.emit(CodeBlock.LOAD_SL);
        c.emit("putfield %s/sl L%s;", scope_env.frame.type, e.frame.type);
        c.emit(CodeBlock.STORE_SL);

        i = 0;
        for (var val : associations.values()) {
            c.emit(CodeBlock.LOAD_SL);
            val.compile(c, e);
            c.emit("putfield %s/s_%d I", scope_env.frame.type, i);
            i++;
        }

        ef.compile(c, scope_env);
        
        // Frame pop off
        c.emit(CodeBlock.LOAD_SL);
        c.emit("getfield %s/sl L%s;", scope_env.frame.type, e.frame.type);
        c.emit(CodeBlock.STORE_SL);

        e = scope_env.endScope(); // still useless

    }

    public ASTDef(Map<String,ASTNode> m, ASTNode ef) {
        
        this.associations = m;
        this.ef = ef;
    }
}

