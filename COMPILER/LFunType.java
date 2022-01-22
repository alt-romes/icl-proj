import java.util.List;

public class LFunType implements LType {

    List<LType> argsTypes;
    LType retType;

    public LFunType(List<LType> l, LType t) { argsTypes = l; retType = t; }

    public String show() { return "fun () -> " + retType.show(); }

    // TODO
    public boolean equals(LType o) {

        return false;
        // return o instanceof LRefType && valType.equals(((LRefType)o).valType);
    }

    @Override
    public String getJVMTypeName() {
        // TODO:
        return "";
        // return "ref_of_" + getJVMInnerValueTypeName();
    }

    // public void dump() {

    //     try {
    //         FileWriter fw = new FileWriter(getJVMTypeName() + ".j");

    //         fw.write(".class " + getJVMTypeName() + "\n");
    //         fw.write(".super java/lang/Object\n");
    //         fw.write(".field public v " + valType.getJVMFieldTypeName() + "\n");
    //         fw.write(".method public <init>()V\n");
    //         fw.write("aload_0\n");
    //         fw.write("invokenonvirtual java/lang/Object/<init>()V\n");
    //         fw.write("return\n");
    //         fw.write(".end method\n");

    //         fw.close();
    //     }
    //     catch(IOException e) {
    //         System.err.println(e);
    //     }
    // }

    public String getJVMFieldTypeName() {

        // TODO:
        // return "L" + getJVMTypeName() + ";";
        return "";
    }

    // public String getJVMInnerValueTypeName() {
    //     return valType.getJVMTypeName();
    // }
}

