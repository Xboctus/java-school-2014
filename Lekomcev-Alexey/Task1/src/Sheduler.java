//import javax.management.Descriptor;
import java.text.SimpleDateFormat;
import java.util.*;

public class Sheduler {

    public static void main(String[] args){
        Coordinator coordinator = new Coordinator();
        String command = "";
        Scanner in = new Scanner(System.in);
        do{
            System.out.println("Enter the command");
            command = in.nextLine();
            int index = command.indexOf('(');
            if (index == -1){
                System.out.println("incorrect command");
                continue;
            }
            String shortCommand = command.substring(0, command.indexOf('('));
            if (shortCommand.equals("Create")){
                coordinator.Create(command);
            }
            if (shortCommand.equals("Modify")){
                coordinator.Modify(command);
            }
            if (shortCommand.equals("AddEvent")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(','));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.addEvent(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (shortCommand.equals("RemoveEvent")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(','));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.removeEvent(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (shortCommand.equals("AddRandomTimeEvent")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(','));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.addRandomTimeEvent(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (shortCommand.equals("CloneEvent")){
                coordinator.CloneEvent(command);
            }
            if (shortCommand.equals("ShowInfo")){
                try{
                    String nameValue = command.substring(command.indexOf('(') + 1, command.indexOf(')'));
                    User user = coordinator.getUser(nameValue);
                    if (user != null) {
                        user.showInfo(command, coordinator);
                    }
                    else {System.out.println("User not found");}
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }while(!command.equals("StartScheduling"));
        Comparator<Sheduling> comp = new Comparator<Sheduling>() {
            @Override
            public int compare(Sheduling o1, Sheduling o2) {
                int result = o1.date.compareTo(o2.date);
                if (result != 0) return result;
                result = o2.user.name.compareTo(o1.user.name);
                if (result !=0) return result;
                result = o2.description.compareTo(o1.description);
                return result;
            }
        };
        TreeSet<Sheduling> ts = new TreeSet<Sheduling>(comp);
        for (User u : coordinator.users){
            if(u.status == true){
                for (Event e : u.events){
                    ts.add(new Sheduling(u, e.date, e.description));
                }
            }
        }

        Timer timer;
        for(Sheduling s : ts){
            timer = new Timer();
            timer.schedule(new TimerShedule(s.user, s.description), s.date);
        }
    }
}

class TimerShedule extends TimerTask{
    User user;
    String description;

    TimerShedule(User p_user, String p_description){
        user = p_user;
        description = p_description;
    }
    @Override
    public void run(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        System.out.println(sdf.format(new Date()));
        System.out.println(user.name);
        System.out.println(description);
    }
}

class Sheduling {
    User user;
    Date date;
    String description;
    Sheduling(User p_user, Date p_date, String p_description){
        user = p_user;
        date = p_date;
        description = p_description;
    }
}