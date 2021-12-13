public class LBoolType implements LType {

    static LBoolType t = null;

    public LBoolType() {}

    public String show() { return "Bool"; }

    public static LType get() {

        if (t == null)
            t = new LBoolType();
            
        return t;
    }
}

