package com.javaschool2014.task1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;

public abstract class AbstractCoordinator extends TimerTask implements Constants {

    private Timer timer                        = new Timer(true);
    private DateFormat dateFormat              = new SimpleDateFormat(DATE_FORMAT);

    private static TreeMap<String, User> users = new TreeMap<String, User>();

    protected DataLoader dataLoader            = new DataLoader();
    protected DataLoaderSQL dataLoaderSQL      = new DataLoaderSQL();
    protected DataSync dataSync                = new DataSync();

    protected void printOutput(String output) {}

    public void start() {}

    public synchronized boolean createUser(String name, String timeZone, String status) {

        if (!validateInput(name, NO_NAME)
                || !validateInput(timeZone, NO_TIMEZONE)
                || !validateInput(status, NO_STATUS)) {
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

    public synchronized boolean modifyUser(String name, String timeZone, String status) {

        if (!validateInput(name, NO_NAME)
                || !validateInput(timeZone, NO_TIMEZONE)
                || !validateInput(status, NO_STATUS)) {
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

    public synchronized boolean addUserEvent(String  name, String text, String dateTime) {

        if (!validateInput(name, NO_NAME)
                || !validateInput(text, NO_TEXT)
                || !validateInput(dateTime, NO_DATE)) {
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

    public synchronized boolean addRandomTimeUserEvent(String name, String text, String from, String to) {

        if (!validateInput(name, NO_NAME)
                || !validateInput(text, NO_TEXT)
                || !validateInput(from, NO_FROM_DATE)
                || !validateInput(to, NO_TO_DATE)) {
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

    public synchronized boolean removeUserEvent(String name, String text) {

        if (!validateInput(name, NO_NAME)
                || !validateInput(text, NO_TEXT)) {
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        User user = users.get(name);

        return (user.removeEvent(text));

    }

    public synchronized boolean cloneUserEvent(String name, String text, String nameTo) {

        if (!validateInput(name, NO_NAME)
                || !validateInput(text, NO_TEXT)
                || !validateInput(nameTo, NO_NAME_TO)) {
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

        if (!validateInput(name, NO_NAME)) {
            return false;
        }

        if (!users.containsKey(name)) {
            printOutput(name + WRONG_NAME);
            return false;
        }

        printOutput(users.get(name).toString());

        return true;

    }

    public synchronized boolean saveUserData(String filename) {

        if (!validateInput(filename, NO_FILENAME)) {
            return false;
        }

        if (!dataLoader.saveData(filename)) {
            printOutput(NO_FILE_SAVED);
            return false;
        }

        printOutput(DATA_SAVED);

        return true;

    }

    public synchronized boolean loadUserData(String filename) {

        if (!validateInput(filename, NO_FILENAME)) {
            return false;
        }

        if (!dataLoader.loadData(filename)) {
            printOutput(NO_FILE_LOADED);
            return false;
        }

        printOutput(DATA_LOADED);

        return true;

    }

    public boolean createServer() {

        dataSync.server();

        return true;

    }

    public synchronized boolean synchronizeData(String ip, String portName) {

        if (!validateInput(ip, NO_IP)
                || !validateInput(portName, NO_PORT)) {
            return false;
        }

        int port = Integer.parseInt(portName);
        TreeMap<String, User> syncData = dataSync.synchronize(ip, port);

        if (syncData == null) {

            printOutput(NO_FILE_LOADED);

            return false;

        } else {

            setUsers(syncData);
            printOutput(DATA_SYNCED);

            return true;

        }

    }

    public boolean getServerPort() {

        printOutput(dataSync.getServerPort());

        return true;

    }

    protected synchronized void connectDefaultDataFile(String filename) {

        if (!loadUserData(filename)) {
            printOutput(NO_DEFAULT_FILE_LOADED);
        }

    }

    public String[] parseString (String command) {

        String tempString = command.substring(command.indexOf("(") + 1, command.lastIndexOf(")"));
        String[] arguments = tempString.split(",");

        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].trim();
        }

        return arguments;
    }

    protected boolean validateInput(String input, String message) {

        if (input.trim().isEmpty()) {
            printOutput(message);
            return false;
        }

        return true;

    }

    public synchronized static TreeMap<String, User> getUsers() {
        return users;
    }

    public synchronized static void setUsers(TreeMap<String, User> users) {
        AbstractCoordinator.users = users;
    }

    public Timer getTimer() {
        return timer;
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