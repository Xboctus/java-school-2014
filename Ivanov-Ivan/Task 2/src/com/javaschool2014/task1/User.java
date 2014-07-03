package com.javaschool2014.task1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class User implements Constants {

    private final String name;
    private boolean status;
    private TimeZone timeZone             = TimeZone.getDefault();
    DateFormat dateFormat                 = new SimpleDateFormat(DATE_FORMAT);

    private TreeMap<String, Event> events = new TreeMap<String, Event>();

    public User (String name) {

        this.name = name;

    }

    public String getName() {
        return name;
    }

    public TimeZone getUserTimeZone() {
        return timeZone;
    }

    public boolean getStatus(){
        return status;
    }

    public Event getEvent(String text){
        return events.get(text);
    }

    public List getEvents(){

        List<Event> eventList = new ArrayList<Event>();

        for (Map.Entry<String, Event> entry : events.entrySet()) {
            eventList.add(entry.getValue());
        }

        return eventList;

    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public boolean addEvent(String text, Calendar calendar) {

        if (events.containsKey(text)) {
            System.out.println(EVENT_EXISTS);
            return false;
        }

        if (calendar == null) {
            return false;
        }

        Event event = new Event(text, calendar);
        events.put(text, event);
        return true;

    }

    public boolean removeEvent(String text) {

        if (!events.containsKey(text)){
            System.out.println(EVENT_MISSING);
            return false;
        }

        events.remove(text);
        return true;

    }

    @Override
    public String toString(){

        String userData =  name + " " + timeZone.getID() + " " + ((status) ? "active" : "idle") + "\n";
        dateFormat.setTimeZone(this.timeZone);

        for (Map.Entry<String, Event> entry : events.entrySet()) {
            userData += "Task: " + entry.getKey() + ". Date: " + dateFormat.format(entry.getValue().getDate().getTime()) + "\n";
        }

        return userData;

    }

}