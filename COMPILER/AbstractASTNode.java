public abstract class AbstractASTNode implements ASTNode {

    LType nodeType = null;

    public void declType(LType t) { nodeType = t; }

}

