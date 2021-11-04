import java.util.*;

public class Environment<T> {
    Map<String, T> associations = new HashMap<>();
    public int depth = 0;

    Environment<T> parent = null;

    public Frame frame = null;

    Environment() { }

    Environment(Environment<T> e, int depth) {
        parent = e;
        this.depth = depth;
    }

    Environment<T> beginScope() {
        return new Environment<T>(this, depth+1);
    }

    Environment<T> endScope() {
        return this.parent;
    }

    void assoc(String id, T val) {
         
        associations.put(id, val);
    }

    void assocFrameType(Frame frame) {
        this.frame = frame;
    }

    T find(String id) {

        var v = associations.get(id);
        if (v != null) return v;
        else if (parent != null) return parent.find(id);
        else {
            System.err.println("No denotation for " + id);
            System.exit(1);
            return null;
        }
    }
}
