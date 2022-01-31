import java.util.LinkedList;
import java.util.List;

public class RefCellTypesGlobalList {

    public static List<LRefType> list = new LinkedList<>();

    public static void add(LRefType r) {
        list.add(r);
    }

    public static void generateClasses() {

        for (var t : list)
            t.dump();

    }

}
