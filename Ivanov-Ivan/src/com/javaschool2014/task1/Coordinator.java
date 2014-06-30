package com.javaschool2014.task1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Coordinator extends TimerTask {

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
    public TreeMap<String, User> users = new TreeMap<String, User>();

    public  Coordinator () {

    }

    public boolean createUser(String name, String timeZone, boolean status) {

        if (users.containsKey(name)) {
            System.out.println("Such user already exists");
            return false;
        }

        User user = new User(name);
        TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);

        user.setTimeZone(userTimeZone);
        user.setStatus(status);
        users.put(name, user);

        return true;

    }

    public boolean modifyUser(String name, String timeZone, boolean status) {

        if (!users.containsKey(name)) {
            System.out.println("No " + name + " in existence");
            return false;
        }

        User user = users.get(name);
        TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);

        user.setTimeZone(userTimeZone);
        user.setStatus(status);
        users.put(name, user);

        return true;

    }

    public boolean addUserEvent(String  name, String text, String dateTime) {

        if (!users.containsKey(name)) {
            System.out.println("No " + name + " in existence");
            return false;
        }

        User user = users.get(name);
        Date eventDate = new Date();

        try {
            eventDate = dateFormat.parse(dateTime + user.getTimeZone().getID());
        } catch (ParseException e) {
            System.out.println("Incorrect date/time");
            return false;
        }

        return (user.addEvent(text, eventDate));

    }

    public boolean removeUserEvent(String name, String text) {

        if (!users.containsKey(name)) {
            System.out.println("No " + name + " in existence");
            return false;
        }

        User user = users.get(name);

        return (user.removeEvent(text));

    }

    public boolean addRandomTimeUserEvent(String name, String text, Date dateFrom, Date dateTo) {
        return false;
    }

    public boolean cloneUserEvent(String name, String text, String nameTo) {

        if (!users.containsKey(name)) {
            System.out.println("No " + name + " in existence");
            return false;
        }

        if (!users.containsKey(nameTo)) {
            System.out.println("No " + nameTo + " in existence");
            return false;
        }

        Event event = users.get(name).getEvent(text);

        if (event==null) {
            System.out.println("No such event in existence");
            return false;
        }

        return (users.get(nameTo).addEvent(event.getText(), event.getDate()));

    }

    public boolean showUserInfo(String name) {

        if (!users.containsKey(name)) {
            System.out.println("No " + name + " in existence");
            return false;
        }

        System.out.println(users.get(name).toString());

        return true;

    }

    public void startScheduling() {

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(this, 0, 1000);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Scheduling stopped");
        timer.cancel();

    }

    @Override
    public void run() {

        Date currentDate = new Date();

        for (Map.Entry<String, User> user : users.entrySet()) {

            String username = user.getValue().getName();
            List<Event> eventList = user.getValue().getEvents();

            for (Event event : eventList) {

                if (event.getDate().toString().equals(currentDate.toString()) && user.getValue().getStstus()) {
                    System.out.println(currentDate + "\n" + username + "\n" + event.getText() + "\n");
                }

            }

        }

    }

}