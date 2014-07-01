package com.javaschool2014.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinator extends TimerTask implements Constants {

    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    public TreeMap<String, User> users = new TreeMap<String, User>();

    public  Coordinator () {

    }

    public void start(){

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            try {

                String command = input.readLine();
                String[] arguments = null;

                Pattern pattern = Pattern.compile(createUserPattern);
                Matcher matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (createUser(arguments[0], arguments[1], arguments[2])) {
                        System.out.println("User created!");
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(modifyUserPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (modifyUser(arguments[0], arguments[1], arguments[2])) {
                        System.out.println("User modified!");
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(addUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (addUserEvent(arguments[0], arguments[1], arguments[2])) {
                        System.out.println("Event added!");
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(addRandomTimeUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (addRandomTimeUserEvent(arguments[0], arguments[1], arguments[2], arguments[3])) {
                        System.out.println("Random time event added!");
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(removeUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (removeUserEvent(arguments[0], arguments[1])) {
                        System.out.println("Event removed!");
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(cloneUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (cloneUserEvent(arguments[0], arguments[1], arguments[2])) {
                        System.out.println("Event cloned!");
                    } else {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(showUserInfoPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!showUserInfo(arguments[0])) {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(startSchedulingPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {
                    startScheduling();
                    continue;
                }

                pattern = Pattern.compile(leavePattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {
                    System.exit(0);
                }

                System.out.println(WRONG_COMMAND);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    public boolean createUser(String name, String timeZone, String status) {

        if (users.containsKey(name)) {
            System.out.println(USER_EXISTS);
            return false;
        }

        User user = new User(name);
        TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);

        user.setTimeZone(userTimeZone);
        users.put(name, user);

        if (status.equals("active")) {
            user.setStatus(true);
        } else if (status.equals("idle")) {
            user.setStatus(false);
        }

        return true;

    }

    public boolean modifyUser(String name, String timeZone, String status) {

        if (!users.containsKey(name)) {
            System.out.println(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);
        TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);

        user.setTimeZone(userTimeZone);
        users.put(name, user);

        if (status.equals("active")) {
            user.setStatus(true);
        } else if (status.equals("idle")) {
            user.setStatus(false);
        }

        return true;

    }

    public boolean addUserEvent(String  name, String text, String dateTime) {

        if (!users.containsKey(name)) {
            System.out.println(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);
        Calendar calendar = new GregorianCalendar(user.getUserTimeZone());
        Date date = new Date();

        try {

            date = dateFormat.parse(dateTime);
            calendar.setTime(date);

        } catch (ParseException e) {

            System.out.println(WRONG_DATE);
            return false;

        }

        return (user.addEvent(text, calendar));

    }

    public boolean removeUserEvent(String name, String text) {

        if (!users.containsKey(name)) {
            System.out.println(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);

        return (user.removeEvent(text));

    }

    public boolean addRandomTimeUserEvent(String name, String text, String dateFrom, String dateTo) {
        return false;
    }

    public boolean cloneUserEvent(String name, String text, String nameTo) {

        if (!users.containsKey(name)) {
            System.out.println(name + WRONG_NAME);
            return false;
        }

        if (!users.containsKey(nameTo)) {
            System.out.println(nameTo + WRONG_NAME);
            return false;
        }

        Event event = users.get(name).getEvent(text);

        if (event==null) {
            System.out.println(EVENT_MISSING);
            return false;
        }

        return (users.get(nameTo).addEvent(event.getText(), event.getDate()));

    }

    public boolean showUserInfo(String name) {

        if (!users.containsKey(name)) {
            System.out.println(name + WRONG_NAME);
            return false;
        }

        System.out.println(users.get(name).toString());

        return true;

    }

    public void startScheduling() {

        System.out.println(SCHEDULING_STARTED);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(this, 0, 1000);

        try {

            System.in.read();

        } catch (IOException e) {

            e.printStackTrace();

        }

        System.out.println(SCHEDULING_STOPPED);
        timer.cancel();

        start();

    }

    public String[] parseString (String command) {

        String tempString = command.substring(command.indexOf("(") + 1, command.lastIndexOf(")"));
        String[] arguments = tempString.split(",");

        for (int i = 0; i < arguments.length; i++){
            arguments[i] = arguments[i].trim();
        }

        return arguments;
    }

    @Override
    public void run() {

        Date currentDate = new Date();
        dateFormat.setTimeZone(TimeZone.getDefault());

        for (Map.Entry<String, User> user : users.entrySet()) {

            String username = user.getValue().getName();
            List<Event> eventList = user.getValue().getEvents();

            for (Event event : eventList) {

                if (dateFormat.format(event.getDate().getTime()).equals(dateFormat.format(currentDate)) && user.getValue().getStatus()) {
                    System.out.println(currentDate + "\n" + username + "\n" + event.getText() + "\n");
                }

            }

        }

    }

}