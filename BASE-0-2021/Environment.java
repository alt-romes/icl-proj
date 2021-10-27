import java.util.*;

public class Environment<T> {
    Map<String, T> associations = new HashMap<>();

    Environment<T> parent = null;;

    Environment() { }

    Environment(Environment<T> e) {
        parent = e;
    }

    Environment<T> beginScope() {
        return new Environment<T>(this);
    }

    Environment<T> endScope() {
        return this.parent;
    }

    void assoc(String id, T val) {
         
        associations.put(id, val);
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
