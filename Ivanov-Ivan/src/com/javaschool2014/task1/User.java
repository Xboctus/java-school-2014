package com.javaschool2014.task1;

import java.util.*;

public class User {

    private final String name;
    private TimeZone timeZone = TimeZone.getDefault();
    private boolean status;

    private TreeMap<String, Event> events = new TreeMap<String, Event>();

    public User (String name) {

        this.name = name;

    }

    public String getName() {
        return name;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public boolean getStstus(){
        return status;
    }

    public Event getEvent(String text){
        return events.get(text);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public boolean addEvent(String text, Date datetime) {

        if (events.containsKey(text)) {
            System.out.println("Such event already exists");
            return false;
        }

        Event event = new Event(text, datetime);
        events.put(text, event);
        return true;

    }

    public boolean removeEvent(String text) {

        if (!events.containsKey(text)){
            System.out.println("No such event in existence");
            return false;
        }

        events.remove(text);
        return true;

    }

    @Override
    public String toString(){

        String userData =  name + " " + timeZone.getID() + " " + ((status) ? "active" : "idle") + "\n";

        for (Map.Entry<String, Event> entry : events.entrySet()) {
            userData += "Task: " + entry.getKey() + ". Date: " + entry.getValue().getDate() + "\n";
        }

        return userData;

    }

}