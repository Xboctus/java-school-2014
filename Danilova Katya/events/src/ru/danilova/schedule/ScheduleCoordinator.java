package ru.danilova.schedule;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import ru.danilova.schedule.model.Event;
import ru.danilova.schedule.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.danilova.schedule.model.User.UserInfo.Status;

public class ScheduleCoordinator {

    private static final Map<String, User> name2user = new HashMap<>();
    private static final DateFormat format = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
    public static Mode mode = Mode.EDIT; //режим


    public static enum Mode {
        EDIT, OUTPUT
    }

    private static final String CREATE = "Create";
    private static final String MODIFY = "Modify";
    private static final String ADD_EVENT = "AddEvent";
    private static final String REMOVE_EVENT = "RemoveEvent";
    private static final String ADD_RANDOM_TIME_EVENT = "AddRandomTimeEvent";
    private static final String CLONE_EVENT = "CloneEvent";
    private static final String SHOW_INFO = "ShowInfo";
    private static final String START_SHEDULING = "StartScheduling";


    private static void create(String name, TimeZone timeZone, Status status) {
        name2user.put(name, new User(name, timeZone, status));
    }

    private static void modify(String name, TimeZone timeZone, Status status) {
        name2user.get(name).modify(timeZone, status);
    }

    private static void addEvent(String name, String text, Date date) {
        TimeZone userTimeZone = TimeZone.getTimeZone(name2user.get(name).getTimeZo());
        TimeZone defaultTimeZone = TimeZone.getDefault();
        int different = defaultTimeZone.getRawOffset() - userTimeZone.getRawOffset();
        date.setTime(date.getTime() + different);
        name2user.get(name).addEvent(new Event(date, text, name));
    }

    private static void removeEvent(String name, String text) {
        name2user.get(name).removeEvent(text);
    }

    private static void addRandomTimeEvent(String name, String text, Date from, Date to) {
        name2user.get(name).addRandomTimeEvent(text, from, to, name);
    }

    private static void cloneEvent(String nameFrom, String text, String nameTo) {
        Event eventClone;
        try{
            eventClone = (name2user.get(nameFrom).getEvent(text)).clone();
            name2user.get(nameTo).addEvent(eventClone);
        } catch (CloneNotSupportedException e) {
            System.out.println(" Событие не может быть клонировано ");
        }
    }

    private static String showInfo(String name) {
        String res = name2user.get(name).getUserInfo().toString();
        List<Event> events = new ArrayList<>(name2user.get(name).getAllEvent());
        Collections.sort(events);
        res += "\n" + name2user.get(name).getAllEvent().toString();
        return res;
    }

    private static void startScheduling() {
        mode = Mode.OUTPUT;
    }

    public static void main(String arguments[]) throws ParseException {

        Scanner cin = new Scanner(System.in);

        while (true) {
            String s = cin.nextLine();
            String command;// = s.substring(0, s.indexOf('('));

            if(s.indexOf('(') == -1) {
                command = s;
                if (command.equals(START_SHEDULING)) {
                    startScheduling();
                }
                continue;
            }

            command = s.substring(0, s.indexOf('('));
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
                addRandomTimeEvent(args[0], args[1], format.parse(args[2]), format.parse(args[3]));
            }
            if (command.equals(CLONE_EVENT)) {
                cloneEvent(args[0], args[1], args[2]);
            }
            if (command.equals(SHOW_INFO)) {
                System.out.println(showInfo(args[0]));
            }
        }
    }

}
