package com.leomze;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;


public class User implements Serializable{
    private String name;
    private int timeZone;
    private boolean active;
    private ArrayList<Event> events = new ArrayList<Event>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public ArrayList<Event> getEvents() {
        return events;
    }

    public String getUserEvents() {
        String str = new String();
        if(!events.equals(null)){
        for(Event event: events){
            str = str + "\n Date event: " + simpleDateFormat.format(event.getDate()) + "\n Event text: " + event.getText() + "\n";
        }
        return str;
        } else {
        return null;
        }
    }

    public void removeEvent(String text){
       for(Iterator<Event> it = events.iterator(); it.hasNext(); ){
           Event event = it.next();
           if(event.getText().equals(text)){
               it.remove();
           }
       }
    }

    public void addEvent(Date date, String text) {
        events.add(new Event((timeToGMT(date)), text));
        Comparator<Event> comparator = new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getDate().compareTo(e2.getDate());
            }
        };
        Collections.sort(events, comparator);
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User(String name, int timeZone, boolean active) {

        this.name = name;
        this.timeZone = timeZone;
        this.active = active;
    }

    public Event getEvent(String eventText){
        for (Event event : events){
            if(event.getText().equals(eventText))
                  return event;
        }
        return null;
    }

    public Date timeToGMT(Date date) {
        return new Date(date.getTime() - timeZone * 60 * 60 * 1000);
    }

}
