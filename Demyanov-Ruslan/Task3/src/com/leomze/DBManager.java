package com.leomze;




import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

                            //----------------------------------------------------------//
                            //                                                          //
                            //              For DB used MySQL v.5.6.17 in               //
                            //                WAMP server version 2.5                   //
                            //                                                          //
                            //----------------------------------------------------------//

public class DBManager {
    //====================================================================================================================//
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS taskstracker.event (\n" +
            "  UserID INT(11) UNSIGNED NOT NULL,\n" +
            "  TextEvent VARCHAR(255) CHARACTER SET 'ascii' COLLATE 'ascii_bin' NOT NULL,\n" +
            "  EventDate TIMESTAMP NOT NULL);";
    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS taskstracker.users (\n" +
            "  UserID INT(11) UNSIGNED UNIQUE NOT NULL AUTO_INCREMENT,\n" +
            "  UserName VARCHAR(255) CHARACTER SET 'ascii' NOT NULL,\n" +
            "  TimeZone INT(2) NOT NULL,\n" +
            "  Status ENUM('1', '0') NOT NULL DEFAULT '1');";

    private static final String CREATE_DATABASE_TASKSTRACKER = "CREATE DATABASE IF NOT EXISTS TasksTracker;";

    //=====================================================================================================================//
    private static final String USER_CREATED = "\n\nUser created!";
    private static final String USER_MODIFIED = "\n\nUser modified!";
    private static final String WRONG_INPUT_DATA = "\n\nWrong input data :(";
    private static final String EVENT_CREATED = "\n\nEvent created!";
    private static final String WRONG_DATE_INTERVAL = "\n\nWrong date interval";
    private static final String EVENT_CLONED = "\n\nEvent cloned!";
    private static final String EVENT_REMOVED = "\n\nEvent removed!";
    //====================================================================================================================//
    Connection connection = null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DBManager()
    {

        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
        }

        System.out.println("MySQL JDBC Driver Registered!");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            //manipulations with db
            try {
                Statement st = connection.createStatement();
                st.executeUpdate(CREATE_DATABASE_TASKSTRACKER);
                st.executeUpdate("USE TasksTracker;");
                st.executeUpdate(CREATE_TABLE_USERS);
                st.executeUpdate(CREATE_EVENT_TABLE);

//                String str  =(String) st.executeQuery("SHOW DATABASES;");

                ResultSet rs = st.executeQuery("SELECT * FROM users");
                while (rs.next()){
                    System.out.println(rs.getString("timezone") );
                    System.out.println(rs.getString("UserName") + "\n");
                }


            } catch (SQLException e){
                e.printStackTrace();
            }



        } else {
            System.out.println("Failed to make connection!");
        }
    }

    public void createUser(String name, String timeZone, String status){

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
                Statement st = connection.createStatement();
                st.executeQuery("USE taskstracker;");
                ResultSet rs = st.executeQuery("SELECT * FROM users WHERE username = '" + name + "';");
                ArrayList<String> users = new ArrayList<>();
                while (rs.next()){
                    users.add(rs.getString("username"));
                 }
                if(name != null & !users.contains(name)){
                st.executeUpdate("INSERT INTO taskstracker.users (UserID, UserName, TimeZone, Status) VALUES (NULL, '" + name + "', '"+ timeZone +"', '" + status + "');");
                    TaskerView.textArea.append(USER_CREATED);
                } else {
                    TaskerView.textArea.append(WRONG_INPUT_DATA);
                }
                rs.close();
                st.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    public void modifyUser(String name, String timeZone, String status){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement st = connection.createStatement();
            st.executeQuery("USE taskstracker;");
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE UserName = '" + name + "'");
            int i = 0;
            if(rs.next()){
              i = rs.getInt("userid");
            }
            if(i != 0){
                st.executeUpdate("UPDATE taskstracker.users SET UserName = '" + name + "', TimeZone = '" + timeZone + "', Status = '" + status + "' WHERE users.UserID = " + i +";");
                TaskerView.textArea.append(USER_MODIFIED);
            } else {
                TaskerView.textArea.append(WRONG_INPUT_DATA);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addEvent(String name, String text, String date){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement st = connection.createStatement();
            st.executeQuery("USE taskstracker;");
            ResultSet rsUser = st.executeQuery("SELECT * FROM users WHERE UserName = '" + name + "'");
            int id = 0;
            String userName = new String();
            int timezone = 0;
            if(rsUser.next()){
                id = rsUser.getInt("userid");
                userName = rsUser.getString("username");
                timezone = rsUser.getInt("timezone");
            }

            if(id != 0 & !userName.equals(null) & !parseDate(date, timezone).equals(null) ){
                st.executeUpdate("INSERT INTO taskstracker.event (UserID, TextEvent, EventDate) VALUES ('" + id + "', '" + text + "', '" + parseDate(date, timezone) + "');");
                TaskerView.textArea.append(EVENT_CREATED);
            } else {
                TaskerView.textArea.append(WRONG_INPUT_DATA);
            }
            rsUser.close();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void addRandomeEvent(String name, String text, String dateFrom, String dateTo){

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
                Statement st = connection.createStatement();
                st.executeQuery("USE taskstracker;");
                ResultSet rsUser = st.executeQuery("SELECT * FROM users WHERE UserName = '" + name + "'");
                int id = 0;
                String userName = new String();
                int timezone = 0;
                if(rsUser.next()){
                    id = rsUser.getInt("userid");
                    userName = rsUser.getString("username");
                    timezone = rsUser.getInt("timezone");
                }

                if(id != 0 & !userName.equals(null) & !parseDate(dateFrom, timezone).equals(null) & !parseDate(dateTo, timezone).equals(null) ){
                    Date userDateFrom = simpleDateFormat.parse(parseDate(dateFrom, timezone));
                    Date userDateTo = simpleDateFormat.parse(parseDate(dateTo, timezone));
                    long diff = (userDateFrom.getTime() - userDateTo.getTime()) / 1000;
                    if(diff <= 0){
                        long randomLong = Math.abs((new Random()).nextLong());
                        randomLong = Math.abs(randomLong);
                        double coef = (double)diff/Long.MAX_VALUE;
                        long datetime  = (long) (userDateFrom.getTime() + randomLong * coef * 1000);
                        Date randDate = new Date(datetime);
                        String correctdate = parseDate(simpleDateFormat.format(randDate), timezone);
                        st.executeUpdate("INSERT INTO taskstracker.event (UserID, TextEvent, EventDate) VALUES ('" + id + "', '" + text + "', '" + correctdate + "');");
                        TaskerView.textArea.append(EVENT_CREATED);


                    } else {
                        TaskerView.textArea.append(WRONG_DATE_INTERVAL);
                    }
                }else{
                    TaskerView.textArea.append(WRONG_INPUT_DATA);
                }
                rsUser.close();
                st.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

    }

    public void cloneEvent(String nameFrom, String text, String nameTo){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement st = connection.createStatement();
            st.executeQuery("USE taskstracker;");
            ResultSet rsUser = st.executeQuery("SELECT * FROM users WHERE UserName LIKE '" + nameFrom + "'");
            int idFrom = 0;
            if(rsUser.next()){
            idFrom = rsUser.getInt("userid");
            }
            rsUser = st.executeQuery("SELECT * FROM event WHERE UserID LIKE '" + idFrom + "'");
            String date = new String();
            if(rsUser.next()){
                date = rsUser.getString("eventDate");
            }
            rsUser = st.executeQuery("SELECT * FROM users WHERE UserName LIKE '" + nameTo + "'");
            int idTo = 0;
            int timezone = 0;
            if(rsUser.next()){
                idTo = rsUser.getInt("userid");
                timezone = rsUser.getInt("timezone");
            }
            if(idFrom != 0 & idTo != 0 & !parseDate(date, timezone).equals(null) ){
                st.executeUpdate("INSERT INTO taskstracker.event (UserID, TextEvent, EventDate) VALUES ('" + idTo + "', '" + text + "', '" + parseDate(date, timezone) + "');");
                TaskerView.textArea.append(EVENT_CLONED);
            } else {
                TaskerView.textArea.append(WRONG_INPUT_DATA);
            }
            rsUser.close();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeEvent(String name, String text){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement st = connection.createStatement();
            st.executeQuery("USE taskstracker;");
            ResultSet rsUser = st.executeQuery("SELECT * FROM users WHERE UserName LIKE '" + name + "'");
            int id = 0;
            if(rsUser.next()){
                id = rsUser.getInt("userid");
            }
            rsUser = st.executeQuery("SELECT * FROM event WHERE textEvent LIKE '" + text + "'");
            String textEvent = new String();
            if(rsUser.next()){
                textEvent = rsUser.getString("textEvent");
            }
            if(id != 0 & !textEvent.equals(null)){
                st.executeUpdate("DELETE FROM taskstracker.event WHERE event.UserID = " + id + " AND event.textEvent = '" + textEvent + "';");
                TaskerView.textArea.append(EVENT_REMOVED);
            }else{
                TaskerView.textArea.append(WRONG_INPUT_DATA);
            }
            rsUser.close();
            st.close();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void showinfo(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement st = connection.createStatement();
            st.executeQuery("USE taskstracker;");
            ResultSet resultSet = st.executeQuery("SELECT * FROM users;");

            String str = new String();
            while(resultSet.next()){
                str = str + ("\n\nUser name:" + resultSet.getString("username")+
                                           "\nUser timezone: GMT" + resultSet.getInt("timezone") +
                                           "\nUser status:" + resultSet.getString("status") );
                int id = resultSet.getInt("userid");
                str = str + showEvent(id);
            }
            TaskerView.textArea.append(str);

            resultSet.close();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public String showEvent(int id){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement st = connection.createStatement();
            st.executeQuery("USE taskstracker;");
            ResultSet resultSetEvent = st.executeQuery("SELECT * FROM event WHERE event.userid = '" + id + "'");
            String str = new String();
            while (resultSetEvent.next()){
                str = str +("\nUsers events:\n   " + resultSetEvent.getString("eventdate")+ "\n   " + resultSetEvent.getString("textevent") + "\n");
            }
            resultSetEvent.close();
            return str;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "empty event table";

    }



    public static String parseDate(String date, int timezone) {
        if(timezone <= 14 & timezone >= -12){

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Calendar calendar = new GregorianCalendar();
                int offset = calendar.get(Calendar.ZONE_OFFSET);
                Date d = simpleDateFormat.parse(date);
                String corDate = simpleDateFormat.format(new Date(d.getTime() - timezone * 60 * 60 * 1000 + offset)) ;
                return corDate;

            } catch (ParseException e) {
                return null;
            }
        }else {
            return null;
        }
    }

    public String[] getUsersname(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement st = connection.createStatement();
            st.executeQuery("USE taskstracker;");
            ResultSet rsUser = st.executeQuery("SELECT * FROM users;");
            ArrayList<String> usersname = new ArrayList<>();
            while (rsUser.next()){
                usersname.add(rsUser.getString("username"));
            }
            rsUser.close();
            st.close();
            return usersname.toArray(new String[0]);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }



}
