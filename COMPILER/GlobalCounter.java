public class GlobalCounter {

    private static int counter = 0; 

    public static int inc() {

        return counter++;
    }
}
