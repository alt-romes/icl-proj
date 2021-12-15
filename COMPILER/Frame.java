import java.util.*;
import java.io.*;

public class Frame {
    public String parent_type;
    public String type;

    List<LType> typelist;

    Frame() {
        // The global environment has an empty frame of type Object,
        // The first actual stack frame will have use the Object type as its
        // ancestor type, and the pointer value will be null
        type = "java/lang/Object";
    }

    Frame(List<LType> typelist, String parent_type) {
        this.typelist = typelist;
        this.parent_type = parent_type;
        this.type = Frame.getFrameTypeName(typelist, parent_type);
    }

    static String getFrameTypeName(List<LType> typelist, String parent_type) {
        StringBuilder res = new StringBuilder("f");
        for (var t : typelist)
            res.append(t.getJVMTypeName());
        res.append(parent_type.equals("java/lang/Object") ? "o" : parent_type);
        return res.toString();
    }

    void dump() {
        try {
            FileWriter fw = new FileWriter(type + ".j");

            fw.write(".class " + type + "\n");
            fw.write(".super java/lang/Object\n");
            fw.write(".field public sl L" + parent_type + ";\n");

            int i = 0;
            for (var t : typelist) {
                fw.write(".field public s_" + i + " " + t.getJVMFieldTypeName() + "\n");
            }

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
}
