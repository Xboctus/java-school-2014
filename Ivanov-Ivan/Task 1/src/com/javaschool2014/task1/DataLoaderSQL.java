package com.javaschool2014.task1;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DataLoaderSQL implements Constants {

    private static Connection connection         = null;
    private static String username               = "root";
    private static String password               = "password";
    private static String url                    = "jdbc:mysql://127.0.0.1:3306/user_data";
    private DateFormat dateFormat                = new SimpleDateFormat(DATE_FORMAT);

    private static List<Event> eventList         = new ArrayList<Event>();
    private static TreeMap<String, User> userMap = new TreeMap<String, User>();

    public synchronized boolean saveData() {

        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }

        try {

            // Clear old data
            Statement statement;
            statement = connection.createStatement();
            statement.execute("DELETE FROM events");
            statement.execute("DELETE FROM users");

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }

        userMap = AbstractCoordinator.getUsers();

        // Save data
        for (Map.Entry<String, User> user : userMap.entrySet()) {

            saveUser(user.getKey(), user.getValue().getUserTimeZone().getID(), user.getValue().getStatus());

            eventList = user.getValue().getEvents();

            for (Event event : eventList) {
                saveEvent(event.getText(), dateFormat.format(event.getDate().getTime()), user.getKey());
            }

        }

        // CLose db connection
        if (connection != null) {

            try {
                connection.close();
            } catch (SQLException ignore) {
                return false;
            }

        }

        return true;

    }

    public synchronized TreeMap<String, User> loadData() {

        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }

        try {
            // Open a connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }

        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // Excute query
            statement = connection.createStatement();

            if (statement.execute("SELECT * FROM users")) {

                resultSet = statement.getResultSet();

                while (resultSet.next()) {

                    // Load users
                    User user = new User(resultSet.getString(1));
                    TimeZone userTimeZone = TimeZone.getTimeZone(resultSet.getString(2));
                    user.setTimeZone(userTimeZone);
                    user.setStatus(resultSet.getBoolean(3));

                    userMap.put(resultSet.getString(1), user);

                }

            }

            if (statement.execute("SELECT * FROM events")) {

                resultSet = statement.getResultSet();

                while (resultSet.next()) {

                    // Load events
                    User user = userMap.get(resultSet.getString(3));

                    Calendar calendar = new GregorianCalendar(user.getUserTimeZone());
                    Date date;

                    try {

                        date = dateFormat.parse(resultSet.getString(2));
                        calendar.setTime(date);

                    } catch (ParseException e) {

                        System.out.println(WRONG_DATE);
                        return null;

                    }

                    user.addEvent(resultSet.getString(1), calendar);

                }

            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        } finally {

            if (resultSet != null) {

                try {
                    resultSet.close();
                } catch (SQLException e) {}

                resultSet = null;

            }

            if (statement != null) {

                try {
                    statement.close();
                } catch (SQLException e) {}

                statement = null;

            }

        }

        // CLose db connection
        if (connection != null) {

            try {
                connection.close();
            } catch (SQLException ignore) {
                return null;
            }

        }

        return userMap;

    }

    public synchronized void saveUser(String userName, String gmt, boolean status) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // Excute query
            statement = connection.createStatement();

            if (statement.execute("SELECT * FROM users WHERE user_name = \"" + userName +"\"")) {
                resultSet = statement.getResultSet();
            }

            if (resultSet.next()) {
                // Update
                statement.execute("UPDATE users SET time_zone=\"" + gmt + "\", status=" + status + " where user_name=\"" + userName + "\"");
            } else {
                // Insert
                statement.execute("INSERT INTO users (user_name, time_zone, status) VALUES (\"" + userName + "\", \"" + gmt + "\", " + status + ")");
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {

            if (resultSet != null) {

                try {
                    resultSet.close();
                } catch (SQLException e) {}

                resultSet = null;

            }

            if (statement != null) {

                try {
                    statement.close();
                } catch (SQLException e) {}

                statement = null;

            }

        }

    }

    public synchronized void saveEvent(String eventName, String dateTime, String userName) {

        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // Excute query
            statement = connection.createStatement();

            if (statement.execute("SELECT * FROM events WHERE event_name = \"" + eventName +"\" AND users_user_name =\"" + userName + "\"")) {
                resultSet = statement.getResultSet();
            }

            if (resultSet.next()) {
                // Update
                statement.execute("UPDATE events SET event_name=\"" + eventName + "\", event_date=\"" + dateTime + "\" where users_user_name=\"" + userName + "\"");
            } else {
                // Insert
                statement.execute("INSERT INTO events (event_name, event_date, users_user_name) VALUES (\"" + eventName + "\", \"" + dateTime + "\", \"" + userName + "\")");
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally {

            if (resultSet != null) {

                try {
                    resultSet.close();
                } catch (SQLException e) {}

                resultSet = null;

            }

            if (statement != null) {

                try {
                    statement.close();
                } catch (SQLException e) {}

                statement = null;

            }

        }

    }

}