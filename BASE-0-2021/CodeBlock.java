import java.util.*;
import java.io.*;

public class CodeBlock {

    final String head = """
.class public Main
.super java/lang/Object
;
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

    ;    1 - the PrintStream object held in java.lang.System.out
    getstatic java/lang/System/out Ljava/io/PrintStream;

    ; place your bytecodes here between START and END
        ; START
""";

    final String tail = """
               ; END
       ; convert to String;
       invokestatic java/lang/String/valueOf(I)Ljava/lang/String;
       ; call println
       invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
       return
.end method
        """;

    List<String> code = new LinkedList<String>();

    void emit(String opcode) {
        code.add(opcode);
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
