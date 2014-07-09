import java.util.*;


public class SortingEvents {
    private static TreeSet<Output> treeSet;
    private static final Object monitor = new Object();

    public SortingEvents(){
        treeSet = new TreeSet<Output>(comp);
    }

    public synchronized void add(User p_user, Date p_date, String p_description){
        synchronized (monitor) {
            treeSet.add(new Output(p_user, p_date, p_description));
            monitor.notifyAll();
        }
    }

    public synchronized void add(User p_user){
        for(Event e : p_user.events){
            treeSet.add(new Output(p_user, e.date, e.description));
        }
    }

    public synchronized void changeTreeSet(User user, String oldDescription, String newDescription) {
        Event event = user.events.get(user.events.indexOf(new Event(newDescription)));
        Output output = new Output(user, event.date, oldDescription);
        if (treeSet.contains(output)) {
            Output requiredOutput = findElement(output);
            if (requiredOutput != null) {
                treeSet.remove(requiredOutput);
            }
        }
        treeSet.add(new Output(user, event.date, newDescription));
    }

    public synchronized void delete(User p_user){
        for(Iterator<Output> it = treeSet.iterator(); it.hasNext();){
            Output output = it.next();
            for(Event e : p_user.events){
                if (output.equals(new Output(p_user, e.date, e.description))){
                    it.remove();
                }
            }
        }
    }

    public static Object getMonitor(){
        return monitor;
    }

    public static synchronized Output getOutput() {
        return treeSet.pollFirst();
    }

    private Output findElement(Output p_output){
        Iterator<Output> iterator = treeSet.iterator();
        while(iterator.hasNext()) {
            Output output = iterator.next();
            if(output.equals(p_output))
                return output;
        }
        return null;
    }

    public static synchronized void setTreeSet(ArrayList<User> users){
        synchronized (monitor){
            treeSet.clear();
            for(User u : users){
                for(Event e : u.events){
                    treeSet.add(new Output(u, e.date, e.description));
                }
            }
            monitor.notifyAll();
        }
    }

    Comparator<Output> comp = new Comparator<Output>() {
        @Override
        public int compare(Output o1, Output o2) {
            int result;
            result = o2.user.name.compareTo(o1.user.name);
            if (result !=0) return result;
            result = o2.description.compareTo(o1.description);
            return result;
        }
    };
}
