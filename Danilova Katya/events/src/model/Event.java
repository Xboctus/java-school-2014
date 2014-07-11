package model;

import java.util.*;

public class Event implements Cloneable, Comparable<Event> {

    private final Date date;
    private final String text;
    Timer timer;
    TimerTask task;
    String name;

    public Event(Date date, final String text, final String name) {
        this.date = date;
        this.text = text;
        this.name = name;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() + "\n" + name + "\n" + text);
            }
        };
        timer.schedule(task, date.getTime());
    }

    @Override
    public int compareTo(Event event) {
        int res = event.name.compareTo(name);
        if(res != 0) {
            return  res;
        }
        return event.text.compareTo(text);
    }

    public String getNames() {
        return name;
    }

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
