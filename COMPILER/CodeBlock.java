import java.util.*;
import java.io.*;

public class CodeBlock {

    static final String head = """
.class public Main
.super java/lang/Object

; standard initializer
.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    ; set limits used by this method
    .limit locals 10
    .limit stack 256

    ; 1 - the PrintStream object held in java.lang.System.out
    getstatic java/lang/System/out Ljava/io/PrintStream;

    ; Move null to local main variable
    aconst_null
    astore 2

    ; START

""";

    static final String tail = """
    ; END

    ; convert to String;
    invokestatic java/lang/String/valueOf(I)Ljava/lang/String;

    ; call println
    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
    return
.end method
""";

    static final String LOAD_SL = "aload 2";
    static final String STORE_SL = "astore 2";

    public Map<String, Frame> frame_types = new HashMap<>();

    List<String> code = new LinkedList<String>();

    void emit(String op, Object ...fmtargs) {
        code.add(String.format(op, fmtargs));
    }

    <T> Frame addFrameType(Environment<T> e) {
        // The first scoped environment's parent will always have a frame because the global environment is started with an empty frame
        String key = Frame.getFrameTypeName(e.associations.size(), e.parent.frame.type);
        Frame fr = frame_types.get(key);
        if (fr == null) {
            fr = new Frame(e.associations.size(), e.parent.frame.type);
            frame_types.put(key, fr);
        }
        return fr;
    }

    void dumpFrames() {
        for (var f : frame_types.values())
            f.dump();
    }

    void dump(String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(head);
            for (int i=0; i<code.size(); i++) {
                fw.write(code.get(i) + "\n");
            }
            fw.write(tail);
            fw.close();
        }
        catch(IOException e) {
            System.err.println(e);
        } 
    }
    
    CodeBlock() { }
}