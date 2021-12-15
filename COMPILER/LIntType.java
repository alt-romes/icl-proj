public class LIntType implements LType {

    static LIntType t = null;

    public LIntType() {}

    public String show() { return "Int"; }

    public static LType get() {

        if (t == null)
            t = new LIntType();
            
        return t;
    }

    public boolean equals(LType o) {

        return o instanceof LIntType;
    }

    @Override
    public String getJVMTypeName() {
        return "I";
    }

    @Override
    public String getJVMFieldTypeName() {
        return getJVMTypeName();
    }
}

