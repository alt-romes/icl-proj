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

    public void compile(CodeBlock c) {

    }

    public ASTDef(Map<String,ASTNode> m, ASTNode ef) {
        
        this.associations = m;
        this.ef = ef;
    }
}

