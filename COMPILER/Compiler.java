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

            ASTNode ast = Parser.Start();

            if (ast == null)
                System.exit(0);

            var cb = new CodeBlock();
            Environment<int[]> env = new Environment<int[]>();
            env.assocFrameType(new Frame()); // Empty frame has type Object, and first scope will have a null pointer to ancestor of type Object (this frame's type)

            ast.compile(cb, env);

            // String[] names = cb.dumpFrames();
            cb.dumpFrames();
            cb.dump("Main.j");

            Runtime.getRuntime().exec("java -jar jasmin.jar Main.j frame_1_object.j frame_1_frame_1_object.j").waitFor();

        } catch (Exception e) {
            System.out.println ("Syntax Error!");
            System.out.println (e);
            Parser.ReInit(System.in);
        }
    }
}
