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

        Frame frame = c.addFrameType(associations.size(), scope_env);
        scope_env.assocFrameType(frame);


        // A frame must be associated to the new environment
        // before we can compile the frame creation
        assert scope_env.frame != null;

        // Frame creation and linkage into environment stack
        c.emit("new %s", scope_env.frame.type);
        c.emit("dup");
        c.emit("invokespecial %s/<init>()V", scope_env.frame.type);
        c.emit("dup");
        c.emit(CodeBlock.LOAD_SL);
        c.emit("putfield %s/sl L%s;", scope_env.frame.type, e.frame.type);
        c.emit(CodeBlock.STORE_SL);


        // The definitions are compiled using the new scope,
        // but without the associations
        assert scope_env.associations.size() == 0;

        int i = 0;
        for (var val : associations.values()) {
            c.emit(CodeBlock.LOAD_SL);
            val.compile(c, scope_env);
            c.emit("putfield %s/s_%d I", scope_env.frame.type, i);
            i++;
        }

        i = 0;
        for (var key : associations.keySet()) {
            scope_env.assoc(key, new int[]{scope_env.depth, i});
            i++;
        }

        
        // When we compile the expression inside this scope, the associations
        // must have been created
        assert scope_env.associations.size() > 0;

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

