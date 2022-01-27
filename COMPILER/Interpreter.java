/** ID lister. */
public class Interpreter {

    /** Main entry point. */
    public static void main(String args[]) {
        Parser parser = new Parser(System.in);

        while (true) {
            try {
                System.out.print( "> " );

                ASTNode ast = parser.Start();

                if (ast == null)
                    System.exit(0);

                System.err.println( "Typechecking..." );
                ast.typecheck(new Environment<LType>());

                System.err.println( "Evaluating..." );
                ast.eval(new Environment<LValue>()).show();

            } catch (TypeError e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println ("Syntax Error!");
                System.out.println (e);
                parser.ReInit(System.in);
            }

        }

    }

}
