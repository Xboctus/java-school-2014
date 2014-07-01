package model;

import java.util.Date;

public class Event {

    private final Date date;
    private final String text;

    public Event(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
