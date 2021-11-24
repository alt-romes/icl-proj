import java.util.*;

public class ASTDef implements ASTNode {

    Map<String, ASTNode> associations = new HashMap<>();
    ASTNode ef;

    public LValue eval(Environment<LValue> e) throws TypeError {

        var scope_env = e.beginScope(); 

        for (var entry : associations.entrySet())
            scope_env.assoc(entry.getKey(), entry.getValue().eval(scope_env));

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


        assert scope_env.associations.size() == 0;

        // The definitions are compiled using the new scope,
        // so that the definitions in this scope can use previous definitions
        // defined in this same scope
        int i = 0;
        for (var pair : associations.entrySet()) {
            c.emit(CodeBlock.LOAD_SL);
            pair.getValue().compile(c, scope_env);
            c.emit("putfield %s/s_%d I", scope_env.frame.type, i);
            scope_env.assoc(pair.getKey(), new int[]{scope_env.depth, i});
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

