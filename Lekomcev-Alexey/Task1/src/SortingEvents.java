import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;


public class SortingEvents {
    private static TreeSet<Output> treeSet;

    public SortingEvents(){
        treeSet = new TreeSet<Output>(comp);
    }

    public synchronized void add(User p_user, Date p_date, String p_description){
        treeSet.add(new Output(p_user, p_date, p_description));
    }

    public static synchronized Output getOutput() {
        return treeSet.pollFirst();
    }

    Comparator<Output> comp = new Comparator<Output>() {
        @Override
        public int compare(Output o1, Output o2) {
            int result = o1.date.compareTo(o2.date);
            if (result != 0) return result;
            result = o2.user.name.compareTo(o1.user.name);
            if (result !=0) return result;
            result = o2.description.compareTo(o1.description);
            return result;
        }
    };
}
