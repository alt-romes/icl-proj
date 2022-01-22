public interface ASTNode {

    LValue eval(Environment<LValue> e);

    void compile(CodeBlock c, Environment<int[]> e);

    LType typecheck(Environment<LType> e) throws TypeError;

    void declType(LType t);
}
