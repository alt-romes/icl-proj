import java.util.*;

public class CodeBlock {

    List<String> code = new LinkedList<String>();

    void emit(String opcode) {
        code.add(opcode);
    }

    void dump() {
        for (int i=0; i<code.size(); i++) {
            System.out.println(code.get(i));
        }
    }
    
    CodeBlock() { }
}
