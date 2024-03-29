import java.io.*;
import java.util.*;


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

        ASTNode ast = null;
        try {

            ast = Parser.Start();

            if (ast == null)
                System.exit(0);

        } catch (Exception e) {
            System.out.println ("Syntax Error?");
            System.out.println(e);
            Parser.ReInit(System.in);
        }

        try {

            System.err.println( "Typechecking..." );
            ast.typecheck(new Environment<>());

            var cb = new CodeBlock();
            Environment<int[]> env = new Environment<>();
            env.assocFrameType(new Frame()); // Empty frame has type Object, and first scope will have a null pointer to ancestor of type Object (this frame's type)

            System.err.println( "Compiling..." );
            ast.compile(cb, env);

            Set<String> closures_names = cb.dumpClosures();
            Set<String> closures_interfaces_names = cb.dumpClosuresInterfaces();
            Set<String> refcells_names = cb.dumpRefCells();
            Set<String> frames_names = cb.dumpFrames();
            cb.dump("Main.j");

            StringBuilder exec = new StringBuilder("java -jar jasmin.jar Main.j");
            for (var f : frames_names)
                exec.append(" ").append(f).append(".j");
            for (var t : refcells_names)
                exec.append(" ").append(t).append(".j");
            for (var t : closures_names)
                exec.append(" ").append(t).append(".j");
            for (var t : closures_interfaces_names)
                exec.append(" ").append(t).append(".j");
            System.out.println(exec);

            System.err.println( "Compiling jasmin..." );
            Runtime.getRuntime().exec(exec.toString()).waitFor();

        } catch (TypeError e) {
            System.out.println(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


// vim: foldmarker={,}
