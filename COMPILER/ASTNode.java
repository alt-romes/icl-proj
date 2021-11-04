public interface ASTNode {

    int eval(Environment<Integer> e);

    void compile(CodeBlock c, Environment<int[]> e);
	
}
