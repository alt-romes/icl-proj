import java.util.*;
import java.io.*;

public class Frame {
    public String parent_type;
    public String type;

    int size = 0;

    Frame() {
        // The global environment has an empty frame of type Object,
        // The first actual stack frame will have use the Object type as its
        // ancestor type, and the pointer value will be null
        type = "java/lang/Object";
    }

    Frame(int size, String parent_type) {
        this.size = size; 
        this.parent_type = parent_type;
        this.type = Frame.getFrameTypeName(size, parent_type);
    }

    static String getFrameTypeName(int size, String parent_type) {
        return "frame_" + size + "_" + (parent_type == "java/lang/Object" ? "object" : parent_type);
    }

    void dump() {
        try {
            FileWriter fw = new FileWriter(type + ".j");

            fw.write(".class " + type + "\n");
            fw.write(".super java/lang/Object\n");
            fw.write(".field public sl L" + parent_type + ";\n");

            for (int i=0; i<size; i++) {
                fw.write(".field public s_" + i + " I\n");
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
