package model;

import java.util.*;

public class Event implements Cloneable {

    private final Date date;
    private final String text;
    Timer timer;
    TimerTask task;
    String name;
    /*private static final Map<String, User> name2user = new HashMap<>();

    private static void addUser(String name, TimeZone timeZone, User.UserInfo.Status status) {
        name2user.put(name, new User(name, timeZone, status));
    }*/

    public Event(Date date, final String text, final String name) {
        this.date = date;
        this.text = text;
        this.name = name;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() + name + text);
                //вывести имя пользователя и текст события
            }
        };
        timer.schedule(task, date.getTime());
    }

 /*   public Timer getTimer() {
        return timer;
    }*/

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public Event clone() throws CloneNotSupportedException {
        Event clone = (Event)super.clone();
        //clone.date = (Date)date.clone();
        //clone.text = (String)text.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
