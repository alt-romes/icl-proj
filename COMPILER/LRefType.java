import java.io.FileWriter;
import java.io.IOException;

public class LRefType implements LType {

    public LType valType;

    public LRefType(LType t) { valType = t; }

    public String show() { return "Ref [ " + valType.show() + " ]"; }

    public LType getInnerType() { return valType; }
    
    public boolean equals(LType o) {

        return o instanceof LRefType && valType.equals(((LRefType)o).valType);
    }

    @Override
    public String getJVMTypeName() {
        return "ref_of_" + getJVMInnerValueTypeName();
    }

    public void dump() {

        try {
            FileWriter fw = new FileWriter(getJVMTypeName() + ".j");

            fw.write(".class " + getJVMTypeName() + "\n");
            fw.write(".super java/lang/Object\n");
            fw.write(".field public v " + valType.getJVMFieldTypeName() + "\n");
            fw.write(".method public <init>()V\n");
            fw.write("aload_0\n");
            fw.write("invokenonvirtual java/lang/Object/<init>()V\n");
            fw.write("return\n");
            fw.write(".end method\n");

            fw.close();
        }
        catch(IOException e) {
            System.err.println(e);
        }
    }

    public String getJVMFieldTypeName() {

        return "L" + getJVMTypeName() + ";";
    }

    public String getJVMInnerValueTypeName() {
        return valType.getJVMTypeName();
    }
}
