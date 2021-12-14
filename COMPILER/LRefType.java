import java.io.FileWriter;
import java.io.IOException;

public class LRefType implements LType {

    LType valType;

    public LRefType(LType t) { valType = t; }

    public String show() { return "Ref [ " + valType.show() + " ]"; }

    public LType getInnerType() { return valType; }
    
    public boolean equals(LType o) {

        return o instanceof LRefType && valType.equals(((LRefType)o).valType);
    }

    @Override
    public String getJVMTypeName() {
        return "ref_of_" + valType.getJVMTypeName();
    }

    public void dump() {

        try {
            FileWriter fw = new FileWriter(getJVMTypeName() + ".j");

            fw.write(".class " + getJVMTypeName() + "\n");
            fw.write(".super java/lang/Object\n");
            fw.write(".field public v " + (getJVMInnerValueTypeName().startsWith("ref_of") ? "L" + getJVMInnerValueTypeName() : getJVMInnerValueTypeName()) + ";\n");
            fw.write(".end method\n");

            fw.close();
        }
        catch(IOException e) {
            System.err.println(e);
        }
    }

    public String getJVMInnerValueTypeName() {
        return valType.getJVMTypeName();
    }
}
