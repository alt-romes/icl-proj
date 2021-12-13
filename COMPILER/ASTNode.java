public interface ASTNode {

    LValue eval(Environment<LValue> e) throws TypeError;

    void compile(CodeBlock c, Environment<int[]> e);

    LType typecheck(Environment<LType> e) throws TypeError;
}
