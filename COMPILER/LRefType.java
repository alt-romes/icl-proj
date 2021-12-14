public class LRefType implements LType {

    LType valType;

    public LRefType(LType t) { valType = t; }

    public String show() { return "Ref [ " + valType.show() + " ]"; }

    public LType getInnerType() { return valType; }
    
    public boolean equals(LType o) {

        return o instanceof LRefType && valType.equals(((LRefType)o).valType);
    }
}
