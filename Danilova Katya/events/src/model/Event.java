package model;

import java.util.Date;
import java.util.Timer;

public class Event implements Cloneable {

    private final Date date;
    private final String text;
    Timer timer;// = new Timer();

    public Event(Date date, String text) {
        this.date = date;
        this.text = text;
        timer = new Timer();
    }

    public Timer getTimer() {
        return timer;
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
