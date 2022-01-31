public class GlobalCounter {

    private static int counter = 0; 

    private static int closureCounter = 0; 

    public static int inc() {

        return counter++;
    }

    public static int incClosureId() {

        return closureCounter++;
    }
}
