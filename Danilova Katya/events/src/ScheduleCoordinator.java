import model.Event;
import model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static model.User.UserInfo.Status;

public class ScheduleCoordinator {

    private static final Map<String, User> name2user = new HashMap<>();
    private static final DateFormat format = new SimpleDateFormat("dd.MM.yy-HH:mm:ss");
    private static Mode mode = Mode.EDIT;

    private static enum Mode {
        EDIT, OUTPUT
    }

    private static final String CREATE = "Create";
    private static final String MODIFY = "Modify";
    private static final String ADD_EVENT = "AddEvent";
    private static final String REMOVE_EVENT = "RemoveEvent";
    private static final String ADD_RANDOM_TIME_EVENT = "AddRandomTimeEvent";


    private static void create(String name, TimeZone timeZone, Status status) {
        name2user.put(name, new User(name, timeZone, status));
    }

    private static void modify(String name, TimeZone timeZone, Status status) {
        name2user.get(name).modify(timeZone, status);
    }

    private static void addEvent(String name, String text, Date date) {
        name2user.get(name).addEvent(new Event(date, text));
    }

    private static void removeEvent(String name, String text) {
        name2user.get(name).removeEvent(text);
    }

    private static String showInfo(String name) {
        return name2user.get(name).toString();
    }

    // private static void

    public static void main(String arguments[]) throws ParseException {

        Scanner cin = new Scanner(System.in);

        while (true) {
            String s = cin.nextLine();
            String command = s.substring(0, s.indexOf('('));
            String[] args = s.substring(s.indexOf('(') + 1, s.indexOf(')')).split(", ");

            if (command.equals(CREATE)) {
                create(args[0], TimeZone.getTimeZone(args[1]), Status.valueOf(args[2]));
            }
            if (command.equals(MODIFY)) {
                modify(args[0], TimeZone.getTimeZone(args[1]), Status.valueOf(args[2]));
            }
            if (command.equals(ADD_EVENT)) {
                addEvent(args[0], args[1], format.parse(args[2]));
            }
            if (command.equals(REMOVE_EVENT)) {
                removeEvent(args[0], args[1]);
            }
            if (command.equals(ADD_RANDOM_TIME_EVENT)) {

            }




        }

    }

}
