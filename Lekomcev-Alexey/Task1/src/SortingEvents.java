import javax.jws.soap.SOAPBinding;
import java.awt.*;
import java.util.*;


public class SortingEvents {
    private static TreeSet<Output> treeSet;

    public SortingEvents(){
        treeSet = new TreeSet<Output>(comp);
    }

    public synchronized void add(User p_user, Date p_date, String p_description){
        treeSet.add(new Output(p_user, p_date, p_description));
    }

    public synchronized void add(User p_user){
        for(Event e : p_user.events){
            treeSet.add(new Output(p_user, e.date, e.description));
        }
    }

    public synchronized void delete(User p_user){
        for(Iterator<Output> it = treeSet.iterator(); it.hasNext();){
            Output output = it.next();
            if (output.equals(new Output(p_user))){
                it.remove();
            }
        }
    }

    public static synchronized Output getOutput() {
        return treeSet.pollFirst();
    }

    public static synchronized void setTreeSet(ArrayList<User> users){
        treeSet.clear();
        for(User u : users){
            for(Event e : u.events){
                treeSet.add(new Output(u, e.date, e.description));
            }
        }
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
