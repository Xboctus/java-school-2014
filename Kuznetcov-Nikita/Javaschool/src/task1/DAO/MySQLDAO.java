package task1.DAO;

import task1.Event;
import task1.User;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Sunrise on 08.07.2014.
 */
public class MySQLDAO implements ISchedulerDAO {

  private Connection connection;

  public MySQLDAO(String url, String username, String password) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    connection = DriverManager.getConnection(url, username, password);
  }

  @Override
  public synchronized void saveSchedulerState(Map<String, User> usersMap) throws SQLException {
    Statement stmt = connection.createStatement();
    stmt.execute("DELETE * FROM `events` WHERE `id` > 0; DELETE * FROM `users` WHERE `id` > 0;");
    stmt.close();

    PreparedStatement userStatement = connection.prepareStatement("INSERT INTO `users` (userName, timeZoneID, isActive) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    PreparedStatement eventStatement = connection.prepareStatement("INSERT INTO `events` (user_id, eventDate, eventText) VALUES (?, ?, ?)");
    for (User user : usersMap.values()) {
      userStatement.clearParameters();
      userStatement.setString(1, user.getUserName());
      userStatement.setString(2, user.getTimeZone().getID());
      userStatement.setBoolean(3, user.isActive());

      int affectedRows = userStatement.executeUpdate();
      if (affectedRows != 0) {
        int user_id = 0;
        ResultSet generatedKeys = userStatement.getGeneratedKeys();
        if (generatedKeys.next())
          user_id = generatedKeys.getInt(1);
        for (Event event : user.getEventsArray()) {
          eventStatement.clearParameters();
          eventStatement.setInt(1, user_id);
          eventStatement.setLong(2, event.getEventDate().getTime());
          eventStatement.setString(3, event.getEventText());
          eventStatement.execute();
        }
      }
    }
    userStatement.close();
    eventStatement.close();
  }

  @Override
  public synchronized ConcurrentSkipListMap<String, User> loadSchedulerState() throws SQLException {
    ConcurrentSkipListMap<String, User> result = new ConcurrentSkipListMap<String, User>();

    PreparedStatement eventsSelector = connection.prepareStatement("SELECT * FROM `events` e WHERE e.user_id = ?");

    Statement usersSelector = connection.createStatement();
    ResultSet usersResultRes = usersSelector.executeQuery("SELECT * FROM `users` u;");
    while (usersResultRes.next()) {
      String userName = usersResultRes.getString("username");
      String timeZoneID = usersResultRes.getString("timeZoneID");
      boolean isActive = usersResultRes.getBoolean("isActive");
      User user = new User(userName, TimeZone.getTimeZone(timeZoneID), isActive);

      eventsSelector.clearParameters();
      eventsSelector.setInt(1, usersResultRes.getInt("id"));
      ResultSet events = eventsSelector.executeQuery();
      while (events.next()) {
        user.addEvent(new Event(new java.util.Date(events.getLong("eventDate")), events.getString("eventText")));
      }
      result.put(userName, user);
    }
    eventsSelector.close();
    usersSelector.close();
    return result;
  }

  @Override
  public void closeConnection() throws SQLException {
    if (connection != null) {
      if (!connection.isClosed())
        connection.close();
    }
  }

}
