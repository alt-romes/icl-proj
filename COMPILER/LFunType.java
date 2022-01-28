import java.util.List;
import java.io.*;

public class LFunType implements LType {

    private int closureId = -1;

    public List<LType> argsTypes;
    public LType retType;

    public LFunType(List<LType> l, LType t, boolean isTypeDecl) {
        argsTypes = l;
        retType = t;
        closureId = GlobalCounter.incClosureId();
        if (isTypeDecl)
             // If a variable is defined as a closure, the next generated closure id will be for the declared closure.
             // That is, this closure type declaration will match the next generated closure, whose id will be the next
            closureId++;
    }

    public String show() { return "fun () -> " + retType.show(); }

    public boolean equals(LType o) {

        if (!(o instanceof LFunType)) return false;

        for (int i = 0; i < argsTypes.size(); i++)
            if (!argsTypes.get(i).equals(((LFunType)o).argsTypes.get(i)))
                return false;

        return retType.equals(((LFunType)o).retType);
    }

    @Override
    public String getJVMTypeName() {
        return "closure_" + closureId;
    }

    public String getJVMInterfaceTypeName() {
        String name = "closure_interface_" + retType.getJVMTypeName();

        for (var ty : argsTypes)
            name += "_" + ty.getJVMTypeName();

        return name;
    }

    public void dumpInterface() {

        try {
            FileWriter fw = new FileWriter(getJVMInterfaceTypeName() + ".j");

            fw.write(".interface " + getJVMInterfaceTypeName() + "\n");
            fw.write(".super java/lang/Object\n");
            String args = "";
            int i = 0;
            for (var arg : argsTypes)
                args += arg.getJVMFieldTypeName();
            fw.write(".method public abstract apply(" + args + ")" + retType.getJVMFieldTypeName() + "\n");
            fw.write(".end method\n");

            fw.close();
        }
        catch(IOException e) {
            System.err.println(e);
        }
    }

    // Yes, this is a bit out of place
    public void dumpClosure(List<String> bodyCode, Environment<int[]> env) {

        try {
            FileWriter fw = new FileWriter(getJVMTypeName() + ".j");

            fw.write(".class " + getJVMTypeName() + "\n");
            fw.write(".super java/lang/Object\n");
            fw.write(".implements " + getJVMInterfaceTypeName() + "\n");
            fw.write(".field public sl L" + env.frame.type + ";\n");

            fw.write(".method public <init>()V\n");
            fw.write("aload_0\n");
            fw.write("invokenonvirtual java/lang/Object/<init>()V\n");
            fw.write("return\n");
            fw.write(".end method\n");

            String args = "";
            int i = 0;
            for (var arg : argsTypes)
                args += arg.getJVMFieldTypeName();
            fw.write(".method public apply(" + args + ")" + retType.getJVMFieldTypeName() + "\n");
            fw.write(".limit locals " + argsTypes.size() + 3 + "\n");
            fw.write(".limit stack 256\n");

            for (String s : bodyCode)
                fw.write(s + "\n");

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

    public int getClosureId() {
        return closureId;
    }

}

