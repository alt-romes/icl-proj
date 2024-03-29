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

    ; Move null to local main variable
    aconst_null
    astore 2

    ; START

""";

    static final String tail = """
    ; END

    ; Remove from stack expression value
    pop

    return
.end method
""";

    static final String LOAD_SL = "aload 2";
    static final String STORE_SL = "astore 2";

    public Map<String, Frame> frame_types = new HashMap<>();

    private final Map<String, Triple<LFunType, CodeBlock, Environment<int[]>>> closures = new HashMap<>();
    private final Map<String, LFunType> closures_interfaces = new HashMap<>();

    private final Map<String, LRefType> refcell_types = new HashMap<>();

    List<String> code = new LinkedList<String>();

    void emit(String op, Object ...fmtargs) {
        code.add(String.format(op, fmtargs));
    }

    <T> Frame addFrameType(List<LType> typelist, Environment<T> e) {
        // The first scoped environment's parent will always have a frame because the global environment is started with an empty frame
        String key = Frame.getFrameTypeName(typelist, e.parent.frame.type);
        Frame fr = frame_types.get(key);
        if (fr == null) {
            fr = new Frame(typelist, e.parent.frame.type);
            frame_types.put(key, fr);
        }
        return fr;
    }

    Set<String> dumpFrames() {
        for (var f : frame_types.values())
            f.dump();
        return frame_types.keySet();
    }

    Set<String> dumpRefCells() {
        for (var t : refcell_types.values())
            t.dump();
        return refcell_types.keySet();
    }

    Set<String> dumpClosures() {
        for (var t : closures.values())
            t.first().dumpClosure(t.second().getCode(), t.third());
        return closures.keySet();
    };

    Set<String> dumpClosuresInterfaces() {
        for (var t : closures_interfaces.values())
            t.dumpInterface();
        return closures_interfaces.keySet();
    };

    void dump(String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(head);
            for (String s : code)
                fw.write(s + "\n");
            fw.write(tail);
            fw.close();
        }
        catch(IOException e) {
            System.err.println(e);
        } 
    }

    public List<String> getCode() { return code; }
    
    CodeBlock() { }

    public void addRefCellType(LRefType type) {

        String key = type.getJVMTypeName();
        refcell_types.putIfAbsent(key, type);
    }

    public CodeBlock addClosure(LFunType ty, Environment<int[]> e) {

        assert(ty.getClosureId() != -1);

        CodeBlock clos = new CodeBlock();
        closures.put(ty.getJVMTypeName(), new Triple<>(ty, clos, e));
        closures_interfaces.putIfAbsent(ty.getJVMInterfaceTypeName(), ty);

        return clos;
    }
}
