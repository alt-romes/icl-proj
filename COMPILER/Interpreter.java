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

                System.out.println( ast.eval(new Environment<Integer>()) );

            } catch (Exception e) {
                System.out.println ("Syntax Error!");
                System.out.println (e);
                parser.ReInit(System.in);
            }

        }

    }

}
