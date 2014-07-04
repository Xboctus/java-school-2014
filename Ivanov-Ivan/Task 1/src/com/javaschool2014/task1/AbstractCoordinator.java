package com.javaschool2014.task1;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCoordinator extends TimerTask implements Constants {

    private Timer timer                 = new Timer(true);
    private DateFormat dateFormat       = new SimpleDateFormat(DATE_FORMAT);

    private TreeMap<String, User> users = new TreeMap<String, User>();

    protected void printOutput(String output) {}

    protected void connectDefaultDataFile() {}

    public void start(){

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        timer.scheduleAtFixedRate(this, 0, 1000);
        connectDefaultDataFile();

        while (true) {

            try {

                String command = input.readLine();
                String[] arguments = null;

                Pattern pattern = Pattern.compile(createUserPattern);
                Matcher matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (createUser(arguments[0], arguments[1], arguments[2])) {
                        System.out.println(USER_CREATED);
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
                        System.out.println(USER_MODIFIED);
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
                        System.out.println(EVENT_ADDED);
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
                        System.out.println(RANDOM_EVENT_ADDED);
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
                        System.out.println(EVENT_REMOVED);
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
                        System.out.println(EVENT_CLONED);
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

                pattern = Pattern.compile(saveUserDataPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!saveUserData(arguments[0])) {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(loadUserDataPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!loadUserData(arguments[0])) {
                        System.out.println(ERROR);
                    }

                    continue;
                }

                pattern = Pattern.compile(leavePattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {
                    timer.cancel();
                    System.exit(0);
                }

                printOutput(WRONG_COMMAND);

            } catch (IOException e) {

                System.out.println(e.getMessage());

            }

        }

    }

    public boolean createUser(String name, String timeZone, String status) {

        if (name.trim().isEmpty()) {
            printOutput(NO_NAME);
            return false;
        }

        if (timeZone.trim().isEmpty()) {
            printOutput(NO_TIMEZONE);
            return false;
        }

        if (users.containsKey(name)) {
            printOutput(USER_EXISTS);
            return false;
        }

        User user = new User(name);
        TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);
        user.setTimeZone(userTimeZone);

        if (status.equals("active")) {
            user.setStatus(true);
        } else if (status.equals("idle")) {
            user.setStatus(false);
        } else {
            printOutput(WRONG_STATUS);
            return false;
        }

        users.put(name, user);

        return true;

    }

    public boolean modifyUser(String name, String timeZone, String status) {

        if (name.trim().isEmpty()) {
            printOutput(NO_NAME);
            return false;
        }

        if (timeZone.trim().isEmpty()) {
            printOutput(NO_TIMEZONE);
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);
        TimeZone userTimeZone = TimeZone.getTimeZone(timeZone);
        user.setTimeZone(userTimeZone);

        if (status.equals("active")) {
            user.setStatus(true);
        } else if (status.equals("idle")) {
            user.setStatus(false);
        } else {
            printOutput(WRONG_STATUS);
            return false;
        }

        users.put(name, user);

        return true;

    }

    public boolean addUserEvent(String  name, String text, String dateTime) {

        if (name.trim().isEmpty()) {
            printOutput(NO_NAME);
            return false;
        }

        if (text.trim().isEmpty()) {
            printOutput(NO_TEXT);
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);
        Calendar calendar = new GregorianCalendar(user.getUserTimeZone());
        Date date = new Date();

        try {

            date = dateFormat.parse(dateTime);
            calendar.setTime(date);

        } catch (ParseException e) {

            printOutput(WRONG_DATE);
            return false;

        }

        return (user.addEvent(text, calendar));

    }

    public boolean addRandomTimeUserEvent(String name, String text, String from, String to) {

        if (name.trim().isEmpty()) {
            printOutput(NO_NAME);
            return false;
        }

        if (text.trim().isEmpty()) {
            printOutput(NO_TEXT);
            return false;
        }

        if (from.trim().isEmpty()) {
            printOutput(NO_FROM_DATE);
            return false;
        }

        if (to.trim().isEmpty()) {
            printOutput(NO_TO_DATE);
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);

        Date dateFrom = new Date();
        Date dateTo = new Date();

        try {

            dateFrom = dateFormat.parse(from);
            dateTo = dateFormat.parse(to);

        } catch (ParseException e) {

            printOutput(WRONG_DATE);
            return false;

        }

        long fromMillis = dateFrom.getTime();
        long toMillis = dateTo.getTime();
        long rand = fromMillis + (long) ((toMillis - fromMillis) * Math.random());

        Calendar calendar = new GregorianCalendar(user.getUserTimeZone());
        calendar.setTimeInMillis(rand);

        return (user.addEvent(text, calendar));

    }

    public boolean removeUserEvent(String name, String text) {

        if (name.trim().isEmpty()) {
            printOutput(NO_NAME);
            return false;
        }

        if (text.trim().isEmpty()) {
            printOutput(NO_TEXT);
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);

        return (user.removeEvent(text));

    }

    public boolean cloneUserEvent(String name, String text, String nameTo) {

        if (name.trim().isEmpty()) {
            printOutput(NO_NAME);
            return false;
        }

        if (text.trim().isEmpty()) {
            printOutput(NO_TEXT);
            return false;
        }

        if (nameTo.trim().isEmpty()) {
            printOutput(NO_NAME_TO);
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        if (!users.containsKey(nameTo)) {
            printOutput(nameTo + WRONG_NAME);
            return false;
        }

        Event event = users.get(name).getEvent(text);

        if (event==null) {
            printOutput(EVENT_MISSING);
            return false;
        }

        return (users.get(nameTo).addEvent(event.getText(), event.getDate()));

    }

    public boolean showUserInfo(String name) {

        if (name.trim().isEmpty()) {
            printOutput(NO_TEXT);
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        printOutput(users.get(name).toString());

        return true;

    }

    public String[] parseString (String command) {

        String tempString = command.substring(command.indexOf("(") + 1, command.lastIndexOf(")"));
        String[] arguments = tempString.split(",");

        for (int i = 0; i < arguments.length; i++){
            arguments[i] = arguments[i].trim();
        }

        return arguments;
    }

    public boolean saveUserData(String filename) {

        if (filename.trim().isEmpty()) {
            printOutput(NO_FILENAME);
            return false;
        }

        try {

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.users);
            out.close();
            fileOut.close();
            printOutput(DATA_SAVED);

            return true;

        } catch(IOException e) {

            printOutput(e.getMessage());
            return false;

        }

    }

    public boolean loadUserData(String filename) {

        if (filename.trim().isEmpty()) {
            printOutput(NO_FILENAME);
            return false;
        }

        try  {

            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.users = (TreeMap<String, User>) in.readObject();
            in.close();
            fileIn.close();
            printOutput(DATA_LOADED);

            return true;

        } catch(IOException e)  {

            printOutput(e.getMessage());
            return false;

        } catch(ClassNotFoundException e) {

            printOutput(e.getMessage());
            return false;

        }

    }

    public boolean server() {

        return true;

    }

    public boolean client() {

        return true;

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
                    printOutput(currentDate + "\n" + username + "\n" + event.getText() + "\n");
                }

            }

        }

    }

}