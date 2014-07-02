package com.javaschool2014.task1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinator extends TimerTask implements Constants {

    private String appName             = "Planfx v0.1";
    private JFrame newFrame            = new JFrame(appName);
    private final JTextArea textArea   = new JTextArea();

    private DateFormat dateFormat      = new SimpleDateFormat(DATE_FORMAT);
    private Timer timer                = new Timer(true);
    public TreeMap<String, User> users = new TreeMap<String, User>();

    public  Coordinator () {

    }

    public void display() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 14));

        JButton create             = new JButton("Create");
        JButton modify             = new JButton("Modify");
        JButton addEvent           = new JButton("AddEvent");
        JButton removeEvent        = new JButton("RemoveEvent");
        JButton addRandomTimeEvent = new JButton("AddRandomTimeEvent");
        JButton cloneEvent         = new JButton("CloneEvent");
        JButton showInfo           = new JButton("ShowInfo");

        final ActionListener createListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("asd! \n");
            }
        };

        create.addActionListener(createListener);

        leftPanel.add(new JScrollPane(textArea));

        rightPanel.add(create);
        rightPanel.add(modify);
        rightPanel.add(addEvent);
        rightPanel.add(removeEvent);
        rightPanel.add(addRandomTimeEvent);
        rightPanel.add(cloneEvent);
        rightPanel.add(showInfo);

        mainPanel.add(BorderLayout.WEST, leftPanel);
        mainPanel.add(rightPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(460, 460);
        newFrame.setVisible(true);

    }

    public void start(){

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        startScheduling();

        while (true) {

            try {

                String command = input.readLine();
                String[] arguments = null;

                Pattern pattern = Pattern.compile(createUserPattern);
                Matcher matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (createUser(arguments[0], arguments[1], arguments[2])) {
                        textArea.append("User created! \n");
                    } else {
                        textArea.append(ERROR + "\n");
                    }

                    continue;
                }

                pattern = Pattern.compile(modifyUserPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (modifyUser(arguments[0], arguments[1], arguments[2])) {
                        textArea.append("User modified! \n");
                    } else {
                        textArea.append(ERROR + "\n");
                    }

                    continue;
                }

                pattern = Pattern.compile(addUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (addUserEvent(arguments[0], arguments[1], arguments[2])) {
                        textArea.append("Event added! \n");
                    } else {
                        textArea.append(ERROR + "\n");
                    }

                    continue;
                }

                pattern = Pattern.compile(addRandomTimeUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (addRandomTimeUserEvent(arguments[0], arguments[1], arguments[2], arguments[3])) {
                        textArea.append("Random time event added! \n");
                    } else {
                        textArea.append(ERROR + "\n");
                    }

                    continue;
                }

                pattern = Pattern.compile(removeUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (removeUserEvent(arguments[0], arguments[1])) {
                        textArea.append("Event removed! \n");
                    } else {
                        textArea.append(ERROR + "\n");
                    }

                    continue;
                }

                pattern = Pattern.compile(cloneUserEventPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (cloneUserEvent(arguments[0], arguments[1], arguments[2])) {
                        textArea.append("Event cloned! \n");
                    } else {
                        textArea.append(ERROR + "\n");
                    }

                    continue;
                }

                pattern = Pattern.compile(showUserInfoPattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {

                    arguments = parseString(command);

                    if (!showUserInfo(arguments[0])) {
                        textArea.append(ERROR + "\n");
                    }

                    continue;
                }

                pattern = Pattern.compile(leavePattern);
                matcher = pattern.matcher(command);

                if (matcher.matches()) {
                    break;
                }

                textArea.append(WRONG_COMMAND + "\n");

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        stopScheduling();

    }

    public boolean createUser(String name, String timeZone, String status) {

        if (users.containsKey(name)) {
            textArea.append(USER_EXISTS + "\n");
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
            textArea.append(name + WRONG_NAME + "\n");
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
            textArea.append(name + WRONG_NAME + "\n");
            return false;
        }

        User user = users.get(name);
        Calendar calendar = new GregorianCalendar(user.getUserTimeZone());
        Date date = new Date();

        try {

            date = dateFormat.parse(dateTime);
            calendar.setTime(date);

        } catch (ParseException e) {

            textArea.append(WRONG_DATE + "\n");
            return false;

        }

        return (user.addEvent(text, calendar));

    }

    public boolean removeUserEvent(String name, String text) {

        if (!users.containsKey(name)) {
            textArea.append(name + WRONG_NAME + "\n");
            return false;
        }

        User user = users.get(name);

        return (user.removeEvent(text));

    }

    public boolean addRandomTimeUserEvent(String name, String text, String from, String to) {

        if (!users.containsKey(name)) {
            textArea.append(name + WRONG_NAME + "\n");
            return false;
        }

        User user = users.get(name);

        Date dateFrom = new Date();
        Date dateTo = new Date();

        try {

            dateFrom = dateFormat.parse(from);
            dateTo = dateFormat.parse(to);

        } catch (ParseException e) {

            textArea.append(WRONG_DATE + "\n");
            return false;

        }

        long fromMillis = dateFrom.getTime();
        long toMillis = dateTo.getTime();
        long rand = fromMillis + (long) ((toMillis - fromMillis) * Math.random());

        Calendar calendar = new GregorianCalendar(user.getUserTimeZone());
        calendar.setTimeInMillis(rand);

        return (user.addEvent(text, calendar));

    }

    public boolean cloneUserEvent(String name, String text, String nameTo) {

        if (!users.containsKey(name)) {
            textArea.append(name + WRONG_NAME + "\n");
            return false;
        }

        if (!users.containsKey(nameTo)) {
            textArea.append(nameTo + WRONG_NAME + "\n");
            return false;
        }

        Event event = users.get(name).getEvent(text);

        if (event==null) {
            textArea.append(EVENT_MISSING + "\n");
            return false;
        }

        return (users.get(nameTo).addEvent(event.getText(), event.getDate()));

    }

    public boolean showUserInfo(String name) {

        if (!users.containsKey(name)) {
            textArea.append(name + WRONG_NAME + "\n");
            return false;
        }

        textArea.append(users.get(name).toString() + "\n");

        return true;

    }

    public void startScheduling() {
        timer.scheduleAtFixedRate(this, 0, 1000);
    }

    public void stopScheduling() {
        timer.cancel();
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
                    textArea.append(currentDate + "\n" + username + "\n" + event.getText() + "\n\n");
                }

            }

        }

    }

}