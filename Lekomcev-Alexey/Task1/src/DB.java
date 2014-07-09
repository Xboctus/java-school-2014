import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DB {

    private static JTextArea textArea;
    private static int key = 0;
    private static ArrayList<User> users = new ArrayList<User>();

    public DB(JTextArea textArea){
        this.textArea = textArea;
    }

    public static void clearTables(){
        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            statement.executeUpdate("TRUNCATE TABLE event");
            statement.executeUpdate("DELETE FROM user");
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void createTables(){
        try{
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS user " +
                    "(" +
                    " id INT NOT NULL AUTO_INCREMENT," +
                    " name CHAR(20)," +
                    " status BOOLEAN," +
                    " tz CHAR(20)," +
                    " PRIMARY KEY(id)" +
                    ")");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS event " +
                    "(" +
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "user_id INT NOT NULL," +
                    "date DATETIME," +
                    "description CHAR(10)," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(user_id)" +
                    "REFERENCES user(id)" +
                    "ON DELETE CASCADE" +
                    ")");
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void dropTables(){
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE event");
            statement.executeUpdate("DROP TABLE user");
            connection.close();
        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        String url = "jdbc:mysql://localhost/db";
        try{
            Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (InstantiationException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, "user", "password");
    }

    public static void insertIntoTable(String tableName, Object object) {
        User user = null;
        Event event = null;
        ResultSet resultSet = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            switch (tableName) {
                case ("user"): {
                    user = (User) object;
                    String stat = "INSERT INTO " + tableName + " (name,status,tz) VALUES ('" +
                            user.name + "'," +
                            user.status + ",'" +
                            user.tz.getID() +
                            "')";
                    statement.executeUpdate(stat, Statement.RETURN_GENERATED_KEYS);
                    resultSet = statement.getGeneratedKeys();
                    resultSet.next();
                    key = resultSet.getInt(1);
                    break;
                }
                case ("event"):{
                    event = (Event) object;
                    String stat = "INSERT INTO " + tableName + " (user_id,date,description) VALUES ('" +
                            key + "'," +
                            "?,'" +
                            event.description +
                            "');";
                    PreparedStatement preparedStatement = connection.prepareStatement(stat);
                    java.sql.Timestamp timestamp = new java.sql.Timestamp(event.date.getTime());
                    preparedStatement.setTimestamp(1, timestamp);
                    preparedStatement.executeUpdate();
                    break;
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void read(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:ss");

            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet resultSetUsers = stat.executeQuery("SELECT * FROM user");
            while (resultSetUsers.next()){
                String status = resultSetUsers.getString(3).equals("1")? "true" : "false";
                users.add(new User(resultSetUsers.getInt(1), resultSetUsers.getString(2), resultSetUsers.getString(4), status));
            }
            ResultSet resultSetEvents = stat.executeQuery("SELECT * FROM event");
            while (resultSetEvents.next()){
                int user_id = resultSetEvents.getInt(2);
                Date date = resultSetEvents.getTimestamp(3);
                User user = users.get(users.indexOf(new User(user_id, "", "", "")));
                user.events.add(new Event(resultSetEvents.getInt(1), user_id, date, resultSetEvents.getString(4)));
            }
            Coordinator.setUsers(users);
            conn.close();
        }
        catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    private static java.sql.Date getSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}
