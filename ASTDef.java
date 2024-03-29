import java.util.*;

public class ASTDef implements ASTNodeX {

    List<Triple<String, LType, ASTNode>> associations;
    List<LType> associationsTypes = new ArrayList<>();
    ASTNode ef;

    public LValue eval(Environment<LValue> e) {

        var scope_env = e.beginScope(); 

        for (var entry : associations)
            scope_env.assoc(entry.first(), entry.third().eval(scope_env));

        var val = ef.eval(scope_env);

        e = scope_env.endScope(); // useless

        return val;
    }

    public void compile(CodeBlock c, Environment<int[]> e) {
        var scope_env = compileCommon(c, e);

        ef.compile(c, scope_env);
        
        // Frame pop off
        c.emit(CodeBlock.LOAD_SL);
        c.emit("getfield %s/sl L%s;", scope_env.frame.type, e.frame.type);
        c.emit(CodeBlock.STORE_SL);

        e = scope_env.endScope(); // still useless

    }

    public void compileShortCircuit(CodeBlock c, Environment<int[]> e, String tl, String fl) {

        var scope_env = compileCommon(c, e);

        ((ASTNodeX)ef).compileShortCircuit(c, scope_env, tl, fl);
        
        // Frame pop off
        c.emit(CodeBlock.LOAD_SL);
        c.emit("getfield %s/sl L%s;", scope_env.frame.type, e.frame.type);
        c.emit(CodeBlock.STORE_SL);

        e = scope_env.endScope(); // still useless

    }

    private Environment<int[]> compileCommon(CodeBlock c, Environment<int[]> e) {

        var scope_env = e.beginScope();

        Frame frame = c.addFrameType(associationsTypes, scope_env);
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
        for (var entry : associations) {
            scope_env.assoc(entry.first(), new int[]{scope_env.depth, i});
            c.emit(CodeBlock.LOAD_SL);
            entry.third().compile(c, scope_env);
            c.emit("putfield %s/s_%d %s", scope_env.frame.type, i, associationsTypes.get(i).getJVMFieldTypeName());
            i++;
        }

        
        // When we compile the expression inside this scope, the associations
        // must have been created
        assert scope_env.associations.size() > 0;

        // This method must be continued by the compile fucntion extending it (compile or compileShortCircuit)
        return scope_env;

    }

    public ASTDef(List<Triple<String,LType,ASTNode>> m, ASTNode ef) {
        
        this.associations = m;
        this.ef = ef;
    }

    public LType typecheck(Environment<LType> e) throws TypeError {

        var scope_env = e.beginScope(); 

        for (var entry : associations) {
            if (entry.second() != null)
                scope_env.assoc(entry.first(), entry.second());

            LType entryType = entry.third().typecheck(scope_env);
            scope_env.assoc(entry.first(), entryType); // Always overwrite with calculated type. This is important because each function type gets assigned an id, and we want to reason with the actual function type, rather than the declared function type (even though they should be the same. this is a bit of a hacky implementation anyway...)

            if (entry.second() != null &&
                    !entry.second().equals(entryType))
                throw new TypeError("Declared type and expression type differ!");

            associationsTypes.add(entryType);
        }

        LType eft = ef.typecheck(scope_env);

        e = scope_env.endScope(); // useless

        return eft;
    }
}

