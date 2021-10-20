import java.io.*;

/** ID lister. */
public class Compiler {

    /** Main entry point. */
    public static void main(String args[]) {

        if (args.length < 1) {
            System.err.println("The first argument needs to be the source filename");
            System.exit(0);
        }

        Parser parser = null;

        try {
            parser = new Parser(new FileInputStream(args[0]));
        }
        catch (Exception e) { System.err.println(e); }

        try {

            ASTNode ast = parser.Start();

            if (ast == null)
                System.exit(0);

            var cb = new CodeBlock();
            ast.compile(cb);
            cb.dump();

        } catch (Exception e) {
            System.out.println ("Syntax Error!");
            System.out.println (e);
            parser.ReInit(System.in);
        }
    }
}

